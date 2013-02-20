//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/coordinatesystems/GeocentricCRS.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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

package org.deegree.crs.coordinatesystems;

import java.util.List;

import org.deegree.crs.Identifiable;
import org.deegree.crs.components.Axis;
import org.deegree.crs.components.GeodeticDatum;
import org.deegree.crs.transformations.polynomial.PolynomialTransformation;

/**
 * A <code>GeocentricCRS</code> is a coordinatesystem having three axis and a mass point defined to be equivalent to
 * earths center.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 10995 $, $Date: 2008-04-09 16:58:30 +0200 (Wed, 09 Apr 2008) $
 * 
 */

public class GeocentricCRS extends CoordinateSystem {

    /**
     * The default geocentric coordinate system. Geocentric datum is WGS84 and linear units are metre. The <var>X</var>
     * axis points towards the prime meridian (e.g. front). The <var>Y</var> axis points East. The <var>Z</var> axis
     * points North.
     */
    public static final GeocentricCRS WGS84 = new GeocentricCRS( GeodeticDatum.WGS84, "GC_WGS84", "Geocentric WGS84" );

    /**
     * @param datum
     * @param axisOrder
     * @param identity
     */
    public GeocentricCRS( GeodeticDatum datum, Axis[] axisOrder, Identifiable identity ) {
        this( null, datum, axisOrder, identity );
    }

    /**
     * @param datum
     * @param axisOrder
     * @param identifiers
     * @param names
     * @param versions
     * @param descriptions
     * @param areasOfUse
     */
    public GeocentricCRS( GeodeticDatum datum, Axis[] axisOrder, String[] identifiers, String[] names,
                          String[] versions, String[] descriptions, String[] areasOfUse ) {
        super( datum, axisOrder, identifiers, names, versions, descriptions, areasOfUse );
    }

    /**
     * @param datum
     * @param axisOrder
     * @param identifier
     * @param name
     * @param version
     * @param description
     * @param areaOfUse
     */
    public GeocentricCRS( GeodeticDatum datum, Axis[] axisOrder, String identifier, String name, String version,
                          String description, String areaOfUse ) {
        this( datum, axisOrder, new String[] { identifier }, new String[] { name }, new String[] { version },
              new String[] { description }, new String[] { areaOfUse } );
    }

    /**
     * @param datum
     * @param axisOrder
     * @param identifier
     */
    public GeocentricCRS( GeodeticDatum datum, Axis[] axisOrder, String identifier ) {
        this( datum, axisOrder, new String[] { identifier }, null, null, null, null );
    }

    /**
     * Geocentric crs with it's axis pointing to x=front, y=east, z=north.
     * 
     * @param datum
     * @param identifier
     * @param name
     */
    public GeocentricCRS( GeodeticDatum datum, String identifier, String name ) {
        this( datum, new Axis[] { new Axis( "X", Axis.AO_FRONT ), new Axis( "Y", Axis.AO_EAST ),
                                 new Axis( "Z", Axis.AO_NORTH ) }, new String[] { identifier }, new String[] { name },
              null, null, null );
    }

    /**
     * @param transformations
     * @param usedDatum
     * @param axisOrder
     * @param identity
     */
    public GeocentricCRS( List<PolynomialTransformation> transformations, GeodeticDatum usedDatum, Axis[] axisOrder,
                          Identifiable identity ) {
        super( transformations, usedDatum, axisOrder, identity );
    }

    @Override
    public int getType() {
        return CoordinateSystem.GEOCENTRIC_CRS;
    }

    @Override
    public int getDimension() {
        return 3;
    }
}
