//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/getcoverage/AxisSubset.java $
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
package org.deegree.ogcwebservices.wcs.getcoverage;

import org.deegree.datatypes.values.Interval;
import org.deegree.datatypes.values.TypedLiteral;
import org.deegree.datatypes.values.ValueEnumBase;

/**
 * 
 * 
 * @version $Revision: 9345 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class AxisSubset extends ValueEnumBase {

    private String name = null;

    /**
     * @param interval
     * @param name
     * @throws IllegalArgumentException
     */
    public AxisSubset( Interval[] interval, String name ) throws IllegalArgumentException {
        super( interval );
        this.name = name;
    }

    /**
     * @param singleValue
     * @param name
     * @throws IllegalArgumentException
     */
    public AxisSubset( TypedLiteral[] singleValue, String name ) throws IllegalArgumentException {
        super( singleValue );
        this.name = name;
    }

    /**
     * @param interval
     * @param singleValue
     * @param name
     */
    public AxisSubset( Interval[] interval, TypedLiteral[] singleValue, String name ) throws IllegalArgumentException {
        super( interval, singleValue );
        this.name = name;
    }

    /**
     * @return Returns the name.
     * 
     */
    public String getName() {
        return name;
    }

}