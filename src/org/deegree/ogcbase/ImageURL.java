//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcbase/ImageURL.java $
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
 * 
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 12071 $, $Date: 2008-06-02 13:46:56 +0200 (Mo, 02. Jun 2008) $
 */
public class ImageURL extends BaseURL {

    private int height = 0;

    private int width = 0;

    /**
     * constructor initializing the class with the <LegendURL>
     * 
     * @param width
     * @param height
     * @param format
     * @param onlineResource
     */
    public ImageURL( int width, int height, String format, URL onlineResource ) {
        super( format, onlineResource );
        setWidth( width );
        setHeight( height );
    }

    /**
     * returns the width of the logo image
     * 
     * @return image width
     */
    public int getWidth() {
        return width;
    }

    /**
     * sets the width of the logo image
     * 
     * @param width
     */
    public void setWidth( int width ) {
        this.width = width;
    }

    /**
     * returns the height of the logo image
     * 
     * @return image height
     */
    public int getHeight() {
        return height;
    }

    /**
     * sets the height of the logo image
     * 
     * @param height
     */
    public void setHeight( int height ) {
        this.height = height;
    }

    @Override
    public String toString() {
        String ret = null;
        ret = "width = " + width + "\n";
        ret += ( "height = " + height + "\n" );
        return ret;
    }

}