//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/io/geotiff/GeoTiffTest.java $
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
package org.deegree.io.geotiff;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.model.crs.CRSFactory;
import org.deegree.model.crs.CoordinateSystem;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;

import alltests.AllTests;
import alltests.Configuration;

public class GeoTiffTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( GeoTiffTest.class );
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for ShapeTest.
     * 
     * @param arg0
     */
    public GeoTiffTest( String arg0 ) {
        super( arg0 );
    }

    public void testGeoTiffWriter() {
        
        try {
            BufferedImage bi = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_ARGB );
            double xmin = 0;
            double xmax = 10;
            double ymin = 0;
            double ymax = 10;
            CoordinateSystem crs = CRSFactory.create( "EPSG:4326" );
            Envelope envelope = GeometryFactory.createEnvelope( xmin, ymin, xmax, ymax, crs );
            
            GeoTiffWriter gtw = 
                new GeoTiffWriter( bi, envelope, (xmax-xmin)/bi.getWidth(),
                                   (ymax-ymin)/bi.getHeight(), crs);
            String outputFile =  "output/geotif_test.tif";
            File f = new File( new File( Configuration.getWFSBaseDir().getFile() ) , outputFile );
            LOG.logInfo( "Writing created geotiff to: " + f.getAbsolutePath() );
            FileOutputStream out = new FileOutputStream( f );
            gtw.write( out );
        } catch (Exception e) {
            String s = "Exception writing geotiff failed: ";
            LOG.logError( s + e.getMessage(), e );
            fail( e.getMessage() );
        } 
    }
    
}