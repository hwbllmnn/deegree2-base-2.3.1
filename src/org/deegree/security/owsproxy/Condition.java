//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/security/owsproxy/Condition.java $
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
package org.deegree.security.owsproxy;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 1.1, $Revision: 13924 $, $Date: 2008-09-15 10:12:49 +0200 (Mo, 15. Sep 2008) $
 * 
 * @since 1.1
 * 
 */
public class Condition {

    private Map<String, OperationParameter> opMap = null;
    
    private boolean any = false;

    /**
     * 
     * @param operationParameters
     */
    public Condition( OperationParameter[] operationParameters ) {
        opMap = new HashMap<String, OperationParameter>();
        setOperationParameters( operationParameters );
    }
    
    /**
     * 
     * @param any
     */
    public Condition( boolean any ) {
        opMap = new HashMap<String, OperationParameter>();
        this.any = any;
    }

    /**
     * 
     * @return all operation parameters
     */
    public OperationParameter[] getOperationParameters() {
        OperationParameter[] op = new OperationParameter[opMap.size()];
        return opMap.values().toArray( op );
    }

    /**
     * @param name
     * @return named operation parameter
     */
    public OperationParameter getOperationParameter( String name ) {
        return opMap.get( name );
    }

    /**
     * @param param
     * 
     */
    public void setOperationParameters( OperationParameter[] param ) {
        opMap.clear();
        for ( int i = 0; i < param.length; i++ ) {
            opMap.put( param[i].getName(), param[i] );
        }
    }

    /**
     * @param param
     * 
     */
    public void addOperationParameter( OperationParameter param ) {
        opMap.put( param.getName(), param );
    }

    /**
     * @param param
     * 
     */
    public void removeOperationParameter( OperationParameter param ) {
        removeOperationParameter( param.getName() );
    }

    /**
     * @param name
     * 
     */
    public void removeOperationParameter( String name ) {
        opMap.remove( name );
    }
    
    /**
     * 
     * @return true if no validation for specific parameters shall be performed
     */
    public boolean isAny() {
        return any;
    }

}