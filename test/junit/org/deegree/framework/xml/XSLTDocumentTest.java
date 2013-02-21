//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/framework/xml/XSLTDocumentTest.java $
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

package org.deegree.framework.xml;

import java.net.URL;

import javax.xml.transform.TransformerException;

import junit.framework.TestCase;
import alltests.Configuration;

public class XSLTDocumentTest extends TestCase {

    private static final String XSL_FILE = "csw/example/deegree/dublincore/xslt/dc_getrecords_out.xsl";

    private static final String XML_FILE = "input.xml";

    private XMLFragment input;

    private XSLTDocument sheet;

    protected void setUp()
                            throws Exception {
        super.setUp();
        URL inputURL = this.getClass().getResource( XML_FILE );
        input = new XSLTDocument();
        input.load( inputURL );
        sheet = new XSLTDocument();
        URL sheetURL = new URL( Configuration.getResourceDir(), XSL_FILE );
        sheet.load( sheetURL );
    }

    protected void tearDown()
                            throws Exception {
        super.tearDown();
    }

    public void testXSLTWithJavaExtension()
                            throws TransformerException {
        sheet.transform( input );
    }

}

/***************************************************************************************************
 * <code>
 Changes to this class. What the people have been up to:

 $Log$
 Revision 1.3  2007/02/12 09:43:35  wanhoff
 added footer, corrected header

 </code>
 **************************************************************************************************/

