//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wmps/DefaultRequestManager.java $
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
package org.deegree.ogcwebservices.wmps;

import java.sql.Connection;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.mail.EMailMessage;
import org.deegree.framework.mail.MailHelper;
import org.deegree.framework.mail.MailMessage;
import org.deegree.framework.mail.SendMailException;
import org.deegree.framework.mail.UnknownMimeTypeException;
import org.deegree.framework.xml.NamespaceContext;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.i18n.Messages;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wmps.configuration.CacheDatabase;
import org.deegree.ogcwebservices.wmps.configuration.WMPSConfiguration;
import org.deegree.ogcwebservices.wmps.operation.PrintMap;
import org.deegree.ogcwebservices.wmps.operation.PrintMapResponse;
import org.deegree.ogcwebservices.wmps.operation.PrintMapResponseDocument;
import org.w3c.dom.Element;

/**
 * Default Handler to save the PrintMap requests to the 'HSQLDB' and send email after processing the
 * request.
 * 
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh</a>
 * @author last edited by: $Author:wanhoff$
 * 
 * @version $Revision: 11498 $, $Date:20.03.2007$
 */
public class DefaultRequestManager implements RequestManager {

    private static final ILogger LOG = LoggerFactory.getLogger( DefaultRequestManager.class );

    protected static NamespaceContext nsContext = CommonNamespaces.getNamespaceContext();

    private WMPSConfiguration configuration;

    private final String MIMETYPE = "text/html";

    private PrintMap request;

    /**
     * Creates a new DefaultRequestManager instance.
     * 
     * @param configuration
     * @param request
     *            request to perform
     */
    public DefaultRequestManager( WMPSConfiguration configuration, PrintMap request ) {
        this.configuration = configuration;
        this.request = request;
    }

    /**
     * returns the configuration used by the handler
     * 
     * @return WMPSConfiguration
     */
    public WMPSConfiguration getConfiguration() {
        return this.configuration;

    }

    /**
     * returns the request used by the handler
     * 
     * @return PrintMap request
     */
    public PrintMap getRequest() {
        return this.request;

    }

    /**
     * Opens a connection to a database based on the properties file in the resources directory and
     * saves the current PrintMap request in the table for later access.
     * 
     * @throws OGCWebServiceException
     */
    public synchronized void saveRequestToDB()
                            throws OGCWebServiceException {

        try {
            CacheDatabase cacheDatabase = this.configuration.getDeegreeParams().getCacheDatabase();
            WMPSDatabase dbConnection = new WMPSDatabase( cacheDatabase );
            Connection connection = dbConnection.acquireConnection();
            dbConnection.insertData( connection, this.request );
            dbConnection.releaseConnection( connection );
        } catch ( Exception e ) {
            LOG.logError( e.getMessage(), e );
            throw new OGCWebServiceException( Messages.getMessage( "WMPS_ERROR_CREATE_WMPSDB" ) );
        }

    }

    /**
     * Send an intial response back to the user, depending on whether the request has been
     * successfull saved in the DB or not. The email address from the request is used to send the
     * reply.
     * 
     * @param message
     * @return PrintMapResponseDocument
     * @throws OGCWebServiceException
     */
    public PrintMapResponseDocument createInitialResponse( String message )
                            throws OGCWebServiceException {

        // before the print operation is finished stage.
        PrintMapResponse initialResponse = new PrintMapResponse( this.request.getId(), this.request.getEmailAddress(),
                                                                 this.request.getTimestamp(),
                                                                 this.request.getTimestamp(), message, "" );

        PrintMapResponseDocument document;
        try {
            document = XMLFactory.export( initialResponse );

        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new OGCWebServiceException( Messages.getMessage( "WMPS_ERROR_CREATE_RESPONSE2" ) );
        }

        return document;
    }

    /**
     * Send an Email to the address provided in the PrintMap request.
     * 
     * @param response
     * @throws OGCWebServiceException
     */
    public void sendEmail( PrintMapResponseDocument response )
                            throws OGCWebServiceException {

        XMLFragment doc = new XMLFragment( response.getRootElement() );
        Element root = doc.getRootElement();
        String id = root.getAttribute( "id" );
        String toEmailAddress = null;
        String timestamp = null;
        String message = null;
        // String processingTime = null;
        try {
            String xPath = "deegreewmps:EmailAddress";
            toEmailAddress = XMLTools.getRequiredNodeAsString( root, xPath, nsContext );
            if ( !isValidEmailAddress( toEmailAddress ) ) {
                throw new PrintMapServiceException( Messages.getMessage( "WMPS_INCORRECT_MAIL_ADDRESS", toEmailAddress ) );
            }
            timestamp = XMLTools.getRequiredNodeAsString( root, "deegreewmps:Timestamp", nsContext );
            message = XMLTools.getRequiredNodeAsString( root, "deegreewmps:Message", nsContext );
            xPath = "deegreewmps:ExpectedProcessingTime";
            // TODO
            // processingTime = XMLTools.getNodeAsString( root, xPath, nsContext, null );
        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new OGCWebServiceException( Messages.getMessage( "WMPS_PARSING_RESPONSE" ) );
        }
        String fromEmailAddress = this.configuration.getDeegreeParams().getPrintMapParam().getAdminMailAddress();

        if ( !isValidEmailAddress( fromEmailAddress ) ) {
            throw new PrintMapServiceException( Messages.getMessage( "WMPS_INCORRECT_MAIL_ADDRESS", fromEmailAddress ) );
        }
        String subject = "PrintMap Notification: " + id + ' ' + timestamp;

        MailMessage email;
        try {
            email = new EMailMessage( fromEmailAddress, toEmailAddress, subject, message, this.MIMETYPE );
        } catch ( UnknownMimeTypeException e ) {
            throw new OGCWebServiceException( "Unknown mime type set." + e );
        }
        String mailHost = this.configuration.getDeegreeParams().getPrintMapParam().getMailHost();
        try {
            MailHelper.createAndSendMail( email, mailHost );
        } catch ( SendMailException e ) {
            LOG.logError( e.getMessage(), e );
            String msg = Messages.getMessage( "WMPS_ERROR_SEND_EMAIL", toEmailAddress, mailHost );
            throw new OGCWebServiceException( msg );
        }

    }

    /**
     * Check if the email address is valid and has a valid name and domain string.
     * 
     * @param aEmailAddress
     * @return boolean
     */
    private boolean hasNameAndDomain( String aEmailAddress ) {

        String[] tokens = aEmailAddress.split( "@" );
        return tokens.length == 2 && ( ( tokens[0] != null ) || ( tokens[0] != "" ) )
               && ( ( tokens[1] != null ) || ( tokens[1] != "" ) );
    }

    /**
     * Check email add validity.
     * 
     * @param aEmailAddress
     * @return boolean
     */
    private boolean isValidEmailAddress( String aEmailAddress ) {

        String status = "VALID";
        if ( aEmailAddress == null )
            status = "NOTVALID";

        try {
            new InternetAddress( aEmailAddress );
            if ( !hasNameAndDomain( aEmailAddress ) ) {
                status = "NOTVALID";
            }
        } catch ( AddressException ex ) {
            status = "NOTVALID " + ex.getMessage();
        }

        return status.startsWith( "VALID" );
    }

    /**
     * Export the PrintMap service final response to a PrintMapResponseDocument.
     * 
     * @param message
     * @param exception
     * @return PrintMapResponseDocument
     * @throws OGCWebServiceException
     */
    public PrintMapResponseDocument createFinalResponse( String message, String exception )
                            throws OGCWebServiceException {

        PrintMapResponse finalResponse = new PrintMapResponse( this.request.getId(), this.request.getEmailAddress(),
                                                               this.request.getTimestamp(),
                                                               this.request.getTimestamp(), message, exception );

        PrintMapResponseDocument document;
        try {
            document = XMLFactory.export( finalResponse );
        } catch ( XMLParsingException e ) {
            LOG.logError( e.getMessage(), e );
            throw new OGCWebServiceException( Messages.getMessage( "WMPS_ERROR_CREATE_RESPONSE1" ) );
        }

        return document;
    }
}