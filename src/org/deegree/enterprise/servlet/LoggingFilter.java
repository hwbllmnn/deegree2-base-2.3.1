//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/LoggingFilter.java $
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
package org.deegree.enterprise.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.util.StringTools;

/**
 * servlet filter for logging incoming requests and outgoing responses. Through the init-params a
 * user may limit logging to:
 * <ul>
 * <li>requests received from a list of defined source/remote addresses</li>
 * <li>returned mime types</li>
 * <li>meta informations about the incoming request</li>
 * </ul>
 * 
 * 
 * @version $Revision: 10660 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 */
public class LoggingFilter implements Filter {

    private List<String> remoteAddresses;

    private List<String> mimeTypes;

    private boolean metaInfo = false;

    /**
     * @param config
     * @throws ServletException
     */
    public void init( FilterConfig config )
                            throws ServletException {

        try {
            if ( config.getInitParameter( "sourceAddresses" ) != null ) {
                remoteAddresses = StringTools.toList( config.getInitParameter( "sourceAddresses" ), ";", true );
            }

            if ( config.getInitParameter( "mimeTypes" ) != null ) {
                mimeTypes = StringTools.toList( config.getInitParameter( "mimeTypes" ), ";", true );
            }
            metaInfo = "true".equalsIgnoreCase( config.getInitParameter( "metaInfo" ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
                            throws IOException, ServletException {

        synchronized ( this ) {
            System.out.println( "==========================================================" );

            ServletResponseWrapper resWrap = new ServletResponseWrapper( (HttpServletResponse) response );

            String url = ( (HttpServletRequest) request ).getRemoteAddr();
            if ( remoteAddresses == null || remoteAddresses.contains( url ) ) {
                ServletRequestWrapper reqWrap = new ServletRequestWrapper( (HttpServletRequest) request );
                // log request content
                System.out.println( reqWrap.getRequestURI() );
                InputStream is = reqWrap.getInputStream();
                int c = 0;
                while ( ( c = is.read() ) > -1 ) {
                    System.out.print( (char) c );
                }
                is.close();

                if ( metaInfo ) {
                    // log request meta information
                    System.out.println( "getRemoteAddr " + reqWrap.getRemoteAddr() );
                    System.out.println( "getPort " + reqWrap.getServerPort() );
                    System.out.println( "getMethod " + reqWrap.getMethod() );
                    System.out.println( "getPathInfo " + reqWrap.getPathInfo() );
                    System.out.println( "getRequestURI " + reqWrap.getRequestURI() );
                    System.out.println( "getServerName " + reqWrap.getServerName() );
                    System.out.println( "getServerPort " + reqWrap.getServerPort() );
                    System.out.println( "getServletPath " + reqWrap.getServletPath() );
                }
                chain.doFilter( reqWrap, resWrap );
            } else {
                chain.doFilter( request, resWrap );
            }

            OutputStream os = resWrap.getOutputStream();
            byte[] b = ( (ServletResponseWrapper.ProxyServletOutputStream) os ).toByteArray();
            os.close();

            String mime = resWrap.getContentType();
            System.out.println( "mime type: " + mime );
            if ( mimeTypes == null || ( mime != null && mimeTypes.contains( mime ) ) ) {
                System.out.write( b );
            }

            response.setContentType( mime );
            response.setContentLength( b.length );

            os = response.getOutputStream();
            os.write( b );
            os.close();
        }

    }

    /**
     * 
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
