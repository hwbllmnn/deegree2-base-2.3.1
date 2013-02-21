//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/shpapi/SHPPoint3D.java $
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
package org.deegree.io.shpapi;

import org.deegree.model.spatialschema.ByteUtils;
import org.deegree.model.spatialschema.Position;

/**
 * 
 * 
 * 
 * @version $Revision: 12146 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version 1.0. $Revision: 12146 $, $Date: 2008-06-04 12:18:44 +0200 (Mi, 04. Jun 2008) $
 * 
 * @since 2.0
 */
public class SHPPoint3D extends SHPPoint {

    /**
     * 
     */
    public double z;

    /**
     * 
     */
    public SHPPoint3D() {
        super();
    }

    /**
     * @param recBuf
     * @param xStart
     * @param numPoints 
     */
    public SHPPoint3D( byte[] recBuf, int xStart, int numPoints ) {
        super( recBuf, xStart );
        // TODO is it possible to read z values w/o telling the no of points?

        this.z = ByteUtils.readLEDouble( recBuffer, xStart + 16 * numPoints );
        // System.out.println(x + ", " + y +" _ " + z);

    }

    /**
     * @param point
     */
    public SHPPoint3D( Position point ) {
        super( point );
        this.z = point.getZ();
    }

    /**
     * 
     * @param x
     * @param y
     * @param z
     */
    public SHPPoint3D( double x, double y, double z ) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "SHPPOINT" + "[" + this.x + "; " + this.y + "; " + this.z + "]";
    }

}