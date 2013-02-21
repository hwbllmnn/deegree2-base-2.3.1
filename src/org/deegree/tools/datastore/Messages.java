//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/tools/datastore/Messages.java $
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
 Aennchenstraße 19
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
package org.deegree.tools.datastore;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static final String BUNDLE_NAME = "org.deegree.tools.datastore.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );

    private Messages() {
        // only static methods are used
    }

    public static String getString( String key ) {
        // TODO Auto-generated method stub
        try {
            return RESOURCE_BUNDLE.getString( key );
        } catch ( MissingResourceException e ) {
            return '!' + key + '!';
        }
    }

    public static String format( String key, Object arg0 ) {
        return format( key, new Object[] { arg0 } );
    }

    public static String format( String key, Object arg0, Object arg1 ) {
        return format( key, new Object[] { arg0, arg1 } );
    }

    public static String format( String key, Object arg0, Object arg1, Object arg2 ) {
        return format( key, new Object[] { arg0, arg1, arg2 } );
    }

    public static String format( String key, Object[] arguments ) {
        return MessageFormat.format( getString( key ), arguments );
    }
}
