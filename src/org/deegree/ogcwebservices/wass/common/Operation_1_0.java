//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wass/common/Operation_1_0.java $
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

package org.deegree.ogcwebservices.wass.common;

import org.deegree.ogcwebservices.getcapabilities.DCPType;
import org.deegree.ogcwebservices.getcapabilities.Operation;
import org.deegree.owscommon.OWSDomainType;
import org.deegree.owscommon.OWSMetadata;

/**
 * The bean for the 1.0 Operation.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class Operation_1_0 extends Operation {

    private static final long serialVersionUID = -6411485975718337498L;

    private OWSDomainType[] constraints = null;

    /**
     * @param name
     *            the Name of the operation
     * @param dcps
     *            the description of the request type.
     * @param parameters
     * @param constraints
     * @param metadata
     */
    public Operation_1_0( String name, DCPType[] dcps, OWSDomainType[] parameters,
                         OWSDomainType[] constraints, OWSMetadata[] metadata ) {
        super( name, dcps, parameters );
        this.constraints = constraints;
        setMetadata( metadata );
    }

    /**
     * @return Returns the constraints.
     */
    public OWSDomainType[] getConstraints() {
        return constraints;
    }

    /**
     * @param constraints
     *            The constraints to set.
     */
    public void setConstraints( OWSDomainType[] constraints ) {
        this.constraints = constraints;
    }

}
