//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/enterprise/servlet/DirectoryAccessServlet.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.util.KVP2Map;

/**
 * This is one possible web application to be used for the printing functionality in iGeoPortal Std.
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */
public class DirectoryAccessServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 2816833149662971995L;

    private String rootDir;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init()
                            throws ServletException {
        super.init();
        rootDir = getInitParameter( "ROOTDIR" );
        if ( rootDir == null ) {
            rootDir = getServletContext().getRealPath( "." );
        } else {
            File file = new File( rootDir );
            if ( !file.isAbsolute() ) {
                rootDir = getServletContext().getRealPath( rootDir );
            }
        }
        if ( !rootDir.endsWith( "/" ) ) {
            rootDir += '/';
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
                            throws ServletException, IOException {
        doPost( request, response );
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
                            throws ServletException, IOException {
        Map<String, String> reqParams = KVP2Map.toMap( request );
        String req = reqParams.get( "REQUEST" );
        if ( "download".equals( req ) ) {
            download( reqParams, response );
        } else {
            throw new ServletException( "Unknown operation: " + req );
        }
    }

    /**
     * @param reqParams
     * @param response
     * @throws IOException
     */
    private void download( Map<String, String> reqParams, HttpServletResponse response )
                            throws IOException {

        String file = reqParams.get( "FILENAME" );
        String path = rootDir + file;
        RandomAccessFile raf = new RandomAccessFile( path, "r" );
        long l = raf.length();
        byte[] b = new byte[(int) l];
        raf.read( b );
        raf.close();

        OutputStream os = response.getOutputStream();
        os.write( b );
        os.close();
    }

}
