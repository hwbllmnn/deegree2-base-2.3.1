//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/i18n/Messages.java $
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
package org.deegree.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.BootLogger;

/**
 * Responsible for the access to messages that are visible to the user.
 * <p>
 * Messages are read from the properties file <code>messages_LANG.properties</code> (LANG is
 * always a lowercased ISO 639 code), so internationalization is supported. If a certain property
 * (or the property file) for the specific default language of the system is not found, the message
 * is taken from <code>messages_en.properties</code>.
 * 
 * @see Locale#getLanguage()
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:taddei@lat-lon.de">Ugo Taddei</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mon, 24 Mar 2008) $
 */
public class Messages {

    private static final ILogger LOG = LoggerFactory.getLogger( Messages.class );

    /* This definition allows Eclipse to display the content of referenced message keys. */
    @SuppressWarnings("unused")
    private static final String BUNDLE_NAME = "org.deegree.i18n.messages_en";

    private static Properties defaultProps = new Properties();

    private static Map<Locale, Properties> props = new HashMap<Locale, Properties>();

    private static String lang;

    /**
     * Initialization done at class loading time.
     */
    static {
        try {
            // load all messages from default file ("org/deegree/i18n/message_en.properties")
            String fileName = "messages_en.properties";
            InputStream is = Messages.class.getResourceAsStream( fileName );
            if ( is == null ) {
                BootLogger.log( "Error while initializing " + Messages.class.getName() + " : "
                                + " default message file: '" + fileName + " not found." );
            }
            is = Messages.class.getResourceAsStream( fileName );
            defaultProps.load( is );
            is.close();

            // override messages using file "/message_en.properties"
            fileName = "/messages_en.properties";
            overrideMessages( fileName, defaultProps );

            lang = Locale.getDefault().getLanguage();
            if ( !"".equals( lang ) && !"en".equals( lang ) ) {
                // override messages using file "org/deegree/i18n/message_LANG.properties"
                fileName = "messages_" + lang + ".properties";
                overrideMessages( fileName, defaultProps );
                // override messages using file "/message_LANG.properties"
                fileName = "/messages_" + lang + ".properties";
                overrideMessages( fileName, defaultProps );
            }
        } catch ( IOException e ) {
            BootLogger.logError( "Error while initializing " + Messages.class.getName() + " : " + e.getMessage(), e );
        }
    }

    private static void overrideMessages( String propertiesFile, Properties props )
                            throws IOException {
        InputStream is = Messages.class.getResourceAsStream( propertiesFile );
        if ( is != null ) {
            // override default messages
            Properties overrideProps = new Properties();
            overrideProps.load( is );
            is.close();
            Iterator<?> iter = overrideProps.keySet().iterator();
            while ( iter.hasNext() ) {
                String key = (String) iter.next();
                props.put( key, overrideProps.get( key ) );
            }
        }
    }

    private static String get( Properties props, String key, Object... args ) {
        String s = (String) props.get( key );
        if ( s != null ) {
            return MessageFormat.format( s, args );
        }

        return "$Message with key: " + key + " not found$";
    }

    /**
     * @param loc
     *            the locale to be used
     * @param key
     *            to get
     * @param arguments
     *            to fill in the message
     * @return the localized message
     */
    public static synchronized String get( Locale loc, String key, Object... arguments ) {
        if ( loc.getLanguage().equals( lang ) ) {
            return get( key, arguments );
        }

        if ( !props.containsKey( loc ) ) {
            Properties p = new Properties();

            String l = loc.getLanguage();

            if ( !"".equals( l ) ) {
                try {
                    // override messages in this order:
                    // messages_en.properties
                    // /messages_en.properties
                    // messages_lang.properties
                    // /messages_lang.properties
                    String fileName = "messages_en.properties";
                    overrideMessages( fileName, p );
                    fileName = "/messages_en.properties";
                    overrideMessages( fileName, p );
                    fileName = "messages_" + l + ".properties";
                    overrideMessages( fileName, p );
                    fileName = "/messages_" + l + ".properties";
                    overrideMessages( fileName, p );
                } catch ( IOException e ) {
                    LOG.logError( "Error loading language file for language '" + l + "': ", e );
                }
            }

            props.put( loc, p );
        }

        String s = get( props.get( loc ), key, arguments );
        return s;
    }

    /**
     * Alias for #getMessage.
     * 
     * @param key
     * @param arguments
     * @return the message
     */
    public static String get( String key, Object... arguments ) {
        return getMessage( key, arguments );
    }

    /**
     * Returns the message assigned to the passed key. If no message is assigned, an error message
     * will be returned that indicates the missing key.
     * 
     * @see MessageFormat for conventions on string formatting and escape characters.
     * 
     * @param key
     * @param arguments
     * @return the message assigned to the passed key
     */
    public static String getMessage( String key, Object... arguments ) {
        return get( defaultProps, key, arguments );
    }
}