//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/shpapi/HasNoDBaseFileException.java $

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

package org.deegree.io.shpapi;

/**
 * class defining an exception that is thrown if no dBase-file is associated with a shape-file
 * 
 * 
 * @version 05.04.2000
 * @author Andreas Poth
 */
public final class HasNoDBaseFileException extends Exception {

    private static final long serialVersionUID = -6816313873039541555L;

    private String message = "org.deegree_impl.io.shpapi.HasNoDBaseFileException";

    // constructor
    /**
     * 
     */
    public HasNoDBaseFileException() {

        super();

    }

    // constructor
    /**
     * @param message
     */
    public HasNoDBaseFileException( String message ) {

        super( message );

        this.message = message;

    }

    @Override
    public String toString() {

        return message + "\n" + this.getLocalizedMessage();

    }

}
