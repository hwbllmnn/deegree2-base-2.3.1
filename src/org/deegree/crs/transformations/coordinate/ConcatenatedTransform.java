//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/transformations/coordinate/ConcatenatedTransform.java $
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

package org.deegree.crs.transformations.coordinate;

import java.util.List;

import javax.vecmath.Point3d;

import org.deegree.crs.Identifiable;
import org.deegree.crs.exceptions.TransformationException;
import org.deegree.crs.transformations.Transformation;

/**
 * The <code>ConcatenatedTransform</code> class allows the connection of two transformations.
 * <p>
 * Calling inverse on this transformation will invert the whole underlying transformation chain. For example, if A * (B
 * *C)=D and D is this transformation calling D.inverse() will result in (C.inverse * B.inverse) * A.inverse.
 * </p>
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 14974 $, $Date: 2008-11-20 16:53:04 +0100 (Do, 20. Nov 2008) $
 * 
 */

public class ConcatenatedTransform extends CRSTransformation {

    private boolean isIdentitiy = false;

    private Transformation firstTransform;

    private Transformation secondTransform;

    /**
     * Creates a transform by concatenating two existing transforms. A concatenated transform applies two transforms,
     * one after the other. The dimension of the output space of the first transform must match the dimension of the
     * input space in the second transform.
     * 
     * @param first
     *            The first transformation to apply to given points.
     * @param second
     *            The second transformation to apply to given points.
     * @param id
     *            an identifiable instance containing information about this transformation
     */
    public ConcatenatedTransform( Transformation first, Transformation second, Identifiable id ) {
        super( first.getSourceCRS(), second.getTargetCRS(), id );
        if ( first.isIdentity() && second.isIdentity() ) {
            isIdentitiy = true;
        }
        firstTransform = first;
        secondTransform = second;
    }

    /**
     * Creates a transform by concatenating two existing transforms. A concatenated transform applies two transforms,
     * one after the other. The dimension of the output space of the first transform must match the dimension of the
     * input space in the second transform.
     * 
     * Creates an Identifiable using the {@link CRSTransformation#createFromTo(String, String)} method.
     * 
     * @param first
     *            The first transformation to apply to given points.
     * @param second
     *            The second transformation to apply to given points.
     */
    public ConcatenatedTransform( Transformation first, Transformation second ) {
        this( first, second, new Identifiable( Transformation.createFromTo( first.getIdentifier(),
                                                                            second.getIdentifier() ) ) );
    }

    @Override
    public List<Point3d> doTransform( List<Point3d> srcPts )
                            throws TransformationException {
        if ( !isIdentitiy ) {
            List<Point3d> dest = firstTransform.doTransform( srcPts );
            return secondTransform.doTransform( dest );
        }
        return srcPts;
    }

    @Override
    public void inverse() {
        super.inverse();
        Transformation tmp = firstTransform;
        firstTransform = secondTransform;
        secondTransform = tmp;
        firstTransform.inverse();
        secondTransform.inverse();
    }

    @Override
    public boolean isIdentity() {
        return isIdentitiy;
    }

    /**
     * @return the firstTransform, which is the second transformation if this transform is inverse.
     */
    public final Transformation getFirstTransform() {
        return firstTransform;
    }

    /**
     * @return the secondTransform, which is the first transformation if this transform is inverse.
     */
    public final Transformation getSecondTransform() {
        return secondTransform;
    }

    @Override
    public String getImplementationName() {
        return "Concatenated-Transform";
    }

}
