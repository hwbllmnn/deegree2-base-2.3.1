//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsrequestvalidator/wms/WMSValidator.java $
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
package org.deegree.security.owsrequestvalidator.wms;

import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceRequest;
import org.deegree.ogcwebservices.getcapabilities.GetCapabilities;
import org.deegree.ogcwebservices.wms.operation.GetFeatureInfo;
import org.deegree.ogcwebservices.wms.operation.GetLegendGraphic;
import org.deegree.ogcwebservices.wms.operation.GetMap;
import org.deegree.security.UnauthorizedException;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsrequestvalidator.Messages;
import org.deegree.security.owsrequestvalidator.OWSValidator;
import org.deegree.security.owsrequestvalidator.Policy;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10559 $, $Date: 2008-03-12 09:35:07 +0100 (Mi, 12. Mär 2008) $
 */

public class WMSValidator extends OWSValidator {

    // message strings
    private static final String MS_INVALIDREQUEST = Messages.getString( "WMSValidator.WMS_INVALIDREQUEST" );

    private GetMapRequestValidator getMapValidator;

    private GetMapResponseValidator getMapValidatorR;

    private GetFeatureInfoRequestValidator getFeatureInfoValidator;

    private GetFeatureInfoResponseValidator getFeatureInfoValidatorR;

    private GetLegendGraphicRequestValidator getLegendGraphicValidator;

    private GetLegendGraphicResponseValidator getLegendGraphicValidatorR;

    /**
     * @param policy
     * @param proxyURL
     */
    public WMSValidator( Policy policy, String proxyURL ) {
        super( policy, proxyURL );

        getMapValidator = new GetMapRequestValidator( policy );
        getMapValidatorR = new GetMapResponseValidator( policy );
        getFeatureInfoValidator = new GetFeatureInfoRequestValidator( policy );
        getFeatureInfoValidatorR = new GetFeatureInfoResponseValidator( policy );
        getLegendGraphicValidator = new GetLegendGraphicRequestValidator( policy );
        getLegendGraphicValidatorR = new GetLegendGraphicResponseValidator( policy );
    }

    /**
     * validates the passed <tt>OGCWebServiceRequest</tt> if it is valid against the defined
     * conditions for WMS requests
     * 
     * @param request
     * @param user
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    @Override
    public void validateRequest( OGCWebServiceRequest request, User user )
                            throws InvalidParameterValueException, UnauthorizedException {
        if ( request instanceof GetCapabilities ) {
            getCapabilitiesValidator.validateRequest( request, user );
        } else if ( request instanceof GetMap ) {
            getMapValidator.validateRequest( request, user );
        } else if ( request instanceof GetFeatureInfo ) {
            getFeatureInfoValidator.validateRequest( request, user );
        } else if ( request instanceof GetLegendGraphic ) {
            getLegendGraphicValidator.validateRequest( request, user );
        } else {
            throw new InvalidParameterValueException( MS_INVALIDREQUEST
                                                      + request.getClass().getName() );
        }
    }

    /**
     * @param request
     * @param response
     * @param mime
     * @param user
     * @return the byte array containing the response.
     * @throws InvalidParameterValueException
     * @throws UnauthorizedException
     */
    @Override
    public byte[] validateResponse( OGCWebServiceRequest request, byte[] response, String mime,
                                    User user )
                            throws InvalidParameterValueException, UnauthorizedException {
        if ( request instanceof GetCapabilities ) {
            response = getCapabilitiesValidatorR.validateResponse( "WMS", response, mime, user );
        } else if ( request instanceof GetMap ) {
            response = getMapValidatorR.validateResponse( "WMS", response, mime, user );
        } else if ( request instanceof GetFeatureInfo ) {
            response = getFeatureInfoValidatorR.validateResponse( "WMS", response, mime, user );
        } else if ( request instanceof GetLegendGraphic ) {
            response = getLegendGraphicValidatorR.validateResponse( "WMS", response, mime, user );
        } else {
            throw new InvalidParameterValueException( MS_INVALIDREQUEST
                                                      + request.getClass().getName() );
        }
        return response;
    }
}