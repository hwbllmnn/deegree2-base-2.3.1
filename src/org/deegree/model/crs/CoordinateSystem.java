//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/crs/CoordinateSystem.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree
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

 Klaus Greve
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: klaus.greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.model.crs;

// OpenGIS dependencies
import java.net.URI;

import org.deegree.crs.components.Unit;
import org.deegree.datatypes.QualifiedName;

/**
 * A coordinate system is a mathematical space, where the elements of the space are called positions. Each position is
 * described by a list of numbers. The length of the list corresponds to the dimension of the coordinate system. So in a
 * 2D coordinate system each position is described by a list containing 2 numbers. <br>
 * <br>
 * However, in a coordinate system, not all lists of numbers correspond to a position - some lists may be outside the
 * domain of the coordinate system. For example, in a 2D Lat/Lon coordinate system, the list (91,91) does not correspond
 * to a position. <br>
 * <br>
 * Some coordinate systems also have a mapping from the mathematical space into locations in the real world. So in a
 * Lat/Lon coordinate system, the mathematical position (lat, long) corresponds to a location on the surface of the
 * Earth. This mapping from the mathematical space into real-world locations is called a Datum.
 * 
 * @version $Revision: 15000 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 15000 $, $Date: 2008-11-21 09:42:09 +0100 (Fr, 21. Nov 2008) $
 * 
 * @since 2.0
 */
public class CoordinateSystem extends QualifiedName {

    /**
     * 
     */
    private static final long serialVersionUID = -170831086069691683L;

    private org.deegree.crs.coordinatesystems.CoordinateSystem realCRS;

    private final String requestedID;

    /**
     * 
     * @param prefix
     *            must be not null
     * @param localName
     * @param namespace
     * @deprecated use {@link #CoordinateSystem(org.deegree.crs.coordinatesystems.CoordinateSystem, String)} instead
     */
    @Deprecated
    CoordinateSystem( String prefix, String localName, URI namespace ) {
        super( prefix.toLowerCase(), localName, namespace );
        requestedID = localName;
    }

    /**
     * 
     * @param name
     * @param namespace
     * @deprecated use {@link #CoordinateSystem(org.deegree.crs.coordinatesystems.CoordinateSystem, String)}
     */
    @Deprecated
    CoordinateSystem( String name, URI namespace ) {
        super( name, namespace );
        requestedID = name;
    }

    /**
     * e.g. epgs:4326, should not be used.
     * 
     * @param name
     * @deprecated use CoordinateSystem(org.deegree.crs.coordinatesystems.CoordinateSystem, String)
     */
    @Deprecated
    protected CoordinateSystem( String name ) {
        super( name );
        requestedID = name;
    }

    /**
     * Creates a CoordinateSystem as a wrapper to the real CRS.
     * 
     * @param realCRS
     */
    CoordinateSystem( org.deegree.crs.coordinatesystems.CoordinateSystem realCRS, String requestedID ) {
        super( realCRS.getIdentifier() );
        this.realCRS = realCRS;
        this.requestedID = requestedID;
    }

    /**
     * @param realCRS
     *            to wrap
     */
    CoordinateSystem( org.deegree.crs.coordinatesystems.CoordinateSystem realCRS ) {
        this( realCRS, realCRS.getIdentifier() );
    }

    /**
     * returns the name of the CRS, this method is actually mapped to {@link #getIdentifier()}
     * 
     * @return the name of the CRS
     * @deprecated use {@link #getIdentifier()} instead
     */
    @Deprecated
    public String getName() {
        return getIdentifier();
    }

    /**
     * This method returns the requested id (given in the constructor) and not the
     * {@link org.deegree.crs.coordinatesystems.CoordinateSystem#getIdentifier()} which only returns the first
     * configured id.
     * 
     * @return the requested id.
     */
    public String getIdentifier() {
        return requestedID;
    }

    /**
     * Since the crs uses a different namespace system as {@link QualifiedName} this method only returns the
     * {@link #getIdentifier()}.
     */
    @Override
    public String getPrefixedName() {
        return getIdentifier();
    }

    /**
     * returns the CRSs code. In case of EPSG:4326 it will be 4326; in case of adv:DE_DHDN_3GK2_NW177 it will be
     * DE_DHDN_3GK2_NW177
     * 
     * @return the CRSs code
     * @deprecated has no relevance, returns a bogus value.
     */
    @Deprecated
    public String getCode() {
        return getLocalName();
    }

    /**
     * returns the unit of the first axis of the encapsulated CRS
     * 
     * @return the units of the first axis of the encapsulated CRS
     * @deprecated this method returns the unit of the fist axis, assuming all axis have the same unit, which is not
     *             necessarily so. It is better to use the {@link  #getAxisUnits()} and iterate over all units.
     */
    @Deprecated
    public String getUnits() {
        return realCRS.getUnits()[0].toString();
    }

    /**
     * @return the units of all axis.
     */
    public Unit[] getAxisUnits() {
        return realCRS.getUnits();
    }

    /**
     * @return the dimension of the encapsulated CRS
     */
    public int getDimension() {
        return realCRS.getDimension();
    }

    /**
     * @return the encapsulated CRS.
     */
    public org.deegree.crs.coordinatesystems.CoordinateSystem getCRS() {
        return realCRS;
    }

    @Override
    public boolean equals( Object other ) {
        if ( other != null && other instanceof CoordinateSystem ) {
            final CoordinateSystem that = (CoordinateSystem) other;
            return this.realCRS.equals( that.realCRS );
        }
        return false;
    }
}
