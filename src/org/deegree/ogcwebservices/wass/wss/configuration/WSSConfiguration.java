//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/wss/configuration/WSSConfiguration.java $
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

package org.deegree.ogcwebservices.wass.wss.configuration;

import org.deegree.ogcwebservices.wass.wss.capabilities.WSSCapabilities;

/**
 * The configuration class represents the capabilities and the "deegree specific parameter" which
 * will be sent back to the clienst getCapabilities request.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */

public class WSSConfiguration extends WSSCapabilities {

    private static final long serialVersionUID = -1135265074478652811L;

    private WSSDeegreeParams deegreeParams = null;

    /**
     * @param capabilities
     * @param deegreeParams
     */
    public WSSConfiguration( WSSCapabilities capabilities, WSSDeegreeParams deegreeParams ) {
        super( capabilities.getVersion(), capabilities.getUpdateSequence(),
               capabilities.getServiceIdentification(), capabilities.getServiceProvider(),
               capabilities.getOperationsMetadata(), capabilities.getSecuredServiceType(),
               capabilities.getAuthenticationMethods() );
        this.deegreeParams = deegreeParams;
    }
    
    

    /**
     * @return Returns the deegree specific Parameters.
     */
    public WSSDeegreeParams getDeegreeParams() {
        return deegreeParams;
    }

}
