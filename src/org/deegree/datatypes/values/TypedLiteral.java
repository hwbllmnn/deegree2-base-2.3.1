//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/datatypes/values/TypedLiteral.java $
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
package org.deegree.datatypes.values;

import java.io.Serializable;
import java.net.URI;

/**
 * 
 * 
 * @version $Revision: 12296 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 12296 $, $Date: 2008-06-11 11:08:58 +0200 (Mi, 11. Jun 2008) $
 * 
 * @since 2.0
 */
public class TypedLiteral implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String value = null;

    private URI type = null;

    /**
     * Identifies the unit of measure of this literal input or output. This unit of measure should
     * be referenced for any numerical value that has units (e.g., "meters", but not a more complete
     * reference system). Shall be a UOM identified in the Process description for this input or
     * output.
     */
    protected URI uom;

    /**
     * initializes a <code>TypedLiteral</code> with <code>this.uom = null;</code>
     * 
     * @param value
     * @param type
     */
    public TypedLiteral( String value, URI type ) {
        this.value = value;
        this.type = type;
    }

    /**
     * @param value
     * @param type
     * @param uom
     *            units of measure
     */
    public TypedLiteral( String value, URI type, URI uom ) {
        this.value = value;
        this.type = type;
        this.uom = uom;
    }

    /**
     * @return Returns the type.
     * 
     */
    public URI getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     * 
     */
    public void setType( URI type ) {
        this.type = type;
    }

    /**
     * @return Returns the value.
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     * 
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * returns the units a <code>TypedLiteral</code> is measured; maybe <code>null</code>
     * 
     * @return the units a <code>TypedLiteral</code> is measured; maybe <code>null</code>
     */
    public URI getUom() {
        return uom;
    }

    /**
     * @param value to set from
     */
    public void setUom( URI value ) {
        this.uom = value;
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        return new TypedLiteral( value, type, uom );
    }

}
