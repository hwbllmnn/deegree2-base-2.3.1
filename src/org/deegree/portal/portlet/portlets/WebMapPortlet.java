//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/portlet/portlets/WebMapPortlet.java $
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
package org.deegree.portal.portlet.portlets;

import org.apache.jetspeed.portal.PortletConfig;
import org.apache.jetspeed.portal.PortletException;
import org.apache.jetspeed.portal.portlets.JspPortlet;
import org.deegree.framework.util.StringTools;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: jmays $
 * 
 * @version $Revision: 12130 $, $Date: 2008-06-03 20:24:55 +0200 (Di, 03. Jun 2008) $
 */
public class WebMapPortlet extends JspPortlet {

    private static final long serialVersionUID = -6786461476321256002L;

    static String INIT_HOMEBBOX = "homeBoundingBox";

    private Envelope home = null;

    /**
     * loads the ViewContext assigend to a portlet instance from the resource defined in the portles configuration
     * 
     * @throws PortletException
     */
    @Override
    public void init()
                            throws PortletException {
        super.init();

        PortletConfig pc = getPortletConfig();

        // get HOME boundingbox
        String tmp = pc.getInitParameter( INIT_HOMEBBOX );
        if ( tmp == null ) {

        } else {
            double[] coords = StringTools.toArrayDouble( tmp, "," );
            home = GeometryFactory.createEnvelope( coords[0], coords[1], coords[2], coords[3], null );
        }

    }

    /**
     * returns the home boundingbox of the context assigned to this portlet
     * 
     * @return the home boundingbox of the context assigned to this portlet
     */
    public Envelope getHome() {
        return home;
    }

    /**
     * sets the home boundingbox of the context assigned to this portlet
     * 
     * @param home
     */
    public void setHome( Envelope home ) {
        this.home = home;
    }

}