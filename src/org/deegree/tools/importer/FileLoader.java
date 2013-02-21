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
package org.deegree.tools.importer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;

/**
 * 
 * The <code>FileLoader</code> loads a file from given path.
 * 
 * @author <a href="mailto:buesching@lat-lon.de">Lyn Buesching</a>
 * @author last edited by: $Author: buesching $
 * 
 * @version $Revision: 1.1 $, $Date: 2007-10-10 14:21:26 $
 * 
 */
public class FileLoader implements Loader {

    private static final ILogger LOG = LoggerFactory.getLogger( FileLoader.class );

    /**
     * @param objectToLoad
     *            contains an Array of strings, where the first entry contains the source to load
     *            and the second the target to export
     * @return an ArrayList of objects, where the first object contains an Array of bytes to export
     *         and the second object the information of the target to export
     */
    public Object loadObject( Object objectToLoad ) {
        List<Object> result = new ArrayList<Object>();
        String source = ( (String[]) objectToLoad )[0];
        LOG.logInfo( Messages.getString( "FileLoader.LOAD", source ) );
        File file = new File( source );
        int fileLength = (int) file.length();
        byte[] byteArray = new byte[fileLength];
        try {
            FileInputStream fileIS = new FileInputStream( file );
            BufferedInputStream bufferedIS = new BufferedInputStream( fileIS );
            bufferedIS.read( byteArray, 0, fileLength );
            bufferedIS.close();
            fileIS.close();
            result.add( byteArray );
            result.add( ( (String[]) objectToLoad )[1] );
            return result;
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
            return null;
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
    }

}