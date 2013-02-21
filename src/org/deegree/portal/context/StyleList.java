//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/portal/context/StyleList.java $
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
package org.deegree.portal.context;

import java.util.HashMap;

/**
 * encapsulates a StyleList as defined by the OGC Web Map Context specification
 * 
 * @version $Revision: 10963 $
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 */
public class StyleList {
    private HashMap<String, Style> styles = new HashMap<String, Style>();

    private Style current = null;

    /**
     * Creates a new StyleList object.
     * 
     * @param styles
     * @throws ContextException if the given styles are <code>null</code> or empty.
     */
    public StyleList( Style[] styles ) throws ContextException {
        setStyles( styles );
    }

    /**
     * returns an array of all styles known by a layer
     * 
     * @return all styles
     */
    public Style[] getStyles() {
        Style[] fr = new Style[styles.size()];
        return styles.values().toArray( fr );
    }

    /**
     * sets alla styles known by a layer
     * 
     * @param styles
     * 
     * @throws ContextException
     */
    public void setStyles( Style[] styles )
                            throws ContextException {
        if ( ( styles == null ) || ( styles.length == 0 ) ) {
            throw new ContextException( "at least one style must be defined for a layer" );
        }

        this.styles.clear();

        for ( int i = 0; i < styles.length; i++ ) {
            if ( styles[i].isCurrent() ) {
                current = styles[i];
            }
            this.styles.put( styles[i].getName(), styles[i] );
        }
    }

    /**
     * returns the current style
     * 
     * @return current style
     */
    public Style getCurrentStyle() {
        return current;
    }

    /**
     * 
     * 
     * @param name
     * 
     * @return named style
     */
    public Style getStyle( String name ) {
        return styles.get( name );
    }

    /**
     * 
     * 
     * @param style
     */
    public void addStyle( Style style ) {
        if ( style.isCurrent() ) {
            current.setCurrent( false );
            current = style;
        }
        styles.put( style.getName(), style );
    }

    /**
     * 
     * 
     * @param name
     * 
     * @return removed style
     */
    public Style removeStyle( String name ) {
        return styles.remove( name );
    }

    /**
     * 
     */
    public void clear() {
        styles.clear();
    }

}