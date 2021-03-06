//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/optimizers/Optimizer.java $
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
package org.deegree.graphics.optimizers;

import java.awt.Graphics2D;

import org.deegree.graphics.MapView;
import org.deegree.graphics.Theme;
import org.deegree.graphics.displayelements.LabelDisplayElement;

/**
 * This is the interface for (graphical) {@link Optimizer}s that need to alter the contents of {@link Theme}s (e.g.
 * positions of display elements} before the parent {@link MapView} object is painted.
 * <p>
 * For example, the placements of {@link LabelDisplayElement}s in a {@link Theme} may be optimized to minimize
 * overlapping using the {@link LabelOptimizer}.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 12150 $, $Date: 2008-06-04 13:44:43 +0200 (Mi, 04. Jun 2008) $
 */
public interface Optimizer {

    /**
     * Sets the associated {@link MapView} instance. This is needed to provide scale and projection information. Called
     * by the {@link MapView}.
     * 
     * @param mapView
     *            {@link MapView} instance to associate with this {@link Optimizer}
     */
    public void setMapView( MapView mapView );

    /**
     * Adds a {@link Theme} to be considered by this {@link Optimizer}.
     * 
     * @param theme
     *            {@link Theme} to be considered
     */
    public void addTheme( Theme theme );

    /**
     * Invokes the optimization process. This {@link Optimizer} will now process and modify the contents of the attached
     * {@link Theme}s.
     * 
     * @param g
     *            graphis context that will be used to draw the optimized display elements
     * @throws Exception
     */
    public abstract void optimize( Graphics2D g )
                            throws Exception;
}
