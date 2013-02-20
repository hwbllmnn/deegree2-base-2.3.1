//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/crs/projections/conic/LambertConformalConicTest.java $
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

package org.deegree.crs.projections.conic;

import javax.vecmath.Point2d;

import org.deegree.crs.components.Unit;
import org.deegree.crs.exceptions.ProjectionException;
import org.deegree.crs.projections.ProjectionTest;

/**
 * <code>StereographicAlternativeTest</code> test the lambert conformal conic projection
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11229 $, $Date: 2008-04-17 12:25:51 +0200 (Thu, 17 Apr 2008) $
 * 
 */
public class LambertConformalConicTest extends ProjectionTest {

    private static final LambertConformalConic projection_26985 = new LambertConformalConic(
                                                                                             Math.toRadians( 39.45 ),
                                                                                             Math.toRadians( 38.3 ),
                                                                                             geographic_4258,
                                                                                             0,
                                                                                             400000.0,
                                                                                             new Point2d(
                                                                                                          Math.toRadians( -77 ),
                                                                                                          Math.toRadians( 37.66666666666665 ) ),
                                                                                             Unit.METRE );

    /**
     * reference point created with proj4 command : <code>
     * proj -f "%.8f" +proj=lcc +ellps=GRS80 +lon_0=-77 +lat_0=37.66666666665 +k=1 +x_0=400000 +y_0=0 +lat_1=39.45 +lat_2=38.3
     * 6.610765 53.235916
     * 5402441.35292079        4213918.86230420
     * </code>
     * 
     * @throws ProjectionException
     */
    public void testAccuracy()
                            throws ProjectionException {

        Point2d sourcePoint = new Point2d( Math.toRadians( 6.610765 ), Math.toRadians( 53.235916 ) );
        Point2d targetPoint = new Point2d( 5402441.35292079, 4213918.86230420 );

        doForwardAndInverse( projection_26985, sourcePoint, targetPoint );
    }

    /**
     * tests the consistency of the {@link LambertConformalConic} projection.
     */
    public void testConsistency() {
        consistencyTest( projection_26985, 0, 400000, new Point2d( Math.toRadians( -77 ),
                                                                   Math.toRadians( 37.66666666666665 ) ), Unit.METRE,
                         1, true, false, "lambertConformalConic" );
        assertEquals( Math.toRadians( 39.45 ), projection_26985.getFirstParallelLatitude() );
        assertEquals( Math.toRadians( 38.3 ), projection_26985.getSecondParallelLatitude() );
    }

}
