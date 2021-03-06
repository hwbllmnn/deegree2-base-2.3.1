//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_1_1_0/operations/GetResourceByIDDocument.java $
/*----------------    FILE HEADER  ------------------------------------------
 This file is part of deegree.
 Copyright (C) 2001-2008 by:
 Department of Geography, University of Bonn
 http://www.giub.uni-bonn.de/deegree/
 lat/lon GmbH
 http://www.lat-lon.de

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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

package org.deegree.owscommon_1_1_0.operations;

import static org.deegree.framework.xml.XMLTools.getNodeAsString;
import static org.deegree.framework.xml.XMLTools.getNodesAsStringList;

import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.ogcbase.CommonNamespaces;
import org.w3c.dom.Element;

/**
 * <code>GetResourceByID</code> supplies the methods for parsing the ows 1.1.0 GetResourceById
 * xml-dom structure.
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11372 $, $Date: 2008-04-22 18:17:22 +0200 (Di, 22. Apr 2008) $
 * 
 */
public class GetResourceByIDDocument extends XMLFragment {

    private static final long serialVersionUID = 2061011143514168497L;

    private static final String PRE = CommonNamespaces.OWS_1_1_0PREFIX + ":";

    /**
     * @return a list of resourceID uri's or an empty list if no resourceID's were given.
     * @throws XMLParsingException
     *             if the node could not be parsed.
     */
    public List<String> parseResourceIDs()
                            throws XMLParsingException {
        Element root = getRootElement();
        List<String> result = new ArrayList<String>();
        if ( root != null ) {
            result = getNodesAsStringList( root, PRE + "ResourceID", nsContext );
        }
        return result;
    }

    /**
     * @return a String containing an output format (mime-type) or an empty String if no output
     *         format was given.
     * @throws XMLParsingException
     *             if the node could not be parsed.
     */
    public String parseOutputFormats()
                            throws XMLParsingException {
        Element root = getRootElement();
        String result = new String();
        if ( root != null ) {
            result = getNodeAsString( root, PRE + "OutputFormat", nsContext, "" );
        }
        return result;
    }

    /**
     * @return the mandatory version string.
     * @throws XMLParsingException
     *             if the attribute was not given.
     */
    public String parseVersion()
                            throws XMLParsingException {
        Element root = getRootElement();
        String result = new String();
        if ( root != null ) {
            result = root.getAttribute( "version" );
            if ( result == null || "".equals( result.trim() ) ) {
                throw new XMLParsingException(
                                               "The version attribute is mandatory for a resourceById (ows 1.1.0) request" );
            }

        }
        return result;
    }

    /**
     * @return the mandatory service string.
     * @throws XMLParsingException
     *             if the attribute was not given.
     */
    public String parseService()
                            throws XMLParsingException {
        Element root = getRootElement();
        String result = new String();
        if ( root != null ) {
            result = root.getAttribute( "service" );
            if ( result == null || "".equals( result.trim() ) ) {
                throw new XMLParsingException(
                                               "The service attribute is mandatory for a resourceById (ows 1.1.0) request" );
            }
        }
        return result;
    }

}
