//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon/com110/HTTP110.java $
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
 Aennchenstraße 19
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

package org.deegree.owscommon.com110;

import org.deegree.ogcwebservices.getcapabilities.Protocol;

/**
 * TODO class description
 * 
 * @author <a href="mailto:mays@lat-lon.de">Judit Mays</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 */
public class HTTP110 extends Protocol {

    private OWSRequestMethod[] getRequests;

    private OWSRequestMethod[] postRequests;

    /**
     * 
     * @param getRequests
     * @param postRequests
     */
    public HTTP110( OWSRequestMethod[] getRequests, OWSRequestMethod[] postRequests ) {

        this.getRequests = new OWSRequestMethod[getRequests.length];
        for ( int i = 0; i < getRequests.length; i++ ) {
            this.getRequests[i] = new OWSRequestMethod( getRequests[i].getLink(), getRequests[i].getConstraints() );
        }

        this.postRequests = new OWSRequestMethod[postRequests.length];
        for ( int i = 0; i < postRequests.length; i++ ) {
            this.postRequests[i] = new OWSRequestMethod( postRequests[i].getLink(), postRequests[i].getConstraints() );
        }

    }

    /**
     * @return Returns the getRequests.
     */
    public OWSRequestMethod[] getGetRequests() {
        return getRequests;
    }

    /**
     * @return Returns the postRequests.
     */
    public OWSRequestMethod[] getPostRequests() {
        return postRequests;
    }

}
