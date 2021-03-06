//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/wfs/DescribeFeatureTypeHandler.java $
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
package org.deegree.ogcwebservices.wfs;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import javax.xml.transform.TransformerException;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLTools;
import org.deegree.framework.xml.XSLTDocument;
import org.deegree.framework.xml.schema.XSDocument;
import org.deegree.i18n.Messages;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.io.datastore.schema.MappedGMLSchema;
import org.deegree.model.feature.schema.FeatureType;
import org.deegree.ogcbase.CommonNamespaces;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.getcapabilities.DCPType;
import org.deegree.ogcwebservices.getcapabilities.HTTP;
import org.deegree.ogcwebservices.wfs.capabilities.WFSCapabilities;
import org.deegree.ogcwebservices.wfs.capabilities.WFSOperationsMetadata;
import org.deegree.ogcwebservices.wfs.operation.DescribeFeatureType;
import org.deegree.ogcwebservices.wfs.operation.FeatureTypeDescription;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Handler for {@link DescribeFeatureType} requests.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth </a>
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh </a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11377 $, $Date: 2008-04-23 09:55:34 +0200 (Mi, 23. Apr 2008) $
 */
class DescribeFeatureTypeHandler {

    private static final ILogger LOG = LoggerFactory.getLogger( DescribeFeatureTypeHandler.class );

    private static final String TEMPLATE_FILE = "SchemaContainerTemplate.xml";

    private static final URI XSNS = CommonNamespaces.XSNS;

    private static final String XS_PREFIX = CommonNamespaces.XS_PREFIX;

    private WFService wfs;

    private String describeFeatureTypeURL;

    private static XSLTDocument annotationFilter;

    static {
        try {
            annotationFilter = new XSLTDocument();
            annotationFilter.load( DescribeFeatureTypeHandler.class.getResource( "descfeaturetype.xsl" ) );
        } catch ( Exception e ) {
            e.printStackTrace();
            LOG.logError( "Could not read describe feature type annotation filter XSLT-script:" + e.getMessage(), e );
        }
    }

    /**
     * Creates a new <code>DescribeFeatureHandler</code> for the given {@link WFService}.
     * 
     * @param wfs
     *            associated WFService
     */
    DescribeFeatureTypeHandler( WFService wfs ) throws OGCWebServiceException {
        this.wfs = wfs;
        this.describeFeatureTypeURL = getDescribeFeatureTypeURL();
    }

    /**
     * Handles a DescribeFeatureType request.
     * <p>
     * If the requested feature types are all defined in the same GML application schema, the
     * corresponding document is returned. Otherwise, a container schema document is generated which
     * imports all necessary GML application schemas.
     * 
     * @param request
     *            DescribeFeatureType request
     * @return schema document encapsulated in a FeatureTypeDescription
     */
    FeatureTypeDescription handleRequest( DescribeFeatureType request )
                            throws OGCWebServiceException {

        XSDocument schema = null;
        // used to collect and eliminate duplicate GML application schema instances
        Set<MappedGMLSchema> schemaSet = new HashSet<MappedGMLSchema>();

        QualifiedName[] ftNames = request.getTypeNames();
        if ( ftNames == null || ftNames.length == 0 ) {
            // no feature types specified in request -> describe all visible feature types
            for ( MappedFeatureType ft : this.wfs.getMappedFeatureTypes().values() ) {
                if ( ft.isVisible() ) {
                    schemaSet.add( ft.getGMLSchema() );
                }
            }
        } else {
            for ( QualifiedName ftName : ftNames ) {
                MappedFeatureType ft = this.wfs.getMappedFeatureType( ftName );
                if ( ft == null ) {
                    String msg = Messages.getMessage( "WFS_FEATURE_TYPE_UNKNOWN", ftName );
                    throw new OGCWebServiceException( this.getClass().getName(), msg );
                }
                if ( !ft.isVisible() ) {
                    String msg = Messages.getMessage( "WFS_FEATURE_TYPE_INVISIBLE", ftName );
                    throw new OGCWebServiceException( this.getClass().getName(), msg );
                }
                schemaSet.add( ft.getGMLSchema() );
            }
        }

        if ( schemaSet.size() == 1 ) {
            schema = schemaSet.iterator().next().getDocument();
            try {
                schema = filterAnnotations( schema );
            } catch ( TransformerException e ) {
                String msg = "Could not remove annotations from annotated GML application schema.";
                LOG.logError( msg, e );
                throw new OGCWebServiceException( this.getClass().getName(), msg );
            }
        } else {
            try {
                schema = getXSDContainer( schemaSet );
                schema = filterAnnotations( schema );
            } catch ( Exception e ) {
                String msg = "Could not create XSD container document.";
                LOG.logError( msg, e );
                throw new OGCWebServiceException( this.getClass().getName(), msg );
            }
        }
        return new FeatureTypeDescription( schema );
    }

    /**
     * Filters out all annotation elements from the schema.
     * 
     * @param schema
     * @return filter document (without annotation elements)
     */
    private XSDocument filterAnnotations( XSDocument schema )
                            throws TransformerException {

        XMLFragment xml = annotationFilter.transform( schema );
        schema.setRootElement( xml.getRootElement() );
        return schema;
    }

    /**
     * Creates a container schema document that imports all necessary GML application schemas.
     * 
     * @param schemaSet
     * @return container schema document
     */
    private XSDocument getXSDContainer( Set<MappedGMLSchema> schemaSet )
                            throws IOException, SAXException {

        XSDocument schemaDoc = new XSDocument();
        schemaDoc.load( this.getClass().getResource( TEMPLATE_FILE ) );
        for ( MappedGMLSchema schema : schemaSet ) {
            FeatureType representative = schema.getFeatureTypes()[0];
            appendImportElement( schemaDoc.getRootElement(), representative.getName() );
        }

        return schemaDoc;
    }

    /**
     * It is assumed that the passed Element is the root of an XML schema description. The different
     * schema described by the passed <code>XMLFragment</code> will be included by adding an
     * import statement to the root schema:
     * 
     * <pre>
     * &lt;?xml version=&quot;1.0&quot; ?&gt;
     * &lt;schema targetNamespace=&quot;http://www.someserver.com/myns&quot;
     *         xmlns:myns=http://www.someserver.com/myns
     *         xmlns=&quot;http://www.w3.org/2001/XMLSchema&quot;
     *         elementFormDefault=&quot;qualified&quot;
     *         attributeFormDefault=&quot;unqualified&quot;&gt;
     *                                 
     *     &lt;import namespace=&quot;http://www.server01.com/ns01&quot; 
     *             schemaLocation=&quot;http://www.deegree.org/wfs?request=DescribeFeatureType&amp;typeName=ns01:TreesA_1M&quot;/&gt;
     * &lt;/schema&gt;
     * </pre>
     * 
     * @param root
     *            root element of the target schema
     * @param representative
     *            that shall be imported to the target schema
     */
    private void appendImportElement( Element root, QualifiedName representative ) {

        MappedFeatureType featureType = this.wfs.getMappedFeatureType( representative );
        MappedGMLSchema schema = featureType.getGMLSchema();

        URI targetNS = schema.getTargetNamespace();
        Element importElement = XMLTools.appendElement( root, XSNS, XS_PREFIX + ":import" );

        StringBuffer describeFeatureTypeURL = new StringBuffer( this.describeFeatureTypeURL );
        if ( !this.describeFeatureTypeURL.endsWith( "?" ) ) {
            describeFeatureTypeURL.append( "?" );
        }
        describeFeatureTypeURL.append( "SERVICE=WFS&VERSION=1.1.0&REQUEST=DescribeFeatureType&TYPENAME=" );
        describeFeatureTypeURL.append( representative.getPrefixedName() );
        describeFeatureTypeURL.append( "&NAMESPACE=xmlns(" );
        describeFeatureTypeURL.append( representative.getPrefix() ).append( "=" );
        describeFeatureTypeURL.append( representative.getNamespace() );
        describeFeatureTypeURL.append( ")" );

        importElement.setAttribute( "namespace", targetNS.toString() );
        importElement.setAttribute( "schemaLocation", describeFeatureTypeURL.toString() );
    }

    /**
     * Extracts one valid URL with HTTP binding that can be used for describe feature type
     * operations.
     * 
     * @return a valid URL for DescribeFeatureType requests
     * @throws OGCWebServiceException
     */
    private String getDescribeFeatureTypeURL()
                            throws OGCWebServiceException {
        WFSCapabilities capa = wfs.getCapabilities();
        WFSOperationsMetadata om = (WFSOperationsMetadata) capa.getOperationsMetadata();
        DCPType[] dcp = om.getDescribeFeatureType().getDCPs();
        int i = 0;
        while ( i < dcp.length - 1 && !( dcp[i].getProtocol() instanceof HTTP ) ) {
            i++;
        }

        if ( i >= dcp.length ) {
            String msg = "No HTTP DCP for DescribeFeatureType operation defined in WFS capabilities.";
            throw new OGCWebServiceException( this.getClass().getName(), msg );
        }

        String address = null;
        if ( ( (HTTP) dcp[i].getProtocol() ).getGetOnlineResources() != null
             && ( (HTTP) dcp[i].getProtocol() ).getGetOnlineResources().length > 0 ) {
            address = ( (HTTP) dcp[i].getProtocol() ).getGetOnlineResources()[0].toExternalForm();
        } else {
            address = ( (HTTP) dcp[i].getProtocol() ).getPostOnlineResources()[0].toExternalForm();
        }
        return address;
    }
}