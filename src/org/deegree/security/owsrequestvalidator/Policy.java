//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsrequestvalidator/Policy.java $
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

import java.util.HashMap;
import java.util.Map;

import org.deegree.security.owsproxy.Condition;
import org.deegree.security.owsproxy.Request;
import org.deegree.security.owsproxy.SecurityConfig;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */

public class Policy {

    private SecurityConfig securityConfig;

    private Map<String, Request> requests;

    private Condition generalCondition;

    /**
     * @param securityConfig
     *            configuration for accessing user based security/right informations
     * @param requests
     *            description of constraints for several OWS requests
     * @param generalCondition
     *            general security/right constraints
     */
    public Policy( SecurityConfig securityConfig, Condition generalCondition, Request[] requests ) {
        this.requests = new HashMap<String, Request>();
        this.securityConfig = securityConfig;
        this.generalCondition = generalCondition;
        setRequests( requests );
    }

    /**
     * returns the requests/condintions described by a <tt>Policy</tt>. A request objects
     * contains conditions for each parameter and maybe for combinations of two or more parameters.
     * 
     * @return the requests/condintions described by a <tt>Policy</tt>.
     * 
     */
    public Request[] getRequests() {
        Request[] req = new Request[requests.size()];
        return requests.values().toArray( req );
    }

    /**
     * returns one request/condintionset from the <tt>Policy</tt> matching the passed service and
     * request name. If no request for the passed combination of service and request name is
     * registered <tt>null</tt> will be returned
     * 
     * @see #getRequests()
     * @param service
     * @param request
     * @return one request/condintionset from the <tt>Policy</tt> matching the passed service and
     *         request name. If no request for the passed combination of service and request name is
     *         registered <tt>null</tt> will be returned
     */
    public Request getRequest( String service, String request ) {
        return requests.get( service + ':' + request );
    }

    /**
     * sets the requests/condintions described by a <tt>Policy</tt>
     * 
     * @see #getRequests()
     * @param requests
     */
    public void setRequests( Request[] requests ) {
        this.requests.clear();
        for ( int i = 0; i < requests.length; i++ ) {
            addRequest( requests[i] );
        }
    }

    /**
     * adds a request/condintions to the <tt>Policy</tt>
     * 
     * @see #getRequests()
     * @param request
     */
    public void addRequest( Request request ) {
        String key = request.getService() + ':' + request.getName();
        this.requests.put( key, request );
    }

    /**
     * removes a request/condintions from the Policy
     * 
     * @see #getRequests()
     * @param service
     * @param name
     */
    public void removeRequest( String service, String name ) {
        requests.remove( service + ':' + name );
    }

    /**
     * sets the configuration for access to the configuration of the security persistence mechanim
     * 
     * @return securityConfig
     * 
     */
    public SecurityConfig getSecurityConfig() {
        return securityConfig;
    }

    /**
     * returns the general conditions that must be fullfilled by a request
     * 
     * @return the general conditions that must be fullfilled by a request
     * 
     */
    public Condition getGeneralCondition() {
        return generalCondition;
    }

}
