//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/coordinatesystems/CompoundCRS.java $
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

import org.deegree.crs.Identifiable;
import org.deegree.crs.components.Axis;
import org.deegree.crs.components.Unit;

/**
 * A <code>CompoundCRS</code> is a {@link GeographicCRS} with a third axis (the height axis) attached. This axis
 * denotes a vertical axis, which defines a unit for a given height above or below the datums surface.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11235 $, $Date: 2008-04-17 12:33:17 +0200 (Thu, 17 Apr 2008) $
 * 
 */
public class CompoundCRS extends CoordinateSystem {

    private CoordinateSystem underlyingCRS;

    private final Axis[] axis;

    private final double defaultHeight;

    /**
     * @param heightAxis
     *            defining the name and the units of the 'vertical' axis
     * @param underlyingCRS
     *            the crs to which the height axis is added, only geographic, geocentric or projected crs are valid.
     * @param defaultHeight
     *            of the any coordinate which does not have a height-axis-value.
     * @param identity
     *            containing the identifiable values.
     * @throws IllegalArgumentException
     *             if the underlying crs is not of type geographic, geocentric or projected or one of the other values
     *             is <code>null</code>.
     */
    public CompoundCRS( Axis heightAxis, CoordinateSystem underlyingCRS, double defaultHeight, Identifiable identity )
                            throws IllegalArgumentException {
        super( underlyingCRS.getGeodeticDatum(), underlyingCRS.getAxis(), identity );
        int tmp = underlyingCRS.getType();
        if ( tmp != CoordinateSystem.GEOCENTRIC_CRS && tmp != CoordinateSystem.PROJECTED_CRS
             && tmp != CoordinateSystem.GEOGRAPHIC_CRS ) {
            throw new IllegalArgumentException(
                                                "A compound crs can only have a geographic, projected or geocentric crs as underlying coordinate system." );
        }
        checkForNullObject( heightAxis, "CompoundCRS", "heightAxis" );
        this.underlyingCRS = underlyingCRS;
        axis = new Axis[3];
        axis[0] = underlyingCRS.getAxis()[0];
        axis[1] = underlyingCRS.getAxis()[1];
        axis[2] = heightAxis;
        this.defaultHeight = defaultHeight;
    }

    @Override
    public int getDimension() {
        return 3;
    }

    @Override
    public final int getType() {
        return COMPOUND_CRS;
    }

    /**
     * @return the heightAxis.
     */
    public final Axis getHeightAxis() {
        return axis[2];
    }

    /**
     * @return the units of the heightAxis.
     */
    public final Unit getHeightUnits() {
        return axis[2].getUnits();
    }

    /**
     * @return the geographic Axis and the heightAxis as the third component.
     */
    @Override
    public Axis[] getAxis() {
        return axis;
    }

    /**
     * @return the underlyingCRS.
     */
    public final CoordinateSystem getUnderlyingCRS() {
        return underlyingCRS;
    }

    /**
     * @return the defaultHeight or 0 if it was not set.
     */
    public double getDefaultHeight() {
        return defaultHeight;
    }

}
