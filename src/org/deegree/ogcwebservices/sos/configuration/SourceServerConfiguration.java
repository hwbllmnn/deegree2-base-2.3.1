//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/ogcwebservices/sos/configuration/SourceServerConfiguration.java $
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
package org.deegree.ogcwebservices.sos.configuration;

import java.net.URL;
import java.util.ArrayList;

import org.deegree.datatypes.QualifiedName;
import org.deegree.ogcwebservices.OGCWebService;

/**
 * represent a sourceServer configuration
 * 
 * @author <a href="mailto:mkulbe@lat-lon.de">Matthias Kulbe </a>
 * 
 */

public class SourceServerConfiguration {

    private String id;

    private String service;

    private String version;

    private OGCWebService dataService;

    // platform description configs
    private QualifiedName platformDescriptionFeatureType;

    private QualifiedName platformDescriptionIdPropertyName;

    private QualifiedName platformDescriptionCoordPropertyName;

    private URL platformDescriptionXSLTScriptSource;

    // sensorDescription configs
    private QualifiedName sensorDescriptionFeatureType;

    private QualifiedName sensorDescriptionIdPropertyName;

    private URL sensorDescriptionXSLTScriptSource;

    // list of all provided sensors
    private ArrayList<SensorConfiguration> sensors = new ArrayList<SensorConfiguration>();

    // list of all provided platforms
    private ArrayList<PlatformConfiguration> platforms = new ArrayList<PlatformConfiguration>();

    /**
     * 
     * @param id
     * @param service
     * @param version
     * @param dataService
     * @param platformDescriptionFeatureType
     * @param platformDescriptionIdPropertyName
     * @param platformDescriptionCoordPropertyName
     * @param platformDescriptionXSLTScriptSource
     * @param sensorDescriptionFeatureType
     * @param sensorDescriptionIdPropertyName
     * @param sensorDescriptionXSLTScriptSource
     */
    public SourceServerConfiguration( String id, String service, String version, OGCWebService dataService,
                                      QualifiedName platformDescriptionFeatureType,
                                      QualifiedName platformDescriptionIdPropertyName,
                                      QualifiedName platformDescriptionCoordPropertyName,
                                      URL platformDescriptionXSLTScriptSource,
                                      QualifiedName sensorDescriptionFeatureType,
                                      QualifiedName sensorDescriptionIdPropertyName,
                                      URL sensorDescriptionXSLTScriptSource ) {

        this.service = service;
        this.id = id;
        this.version = version;
        this.dataService = dataService;
        this.platformDescriptionFeatureType = platformDescriptionFeatureType;
        this.platformDescriptionIdPropertyName = platformDescriptionIdPropertyName;
        this.platformDescriptionCoordPropertyName = platformDescriptionCoordPropertyName;
        this.platformDescriptionXSLTScriptSource = platformDescriptionXSLTScriptSource;
        this.sensorDescriptionFeatureType = sensorDescriptionFeatureType;
        this.sensorDescriptionIdPropertyName = sensorDescriptionIdPropertyName;
        this.sensorDescriptionXSLTScriptSource = sensorDescriptionXSLTScriptSource;

    }

    public QualifiedName getPlatformDescriptionCoordPropertyName() {
        return platformDescriptionCoordPropertyName;
    }

    public QualifiedName getPlatformDescriptionFeatureType() {
        return platformDescriptionFeatureType;
    }

    public QualifiedName getPlatformDescriptionIdPropertyName() {
        return platformDescriptionIdPropertyName;
    }

    public QualifiedName getSensorDescriptionFeatureType() {
        return sensorDescriptionFeatureType;
    }

    public QualifiedName getSensorDescriptionIdPropertyName() {
        return sensorDescriptionIdPropertyName;
    }

    /**
     * adds a new sensor
     * 
     * @param sensor
     */
    public void addSensor( SensorConfiguration sensor ) {
        this.sensors.add( sensor );

    }

    /**
     * returns all sensors
     * 
     * @return ll sensors
     */
    public SensorConfiguration[] getSensors() {
        return this.sensors.toArray( new SensorConfiguration[this.sensors.size()] );
    }

    /**
     * returns the sensor by the given scs id
     * 
     * @param id
     * @return the sensor by the given scs id
     */
    public SensorConfiguration getSensorById( String id ) {
        for ( int i = 0; i < this.sensors.size(); i++ ) {
            if ( this.sensors.get( i ).getId().equals( id ) ) {
                return this.sensors.get( i );
            }
        }
        return null;
    }

    /**
     * returns the sensor by the given idPropertyValue on the sourceServer
     * 
     * @param id
     * @return the sensor by the given idPropertyValue on the sourceServer
     */
    public SensorConfiguration getSensorByIdPropertyValue( String id ) {
        for ( int i = 0; i < this.sensors.size(); i++ ) {
            if ( this.sensors.get( i ).getIdPropertyValue().equals( id ) ) {
                return this.sensors.get( i );
            }
        }
        return null;
    }

    /**
     * returns true if the sensor with the given id exists
     * 
     * @param id
     * @return <code>true</code> if the sensor with the given id exists
     */
    public boolean haveSensorById( String id ) {
        for ( int i = 0; i < this.sensors.size(); i++ ) {
            if ( this.sensors.get( i ).getId().equals( id ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns true if the sensor with the given idPropertyValue exists
     * 
     * @param id
     * @return <code>true</code> if the sensor with the given idPropertyValue exists
     */
    public boolean haveSensorByIdPropertyValue( String id ) {
        for ( int i = 0; i < this.sensors.size(); i++ ) {
            if ( this.sensors.get( i ).getIdPropertyValue().equals( id ) ) {
                return true;
            }
        }
        return false;
    }

    public OGCWebService getDataService() {
        return dataService;
    }

    public String getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public String getVersion() {
        return version;
    }

    /**
     * overwrites the equals function
     */
    public boolean equals( Object obj ) {
        if ( !( obj instanceof SourceServerConfiguration ) ) {
            return false;
        }
        if ( this.getId().equals( ( (SourceServerConfiguration) obj ).getId() ) ) {
            return true;
        }
        return false;
    }

    /**
     * overrides the hashCode function
     */
    public int hashCode() {
        return this.getId().hashCode();
    }

    /**
     * adds a new platform
     * 
     * @param platform
     */
    public void addPlatform( PlatformConfiguration platform ) {
        this.platforms.add( platform );

    }

    /**
     * returns all platforms
     * 
     * @return all platforms
     */
    public PlatformConfiguration[] getPlatforms() {
        return this.platforms.toArray( new PlatformConfiguration[this.platforms.size()] );
    }

    /**
     * returns the platform by the given scs id
     * 
     * @param id
     * @return the platform by the given scs id
     */
    public PlatformConfiguration getPlatformById( String id ) {
        for ( int i = 0; i < this.platforms.size(); i++ ) {
            if ( this.platforms.get( i ).getId().equals( id ) ) {
                return this.platforms.get( i );
            }
        }
        return null;
    }

    /**
     * returns the platform by the given idPropertyValue on the sourceServer
     * 
     * @param id
     * @return the platform by the given idPropertyValue on the sourceServer
     */
    public PlatformConfiguration getPlatformByIdPropertyValue( String id ) {

        for ( int i = 0; i < this.platforms.size(); i++ ) {
            if ( this.platforms.get( i ).getId().equals( id ) ) {
                return this.platforms.get( i );
            }
        }

        return null;
    }

    /**
     * returns true if the platform with the given id exists
     * 
     * @param id
     * @return <code>true</code> if the platform with the given id exists
     */
    public boolean havePlatformById( String id ) {
        for ( int i = 0; i < this.platforms.size(); i++ ) {
            if ( this.platforms.get( i ).getId().equals( id ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns true if the platform with the given idPropertyValue exists
     * 
     * @param id
     * @return <code>true</code> if the platform with the given idPropertyValue exists
     */
    public boolean havePlatformByIdPropertyValue( String id ) {
        for ( int i = 0; i < this.platforms.size(); i++ ) {
            if ( this.platforms.get( i ).getIdPropertyValue().equals( id ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * returns true, if all parameters, to request platform metadata, is set
     * 
     * @return <code>true</code>, if all parameters, to request platform metadata, is set
     */
    public boolean havePlatformDescriptionData() {

        if ( ( this.platformDescriptionFeatureType != null ) && ( this.platformDescriptionIdPropertyName != null )
             && ( this.platformDescriptionCoordPropertyName != null ) ) {
            return true;
        }

        return false;
    }

    /**
     * returns true, if all parameters, to request sensor metadata, is set
     * 
     * @return <code>true</code>, if all parameters, to request sensor metadata, is set
     */
    public boolean haveSensorDescriptionData() {
        if ( ( this.sensorDescriptionFeatureType != null ) && ( this.sensorDescriptionIdPropertyName != null ) ) {
            return true;
        }

        return false;
    }

    public URL getPlatformDescriptionXSLTScriptSource() {
        return this.platformDescriptionXSLTScriptSource;
    }

    public URL getSensorDescriptionXSLTScriptSource() {
        return this.sensorDescriptionXSLTScriptSource;
    }
}
