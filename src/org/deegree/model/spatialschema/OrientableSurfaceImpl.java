//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/spatialschema/OrientableSurfaceImpl.java $
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
package org.deegree.model.spatialschema;

import java.io.Serializable;

import org.deegree.model.crs.CoordinateSystem;

/**
 * default implementation of the OrientableSurface interface from package deegree.model. the implementation is abstract
 * because only initialization of the spatial reference system is unique to all orientated surfaces
 * 
 * <p>
 * -----------------------------------------------------------------------
 * </p>
 * 
 * @version 05.04.2002
 * @author Andreas Poth
 */
public abstract class OrientableSurfaceImpl extends OrientablePrimitiveImpl implements OrientableSurface, Serializable {
    /** Use serialVersionUID for interoperability. */
    private final static long serialVersionUID = 4169996004405925850L;

    /**
     * Creates a new OrientableSurfaceImpl object.
     * 
     * @param crs
     * 
     * @throws GeometryException
     */
    protected OrientableSurfaceImpl( CoordinateSystem crs ) throws GeometryException {
        super( crs, '+' );
    }

    /**
     * Creates a new OrientableSurfaceImpl object.
     * 
     * @param crs
     * @param orientation
     * 
     * @throws GeometryException
     */
    protected OrientableSurfaceImpl( CoordinateSystem crs, char orientation ) throws GeometryException {
        super( crs, orientation );
    }
}
