//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/processing/raster/RawFloatDataMatrix.java $
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
package org.deegree.processing.raster;

/**
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.0. $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 */
public class RawFloatDataMatrix implements DataMatrix {

    private float[][][] data = null;

    /**
     * 
     * @param data
     */
    public RawFloatDataMatrix( float[][][] data ) {
        this.data = data;
    }

    /**
     * returns the data vector at the passed index position
     * 
     * @param x
     * @param y
     * @return the data vector at the passed index position
     */
    public double[] getCellAt( int x, int y ) {
        double[] d = new double[data.length];
        for ( int i = 0; i < d.length; i++ ) {
            d[i] = data[i][y][x];
        }
        return d;
    }

    /**
     * returns the data matrix width (number of cells in x-direction)
     * 
     * @return the data matrix width (number of cells in x-direction)
     */
    public int getHeight() {
        return data[0].length;
    }

    /**
     * returns the data matrix height (number of cells in y-direction)
     * 
     * @return the data matrix height (number of cells in y-direction)
     */
    public int getWidth() {
        return data[0][0].length;
    }

}
