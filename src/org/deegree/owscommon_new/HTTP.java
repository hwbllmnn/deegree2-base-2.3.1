//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/owscommon_new/HTTP.java $
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
package org.deegree.owscommon_new;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.deegree.model.metadata.iso19115.Linkage;
import org.deegree.model.metadata.iso19115.OnlineResource;

/**
 * <code>HTTP</code> describes the distributed computing platform which a service uses. In terms
 * of HTTP: it stores the links where it can be reached.
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */

public class HTTP implements DCP {

    private static final long serialVersionUID = 989887096571428263L;

    private List<List<DomainType>> constraints;

    private List<Type> types;

    private List<OnlineResource> links;

    /**
     * Standard constructor that initializes all encapsulated data.
     * 
     * @param links
     * @param constraints
     * @param types
     */
    public HTTP( List<OnlineResource> links, List<List<DomainType>> constraints, List<Type> types ) {
        this.links = links;
        this.constraints = constraints;
        this.types = types;
    }

    /**
     * @return Returns the constraints.
     */
    public List<List<DomainType>> getConstraints() {
        return constraints;
    }

    /**
     * @return Returns the types.
     */
    public List<Type> getTypes() {
        return types;
    }

    /**
     * @return the links.
     */
    public List<OnlineResource> getLinks() {
        return links;
    }

    /**
     * @return a list of all Get method URLs.
     */
    public List<URL> getGetOnlineResources() {
        List<URL> result = new ArrayList<URL>();

        for ( int i = 0; i < types.size(); ++i ) {
            if ( types.get( i ) == Type.Get ) {
                result.add( links.get( i ).getLinkage().getHref() );
            }
        }

        return result;
    }

    /**
     * @return a list of all Get method URLs.
     */
    public List<URL> getPostOnlineResources() {
        List<URL> result = new ArrayList<URL>();

        for ( int i = 0; i < types.size(); ++i ) {
            if ( types.get( i ) == Type.Post ) {
                result.add( links.get( i ).getLinkage().getHref() );
            }
        }

        return result;
    }

    /**
     * @param urlsnew
     */
    public void setGetOnlineResources( List<URL> urlsnew ) {
        List<OnlineResource> result = new ArrayList<OnlineResource>( links.size() );
        List<Type> typesnew = new ArrayList<Type>( links.size() );

        for ( int i = 0; i < types.size(); ++i ) {
            if ( types.get( i ) == Type.Post ) {
                result.add( links.get( i ) );
                typesnew.add( types.get( i ) );
            }
        }

        for ( URL url : urlsnew ) {
            result.add( new OnlineResource( new Linkage( url ) ) );
            typesnew.add( Type.Get );
        }

        links = result;
        types = typesnew;
    }

    /**
     * @param urlsnew
     */
    public void setPostOnlineResources( List<URL> urlsnew ) {
        List<OnlineResource> result = new ArrayList<OnlineResource>( links.size() );
        List<Type> typesnew = new ArrayList<Type>( links.size() );

        for ( int i = 0; i < types.size(); ++i ) {
            if ( types.get( i ) == Type.Get ) {
                result.add( links.get( i ) );
            }
        }

        for ( URL url : urlsnew ) {
            result.add( new OnlineResource( new Linkage( url ) ) );
            typesnew.add( Type.Post );
        }

        links = result;
        types = typesnew;
    }

    /**
     * Enumeration type indicating the used HTTP request method.
     * 
     * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
     * @author last edited by: $Author: apoth $
     * 
     * @version 2.0, $Revision: 9346 $, $Date: 2007-12-27 17:39:07 +0100 (Do, 27. Dez 2007) $
     * 
     * @since 2.0
     */
    public enum Type {
        /**
         * The Get HTTP method.
         */
        Get,
        /**
         * The Post HTTP method.
         */
        Post
    }

}
