//$HeadURL: svn+ssh://aerben@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/io/datastore/DatastoreTransaction.java $
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
 Department of Geography
 University of Bonn
 Meckenheimer Allee 166
 53115 Bonn
 Germany
 E-Mail: greve@giub.uni-bonn.de
 
 ---------------------------------------------------------------------------*/
package org.deegree.io.datastore;

import java.util.List;
import java.util.Map;

import org.deegree.io.datastore.idgenerator.FeatureIdAssigner;
import org.deegree.io.datastore.schema.MappedFeatureType;
import org.deegree.model.feature.Feature;
import org.deegree.model.feature.FeatureProperty;
import org.deegree.model.filterencoding.Filter;
import org.deegree.ogcbase.PropertyPath;
import org.deegree.ogcwebservices.wfs.operation.transaction.Delete;
import org.deegree.ogcwebservices.wfs.operation.transaction.Insert;
import org.deegree.ogcwebservices.wfs.operation.transaction.Native;
import org.deegree.ogcwebservices.wfs.operation.transaction.TransactionOperation;
import org.deegree.ogcwebservices.wfs.operation.transaction.Update;

/**
 * Handler for {@link TransactionOperation}s ({@link Insert}, {@link Update}, {@link Delete},
 * {@link Native}). One instance is bound to exactly one {@link Datastore} instance (and one
 * {@link Datastore} has no more than one active <code>DatastoreTransaction</code> at a time.
 * 
 * @author <a href="mailto:deshmukh@lat-lon.de">Anup Deshmukh</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author: apoth $
 * 
 * @version $Revision: 10660 $, $Date: 2008-03-24 22:39:54 +0100 (Mo, 24. Mär 2008) $
 */
public interface DatastoreTransaction {

    /**
     * Returns the associated {@link Datastore} instance.
     * 
     * @return the associated Datastore instance
     */
    public Datastore getDatastore();

    /**
     * Makes the changes persistent that have been performed in this transaction.
     * 
     * @throws DatastoreException
     */
    public void commit()
                            throws DatastoreException;

    /**
     * Aborts the changes that have been performed in this transaction.
     * 
     * @throws DatastoreException
     */
    public void rollback()
                            throws DatastoreException;

    /**
     * Releases the transaction instance so other clients may acquire a transaction (and underlying
     * resources, such as JDBCConnections can be cleaned up).
     * 
     * @throws DatastoreException
     */
    public void release()
                            throws DatastoreException;

    /**
     * Inserts the given feature instances into the datastore.
     * <p>
     * Please note that the features to be inserted must have suitable feature ids at this point.
     * 
     * @param features
     * @return feature ids of the inserted (root) features
     * @throws DatastoreException
     * @see FeatureIdAssigner
     */
    public List<FeatureId> performInsert( List<Feature> features )
                            throws DatastoreException;

    /**
     * Performs an update operation against the datastore.
     * 
     * @param mappedFeatureType
     *            feature type that is to be updated
     * @param replacementProps
     *            properties and their replacement values
     * @param filter
     *            selects the feature instances that are to be updated
     * @param lockId
     *            optional id of associated lock (may be null)
     * @return number of updated feature instances
     * @throws DatastoreException
     */
    public int performUpdate( MappedFeatureType mappedFeatureType, Map<PropertyPath, FeatureProperty> replacementProps,
                              Filter filter, String lockId )
                            throws DatastoreException;

    /**
     * Performs an update operation against the datastore.
     * <p>
     * The filter is expected to match exactly one feature which will be replaced by the specified
     * replacement feature.
     * 
     * @param mappedFeatureType
     *            feature type that is to be updated
     * @param replacementFeature
     *            feature instance that will replace the selected feature
     * @param filter
     *            selects the single feature instances that is to be replaced
     * @param lockId
     *            optional id of associated lock (may be null)
     * @return number of updated feature instances (must be 0 or 1)
     * @throws DatastoreException
     */
    public int performUpdate( MappedFeatureType mappedFeatureType, Feature replacementFeature, Filter filter,
                              String lockId )
                            throws DatastoreException;

    /**
     * Deletes the features from the datastore that are matched by the given filter and type.
     * 
     * @param mappedFeatureType
     * @param filter
     * @param lockId
     *            optional id of associated lock (may be null)
     * @return number of deleted feature instances
     * @throws DatastoreException
     */
    public int performDelete( MappedFeatureType mappedFeatureType, Filter filter, String lockId )
                            throws DatastoreException;

    /**
     * Performs a native operation against the datastore.
     * 
     * @param operation
     *            operation to perform
     * @return int
     * @throws DatastoreException
     */
    public int performNative( Native operation )
                            throws DatastoreException;
}