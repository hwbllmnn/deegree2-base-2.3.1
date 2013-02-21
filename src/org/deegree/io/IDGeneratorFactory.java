//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/IDGeneratorFactory.java $
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
package org.deegree.io;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.DataBaseIDGenerator;

/**
 * Factory for <tt>IDGenerator</tt>-instances. The generated instance is suitable for the
 * database used and for the configuration (i.e. table and field type).
 * <p>
 * 
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider</a>
 * @version $Revision: 12146 $ $Date: 2008-06-04 12:18:44 +0200 (Mi, 04. Jun 2008) $
 */
public class IDGeneratorFactory {

    private static final ILogger LOG = LoggerFactory.getLogger( IDGeneratorFactory.class );

    /**
     * @param con
     * @param tableName
     * @param fieldName
     * @return the id generator
     * @throws SQLException
     */
    public static DataBaseIDGenerator createIDGenerator( Connection con, String tableName, String fieldName )
                            throws SQLException {

        // retrieve type of field
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT " + fieldName + " FROM " + tableName );
        ResultSetMetaData rsmd = rs.getMetaData();
        int type = rsmd.getColumnType( 1 );
        rs.close();
        stmt.close();
        boolean isNumeric = false;

        switch ( type ) {
        case Types.INTEGER:
        case Types.BIGINT:
        case Types.DECIMAL:
        case Types.NUMERIC:
        case Types.SMALLINT:
        case Types.TINYINT: {
            isNumeric = true;
            break;
        }
        case Types.CHAR:
        case Types.LONGVARCHAR:
        case Types.VARCHAR: {
            isNumeric = false;
            break;
        }
        default: {
            throw new SQLException( "Cannot create DataBaseIDGenerator for table '" + tableName + "' and field '"
                                    + fieldName + "'. Only integer and alphanumeric " + "fields are supported" );
        }
        }

        // find suitable instance of IDGenerator
        DatabaseMetaData dbmd = con.getMetaData();
        String dbName = dbmd.getDatabaseProductName();
        String dbVersion = dbmd.getDatabaseProductVersion();
        String driverName = dbmd.getDriverName();
        String driverVersion = dbmd.getDriverVersion();

        LOG.logDebug( "dbName       : " + dbName );
        LOG.logDebug( "dbVersion    : " + dbVersion );
        LOG.logDebug( "driverName   : " + driverName );
        LOG.logDebug( "driverVersion: " + driverVersion );

        DataBaseIDGenerator idGenerator = new GenericSQLIDGenerator( con, tableName, fieldName, type, isNumeric );
        return idGenerator;
    }
}