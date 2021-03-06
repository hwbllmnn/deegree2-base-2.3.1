//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/portlet/modules/map/actions/portlets/OverviewPortletPerform.java $
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
package org.deegree.portal.portlet.modules.map.actions.portlets;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.jetspeed.portal.Portlet;
import org.deegree.framework.util.StringTools;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;
import org.deegree.portal.PortalException;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.portlet.modules.actions.IGeoPortalPortletPerform;

/**
 * handles actions performed on a map overview
 * 
 * 
 * @version $Revision: 9346 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class OverviewPortletPerform extends IGeoPortalPortletPerform {

    protected static String PARAM_OVRECTCOLOR = "rectColor";

    protected static String PARAM_OVWIDTH = "width";

    protected static String PARAM_OVHEIGHT = "height";

    protected static String PARAM_OVTITLE = "title";

    protected static String PARAM_OVFOOTER = "footer";

    protected static String PARAM_OVSRC = "imageSource";

    /**
     * 
     * @param request
     * @param portlet
     * @param sc
     */
    public OverviewPortletPerform( HttpServletRequest request, Portlet portlet, ServletContext sc ) {
        super( request, portlet, sc );
    }

    @Override
    public void buildNormalContext()
                            throws PortalException {
        super.buildNormalContext();

        ViewContext vc = getCurrentViewContext( getInitParam( INIT_MAPPORTLETID ) );

        // static bbox of the map overview
        double[] bbox = null;

        Point[] env = null;
        String crs = "";
        if ( vc != null ) {
            env = vc.getGeneral().getBoundingBox();
            crs = env[0].getCoordinateSystem().getPrefixedName();
            String tmp = getInitParam( crs + ':' + PARAM_BBOX );
            if ( tmp == null ) {
                tmp = getInitParam( PARAM_BBOX );
            }
            bbox = StringTools.toArrayDouble( tmp, "," );
        } else {
            String tmp = getInitParam( PARAM_BBOX );
            bbox = StringTools.toArrayDouble( tmp, "," );
            // use overviews bbox if no map model is available
            env = new Point[2];
            env[0] = GeometryFactory.createPoint( bbox[0], bbox[1], null );
            env[1] = GeometryFactory.createPoint( bbox[2], bbox[3], null );
        }

        String rectColor = getInitParam( PARAM_OVRECTCOLOR );
        if ( rectColor == null ) {
            // default color = red
            rectColor = "#FF0000";
        }
        String tmp = getInitParam( PARAM_OVWIDTH );
        if ( tmp == null ) {
            tmp = "150";
        }
        int width = new Integer( tmp );
        tmp = getInitParam( PARAM_OVHEIGHT );
        if ( tmp == null ) {
            tmp = "150";
        }
        int height = new Integer( tmp );
        String title = getInitParam( PARAM_OVTITLE );
        String footer = getInitParam( PARAM_OVFOOTER );
        String src = getInitParam( crs + ':' + PARAM_OVSRC );
        if ( src == null ) {
            src = getInitParam( PARAM_OVSRC );
        }
        request.setAttribute( "TITLE", title );
        request.setAttribute( "FOOTER", footer );
        request.setAttribute( "RECTCOLOR", rectColor );
        request.setAttribute( "WIDTH", width );
        request.setAttribute( "HEIGHT", height );
        request.setAttribute( "SRC", src );
        request.setAttribute( "MINX", bbox[0] );
        request.setAttribute( "MINY", bbox[1] );
        request.setAttribute( "MAXX", bbox[2] );
        request.setAttribute( "MAXY", bbox[3] );
        request.setAttribute( "VIEWMINX", env[0].getX() );
        request.setAttribute( "VIEWMINY", env[0].getY() );
        request.setAttribute( "VIEWMAXX", env[1].getX() );
        request.setAttribute( "VIEWMAXY", env[1].getY() );
    }

}