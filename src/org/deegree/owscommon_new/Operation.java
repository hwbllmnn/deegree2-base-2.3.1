//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_new/Operation.java $
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

import java.util.List;

import org.deegree.datatypes.QualifiedName;

/**
 * <code>Operation</code> stores the contents of an Operation element according to the OWS common
 * specification version 1.0.0.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * 
 * @since 2.0
 */

public class Operation {

    private QualifiedName name = null;

    private List<DCP> dcpList = null;

    private List<Parameter> parameters = null;

    private List<DomainType> constraints = null;

    private Object metadata = null;

    private String description = null;

    /**
     * Standard constructor that initializes all encapsulated data.
     * 
     * @param name
     * @param dcpList
     * @param parameters
     * @param constraints
     * @param metadata
     * @param description
     */
    public Operation( QualifiedName name, List<DCP> dcpList, List<Parameter> parameters, List<DomainType> constraints,
                      Object metadata, String description ) {
        this.name = name;
        this.dcpList = dcpList;
        this.parameters = parameters;
        this.constraints = constraints;
        this.metadata = metadata;
        this.description = description;
    }

    /**
     * @return Returns the constraints.
     */
    public List<DomainType> getConstraints() {
        return constraints;
    }

    /**
     * @return Returns the dCP.
     */
    public List<DCP> getDCP() {
        return dcpList;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Returns the metadata.
     */
    public Object getMetadata() {
        return metadata;
    }

    /**
     * @return Returns the parameters.
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * @param name
     * @return the <code>DomainType</code> with the specified name or null, if there is no
     *         constraint with that name.
     */
    public DomainType getConstraint( QualifiedName name ) {
        for ( DomainType constraint : constraints ) {
            if ( constraint.getName().equals( name ) ) {
                return constraint;
            }
        }

        return null;
    }

    /**
     * @param name
     * @return the <code>Parameter</code> with the specified name or null, if there is no
     *         parameter with that name. This method only tests Parameters that are
     *         <code>DomainType</code>s.
     */
    public Parameter getParameter( QualifiedName name ) {
        for ( Parameter parameter : parameters ) {
            if ( parameter instanceof DomainType ) {
                if ( ( (DomainType) parameter ).getName().equals( name ) ) {
                    return parameter;
                }
            }
        }

        return null;
    }

    /**
     * @return Returns the name.
     */
    public QualifiedName getName() {
        return name;
    }

}
