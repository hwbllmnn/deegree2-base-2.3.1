//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcbase/BaseURL.java $
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
package org.deegree.ogcbase;

import java.net.URL;

/**
 * The address is represented by the &lt;onlineResource&gt; element.
 * 
 * @author <a href="mailto:k.lupp@web.de">Katharina Lupp </a>
 * @version 2002-03-01, $Revision: 12071 $, $Date: 2008-06-02 13:46:56 +0200 (Mo, 02. Jun 2008) $
 * @since 1.0
 */
public class BaseURL {

    private String format = null;

    private URL onlineResource = null;

    /**
     * constructor initializing the class with the &lt;BaseURL&gt;
     * 
     * @param format
     * @param onlineResource
     */
    public BaseURL( String format, URL onlineResource ) {
        setFormat( format );
        setOnlineResource( onlineResource );
    }

    /**
     * returns the MIME type of the resource
     * 
     * @return the MIME type of the resource
     */
    public String getFormat() {
        return format;
    }

    /**
     * sets the MIME type of the resource
     * 
     * @param format
     * 
     */
    public void setFormat( String format ) {
        this.format = format;
    }

    /**
     * returns the address (URL) of the resource
     * 
     * @return the address (URL) of the resource
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * returns the address (URL) of the resource
     * 
     * @param onlineResource
     */
    public void setOnlineResource( URL onlineResource ) {
        this.onlineResource = onlineResource;
    }

    @Override
    public String toString() {
        String ret = null;
        ret = "format = " + format + "\n";
        ret += ( "onlineResource = " + onlineResource + "\n" );
        return ret;
    }
}