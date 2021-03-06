//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/standard/context/control/ContextSaveListener.java $
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

 Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: klaus.greve@uni-bonn.de
 
 ---------------------------------------------------------------------------*/

package org.deegree.portal.standard.context.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.deegree.enterprise.control.FormEvent;
import org.deegree.enterprise.control.RPCMember;
import org.deegree.enterprise.control.RPCMethodCall;
import org.deegree.enterprise.control.RPCParameter;
import org.deegree.enterprise.control.RPCStruct;
import org.deegree.enterprise.control.RPCUtils;
import org.deegree.enterprise.control.RPCWebEvent;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.i18n.Messages;
import org.deegree.model.spatialschema.Envelope;
import org.deegree.portal.Constants;
import org.deegree.portal.PortalException;
import org.deegree.portal.context.ViewContext;
import org.deegree.portal.context.XMLFactory;
import org.w3c.dom.Document;

/**
 * This class saves a new context based on changes made by the user (on the client) and based on the
 * original context xml. <br/> Files are saved under .../WEB-INF/xml/users/some_user, where
 * some_user is passed as an RPC parameter. Files should be saved with .xml extension because the
 * default load context listener class looks up those files. <br/>
 * 
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 */
public class ContextSaveListener extends AbstractContextListener {

    private static final ILogger LOG = LoggerFactory.getLogger( ContextSaveListener.class );

    private static String userDir = "WEB-INF/conf/igeoportal/users/";

    private static String contextDir = "WEB-INF/conf/igeoportal/";

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.enterprise.control.WebListener#actionPerformed(org.deegree.enterprise.control.FormEvent)
     */
    @Override
    public void actionPerformed( FormEvent event ) {

        RPCWebEvent rpc = (RPCWebEvent) event;
        try {
            validate( rpc );
        } catch ( PortalException e ) {
            LOG.logError( e.getMessage(), e );
            gotoErrorPage( Messages.getMessage( "IGEO_STD_CNTXT_INVALID_RPC", "ContextSave", e.getMessage() ) );
            return;
        }

        String newContext = null;
        try {
            newContext = storeContext( rpc );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            gotoErrorPage( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_SAVE_CNTXT" ) );
            return;
        }

        // forward to new page
        this.getRequest().setAttribute( Constants.MESSAGE,
                                        Messages.getMessage( "IGEO_STD_CNTXT_SUCCESS_SAVE_CNTXT", newContext ) );
    }

    /**
     * stores the current context of the user with a defined name
     * 
     * @param event
     * @return name of the context that has been stored
     * @throws PortalException
     */
    private String storeContext( RPCWebEvent event )
                            throws PortalException {

        RPCMethodCall mc = event.getRPCMethodCall();
        RPCParameter[] pars = mc.getParameters();
        RPCStruct struct = (RPCStruct) pars[0].getValue();

        // read base context
        StringBuffer path2Dir = new StringBuffer( getHomePath() );
        path2Dir.append( contextDir );

        // access base context
        HttpSession session = ( (HttpServletRequest) getRequest() ).getSession();
        ViewContext vc = (ViewContext) session.getAttribute( Constants.CURRENTMAPCONTEXT );
        // change values: BBOX and Layer List
        Envelope bbox = extractBBox( (RPCStruct) struct.getMember( Constants.RPC_BBOX ).getValue(), null );
        changeBBox( vc, bbox );
        RPCMember[] layerList = ( (RPCStruct) struct.getMember( "layerList" ).getValue() ).getMembers();
        changeLayerList( vc, layerList );

        // save new context
        // get map context value
        String username = "default";
        try {
            String sid = RPCUtils.getRpcPropertyAsString( struct, "sessionID" );
            LOG.logDebug( "sessionID ", sid );
            username = getUserName( sid );
            if ( username == null ) {
                username = "default";
            }
            LOG.logDebug( "username ", username );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
        }

        String newContext = RPCUtils.getRpcPropertyAsString( struct, "newContext" );
        path2Dir = new StringBuffer( getHomePath() );
        path2Dir.append( userDir );
        path2Dir.append( username );
        File file = new File( path2Dir.toString() );
        if ( !file.exists() ) {
            // create directory if not exists
            file.mkdir();
        }
        path2Dir.append( "/" );
        path2Dir.append( newContext );

        saveDocument( vc, path2Dir.toString() );

        return newContext;
    }

    /**
     * saves the new context as xml
     * 
     * @param vc
     * @param filename
     * @throws PortalException
     */
    public static final void saveDocument( ViewContext vc, String filename )
                            throws PortalException {
        try {
            XMLFragment xml = XMLFactory.export( vc );
            FileOutputStream fos = new FileOutputStream( filename );
            xml.write( fos );
            fos.close();
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_SAVE_FILE", filename ) );
        }
    }

    /**
     * validates the incoming RPC event
     * 
     * @param rpc
     * @throws PortalException
     */
    private void validate( RPCWebEvent rpc )
                            throws PortalException {
        RPCMethodCall mc = rpc.getRPCMethodCall();
        RPCParameter param = mc.getParameters()[0];
        RPCStruct struct = (RPCStruct) param.getValue();
        RPCMember username = struct.getMember( "sessionID" );
        if ( username == null ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_MISSING_PARAM", "sessionID", "ContextSave" ) );
        }
        RPCMember newContext = struct.getMember( "newContext" );
        if ( newContext == null ) {
            throw new PortalException(
                                       Messages.getMessage( "IGEO_STD_CNTXT_MISSING_PARAM", "newContext", "ContextSave" ) );
        }
        RPCMember layerList = struct.getMember( "layerList" );
        if ( layerList == null ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_MISSING_PARAM", "layerList", "ContextSave" ) );
        }
        // TODO validate box: should do this in a common (static) method
        // for many listeners that need a bbox
    }

    /**
     * common method to save xml
     * 
     * @param os
     * @param doc
     * @throws PortalException
     */
    protected static void internalSave( OutputStream os, Document doc )
                            throws PortalException {
        try {
            Source source = new DOMSource( doc );
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform( source, new StreamResult( os ) );
        } catch ( Exception e ) {
            throw new PortalException( Messages.getMessage( "IGEO_STD_CNTXT_ERROR_INTERNAL_SAVE",
                                                            StringTools.stackTraceToString( e.getStackTrace() ) ) );
        }
    }

}
