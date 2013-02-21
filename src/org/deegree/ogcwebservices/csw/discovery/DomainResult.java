//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/csw/discovery/DomainResult.java $
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
package org.deegree.ogcwebservices.csw.discovery;

import org.deegree.ogcwebservices.DefaultOGCWebServiceResponse;

/**
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:tfr@users.sourceforge.net">Torsten Friebe </a>
 * 
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 11997 $, $Date: 2008-05-29 09:57:58 +0200 (Do, 29. Mai 2008) $
 */

public class DomainResult extends DefaultOGCWebServiceResponse {

    private DomainValues[] m_DomainValues;

    /**
     * 
     * @param request
     */
    public DomainResult( GetDomain request ) {
        super( request );
    }

    /**
     * @exception Throwable
     */
    @Override
    public void finalize()
                            throws Throwable {
        // why is this overridden?
    }

    /**
     * returns all domain values
     * 
     * @return all domain values
     */
    public DomainValues[] getDomainValues() {
        return m_DomainValues;
    }

}