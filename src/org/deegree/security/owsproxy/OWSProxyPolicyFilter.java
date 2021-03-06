//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsproxy/OWSProxyPolicyFilter.java $
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
package org.deegree.security.owsproxy;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.deegree.framework.trigger.TriggerProvider;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.security.UnauthorizedException;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsrequestvalidator.GeneralPolicyValidator;
import org.deegree.security.owsrequestvalidator.OWSValidator;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.1, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 1.1
 * 
 */
public class OWSProxyPolicyFilter {

    private static TriggerProvider TP = TriggerProvider.create( OWSProxyPolicyFilter.class );

    private Map<String, OWSValidator> validators = null;

    /**
     * if this constructor is used the OWSProxyPolicyFilter doesn't constain a Validator. Validators
     * must be set using the addValidator(OWSPolicyValidator) method
     */
    public OWSProxyPolicyFilter() {
        validators = new HashMap<String, OWSValidator>();
    }

    /**
     * adds a <tt>OWSPolicyValidator</tt> to the <tt>OWSProxyPolicyFilter</tt>
     * 
     * @param service
     * 
     * @param validator
     */
    public void addValidator( String service, OWSValidator validator ) {
        validators.put( service, validator );
    }

    /**
     * validate the passed <tt>OGCWebServiceRequest</tt> againsted the Policy encapsulated by the
     * <tt>OWSProxyPolicyFilter</tt>
     * 
     * @param request
     * @param length
     *            length (characters) of the request
     * @param user
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    public void validateGeneralConditions( HttpServletRequest request, int length, User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        Object o = validators.keySet().iterator().next();
        OWSValidator validator = validators.get( o );
        // create GeneralPolicyValidatora and perform validation of
        // general request parameters
        GeneralPolicyValidator gpValidator = new GeneralPolicyValidator( validator.getGeneralCondtion() );
        validateGeneralConditions( gpValidator, request, length, user );
    }

    /**
     * validate the passed <tt>OGCWebServiceRequest</tt> againsted the Policy encapsulated by the
     * <tt>OWSProxyPolicyFilter</tt>
     * 
     * @param request
     * @param user
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    public void validate( OGCWebServiceRequest request, User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        Object[] o = TP.doPreTrigger( this, request, user );
        request = (OGCWebServiceRequest) o[0];
        user = (User) o[1];

        String service = request.getServiceName();
        if ( service.equals( "urn:x-ogc:specification:cswebrim:Service:OGC-CSW:ebRIM" ) ) {
            service = "CSW";
        }
        // get validator assigned to the requested service
        OWSValidator validator = validators.get( service );

        if ( validator == null ) {
            throw new InvalidParameterValueException( "No Validator registered for service: " + service );
        }
        // validate the OWS request
        validator.validateRequest( request, user );

        TP.doPostTrigger( this, request, user );

    }

    /**
     * validates the general conditions of a Http request. validated are:
     * <ul>
     * <li>content length
     * <li>request method
     * <li>header fields
     * </ul>
     * 
     * @param gpValidator
     * @param request
     * @param length
     *            length (characters) of the request
     * @param user
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    private void validateGeneralConditions( GeneralPolicyValidator gpValidator, HttpServletRequest request, int length,
                                            User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        gpValidator.validateRequestMethod( request.getMethod() );
        if ( request.getContentLength() > 0 )
            length = request.getContentLength();
        if ( request.getMethod().equalsIgnoreCase( "GET" ) ) {
            gpValidator.validateGetContentLength( length );
        } else {
            gpValidator.validatePostContentLength( length );
        }
        Enumeration<?> iterator = request.getHeaderNames();
        Map<String, Object> header = new HashMap<String, Object>();
        while ( iterator.hasMoreElements() ) {
            String key = (String) iterator.nextElement();
            Object value = request.getHeaders( key );
            header.put( key, value );
        }
        gpValidator.validateHeader( header, user );
    }

    /**
     * validates the response (data) to a request
     * 
     * @param request
     * @param data
     * @param mime
     * @param user
     * @return the response data
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    public byte[] validate( OGCWebServiceRequest request, byte[] data, String mime, User user )
                            throws InvalidParameterValueException, UnauthorizedException {

        Object[] o = TP.doPreTrigger( this, request, data, mime, user );
        request = (OGCWebServiceRequest) o[0];
        data = (byte[]) o[1];
        mime = (String) o[2];
        user = (User) o[3];

        String service = request.getServiceName();
        if ( service.equals( "urn:x-ogc:specification:cswebrim:Service:OGC-CSW:ebRIM" ) ) {
            service = "CSW";
        }
        // get validator assigned to the requested service
        OWSValidator validator = validators.get( service );
        if ( validator == null ) {
            throw new InvalidParameterValueException( "No Validator registered for service: " + service );
        }
        // validate the OWS request
        data = validator.validateResponse( request, data, mime, user );
        o = TP.doPostTrigger( this, request, data, mime, user );
        data = (byte[]) o[1];
        return data;

    }

}
