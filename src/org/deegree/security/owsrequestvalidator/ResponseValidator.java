//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsrequestvalidator/ResponseValidator.java $
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
package org.deegree.security.owsrequestvalidator;

import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.security.UnauthorizedException;
import org.deegree.security.drm.model.User;
import org.deegree.security.owsproxy.Condition;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.1, $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 1.1
 */

public abstract class ResponseValidator {

    protected static final String UNKNOWNMIMETYPE = Messages.getString( "ResponseValidator.UNKNOWNMIMETYPE" );

    protected Policy policy = null;

    protected GeneralPolicyValidator gpv = null;

    /**
     * @param policy
     */
    public ResponseValidator( Policy policy ) {
        this.policy = policy;
        Condition cond = policy.getGeneralCondition();
        gpv = new GeneralPolicyValidator( cond );
    }

    /**
     * @return Returns the policy.
     * 
     */
    public Policy getPolicy() {
        return policy;
    }

    /**
     * @param policy
     *            The policy to set.
     */
    public void setPolicy( Policy policy ) {
        this.policy = policy;
    }

    /**
     * validates if the passed response itself and its content is valid against the conditions
     * defined in the policies assigned to a <tt>OWSPolicyValidator</tt>
     * 
     * @param service
     *            service which produced the response (WMS, WFS ...)
     * @param response
     * @param mime
     *            mime-type of the response
     * @param user
     */
    public abstract byte[] validateResponse( String service, byte[] response, String mime, User user )
                            throws InvalidParameterValueException, UnauthorizedException;

}
