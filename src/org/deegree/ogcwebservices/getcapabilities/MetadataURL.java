//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/getcapabilities/MetadataURL.java $
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
package org.deegree.ogcwebservices.getcapabilities;

import java.net.URL;

import org.deegree.ogcbase.BaseURL;

/**
 * A WFS/WMS/WCS should use one or more <MetadataURL>elements to offer detailed, standardized
 * metadata about the data underneath a particular layer. The <code>MetadataURL</code> element
 * shall not be used to reference metadata in a non-standardized metadata format.
 * <p>
 * The type attribute indicates the standard to which the metadata complies, three types are defined
 * at present (from the WFS 1.1.1 specification):
 * <p>
 * <table border="1">
 * <tr>
 * <th>Type value</th>
 * <th>Metadata standard</th>
 * </tr>
 * <tr>
 * <td>'TC211' or 'ISO19115'</td>
 * <td>ISO TC211 19115</td>
 * </tr>
 * <tr>
 * <td>'FGDC'</td>
 * <td>FGDC CSDGM</td>
 * </tr>
 * <tr>
 * <td>'ISO19139'</td>
 * <td>ISO 19139</td>
 * </tr>
 * </table>
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp </a>
 * @author <a href="mailto:mschneider@lat-lon.de">Markus Schneider </a>
 * @version $Revision: 11902 $, $Date: 2008-05-26 18:22:23 +0200 (Mo, 26. Mai 2008) $
 */
public class MetadataURL extends BaseURL {

    private String type = null;

    /**
     * Constructs a new MetadataURL instance.
     * 
     * @param type
     * @param format
     * @param onlineResource
     */
    public MetadataURL( String type, String format, URL onlineResource ) {
        super( format, onlineResource );
        setType( type );
    }

    /**
     * returns the type attribute indicating the standard to which the metadata complies.
     * 
     * @return the type attribute indicating the standard to which the metadata complies.
     */
    public String getType() {
        return type;
    }

    /**
     * sets the type attribute indicating the standard to which the metadata complies.
     * 
     * @param type
     */
    public void setType( String type ) {
        this.type = type;
    }

    @Override
    public String toString() {
        String ret = null;
        ret = "type = " + type + "\n";
        return ret;
    }

}