//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/operation/LockFeatureResponseDocument.java $
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
package org.deegree.ogcwebservices.wfs.operation;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.SAXException;

/**
 * Parser for "wfs:LockFeatureResponse" documents.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 9345 $, $Date: 2007-12-27 17:22:25 +0100 (Do, 27. Dez 2007) $
 */
public class LockFeatureResponseDocument extends AbstractWFSRequestDocument {

    private static final long serialVersionUID = 7168101158239058596L;

    private static final String XML_TEMPLATE = "LockFeatureResponseTemplate.xml";

    /**
     * Creates a skeleton document that contains the root element and the namespace bindings only.
     * 
     * @throws IOException
     * @throws SAXException
     */
    public void createEmptyDocument()
                            throws IOException, SAXException {
        URL url = LockFeatureResponseDocument.class.getResource( XML_TEMPLATE );
        if ( url == null ) {
            throw new IOException( "The resource '" + XML_TEMPLATE + " could not be found." );
        }
        load( url );
    }
}