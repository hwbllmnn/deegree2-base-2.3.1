//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/describeplatform/PlatformDescriptionDocument.java $
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
 Aennchenstraße 19  
 53177 Bonn
 Germany
 E-Mail: poth@lat-lon.de

 Prof. Dr. Klaus Greve
 lat/lon GmbH
 Aennchenstraße 19
 53177 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de

 ---------------------------------------------------------------------------*/
package org.deegree.ogcwebservices.sos.describeplatform;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.deegree.datatypes.QualifiedName;
import org.deegree.framework.util.StringTools;
import org.deegree.framework.xml.XMLFragment;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.deegree.framework.xml.XSLTDocument;
import org.deegree.ogcwebservices.OGCWebServiceException;
import org.deegree.ogcwebservices.sos.ComponentDescriptionDocument;
import org.deegree.ogcwebservices.sos.WFSRequestGenerator;
import org.deegree.ogcwebservices.sos.WFSRequester;
import org.deegree.ogcwebservices.sos.configuration.SOSDeegreeParams;
import org.deegree.ogcwebservices.sos.configuration.SourceServerConfiguration;
import org.deegree.ogcwebservices.sos.sensorml.Classifier;
import org.deegree.ogcwebservices.sos.sensorml.ComponentDescription;
import org.deegree.ogcwebservices.sos.sensorml.EngineeringCRS;
import org.deegree.ogcwebservices.sos.sensorml.Identifier;
import org.deegree.ogcwebservices.sos.sensorml.LocationModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * gets the platform metadata from a xsl transformed wfs result
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 */

public class PlatformDescriptionDocument extends ComponentDescriptionDocument {

    /**
     * 
     */
    private static final long serialVersionUID = 645409071795675313L;

    private static final String XML_TEMPLATE = "DescribePlatformTemplate.xml";

    /**
     * creates an document from a template file
     */
    @Override
    public void createEmptyDocument()
                            throws IOException, SAXException {
        URL url = PlatformDescriptionDocument.class.getResource( XML_TEMPLATE );
        if ( url == null ) {
            throw new IOException( "The resource '" + XML_TEMPLATE + " could not be found." );
        }
        load( url );
    }

    /**
     * gets the platform descriptions from a wfs and transform it with a xslt script
     * 
     * @param deegreeParams
     * @param typNames
     * @return the metadata
     * @throws OGCWebServiceException
     * 
     */
    public PlatformMetadata[] getPlatform( SOSDeegreeParams deegreeParams, String[] typNames )
                            throws OGCWebServiceException {

        try {

            // gets the documents from wfs server
            Document[] docs = getPlatformDescriptionDocuments( deegreeParams, typNames );

            ArrayList<PlatformMetadata> platformMetadata = new ArrayList<PlatformMetadata>( 1000 );

            for ( int d = 0; d < docs.length; d++ ) {

                if ( docs[d] != null ) {

                    List<Node> nl = XMLTools.getNodes( docs[d], "sml:Platforms/sml:Platform", nsContext );

                    // process all platforms in document
                    for ( int y = 0; y < nl.size(); y++ ) {

                        Node platformNode = nl.get( y );

                        // get identifiedAs
                        List<Node> identifierList = XMLTools.getNodes( platformNode, "sml:identifiedAs", nsContext );
                        if ( ( identifierList == null ) || ( identifierList.size() <= 0 ) ) {
                            throw new XMLParsingException( "at least one identifiedAs required" );

                        }
                        ArrayList<Identifier> identifiers = new ArrayList<Identifier>( identifierList.size() );
                        for ( int i = 0; i < identifierList.size(); i++ ) {
                            identifiers.add( getIdentifiedAs( identifierList.get( i ) ) );
                        }

                        // get ClassifiedAs
                        List<Node> classifierList = XMLTools.getNodes( platformNode, "sml:classifiedAs", nsContext );
                        ArrayList<Classifier> classifiers = new ArrayList<Classifier>( classifierList.size() );
                        for ( int i = 0; i < classifierList.size(); i++ ) {
                            classifiers.add( getClassifiedAs( classifierList.get( i ) ) );
                        }

                        // get attachedTo
                        String attachedTo = getAttachedTo( XMLTools.getNode( platformNode, "sml:attachedTo", nsContext ) );

                        // get hasCRS
                        EngineeringCRS hasCRS = getHasCRS( XMLTools.getNode( platformNode, "sml:hasCRS", nsContext ) );

                        // get locatedUsing
                        List<Node> locationModelList = XMLTools.getNodes( platformNode, "sml:locatedUsing", nsContext );
                        ArrayList<LocationModel> locationModels = new ArrayList<LocationModel>(
                                                                                                locationModelList.size() );
                        for ( int i = 0; i < locationModelList.size(); i++ ) {
                            locationModels.add( getLocatedUsing( locationModelList.get( i ) ) );
                        }

                        // get describedBy
                        ComponentDescription describedBy = getDescribedBy( XMLTools.getNode( platformNode,
                                                                                             "sml:describedBy",
                                                                                             nsContext ) );

                        // get carries
                        List<Node> carriesList = XMLTools.getNodes( platformNode, "sml:carries", nsContext );
                        ArrayList<String> carries = new ArrayList<String>( carriesList.size() );
                        for ( int i = 0; i < carriesList.size(); i++ ) {
                            String s = XMLTools.getRequiredNodeAsString( carriesList.get( i ), "sml:Asset/text()",
                                                                         nsContext );
                            carries.add( s );
                        }

                        Identifier[] ids = identifiers.toArray( new Identifier[identifiers.size()] );
                        Classifier[] cls = classifiers.toArray( new Classifier[classifiers.size()] );
                        LocationModel[] lm = locationModels.toArray( new LocationModel[locationModels.size()] );
                        String[] crrs = carries.toArray( new String[carries.size()] );
                        PlatformMetadata pmd = new PlatformMetadata( ids, cls, hasCRS, lm, describedBy, attachedTo,
                                                                     crrs );
                        // add act Metadata to ArrayList
                        platformMetadata.add( pmd );
                    }

                }
            }

            // return the Array with Sensormetadata
            PlatformMetadata[] pfmd = new PlatformMetadata[platformMetadata.size()];
            return platformMetadata.toArray( pfmd );

        } catch ( Exception e ) {
            e.printStackTrace();
            throw new OGCWebServiceException( "sos webservice failure" );
        }
    }

    /**
     * requests all servers which serves one of the requested platforms and returns the transformed
     * result docs
     * 
     */
    private Document[] getPlatformDescriptionDocuments( SOSDeegreeParams dp, String[] typNames )
                            throws IOException, SAXException, XMLParsingException, TransformerException,
                            OGCWebServiceException {

        ArrayList<Document> resultDocuments = new ArrayList<Document>();

        Hashtable<String, ArrayList<String>> servers = new Hashtable<String, ArrayList<String>>();

        for ( int t = 0; t < typNames.length; t++ ) {
            String sourceServerId = dp.getPlatformConfiguration( typNames[t] ).getSourceServerId();

            if ( servers.containsKey( sourceServerId ) ) {
                servers.get( sourceServerId ).add( typNames[t] );
            } else {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add( typNames[t] );
                servers.put( sourceServerId, temp );
            }

        }

        String[] keySet = servers.keySet().toArray( new String[servers.keySet().size()] );

        // request all servers from servers hashtable
        for ( int i = 0; i < keySet.length; i++ ) {

            List<String> ids = servers.get( keySet[i] );

            String[] idProps = new String[ids.size()];
            for ( int a = 0; a < ids.size(); a++ ) {
                idProps[a] = dp.getPlatformConfiguration( ids.get( a ) ).getIdPropertyValue();
            }

            QualifiedName pdft = dp.getSourceServerConfiguration( keySet[i] ).getPlatformDescriptionFeatureType();
            QualifiedName pdid = dp.getSourceServerConfiguration( keySet[i] ).getPlatformDescriptionIdPropertyName();
            Document request = WFSRequestGenerator.createIsLikeOperationWFSRequest( idProps, pdft, pdid );

            SourceServerConfiguration ssc = dp.getSourceServerConfiguration( keySet[i] );

            Document resultDoc = null;
            try {
                resultDoc = WFSRequester.sendWFSrequest( request, ssc.getDataService() );
            } catch ( Exception e ) {
                throw new OGCWebServiceException( this.getClass().getName(), "could not get platform data from WFS "
                                                                             + StringTools.stackTraceToString( e ) );
            }
            if ( resultDoc != null ) {
                URL pdxs = dp.getSourceServerConfiguration( keySet[i] ).getPlatformDescriptionXSLTScriptSource();
                XSLTDocument sheet = new XSLTDocument();
                sheet.load( pdxs );
                XMLFragment input = new XMLFragment();
                input.setRootElement( resultDoc.getDocumentElement() );
                XMLFragment result = sheet.transform( input );
                resultDocuments.add( result.getRootElement().getOwnerDocument() );
            }

        }

        return resultDocuments.toArray( new Document[resultDocuments.size()] );
    }

}