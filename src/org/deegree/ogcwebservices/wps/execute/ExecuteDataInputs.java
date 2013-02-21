//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wps/execute/ExecuteDataInputs.java $
/*----------------    FILE HEADER  ------------------------------------------

 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 EXSE, Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/exse/
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
package org.deegree.ogcwebservices.wps.execute;

import java.util.HashMap;
import java.util.Map;

/**
 * DataInputs.java
 * 
 * Created on 24.03.2006. 16:31:53h
 * 
 * List of the Inputs provided as part of the Execute Request
 * 
 * @author <a href="mailto:christian@kiehle.org">Christian Kiehle</a>
 * @author <a href="mailto:christian.heier@gmx.de">Christian Heier</a>
 * 
 * @version 1.0.
 * 
 * @since 2.0
 */

public class ExecuteDataInputs {

	/**
	 * List of input (or parameter) values provided to the process, including
	 * each of the Inputs needed to execute the process. It is possible to have
	 * no inputs provided only when all the inputs are predetermined fixed
	 * resources. In all other cases, at least one input is required.
	 */
	private Map<String, IOValue> inputs;

	/**
	 * @return the mapping of iovalues
	 */
	public Map<String, IOValue> getInputs() {
		if ( inputs == null ) {
			inputs = new HashMap<String, IOValue>();
		}
		return this.inputs;
	}

	/**
	 * @param inputs to set.
	 */
	public void setInputs( Map<String, IOValue> inputs ) {
		this.inputs = inputs;
	}

}
