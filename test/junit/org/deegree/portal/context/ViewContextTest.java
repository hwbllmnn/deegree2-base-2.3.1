//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/portal/context/ViewContextTest.java $
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
package org.deegree.portal.context;

import java.net.URL;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;

import alltests.Configuration;

/**
 * 
 * 
 * 
 * @version $Revision: 11917 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11917 $, $Date: 2008-05-27 12:07:06 +0200 (Di, 27. Mai 2008) $
 * 
 * @since 2.0
 */
public class ViewContextTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( ViewContextTest.class );

    /**
     * @throws Exception
     */
    public void testReadingWMC()
                            throws Exception {
        URL file = new URL( Configuration.getResourceDir() + "igeoportal/wmc_saltlake.xml" );
        ViewContext vc = WebMapContextFactory.createViewContext( file, null, null );
        Format frm = vc.getLayerList().getLayers()[0].getFormatList().getCurrentFormat();
        assertEquals( "image/png", frm.getName() );
    }

}
