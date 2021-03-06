//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/coverage/grid/oracle/OracleGeoRasterGridCoverageReader.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 Contact:

 Andreas Poth
 lat/lon GmbH
 Aennchenstr. 19
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.model.coverage.grid.oracle;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.deegree.datatypes.CodeList;
import org.deegree.datatypes.parameter.GeneralParameterValueIm;
import org.deegree.datatypes.parameter.InvalidParameterNameException;
import org.deegree.datatypes.parameter.InvalidParameterValueException;
import org.deegree.datatypes.parameter.OperationParameterIm;
import org.deegree.datatypes.parameter.ParameterNotFoundException;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.StringTools;
import org.deegree.io.oraclegeoraster.GeoRasterDescription;
import org.deegree.io.oraclegeoraster.GeoRasterReader;
import org.deegree.model.coverage.grid.AbstractGridCoverageReader;
import org.deegree.model.coverage.grid.Format;
import org.deegree.model.coverage.grid.GridCoverage;
import org.deegree.model.coverage.grid.ImageGridCoverage;
import org.deegree.model.crs.GeoTransformer;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.ogcwebservices.LonLatEnvelope;
import org.deegree.ogcwebservices.wcs.describecoverage.CoverageOffering;

/**
 * Reader for Coverages stored in Oracle 10g GeoRaster format
 * 
 * 
 * @version $Revision: 12140 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 12140 $, $Date: 2008-06-04 10:54:18 +0200 (Mi, 04. Jun 2008) $
 * 
 * @since 2.0
 */
public class OracleGeoRasterGridCoverageReader extends AbstractGridCoverageReader {

    private static final ILogger LOG = LoggerFactory.getLogger( OracleGeoRasterGridCoverageReader.class );

    /**
     * 
     * @param grDesc
     * @param description
     * @param envelope
     * @param format
     */
    public OracleGeoRasterGridCoverageReader( GeoRasterDescription grDesc, CoverageOffering description,
                                              Envelope envelope, Format format ) {
        super( grDesc, description, envelope, format );
    }

    public void dispose()
                            throws IOException {
        // nothing happening here
    }

    /**
     * reads a GridCoverage from a Oracle 10g GeoRaster
     * 
     * @param parameters -
     */
    public GridCoverage read( GeneralParameterValueIm[] parameters )
                            throws InvalidParameterNameException, InvalidParameterValueException,
                            ParameterNotFoundException, IOException {

        float width = -1;
        float height = -1;
        for ( int i = 0; i < parameters.length; i++ ) {
            OperationParameterIm op = (OperationParameterIm) parameters[i].getDescriptor();
            String name = op.getName();
            if ( name.equalsIgnoreCase( "WIDTH" ) ) {
                Object o = op.getDefaultValue();
                width = ( (Integer) o ).intValue();
            } else if ( name.equalsIgnoreCase( "HEIGHT" ) ) {
                Object o = op.getDefaultValue();
                height = ( (Integer) o ).intValue();
            }
        }

        // get the region of the georaster that intersects with the requested
        // envelope. First field of the returned array contains the intersection
        // envelope in the rasters native CRS; second field contains the
        // corresponding LonLatEnvelope
        Object[] o = getImageRegion();

        GeoRasterDescription grDesc = (GeoRasterDescription) getSource();
        RenderedImage img = null;
        try {
            LOG.logDebug( "reading GeoRaster from Oracle DB" );
            img = GeoRasterReader.exportRaster( grDesc, (Envelope) o[0] );
        } catch ( Exception e ) {
            LOG.logError( "could not read GeoRaster: ", e );
            throw new IOException( "could not read GeoRaster: " + e.getMessage() );
        }

        ParameterBlock pb = new ParameterBlock();
        pb.addSource( img );
        pb.add( width / img.getWidth() ); // The xScale
        pb.add( height / img.getHeight() ); // The yScale
        pb.add( 0.0F ); // The x translation
        pb.add( 0.0F ); // The y translation
        pb.add( new InterpolationNearest() ); // The interpolation
        // Create the scale operation
        RenderedOp ro = JAI.create( "scale", pb, null );
        try {
            img = ro.getAsBufferedImage();
        } catch ( Exception e ) {
            LOG.logError( "could not rescale image", e );
            throw new IOException( "could not rescale image" + e.getMessage() );
        }

        CoverageOffering co = (CoverageOffering) description.clone();
        co.setLonLatEnvelope( (LonLatEnvelope) o[1] );

        return new ImageGridCoverage( co, (Envelope) o[0], (BufferedImage) img );

    }

    /**
     * returns the region of the source image that intersects with the GridCoverage to be created as
     * Rectange as well as the Envelope of the region in the native CRS and the LonLatEnvelope of
     * this region.
     * 
     * @return the region of the source image that intersects with the GridCoverage to be created as
     *         Rectange as well as the Envelope of the region in the native CRS and the
     *         LonLatEnvelope of this region.
     */
    private Object[] getImageRegion() {

        CodeList[] cl = description.getSupportedCRSs().getNativeSRSs();
        String code = cl[0].getCodes()[0];

        LonLatEnvelope lle = description.getLonLatEnvelope();
        Envelope tmp = GeometryFactory.createEnvelope( lle.getMin().getX(), lle.getMin().getY(), lle.getMax().getX(),
                                                       lle.getMax().getY(), null );
        try {
            if ( !code.equals( "EPSG:4326" ) ) {
                GeoTransformer trans = new GeoTransformer( code );
                tmp = trans.transform( tmp, "EPSG:4326" );
            }
        } catch ( Exception e ) {
            LOG.logError( StringTools.stackTraceToString( e ) );
        }

        // calculate envelope of the part of the grid coverage that intersects
        // within the image
        Envelope env = envelope.createIntersection( tmp );
        LonLatEnvelope lonLatEnvelope = calcLonLatEnvelope( env, code );

        return new Object[] { env, lonLatEnvelope };
    }

}
