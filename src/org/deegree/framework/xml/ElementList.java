//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/framework/xml/ElementList.java $
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

package org.deegree.framework.xml;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * Convenience class for easy handling of <code>NodeLists<code> containing only objects of
 * type org.w3c.dom.Element.
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version 1.0. $Revision: 11989 $, $Date: 2008-05-29 09:48:29 +0200 (Do, 29. Mai 2008) $
 */
public class ElementList {
    /**
     * The actual elements this list wraps.
     */
    ArrayList<Element> elements = new ArrayList<Element>( 100 );

    /**
     * 
     * @param element
     */
    public void addElement( Element element ) {
        elements.add( element );
    }

    /**
     * 
     * @return size of the list
     */
    public int getLength() {
        return elements.size();
    }

    /**
     * 
     * @param i
     * @return i-th element or <code>null</code> if i is out of the list bounds
     */
    public Element item( int i ) {
        if ( i < 0 || i > elements.size() - 1 )
            return null;
        return elements.get( i );
    }
}