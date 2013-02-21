//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/dbaseapi/KeyNotFoundException.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.

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

 Copyright (C) May 2003 by IDgis BV, The Netherlands - www.idgis.nl
 */
package org.deegree.io.dbaseapi;

/**
 * 
 * 
 * @version $Revision: 10660 $
 */
public class KeyNotFoundException extends DBaseIndexException {
    /**
     * Creates a new KeyNotFoundException object.
     * 
     * @param key
     * @param index
     */
    public KeyNotFoundException( Comparable key, DBaseIndex index ) {
        super( "Key " + key + " not found in index " + index, key, index );
    }
}