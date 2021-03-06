//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/graphics/sld/ExternalGraphic.java $
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
package org.deegree.graphics.sld;

import static org.deegree.framework.xml.XMLTools.escape;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.NetWorker;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.Marshallable;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;

import com.sun.media.jai.codec.MemoryCacheSeekableStream;

/**
 * The ExternalGraphic element allows a reference to be made to an external graphic file with a Web
 * URL. The OnlineResource sub-element gives the URL and the Format sub-element identifies the
 * expected document MIME type of a successful fetch. Knowing the MIME type in advance allows the
 * styler to select the best- supported format from the list of URLs with equivalent content.
 * 
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: aschmitz $
 * 
 * @version $Revision: 12168 $, $Date: 2008-06-04 16:08:42 +0200 (Mi, 04. Jun 2008) $
 */
public class ExternalGraphic implements Marshallable {

    private static final ILogger LOG = LoggerFactory.getLogger( ExternalGraphic.class );

    private BufferedImage image = null;

    private String format = null;

    private URL onlineResource = null;

    private TranscoderInput input = null;

    private ByteArrayOutputStream bos = null;

    private TranscoderOutput output = null;

    private Transcoder trc = null;

    /**
     * Creates a new ExternalGraphic_Impl object.
     * 
     * @param format
     * @param onlineResource
     */
    ExternalGraphic( String format, URL onlineResource ) {
        setFormat( format );
        setOnlineResource( onlineResource );
    }

    /**
     * the Format sub-element identifies the expected document MIME type of a successful fetch.
     * 
     * @return Format of the external graphic
     * 
     */
    public String getFormat() {
        return format;
    }

    /**
     * sets the format (MIME type)
     * 
     * @param format
     *            Format of the external graphic
     * 
     */
    public void setFormat( String format ) {
        this.format = format;
    }

    /**
     * The OnlineResource gives the URL of the external graphic
     * 
     * @return URL of the external graphic
     * 
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * sets the online resource / URL of the external graphic
     * 
     * @param onlineResource
     *            URL of the external graphic
     * 
     */
    public void setOnlineResource( URL onlineResource ) {

        this.onlineResource = onlineResource;
        String file = onlineResource.getFile();
        int idx = file.indexOf( "$" );
        if ( idx == -1 ) {
            retrieveImage( onlineResource );
        }
    }

    /**
     * @param onlineResource
     */
    private void retrieveImage( URL onlineResource ) {

        try {
            String t = onlineResource.toExternalForm();
            if ( t.trim().toLowerCase().endsWith( ".svg" ) ) {
                // initialize the the classes required for svg handling
                bos = new ByteArrayOutputStream( 2000 );
                output = new TranscoderOutput( bos );
                // PNGTranscoder is needed to handle transparent parts
                // of a SVG
                trc = new PNGTranscoder();
                try {
                    input = new TranscoderInput( NetWorker.url2String( onlineResource ) );
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            } else {
                InputStream is = onlineResource.openStream();
                MemoryCacheSeekableStream mcss = new MemoryCacheSeekableStream( is );
                RenderedOp rop = JAI.create( "stream", mcss );
                image = rop.getAsBufferedImage();
                mcss.close();
                is.close();
            }
        } catch ( IOException e ) {
            LOG.logError( e.getMessage(), e );
        }
    }

    /**
     * returns the external graphic as an image. this method is not part of the sld specifications
     * but it is added for speed up applications
     * @param targetSizeX 
     * @param targetSizeY 
     * @param feature 
     * 
     * @return the external graphic as BufferedImage
     */
    public BufferedImage getAsImage( int targetSizeX, int targetSizeY, Feature feature ) {

        if ( ( ( this.input == null ) && ( this.image == null ) ) || feature != null ) {
            URL onlineResource = initializeOnlineResource( feature );
            retrieveImage( onlineResource );
        }

        if ( input != null ) {
            if ( targetSizeX <= 0 ) {
                targetSizeX = 1;
            }
            if ( targetSizeY <= 0 ) {
                targetSizeY = 1;
            }

            trc.addTranscodingHint( SVGAbstractTranscoder.KEY_HEIGHT, new Float( targetSizeX ) );
            trc.addTranscodingHint( SVGAbstractTranscoder.KEY_WIDTH, new Float( targetSizeY ) );
            try {
                trc.transcode( input, output );
                try {
                    bos.flush();
                    bos.close();
                } catch ( IOException e3 ) {
                    e3.printStackTrace();
                }
            } catch ( TranscoderException e ) {
                LOG.logError( e.getMessage(), e );
            }
            try {
                ByteArrayInputStream is = new ByteArrayInputStream( bos.toByteArray() );
                MemoryCacheSeekableStream mcss = new MemoryCacheSeekableStream( is );
                RenderedOp rop = JAI.create( "stream", mcss );
                image = rop.getAsBufferedImage();
                mcss.close();
            } catch ( IOException e1 ) {
                LOG.logError( e1.getMessage(), e1 );
            }
        }

        return image;
    }

    /**
     * @param feature
     * @return online resource URL
     */
    private URL initializeOnlineResource( Feature feature ) {

        String file = null;
        try {
            file = this.onlineResource.toURI().toASCIIString();
            LOG.logDebug( "external graphic pattern: ", file );
        } catch ( URISyntaxException e1 ) {
            e1.printStackTrace();
        }
        String[] tags = StringTools.extractStrings( file, "$", "$" );

        if ( tags != null ) {
            FeatureProperty[] properties = feature.getProperties();
            for ( int i = 0; i < tags.length; i++ ) {
                String tag = tags[i].substring( 1, tags[i].length() - 1 );
                for ( int j = 0; j < properties.length; j++ ) {
                    if ( properties[j].getName().getLocalName().equals( tag ) ) {
                        String to = properties[j].getValue().toString();
                        file = StringTools.replace( file, tags[i], to, true );
                    }
                }
            }
        }
        URL onlineResource = null;
        try {
            onlineResource = new URL( file );
            LOG.logDebug( "external graphic URL: ", file );
        } catch ( MalformedURLException e ) {
            LOG.logError( e.getMessage(), e );
        }
        return onlineResource;
    }

    /**
     * sets the external graphic as an image.
     * 
     * @param image
     *            the external graphic as BufferedImage
     */
    public void setAsImage( BufferedImage image ) {
        this.image = image;
    }

    /**
     * exports the content of the ExternalGraphic as XML formated String
     * 
     * @return xml representation of the ExternalGraphic
     */
    public String exportAsXML() {

        StringBuffer sb = new StringBuffer( 200 );
        sb.append( "<ExternalGraphic>" );
        sb.append( "<OnlineResource xmlns:xlink='http://www.w3.org/1999/xlink' " );
        sb.append( "xlink:type='simple' xlink:href='" );
        sb.append( NetWorker.url2String( onlineResource ) + "'/>" );
        sb.append( "<Format>" ).append( escape( format ) ).append( "</Format>" );
        sb.append( "</ExternalGraphic>" );
        return sb.toString();
    }

}