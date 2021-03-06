//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/displayelements/RasterDisplayElement.java $
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
 53115 Bonn
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
package org.deegree.graphics.displayelements;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.graphics.sld.Categorize;
import org.deegree.graphics.sld.Interpolate;
import org.deegree.graphics.sld.RasterSymbolizer;
import org.deegree.graphics.transformation.GeoTransform;
import org.deegree.model.coverage.grid.GridCoverage;
import org.deegree.model.coverage.grid.ImageGridCoverage;
import org.deegree.processing.raster.converter.Image2RawData;
import org.opengis.pt.PT_Envelope;

/**
 * 
 * 
 * 
 * @version $Revision: 14881 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 14881 $, $Date: 2008-11-18 09:54:25 +0100 (Di, 18. Nov 2008) $
 * 
 * @since 2.0
 */
public class RasterDisplayElement extends AbstractDisplayElement implements Serializable {

    private static final long serialVersionUID = -5195730721807600940L;

    private static ILogger LOG = LoggerFactory.getLogger( RasterDisplayElement.class );

    private RasterSymbolizer symbolizer = null;

    private GridCoverage gc = null;

    /**
     * Creates a new RasterDisplayElement_Impl object.
     * 
     * @param gc
     *            raster
     */
    RasterDisplayElement( GridCoverage gc ) {
        setRaster( gc );
        symbolizer = new RasterSymbolizer();
    }

    /**
     * Creates a new RasterDisplayElement_Impl object.
     * 
     * @param gc
     *            raster
     * @param symbolizer
     */
    RasterDisplayElement( GridCoverage gc, RasterSymbolizer symbolizer ) {
        setRaster( gc );
        this.symbolizer = symbolizer;
    }

    private void paintCoverage( Graphics2D g, float[][] data, int minx, int miny, int maxx, int maxy ) {
        Categorize categorize = symbolizer.getCategorize();
        Interpolate interpolate = symbolizer.getInterpolate();

        int opac = (int) ( 255 * symbolizer.getOpacity() ) << 24;

        int width = maxx - minx;
        int height = maxy - miny;

        BufferedImage img = new BufferedImage( data[0].length, data.length, BufferedImage.TYPE_INT_ARGB );

        for ( int x = 0; x < data[0].length; ++x ) {
            for ( int y = 0; y < data.length; ++y ) {
                float d = data[y][x];
                int val = 0;

                if ( categorize != null ) {
                    val = categorize.categorize( d, opac );
                }
                if ( interpolate != null ) {
                    val = interpolate.interpolate( d, opac );
                }

                img.setRGB( x, y, val );
            }
        }

        g.drawImage( img, minx, miny, width, height, null );
    }

    /**
     * renders the DisplayElement to the submitted graphic context
     * 
     */
    public void paint( Graphics g, GeoTransform projection, double scale ) {
        synchronized ( symbolizer ) {
            try {
                if ( doesScaleConstraintApply( scale ) ) {
                    PT_Envelope env = gc.getEnvelope();
                    int minx = (int) ( projection.getDestX( env.minCP.ord[0] ) );
                    int maxy = (int) ( projection.getDestY( env.minCP.ord[1] ) );
                    int maxx = (int) ( projection.getDestX( env.maxCP.ord[0] ) );
                    int miny = (int) ( projection.getDestY( env.maxCP.ord[1] ) );

                    if ( gc instanceof ImageGridCoverage ) {
                        ImageGridCoverage igc = (ImageGridCoverage) gc;
                        Graphics2D g2 = (Graphics2D) g;
                        BufferedImage image = igc.getAsImage( -1, -1 );

                        if ( symbolizer.isDefault() ) {
                            g2.drawImage( image, minx, miny, maxx - minx, maxy - miny, null );
                        } else {
                            if ( symbolizer.scaleValid( scale ) ) {
                                paintCoverage( g2, new Image2RawData( image ).parse(), minx, miny, maxx, maxy );
                            }
                        }
                    } else {
                        // TODO
                        // handle other grid coverages
                    }
                }
            } catch ( Exception e ) {
                LOG.logError( e.getMessage(), e );
                throw new RuntimeException( e.getMessage(), e );
            }
        }
    }

    /**
     * returns the content of the <code>RasterDisplayElement</code>
     * 
     * @return gird coverage
     */
    public GridCoverage getRaster() {
        return gc;
    }

    /**
     * sets the grid coverage that represents the content of the <code>RasterDisplayElement</code>
     * 
     * @param gc
     * 
     */
    public void setRaster( GridCoverage gc ) {
        this.gc = gc;
    }

    @Override
    public boolean doesScaleConstraintApply( double scale ) {
        return symbolizer.scaleValid( scale );
    }

}