//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/quadtree/MemPointQuadtree.java $
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
package org.deegree.io.quadtree;

import java.util.ArrayList;
import java.util.List;

import org.deegree.model.spatialschema.Envelope;
import org.deegree.model.spatialschema.GeometryFactory;
import org.deegree.model.spatialschema.Point;

/**
 * <code>MemPointQuadtree</code> is a memory based quadtree implementation for points.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 * @param <T>
 *            the datatype to be used as id
 * 
 * @since 2.0
 */

public class MemPointQuadtree<T> implements Quadtree<T> {

    private MemPointNode<T> root;

    private double accuracyX = 0.0001;

    private double accuracyY = 0.0001;

    int maxDepth = 8;

    /**
     * Creates a new instance with the specified region. This envelope cannot be changed.
     * 
     * @param region
     */
    public MemPointQuadtree( Envelope region ) {
        root = new MemPointNode<T>( this, region, 0 );
    }

    /**
     * Creates a new instance with the specified region. This envelope cannot be changed.
     * 
     * @param region
     * @param accuracyX
     * @param accuracyY
     */
    public MemPointQuadtree( Envelope region, double accuracyX, double accuracyY ) {
        root = new MemPointNode<T>( this, region, 0 );
        this.accuracyX = accuracyX;
        this.accuracyY = accuracyY;
    }

    /**
     * Inserts the item with the envelope into the quadtree.
     * 
     * @param item
     * @param envelope
     */
    public void insert( T item, Envelope envelope )
                            throws IndexException {
        root.insert( item, envelope );
    }

    /**
     * Inserts the item with the given point as envelope.
     * 
     * @param item
     * @param point
     */
    public void insert( T item, Point point )
                            throws IndexException {
        Envelope envelope = GeometryFactory.createEnvelope( point.getX() - accuracyX, point.getY() - accuracyY,
                                                            point.getX() + accuracyX, point.getY() + accuracyY, null );
        root.insert( item, envelope );
    }

    /**
     * Searches for all items intersecting with the envelope.
     * 
     * @param envelope
     * @return a list with the resulting items
     */
    public List<T> query( Envelope envelope )
                            throws IndexException {
        return root.query( envelope, new ArrayList<T>( 1000 ), 0 );
    }

    /**
     * Deletes the item from the quadtree. Untested method!
     * 
     * @param item
     *            the item to be deleted
     * @throws IndexException
     */
    public void deleteItem( T item )
                            throws IndexException {
        root.delete( item, root.getEnvelope() );
    }

    /**
     * Deletes all items intersecting the envelope. Untested method!
     * 
     * @param envelope
     */
    public void deleteRange( Envelope envelope ) {
        root.deleteRange( envelope );
    }

    /**
     * @return the deepest level of this subtree
     */
    public int getDepth() {
        return root.getDepth();
    }

    /**
     * @return the root bounding box
     */
    public Envelope getRootBoundingBox()
                            throws IndexException {
        return root.getEnvelope();
    }

    public void update( T item, Envelope newBBox ) {
        throw new UnsupportedOperationException(
                                                 "This method is not implemented for the mempoint quatree, maybe use a delete and insert combination?" );
    }

}
