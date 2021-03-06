//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_new/ServiceProvider.java $
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
package org.deegree.owscommon_new;

import org.deegree.model.metadata.iso19115.CitedResponsibleParty;
import org.deegree.model.metadata.iso19115.OnlineResource;

/**
 * <code>ServiceProvider</code> stores metadata contained within a ServiceProvider element
 * according to the OWS common specification version 1.0.0.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 */

public class ServiceProvider {

    private String providerName = null;

    private OnlineResource providerSite = null;

    private CitedResponsibleParty serviceContact = null;

    /**
     * Standard constructor that initializes all encapsulated data.
     * 
     * @param providerName
     * @param providerSite
     * @param serviceContact
     */
    public ServiceProvider( String providerName, OnlineResource providerSite, CitedResponsibleParty serviceContact ) {
        this.providerName = providerName;
        this.providerSite = providerSite;
        this.serviceContact = serviceContact;
    }

    /**
     * @return Returns the providerName.
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * @return Returns the providerSite.
     */
    public OnlineResource getProviderSite() {
        return providerSite;
    }

    /**
     * @return Returns the serviceContact.
     */
    public CitedResponsibleParty getServiceContact() {
        return serviceContact;
    }

}
