//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wcs/describecoverage/DescribeCoverage.java $
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
package org.deegree.ogcwebservices.wcs.describecoverage;

import java.util.Map;

import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.util.KVP2Map;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.ElementList;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcbase.ExceptionCode;
import org.deegree.ogcwebservices.InvalidParameterValueException;
import org.deegree.ogcwebservices.MissingParameterValueException;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.wcs.WCSException;
import org.deegree.ogcwebservices.wcs.WCSExceptionCode;
import org.deegree.ogcwebservices.wcs.WCSRequestBase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A DescribeCoverage request lists the coverages to be described, identified by the Coverage
 * parameter. A request that lists no coverages shall be interpreted as requesting descriptions of
 * all coverages that a WCS can serve.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 12806 $, $Date: 2008-07-10 13:47:55 +0200 (Do, 10. Jul 2008) $
 */
public class DescribeCoverage extends WCSRequestBase {
    
    private static final ILogger LOG = LoggerFactory.getLogger( DescribeCoverage.class );

    private String[] coverages = null;

    /**
     * creates a DescribeCoverage request from its KVP representation
     * 
     * @param map
     *            request
     * @return created <tt>DescribeCoverage</tt>
     * @throws OGCWebServiceException
     *             will be thrown if something general is wrong
     * @throws MissingParameterValueException
     * @throws InvalidParameterValueException
     * @throws WCSException
     *             will be thrown if a WCS/DescribeCoverage specific part of the request is
     *             erroreous
     */
    public static DescribeCoverage create( Map map )
                            throws OGCWebServiceException, MissingParameterValueException,
                            InvalidParameterValueException {

        String version = (String) map.get( "VERSION" );
        if ( version == null ) {
            ExceptionCode code = ExceptionCode.MISSINGPARAMETERVALUE;
            throw new MissingParameterValueException( "DescribeCoverage", "'version' is missing", code );
        }
        if ( !version.equals( "1.0.0" ) ) {
            ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
            throw new InvalidParameterValueException( "DescribeCoverage", "'version' <> 1.0.0", code );
        }
        String service = (String) map.get( "SERVICE" );
        if ( service == null ) {
            ExceptionCode code = ExceptionCode.MISSINGPARAMETERVALUE;
            throw new MissingParameterValueException( "DescribeCoverage", "'service' is missing", code );
        }
        if ( !"WCS".equalsIgnoreCase( service ) ) {
            ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
            throw new InvalidParameterValueException( "DescribeCoverage", "'service' <> WCS", code );
        }

        String[] coverages = new String[0];
        if ( map.get( "COVERAGE" ) != null ) {
            String s = (String) map.get( "COVERAGE" );
            coverages = StringTools.toArray( s, ",", true );
        }

        String id = (String) map.get( "ID" );

        return new DescribeCoverage( id, version, coverages );
    }

    /**
     * creates a DescribeCoverage request from its KVP representation
     * 
     * @param id
     *            unique ID of the request
     * @param kvp
     *            request
     * @return created <tt>DescribeCoverage</tt>
     * @throws OGCWebServiceException
     *             will be thrown if something general is wrong
     * @throws MissingParameterValueException
     * @throws InvalidParameterValueException
     * @throws WCSException
     *             will be thrown if a WCS/DescribeCoverage specific part of the request is
     *             erroreous
     */
    public static DescribeCoverage createDescribeCoverage( String id, String kvp )
                            throws OGCWebServiceException, MissingParameterValueException,
                            InvalidParameterValueException {
        Map<String, String> map = KVP2Map.toMap( kvp );
        map.put( "ID", id );
        return create( map );
    }

    /**
     * creates a DescribeCoverage request from its XML representation
     * 
     * @param id
     *            unique ID of the request
     * @param doc
     *            XML representation of the request
     * @return created <tt>DescribeCoverage</tt>
     * @throws OGCWebServiceException
     *             will be thrown if something general is wrong
     * @throws MissingParameterValueException
     * @throws InvalidParameterValueException
     * @throws WCSException
     *             will be thrown if a WCS/DescribeCoverage specific part of the request is
     *             erroreous
     */
    public static DescribeCoverage create( String id, Document doc )
                            throws OGCWebServiceException, MissingParameterValueException,
                            InvalidParameterValueException {

        String[] coverages = null;
        String version = null;
        try {
            Element root = XMLTools.getRequiredChildElement( "DescribeCoverage", CommonNamespaces.WCSNS, doc );

            version = XMLTools.getAttrValue( root, null, "version", null );
            if ( version == null ) {
                ExceptionCode code = ExceptionCode.MISSINGPARAMETERVALUE;
                throw new MissingParameterValueException( "DescribeCoverage", "'version' is missing", code );
            }
            if ( !version.equals( "1.0.0" ) ) {
                ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
                throw new InvalidParameterValueException( "DescribeCoverage", "'version' <> 1.0.0", code );
            }

            String service = XMLTools.getAttrValue( root, null, "service", null );
            if ( service == null ) {
                ExceptionCode code = ExceptionCode.MISSINGPARAMETERVALUE;
                throw new MissingParameterValueException( "DescribeCoverage", "'service' is missing", code );
            }
            if ( !"WCS".equalsIgnoreCase( service ) ) {
                ExceptionCode code = ExceptionCode.INVALIDPARAMETERVALUE;
                throw new InvalidParameterValueException( "DescribeCoverage", "'service' <> WCS", code );
            }

            ElementList el = XMLTools.getChildElements( "Coverage", CommonNamespaces.WCSNS, root );
            coverages = new String[el.getLength()];
            for ( int i = 0; i < coverages.length; i++ ) {
                coverages[i] = XMLTools.getStringValue( el.item( i ) );
            }
        } catch ( XMLParsingException e ) {
            ExceptionCode code = WCSExceptionCode.INVALID_FORMAT;
            throw new WCSException( "DescribeCoverage", e.toString(), code );
        }

        return new DescribeCoverage( id, version, coverages );
    }

    /**
     * @param id
     *            unique ID of the request
     * @param version
     *            Request protocol version
     * @param coverages
     *            list of coverages to describe (identified by their name values in the Capabilities
     *            response). If <tt>null</tt> or length == 0 all coverages of the service
     *            instances will be described
     */
    public DescribeCoverage( String id, String version, String[] coverages ) {
        super( id, version );
        this.coverages = coverages;
    }

    /**
     * @return Returns the coverages.
     * 
     */
    public String[] getCoverages() {
        return coverages;
    }

    @Override
    public String getRequestParameter()
                            throws OGCWebServiceException {
        StringBuffer sb = new StringBuffer(1000);
        sb.append( "Service=WCS&REQUEST=DescribeCoverage&VERSION=1.0.0&coverage=" );
        if ( coverages != null ) {
            for ( int i = 0; i < coverages.length; i++ ) {
                sb.append( coverages[i] );
                if ( i < coverages.length - 1 ) {
                    sb.append( ',' );
                }
            }
        }        
        
        LOG.logDebug( "DescribeCoverage parameter", sb );
        
        return sb.toString();
    }
    
}
