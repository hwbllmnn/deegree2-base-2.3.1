//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/ModuleConfiguration.java $
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
package org.deegree.portal.context;

import java.net.URL;

/**
 * provides the connection point where to access the configuration of a module
 * 
 * @version $Revision: 10906 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public class ModuleConfiguration {
    private URL onlineResource = null;

    /**
     * Creates a new ModuleConfiguration_Impl object.
     * 
     * @param onlineResource
     */
    public ModuleConfiguration( URL onlineResource ) {
        setOnlineResource( onlineResource );
    }

    /**
     * returns the online resource where to access the configuration document for a module
     * 
     * @return the online resource where to access the configuration document for a module
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * sets the online resource where to access the configuration document for a module
     * @param onlineResource access to the configuration document for a module
     * 
     */
    public void setOnlineResource( URL onlineResource ) {
        this.onlineResource = onlineResource;
    }

}