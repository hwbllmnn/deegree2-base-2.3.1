//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/test/junit/org/deegree/ogcwebservices/AbstractServiceTest.java $
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
package org.deegree.ogcwebservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.ConvenienceFileFilter;

import alltests.AllTests;

public class AbstractServiceTest extends TestCase {

    private static ILogger LOG = LoggerFactory.getLogger( AbstractServiceTest.class );
    protected Map<String, String> createKVPMap( URL fileURL )
                            throws IOException {
        Map<String, String> kvpMap = new HashMap<String, String>();
        BufferedReader reader = new BufferedReader( new FileReader( fileURL.getFile() ) );
        // skip first line
        String line = reader.readLine();
        while ( ( line = reader.readLine() ) != null ) {
            int equalsPos = line.indexOf( '=' );
            if ( equalsPos != -1 ) {
                String key = line.substring( 0, equalsPos );
                String value = line.substring( equalsPos + 1 );
                if ( value.endsWith( "&" ) ) {
                    value = value.substring( 0, value.length() - 1 );
                }
                kvpMap.put( key, value );
            }
        }
        return kvpMap;
    }

    /**
     * Returns a filename that is suitable as a target to write the request result to.
     * <p>
     * The file will be placed in the "output"-subdirectory of the services resource directory.
     * 
     * @param requestFile
     * @param outputDir
     * @return
     * @throws MalformedURLException
     */
    protected String getResultFilename( URL requestFile, URL outputDir )
                            throws MalformedURLException {
        String fileName = requestFile.toString();

        // request file name extension (kvp / xml)
        String ext = fileName.substring( fileName.lastIndexOf( '.' ) + 1, fileName.length() );

        // last directory (name of operation)
        String[] directories = fileName.split( "/" );
        String operation = directories[directories.length - 3];

        // example name
        String exampleName = fileName.substring( fileName.lastIndexOf( '/' ) + 1, fileName.lastIndexOf( '.' ) );

        String targetName = operation + ext.toUpperCase() + exampleName + "_result.xml";
        String resultName = new URL( outputDir, targetName ).getFile();
        return resultName;
    }

    /**
     * Returns all files in the given directory that end with the given extension.
     * 
     * @param directoryURL
     * @param ext
     * @return
     * @throws MalformedURLException
     */
    protected List<URL> scanDirectory( URL directoryURL, String ext )
                            throws MalformedURLException {
        File directory = new File( directoryURL.getFile() );
        String[] fileNames = directory.list( new ConvenienceFileFilter( false, ext ) );
        List<URL> fileURLs = null;

        if ( fileNames == null ) {
            LOG.logDebug( "Specified directory '" + directory.toString() + "' does not exist." );
            fileNames = new String[0];
        } else {
            fileURLs = new ArrayList<URL>( fileNames.length );
            for ( int i = 0; i < fileNames.length; i++ ) {
                fileURLs.add( new URL( directoryURL.toString() + "/" + fileNames[i] ) );
            }
        }
        return fileURLs;
    }

}
