//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/optimizers/AbstractOptimizer.java $
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

import java.util.HashSet;
import java.util.Set;

import org.deegree.graphics.MapView;
import org.deegree.graphics.Theme;

/**
 * Abstract base class for {@link Optimizer} implementations.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: mschneider $
 * 
 * @version $Revision: 12150 $, $Date: 2008-06-04 13:44:43 +0200 (Mi, 04. Jun 2008) $
 */
public abstract class AbstractOptimizer implements Optimizer {

    /** Contains all registered {@link Theme}s. */
    protected Set<Theme> themes = new HashSet<Theme>();

    /** Associated {@link MapView} instance. */
    protected MapView mapView;

    public void setMapView( MapView mapView ) {
        this.mapView = mapView;
    }

    public void addTheme( Theme theme ) {
        themes.add( theme );
    }
}
