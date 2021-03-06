//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/transformations/coordinate/NotSupportedTransformation.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2007 by:
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

package org.deegree.crs.transformations.coordinate;

import java.util.List;

import javax.vecmath.Point3d;

import org.deegree.crs.Identifiable;
import org.deegree.crs.coordinatesystems.CoordinateSystem;
import org.deegree.crs.exceptions.TransformationException;

/**
 * The <code>NotSupportedTransformation</code> class simply wraps the source and target crs. This transformation
 * doesn't do anything, it only provides an opportunity to create a transformation chain, without losing the source and
 * target information as well as the causality of actually having to implement anything. Note that incoming points are
 * returned immediately.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14551 $, $Date: 2008-10-29 17:56:19 +0100 (Mi, 29. Okt 2008) $
 * 
 */
public class NotSupportedTransformation extends CRSTransformation {

    /**
     * @param sourceCRS
     * @param targetCRS
     * @param id
     */
    public NotSupportedTransformation( CoordinateSystem sourceCRS, CoordinateSystem targetCRS, Identifiable id ) {
        super( sourceCRS, targetCRS, id );
    }

    /**
     * @param sourceCRS
     * @param targetCRS
     */
    public NotSupportedTransformation( CoordinateSystem sourceCRS, CoordinateSystem targetCRS ) {
        this( sourceCRS, targetCRS, new Identifiable( createFromTo( sourceCRS.getIdentifier(),
                                                                    targetCRS.getIdentifier() ) ) );
    }

    @Override
    public String getImplementationName() {
        return "NotSupportedTransformation";
    }

    @Override
    public List<Point3d> doTransform( List<Point3d> srcPts )
                            throws TransformationException {
        return srcPts;
    }

    @Override
    public boolean isIdentity() {
        return true;
    }

}
