//$$Header: $$
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

package org.deegree.portal.owswatch.validator;

import java.io.InputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpMethodBase;

import org.deegree.portal.owswatch.Messages;
import org.deegree.portal.owswatch.Status;
import org.deegree.portal.owswatch.ValidatorResponse;

/**
 * A specific implementation of AbstractValidator
 * 
 * @author <a href="mailto:elmasry@lat-lon.de">Moataz Elmasry</a>
 * @author last edited by: $Author: elmasry $
 * 
 * @version $Revision: 1.2 $, $Date: 2008-03-20 11:30:24 $
 */
public class WCSGetCoverageValidator extends AbstractValidator implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2869864955426400441L;

    /*
     * (non-Javadoc)
     * 
     * @see org.deegree.portal.owswatch.validator.AbstractValidator#validateAnswer(org.apache.commons.httpclient.HttpMethodBase,
     *      int)
     */
    @Override
    public ValidatorResponse validateAnswer( HttpMethodBase method, int statusCode ) {

        String lastMessage = null;
        Status status = null;
        String contentType = method.getResponseHeader( "Content-Type" ).getValue();
        if ( contentType.contains( "image" ) ) {
            try {
                InputStream stream = copyStream( method.getResponseBodyAsStream() );
                stream.reset();
                ImageIO.read( stream );
                status = Status.RESULT_STATE_AVAILABLE;
                lastMessage = status.getStatusMessage();
                return new ValidatorResponse( lastMessage, status );
            } catch ( Exception e ) {
                status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
                lastMessage = e.getLocalizedMessage();
                return new ValidatorResponse( lastMessage, status );
            }
        } else if ( contentType.contains( "xml" ) ) {
            return validateXml( method );
        } else {
            StringBuilder builder = new StringBuilder( "Response content is: " );
            builder.append( contentType );
            status = Status.RESULT_STATE_SERVICE_UNAVAILABLE;
            lastMessage = Messages.getMessage( "ERROR_INCORRECT_RESP_CONTENT2", contentType, "image", "xml" );
            return new ValidatorResponse( lastMessage, status );
        }
    }
}
