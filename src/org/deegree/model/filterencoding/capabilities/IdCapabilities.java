//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/model/filterencoding/capabilities/IdCapabilities.java $
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
package org.deegree.model.filterencoding.capabilities;

import org.w3c.dom.Element;

/**
 * IdCapabilitiesBean
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * 
 * @author last edited by: $Author: apoth $
 * 
 * @version 2.0, $Revision: 9343 $, $Date: 2007-12-27 14:30:32 +0100 (Do, 27. Dez 2007) $
 * 
 * @since 2.0
 */
public class IdCapabilities {

    private Element[] eidElements;

    private Element[] fidElements;

    IdCapabilities(Element[] eidElements, Element[] fidElements) {
        this.eidElements = eidElements;
        this.fidElements = fidElements;
    }

    /**
     * @return Returns the eidElements.
     */
    public Element[] getEidElements() {
        return eidElements;
    }

    /**
     * @param eidElements
     *            The eidElements to set.
     */
    public void setEidElements(Element[] eidElements) {
        this.eidElements = eidElements;
    }

    /**
     * @return Returns the fidElements.
     */
    public Element[] getFidElements() {
        return fidElements;
    }

    /**
     * @param fidElements
     *            The fidElements to set.
     */
    public void setFidElements(Element[] fidElements) {
        this.fidElements = fidElements;
    }
}