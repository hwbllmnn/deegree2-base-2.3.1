//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/wmps/configuration/WMPSConfigurationDocumentTest.java $
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

package org.deegree.ogcwebservices.wmps.configuration;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.InvalidConfigurationException;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.ogcwebservices.wmps.XMLFactory;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import alltests.AllTests;
import alltests.Configuration;

/**
 * Test class for wmps Configuration Document
 * 
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11917 $, $Date: 2008-05-27 12:07:06 +0200 (Di, 27. Mai 2008) $
 */

public class WMPSConfigurationDocumentTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( WMPSConfigurationDocumentTest.class );

    public static Test suite() {
        return new TestSuite( WMPSConfigurationDocumentTest.class );
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
                            throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown()
                            throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for GetCoverageTest
     * 
     * @param arg0
     */
    public WMPSConfigurationDocumentTest( String arg0 ) {
        super( arg0 );
    }

    public void testCreateEmptyDocument() {
        try {
            WMPSConfigurationDocument doc = new WMPSConfigurationDocument();
            doc.createEmptyDocument();
            Node rootNode = doc.getRootElement();
            assertNotNull( rootNode );
            assertEquals( "WMT_PS_Capabilities", rootNode.getNodeName() );
        } catch ( Exception e ) {
            LOG.logError( "Unit test failed", e );
            fail( "Error: " + e.getMessage() );
        }
    }

    /**
     * @throws MalformedURLException
     * @throws SAXException
     * @throws IOException
     * @throws InvalidConfigurationException
     */
    public void testGetCapabilities()
                            throws MalformedURLException, SAXException, IOException, InvalidConfigurationException {
//        fail( "Not testing GetCapabilities -- fixme" );
        WMPSConfigurationDocument doc = new WMPSConfigurationDocument();
         doc.load( Configuration.getWMPSConfigurationURL() );
        WMPSConfiguration conf = doc.parseConfiguration();
         XMLFragment xml = XMLFactory.export( conf );
         xml.write( System.out );
    }
}