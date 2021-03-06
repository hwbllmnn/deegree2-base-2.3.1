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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.deegree.framework.util.BootLogger;
import org.deegree.i18n.Messages;

/**
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: poth $
 * 
 * @version $Revision: 6251 $, $Date: 2007-03-19 16:59:28 +0100 (Mo, 19 Mrz 2007) $
 */
public class DBSchemaToDatastoreConfSQLMSGAccess {

    private static Properties props = new Properties();

    /**
     * Initialization done at class loading time.
     */
    static {
        try {
            String fileName = "DBSchemaToDatastoreConfMSG.properties";
            InputStream is = DBSchemaToDatastoreConfSQLMSGAccess.class.getResourceAsStream( fileName );
            props.load( is );
            is.close();
        } catch ( IOException e ) {
            BootLogger.logError( "Error while initializing " + Messages.class.getName() + " : " + e.getMessage(), e );
        }
    }

    /**
     * 
     * @see MessageFormat for conventions on string formatting and escape characters.
     * 
     * @param key
     * @param arguments
     * @return the sql statement assigned to the passed key
     */
    public static String getMessage( String key, Object... arguments ) {
        String s = props.getProperty( key );
        if ( s != null ) {
            return MessageFormat.format( s, arguments );
        }

        // to avoid NPEs
        return "$Messages with key: " + key + " not found$";
    }
}
