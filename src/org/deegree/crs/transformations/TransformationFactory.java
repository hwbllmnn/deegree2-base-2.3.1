//$HeadURL: svn+ssh://jwilden@scm.wald.intevation.org/deegree/base/trunk/src/org/deegree/crs/transformations/TransformationFactory.java $
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
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
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
package org.deegree.crs.transformations;

import static org.deegree.crs.projections.ProjectionUtils.EPS11;

import javax.vecmath.GMatrix;
import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;

import org.deegree.crs.Identifiable;
import org.deegree.crs.components.Axis;
import org.deegree.crs.components.Ellipsoid;
import org.deegree.crs.components.GeodeticDatum;
import org.deegree.crs.components.PrimeMeridian;
import org.deegree.crs.components.Unit;
import org.deegree.crs.coordinatesystems.CompoundCRS;
import org.deegree.crs.coordinatesystems.CoordinateSystem;
import org.deegree.crs.coordinatesystems.GeocentricCRS;
import org.deegree.crs.coordinatesystems.GeographicCRS;
import org.deegree.crs.coordinatesystems.ProjectedCRS;
import org.deegree.crs.exceptions.TransformationException;
import org.deegree.crs.transformations.coordinate.CRSTransformation;
import org.deegree.crs.transformations.coordinate.ConcatenatedTransform;
import org.deegree.crs.transformations.coordinate.DirectTransform;
import org.deegree.crs.transformations.coordinate.GeocentricTransform;
import org.deegree.crs.transformations.coordinate.MatrixTransform;
import org.deegree.crs.transformations.coordinate.ProjectionTransform;
import org.deegree.crs.transformations.helmert.WGS84ConversionInfo;
import org.deegree.crs.transformations.polynomial.PolynomialTransformation;
import org.deegree.crs.utilities.Matrix;
import org.deegree.framework.log.ILogger;
import org.deegree.framework.log.LoggerFactory;
import org.deegree.i18n.Messages;

/**
 * The <code>TransformationFactory</code> class is the central access point for all transformations between different
 * crs's.
 * <p>
 * It creates a transformation chain for two given CoordinateSystems by considering their type. For example the
 * Transformation chain from EPSG:31466 ( a projected crs with underlying geographic crs epsg:4314 using the DHDN datum
 * and the TransverseMercator Projection) to EPSG:28992 (another projected crs with underlying geographic crs epsg:4289
 * using the 'new Amersfoort Datum' and the StereographicAzimuthal Projection) would result in following Transformation
 * Chain:
 * <ol>
 * <li>Inverse projection - thus getting the coordinates in lat/lon for geographic crs epsg:4314</li>
 * <li>Geodetic transformation - thus getting x-y-z coordinates for geographic crs epsg:4314</li>
 * <li>WGS84 transformation -thus getting the x-y-z coordinates for the WGS84 datum </li>
 * <li>Inverse WGS84 transformation -thus getting the x-y-z coordinates for the geodetic from epsg:4289</li>
 * <li>Inverse geodetic - thus getting the lat/lon for epsg:4289</li>
 * <li>projection - getting the coordinates (in meters) for epsg:28992</li>
 * </ol>
 * 
 * @author <a href="mailto:bezema@lat-lon.de">Rutger Bezema</a>
 * 
 * @author last edited by: $Author: rbezema $
 * 
 * @version $Revision: 11358 $, $Date: 2008-04-22 14:30:55 +0200 (Tue, 22 Apr 2008) $
 * 
 */
public class TransformationFactory {
    private static ILogger LOG = LoggerFactory.getLogger( TransformationFactory.class );

    /**
     * The default coordinate transformation factory. Will be constructed only when first needed.
     */
    private static TransformationFactory DEFAULT_INSTANCE = null;

    private TransformationFactory() {
        // nottin
    }

    /**
     * @return the default coordinate transformation factory.
     */
    public static synchronized TransformationFactory getInstance() {
        if ( DEFAULT_INSTANCE == null ) {
            DEFAULT_INSTANCE = new TransformationFactory();
        }
        return DEFAULT_INSTANCE;
    }

    /**
     * Creates a transformation between two coordinate systems. This method will examine the coordinate systems in order
     * to construct a transformation between them.
     * 
     * @param sourceCRS
     *            Input coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     * @throws TransformationException
     *             if no transformation path has been found.
     * @throws IllegalArgumentException
     *             if the sourceCRS or targetCRS are <code>null</code>.
     * 
     */
    public CRSTransformation createFromCoordinateSystems( final CoordinateSystem sourceCRS,
                                                          final CoordinateSystem targetCRS )
                            throws TransformationException, IllegalArgumentException {
        if ( sourceCRS == null ) {
            throw new IllegalArgumentException( "The source CRS may not be null" );
        }
        if ( targetCRS == null ) {
            throw new IllegalArgumentException( "The target CRS may not be null" );
        }
        if ( ( sourceCRS.getType() != CoordinateSystem.GEOGRAPHIC_CRS
               && sourceCRS.getType() != CoordinateSystem.COMPOUND_CRS
               && sourceCRS.getType() != CoordinateSystem.PROJECTED_CRS && sourceCRS.getType() != CoordinateSystem.GEOCENTRIC_CRS )
             || ( targetCRS.getType() != CoordinateSystem.GEOGRAPHIC_CRS
                  && targetCRS.getType() != CoordinateSystem.COMPOUND_CRS
                  && targetCRS.getType() != CoordinateSystem.PROJECTED_CRS && targetCRS.getType() != CoordinateSystem.GEOCENTRIC_CRS ) ) {
            throw new TransformationException( sourceCRS, targetCRS,
                                               "Either the target crs type or the source crs type was unknown" );
        }

        if ( sourceCRS.equals( targetCRS ) ) {
            LOG.logDebug( "Source crs and target crs are equal, no transformation needed (returning identity matrix)." );
            final Matrix matrix = new Matrix( sourceCRS.getDimension() + 1 );
            matrix.setIdentity();
            return createAffineTransform( sourceCRS, targetCRS, matrix );
        }

        CRSTransformation result = null;
        // check if the source crs has an alternative transformation for the given target, if so use it
        if ( sourceCRS.hasDirectTransformation( targetCRS ) ) {
            PolynomialTransformation direct = sourceCRS.getDirectTransformation( targetCRS );
            LOG.logDebug( "Using direct (polynomial) transformation instead of a helmert transformation: "
                          + direct.getName() );
            result = new DirectTransform( direct, sourceCRS );
        } else {
            if ( sourceCRS.getType() == CoordinateSystem.GEOGRAPHIC_CRS ) {
                /**
                 * Geographic --> Geographic, Projected, Geocentric or Compound
                 */
                final GeographicCRS source = (GeographicCRS) sourceCRS;
                if ( targetCRS.getType() == CoordinateSystem.PROJECTED_CRS ) {
                    result = createTransformation( source, (ProjectedCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.GEOGRAPHIC_CRS ) {
                    result = createTransformation( source, (GeographicCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.GEOCENTRIC_CRS ) {
                    result = createTransformation( source, (GeocentricCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.COMPOUND_CRS ) {
                    CompoundCRS target = (CompoundCRS) targetCRS;
                    CompoundCRS sTmp = new CompoundCRS( target.getHeightAxis(), source, target.getDefaultHeight(),
                                                        new Identifiable( new String[] { source.getIdentifier()
                                                                                         + "_compound" } ) );
                    result = createTransformation( sTmp, target );
                }
            } else if ( sourceCRS.getType() == CoordinateSystem.PROJECTED_CRS ) {
                /**
                 * Projected --> Projected, Geographic, Geocentric or Compound
                 */
                final ProjectedCRS source = (ProjectedCRS) sourceCRS;
                if ( targetCRS.getType() == CoordinateSystem.PROJECTED_CRS ) {
                    result = createTransformation( source, (ProjectedCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.GEOGRAPHIC_CRS ) {
                    result = createTransformation( source, (GeographicCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.GEOCENTRIC_CRS ) {
                    result = createTransformation( source, (GeocentricCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.COMPOUND_CRS ) {
                    CompoundCRS target = (CompoundCRS) targetCRS;
                    CompoundCRS sTmp = new CompoundCRS( target.getHeightAxis(), source, target.getDefaultHeight(),
                                                        new Identifiable( new String[] { source.getIdentifier()
                                                                                         + "_compound" } ) );
                    result = createTransformation( sTmp, target );
                }
            } else if ( sourceCRS.getType() == CoordinateSystem.GEOCENTRIC_CRS ) {
                /**
                 * Geocentric --> Projected, Geographic, Geocentric or Compound
                 */
                final GeocentricCRS source = (GeocentricCRS) sourceCRS;
                if ( targetCRS.getType() == CoordinateSystem.PROJECTED_CRS ) {
                    result = createTransformation( source, (ProjectedCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.GEOGRAPHIC_CRS ) {
                    result = createTransformation( source, (GeographicCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.GEOCENTRIC_CRS ) {
                    result = createTransformation( source, (GeocentricCRS) targetCRS );
                } else if ( targetCRS.getType() == CoordinateSystem.COMPOUND_CRS ) {
                    CompoundCRS target = (CompoundCRS) targetCRS;
                    CompoundCRS sTmp = new CompoundCRS( target.getHeightAxis(), source, target.getDefaultHeight(),
                                                        new Identifiable( new String[] { source.getIdentifier()
                                                                                         + "_compound" } ) );
                    result = createTransformation( sTmp, target );
                }
            } else if ( sourceCRS.getType() == CoordinateSystem.COMPOUND_CRS ) {
                /**
                 * Compound --> Projected, Geographic, Geocentric or Compound
                 */
                final CompoundCRS source = (CompoundCRS) sourceCRS;
                CompoundCRS target = null;
                if ( targetCRS.getType() != CoordinateSystem.COMPOUND_CRS ) {
                    target = new CompoundCRS(
                                              source.getHeightAxis(),
                                              targetCRS,
                                              source.getDefaultHeight(),
                                              new Identifiable(
                                                                new String[] { targetCRS.getIdentifier() + "_compound" } ) );
                } else {
                    target = (CompoundCRS) targetCRS;
                }
                result = createTransformation( source, target );
            }
        }
        if ( result == null ) {
            LOG.logDebug( "The resulting transformation was null, returning an identity matrix." );
            final Matrix matrix = new Matrix( sourceCRS.getDimension() + 1 );
            matrix.setIdentity();
            result = createAffineTransform( sourceCRS, targetCRS, matrix );
        } else {
            result = concatenate(
                                  createAffineTransform( sourceCRS, sourceCRS, toStandardizedValues( sourceCRS, false ) ),
                                  result, createAffineTransform( targetCRS, targetCRS, toStandardizedValues( targetCRS,
                                                                                                             true ) ) );
        }
        if ( LOG.getLevel() == ILogger.LOG_DEBUG ) {
            StringBuilder output = new StringBuilder( "The resulting transformation chain: \n" );
            output = result.getTransformationPath( output );
            LOG.logDebug( output.toString() );
        }
        return result;

    }

    /**
     * @param sourceCRS
     * @param targetCRS
     * @return the transformation chain or <code>null</code> if the transformation operation is the identity.
     * @throws TransformationException
     */
    private CRSTransformation createTransformation( GeocentricCRS sourceCRS, GeographicCRS targetCRS )
                            throws TransformationException {
        final CRSTransformation result = createTransformation( targetCRS, sourceCRS );
        if ( result != null ) {
            result.inverse();
        }
        return result;
    }

    /**
     * @param sourceCRS
     * @param targetCRS
     * @return the transformation chain or <code>null</code> if the transformation operation is the identity.
     * @throws TransformationException
     */
    private CRSTransformation createTransformation( GeocentricCRS sourceCRS, ProjectedCRS targetCRS )
                            throws TransformationException {
        final CRSTransformation result = createTransformation( targetCRS, sourceCRS );
        if ( result != null ) {
            result.inverse();
        }
        return result;
    }

    /**
     * This method is valid for all transformations which use a compound crs, because the extra heightvalues need to be
     * considered throughout the transformation.
     * 
     * @param sourceCRS
     * @param targetCRS
     * @return the transformation chain or <code>null</code> if the transformation operation is the identity.
     * @throws TransformationException
     */
    private CRSTransformation createTransformation( CompoundCRS sourceCRS, CompoundCRS targetCRS )
                            throws TransformationException {
        if ( sourceCRS.getUnderlyingCRS().equals( targetCRS.getUnderlyingCRS() ) ) {
            return null;
        }
        LOG.logDebug( "Creating compound( " + sourceCRS.getUnderlyingCRS().getIdentifier()
                      + ") ->compound transformation( " + targetCRS.getUnderlyingCRS().getIdentifier()
                      + "): from (source): " + sourceCRS.getIdentifier() + " to(target): " + targetCRS.getIdentifier() );
        final int sourceType = sourceCRS.getUnderlyingCRS().getType();
        final int targetType = targetCRS.getUnderlyingCRS().getType();
        CRSTransformation result = null;
        // basic check for simple (invert) projections
        if ( sourceType == CoordinateSystem.PROJECTED_CRS && targetType == CoordinateSystem.GEOGRAPHIC_CRS ) {
            if ( ( ( (ProjectedCRS) sourceCRS.getUnderlyingCRS() ).getGeographicCRS() ).equals( targetCRS.getUnderlyingCRS() ) ) {
                result = new ProjectionTransform( (ProjectedCRS) sourceCRS.getUnderlyingCRS() );
                result.inverse();
            }
        }
        if ( sourceType == CoordinateSystem.GEOGRAPHIC_CRS && targetType == CoordinateSystem.PROJECTED_CRS ) {
            if ( ( ( (ProjectedCRS) targetCRS.getUnderlyingCRS() ).getGeographicCRS() ).equals( sourceCRS.getUnderlyingCRS() ) ) {
                result = new ProjectionTransform( (ProjectedCRS) targetCRS.getUnderlyingCRS() );
            }
        }
        if ( result == null ) {
            GeocentricCRS sourceGeocentric = null;
            if ( sourceType == CoordinateSystem.GEOCENTRIC_CRS ) {
                sourceGeocentric = (GeocentricCRS) sourceCRS.getUnderlyingCRS();
            } else {
                sourceGeocentric = new GeocentricCRS( sourceCRS.getGeodeticDatum(), "tmp_" + sourceCRS.getIdentifier()
                                                                                    + "_geocentric",
                                                      sourceCRS.getName() + "_Geocentric" );
            }
            GeocentricCRS targetGeocentric = null;
            if ( targetType == CoordinateSystem.GEOCENTRIC_CRS ) {
                targetGeocentric = (GeocentricCRS) targetCRS.getUnderlyingCRS();
            } else {
                targetGeocentric = new GeocentricCRS( targetCRS.getGeodeticDatum(), "tmp_" + targetCRS.getIdentifier()
                                                                                    + "_geocentric",
                                                      targetCRS.getName() + "_Geocentric" );
            }
            CRSTransformation helmertTransformation = createTransformation( sourceGeocentric, targetGeocentric );

            CRSTransformation sourceTransformationChain = null;
            CRSTransformation targetTransformationChain = null;
            GeographicCRS sourceGeographic = null;
            GeographicCRS targetGeographic = null;
            switch ( sourceType ) {
            case CoordinateSystem.GEOCENTRIC_CRS:
                break;
            case CoordinateSystem.PROJECTED_CRS:
                sourceTransformationChain = new ProjectionTransform( (ProjectedCRS) sourceCRS.getUnderlyingCRS() );
                sourceTransformationChain.inverse();
                sourceGeographic = ( (ProjectedCRS) sourceCRS.getUnderlyingCRS() ).getGeographicCRS();
            case CoordinateSystem.GEOGRAPHIC_CRS:
                if ( sourceGeographic == null ) {
                    sourceGeographic = (GeographicCRS) sourceCRS.getUnderlyingCRS();
                }
                /*
                 * Only create a geocentric transform if the helmert transformation != null, e.g. the datums and
                 * ellipsoids are not equal.
                 */
                if ( helmertTransformation != null ) {
                    // create a 2d->3d mapping.
                    final CRSTransformation axisAligned = createAffineTransform( sourceGeographic, sourceGeocentric,
                                                                                 swapAndScaleAxis( sourceGeographic,
                                                                                                   GeographicCRS.WGS84 ) );
                    final CRSTransformation geoCentricTransform = new GeocentricTransform( sourceCRS, sourceGeocentric );
                    // concatenate the possible projection with the axis alignment and the geocentric transform.
                    sourceTransformationChain = concatenate( sourceTransformationChain, axisAligned,
                                                             geoCentricTransform );
                }
                break;
            }
            switch ( targetType ) {
            case CoordinateSystem.GEOCENTRIC_CRS:
                break;
            case CoordinateSystem.PROJECTED_CRS:
                targetTransformationChain = new ProjectionTransform( (ProjectedCRS) targetCRS.getUnderlyingCRS() );
                targetGeographic = ( (ProjectedCRS) targetCRS.getUnderlyingCRS() ).getGeographicCRS();
            case CoordinateSystem.GEOGRAPHIC_CRS:
                if ( targetGeographic == null ) {
                    targetGeographic = (GeographicCRS) targetCRS.getUnderlyingCRS();
                }
                /*
                 * Only create a geocentric transform if the helmert transformation != null, e.g. the datums and
                 * ellipsoids are not equal.
                 */
                if ( helmertTransformation != null ) {
                    // create a 2d->3d mapping.
                    final CRSTransformation axisAligned = createAffineTransform( targetGeocentric, targetGeographic,
                                                                                 swapAndScaleAxis( GeographicCRS.WGS84,
                                                                                                   targetGeographic ) );
                    final CRSTransformation geoCentricTransform = new GeocentricTransform( targetCRS, targetGeocentric );
                    geoCentricTransform.inverse();
                    // concatenate the possible projection with the axis alignment and the geocentric transform.
                    targetTransformationChain = concatenate( geoCentricTransform, axisAligned,
                                                             targetTransformationChain );
                }
                break;
            }
            result = concatenate( sourceTransformationChain, helmertTransformation, targetTransformationChain );
        }
        return result;
    }

    /**
     * Creates a matrix, with which incoming values will be transformed to a standardized form. This means, to radians
     * and meters.
     * 
     * @param sourceCRS
     *            to create the matrix for.
     * @param invert
     *            the values. Using the inverted scale, i.e. going from standard to crs specific.
     * @return the standardized matrix.
     * @throws TransformationException
     *             if the unit of one of the axis could not be transformed to one of the base units.
     */
    private Matrix toStandardizedValues( CoordinateSystem sourceCRS, boolean invert )
                            throws TransformationException {
        final int dim = sourceCRS.getDimension();
        Matrix result = null;
        Axis[] allAxis = sourceCRS.getAxis();
        for ( int i = 0; i < allAxis.length; ++i ) {
            Axis targetAxis = allAxis[i];
            if ( targetAxis != null ) {
                Unit targetUnit = targetAxis.getUnits();
                if ( !( Unit.RADIAN.equals( targetUnit ) || Unit.METRE.equals( targetUnit ) ) ) {
                    if ( !( targetUnit.canConvert( Unit.RADIAN ) || targetUnit.canConvert( Unit.METRE ) ) ) {
                        throw new TransformationException(
                                                           Messages.getMessage(
                                                                                "CRS_TRANSFORMATION_NO_APLLICABLE_UNIT",
                                                                                targetUnit ) );
                    }
                    // lazy instantiation
                    if ( result == null ) {
                        result = new Matrix( dim + 1 );
                        result.setIdentity();
                    }
                    result.setElement( i, i, invert ? 1d / targetUnit.getScale() : targetUnit.getScale() );
                }
            }
        }
        return result;
    }

    /**
     * Creates a transformation between two geographic coordinate systems. This method is automatically invoked by
     * {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}. The default implementation can adjust
     * axis order and orientation (e.g. transforming from <code>(NORTH,WEST)</code> to <code>(EAST,NORTH)</code>),
     * performs units conversion and apply Bursa Wolf transformation if needed.
     * 
     * @param sourceCRS
     *            Input coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final GeographicCRS sourceCRS, final GeographicCRS targetCRS )
                            throws TransformationException {
        final GeodeticDatum sourceDatum = sourceCRS.getGeodeticDatum();
        final GeodeticDatum targetDatum = targetCRS.getGeodeticDatum();
        if ( sourceDatum.equals( targetDatum ) ) {
            LOG.logDebug( "The datums of geographic (source): " + sourceCRS.getIdentifier()
                          + " equals geographic (target): " + targetCRS.getIdentifier() + " returning null" );
            return null;
        }
        LOG.logDebug( "Creating geographic ->geographic transformation: from (source): " + sourceCRS.getIdentifier()
                      + " to(target): " + targetCRS.getIdentifier() );
        final Ellipsoid sourceEllipsoid = sourceDatum.getEllipsoid();
        final Ellipsoid targetEllipsoid = targetDatum.getEllipsoid();
        if ( sourceEllipsoid != null && !sourceEllipsoid.equals( targetEllipsoid )
             && ( sourceDatum.getWGS84Conversion().hasValues() // check if a conversion needs to
             // take place
             || targetDatum.getWGS84Conversion().hasValues() ) ) {
            /*
             * If the two geographic coordinate systems use different ellipsoid, convert from the source to target
             * ellipsoid through the geocentric coordinate system.
             */
            final GeocentricCRS sourceGCS = new GeocentricCRS( sourceDatum, sourceCRS.getIdentifier(),
                                                               sourceCRS.getName() + "_Geocentric" );
            final GeocentricCRS targetGCS = new GeocentricCRS( targetDatum, targetCRS.getIdentifier(),
                                                               targetCRS.getName() + "_Geocentric" );
            CRSTransformation step1 = createTransformation( sourceCRS, sourceGCS );
            LOG.logDebug( "Step 1 from geographic to geographic:\n" + step1 );
            CRSTransformation step2 = createTransformation( sourceGCS, targetGCS );
            LOG.logDebug( "Step 2 from geographic to geographic:\n" + step2 );
            CRSTransformation step3 = createTransformation( targetCRS, targetGCS );
            LOG.logDebug( "Step 3 from geographic to geographic:\n" + step3 );
            if ( step3 != null ) {
                step3.inverse();// call inverseTransform from step 3.
            }
            return concatenate( step1, step2, step3 );
        }

        /*
         * Swap axis order, and rotate the longitude coordinate if prime meridians are different.
         */
        final Matrix matrix = swapAndScaleGeoAxis( sourceCRS, targetCRS );
        return createAffineTransform( sourceCRS, targetCRS, matrix );
    }

    /**
     * Creates a transformation between a geographic and a projected coordinate systems. This method is automatically
     * invoked by {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}.
     * 
     * @param sourceCRS
     *            Input coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final GeographicCRS sourceCRS, final ProjectedCRS targetCRS )
                            throws TransformationException {

        LOG.logDebug( "Creating geographic->projected transformation: from (source): " + sourceCRS.getIdentifier()
                      + " to(target): " + targetCRS.getIdentifier() );
        final GeographicCRS stepGeoCS = targetCRS.getGeographicCRS();

        final CRSTransformation geo2geo = createTransformation( sourceCRS, stepGeoCS );
        final CRSTransformation projection = new ProjectionTransform( targetCRS );
        return concatenate( geo2geo, projection );
    }

    /**
     * Creates a transformation between a geographic and a geocentric coordinate systems. Since the source coordinate
     * systems doesn't have a vertical axis, height above the ellipsoid is assumed equals to zero everywhere. This
     * method is automatically invoked by {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}.
     * 
     * @param sourceCRS
     *            Input geographic coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final GeographicCRS sourceCRS, final GeocentricCRS targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating geographic -> geocentric (helmert) transformation: from (source): "
                      + sourceCRS.getIdentifier() + " to(target): " + targetCRS.getIdentifier() );
        if ( !PrimeMeridian.GREENWICH.equals( targetCRS.getGeodeticDatum().getPrimeMeridian() ) ) {
            throw new TransformationException( "The rotation from  "
                                               + targetCRS.getGeodeticDatum().getPrimeMeridian().getIdentifier()
                                               + " to Greenwich prime meridian is not yet implemented" );
        }
        GeocentricCRS sourceGeocentric = new GeocentricCRS( sourceCRS.getGeodeticDatum(), "tmp_"
                                                                                          + sourceCRS.getIdentifier()
                                                                                          + "_geocentric",
                                                            sourceCRS.getName() + "_geocentric" );
        CRSTransformation helmertTransformation = createTransformation( sourceGeocentric, targetCRS );
        // if no helmert transformation is needed, the targetCRS equals the source-geocentric.
        if ( helmertTransformation == null ) {
            sourceGeocentric = targetCRS;
        }
        final CRSTransformation axisAlign = createAffineTransform( sourceCRS, sourceGeocentric,
                                                               swapAndScaleGeoAxis( sourceCRS, GeographicCRS.WGS84 ) );
        final CRSTransformation geocentric = new GeocentricTransform( sourceCRS, sourceGeocentric );
        return concatenate( axisAlign, geocentric, helmertTransformation );
    }

    /**
     * Creates a transformation between two projected coordinate systems. This method is automatically invoked by
     * {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}. The default implementation can adjust
     * axis order and orientation. It also performs units conversion if it is the only extra change needed. Otherwise,
     * it performs three steps:
     * 
     * <ol>
     * <li>Unproject <code>sourceCRS</code>.</li>
     * <li>Transform from <code>sourceCRS.geographicCS</code> to <code>targetCRS.geographicCS</code>.</li>
     * <li>Project <code>targetCRS</code>.</li>
     * </ol>
     * 
     * @param sourceCRS
     *            Input coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final ProjectedCRS sourceCRS, final ProjectedCRS targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating projected -> projected transformation: from (source): " + sourceCRS.getIdentifier()
                      + " to(target): " + targetCRS.getIdentifier() );
        if ( sourceCRS.getProjection().equals( targetCRS.getProjection() ) ) {
            return null;
        }
        final GeographicCRS sourceGeo = sourceCRS.getGeographicCRS();
        final GeographicCRS targetGeo = targetCRS.getGeographicCRS();
        final CRSTransformation inverseProjection = createTransformation( sourceCRS, sourceGeo );
        final CRSTransformation geo2geo = createTransformation( sourceGeo, targetGeo );
        final CRSTransformation projection = createTransformation( targetGeo, targetCRS );
        return concatenate( inverseProjection, geo2geo, projection );
    }

    /**
     * Creates a transformation between a projected and a geocentric coordinate systems. This method is automatically
     * invoked by {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}. This method doesn't need to be
     * public since its decomposition in two step should be general enough.
     * 
     * @param sourceCRS
     *            Input projected coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final ProjectedCRS sourceCRS, final GeocentricCRS targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating projected -> geocentric transformation: from (source): " + sourceCRS.getIdentifier()
                      + " to(target): " + targetCRS.getIdentifier() );
        final GeographicCRS sourceGCS = sourceCRS.getGeographicCRS();

        final CRSTransformation inverseProjection = createTransformation( sourceCRS, sourceGCS );
        final CRSTransformation geocentric = createTransformation( sourceGCS, targetCRS );
        return concatenate( inverseProjection, geocentric );
    }

    /**
     * Creates a transformation between a projected and a geographic coordinate systems. This method is automatically
     * invoked by {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}. The default implementation
     * returns <code>{@link #createTransformation(GeographicCRS, ProjectedCRS)}
     * createTransformation}(targetCRS, sourceCRS) inverse)</code>.
     * 
     * @param sourceCRS
     *            Input coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code> or
     *         <code>null</code> if {@link ProjectedCRS#getGeographicCRS()}.equals targetCRS.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final ProjectedCRS sourceCRS, final GeographicCRS targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating projected->geographic transformation: from (source): " + sourceCRS.getIdentifier()
                      + " to(target): " + targetCRS.getIdentifier() );
        CRSTransformation result = createTransformation( targetCRS, sourceCRS );
        if( result != null ){
            result.inverse();
        }
        return result;

    }

    /**
     * Creates a transformation between two geocentric coordinate systems. This method is automatically invoked by
     * {@link #createFromCoordinateSystems createFromCoordinateSystems(...)}. The default implementation can adjust for
     * axis order and orientation, adjust for prime meridian, performs units conversion and apply Bursa Wolf
     * transformation if needed.
     * 
     * @param sourceCRS
     *            Input coordinate system.
     * @param targetCRS
     *            Output coordinate system.
     * @return A coordinate transformation from <code>sourceCRS</code> to <code>targetCRS</code>.
     * @throws TransformationException
     *             if no transformation path has been found.
     */
    private CRSTransformation createTransformation( final GeocentricCRS sourceCRS, final GeocentricCRS targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating geocentric->geocetric transformation: from (source): " + sourceCRS.getIdentifier()
                      + " to(target): " + targetCRS.getIdentifier() );
        final GeodeticDatum sourceDatum = sourceCRS.getGeodeticDatum();
        final GeodeticDatum targetDatum = targetCRS.getGeodeticDatum();
        if ( !PrimeMeridian.GREENWICH.equals( sourceDatum.getPrimeMeridian() )
             || !PrimeMeridian.GREENWICH.equals( targetDatum.getPrimeMeridian() ) ) {
            throw new TransformationException( "Rotation of prime meridian not yet implemented" );
        }
        CRSTransformation result = null;
        if ( !sourceDatum.equals( targetDatum ) ) {
            final Ellipsoid sourceEllipsoid = sourceDatum.getEllipsoid();
            final Ellipsoid targetEllipsoid = targetDatum.getEllipsoid();
            /*
             * If the two coordinate systems use different ellipsoid, convert from the source to target ellipsoid
             * through the geocentric coordinate system.
             */
            if ( sourceEllipsoid != null && !sourceEllipsoid.equals( targetEllipsoid )
                 && ( sourceDatum.getWGS84Conversion().hasValues() // check for helmert transform.
                 || targetDatum.getWGS84Conversion().hasValues() ) ) {
                LOG.logDebug( "Creating helmert transformation: source(" + sourceCRS.getIdentifier() + ")->target("
                              + targetCRS.getIdentifier() + ")." );

                // Transform between different ellipsoids using Bursa Wolf parameters.
                Matrix tmp = swapAndScaleAxis( sourceCRS, GeocentricCRS.WGS84 );
                Matrix4d forwardAxisAlign = null;
                if ( tmp != null ) {
                    forwardAxisAlign = new Matrix4d();
                    tmp.get( forwardAxisAlign );
                }
                final Matrix4d forwardToWGS = getWGS84Parameters( sourceDatum );
                final Matrix4d inverseToWGS = getWGS84Parameters( targetDatum );
                tmp = swapAndScaleAxis( GeocentricCRS.WGS84, targetCRS );
                Matrix4d resultMatrix = null;
                if ( tmp != null ) {
                    resultMatrix = new Matrix4d();
                    tmp.get( resultMatrix );
                }
                if ( forwardAxisAlign == null && forwardToWGS == null && inverseToWGS == null && resultMatrix == null ) {
                    LOG.logDebug( "The given geocentric crs's do not need a helmert transformation (but they are not equal), returning identity" );
                    resultMatrix = new Matrix4d();
                    resultMatrix.setIdentity();
                } else {
                    LOG.logDebug( "step1 matrix: \n " + forwardAxisAlign );
                    LOG.logDebug( "step2 matrix: \n " + forwardToWGS );
                    LOG.logDebug( "step3 matrix: \n " + inverseToWGS );
                    LOG.logDebug( "step4 matrix: \n " + resultMatrix );
                    if ( inverseToWGS != null ) {
                        inverseToWGS.invert(); // Invert in place.
                        LOG.logDebug( "inverseToWGS inverted matrix: \n " + inverseToWGS );
                    }
                    if ( resultMatrix != null ) {
                        if ( inverseToWGS != null ) {
                            resultMatrix.mul( inverseToWGS ); // step4 = step4*step3
                            LOG.logDebug( "resultMatrix (after mul with inverseToWGS): \n " + resultMatrix );
                        }
                        if ( forwardToWGS != null ) {
                            resultMatrix.mul( forwardToWGS ); // step4 = step4*step3*step2
                            LOG.logDebug( "resultMatrix (after mul with forwardToWGS2): \n " + resultMatrix );
                        }
                        if ( forwardAxisAlign != null ) {
                            resultMatrix.mul( forwardAxisAlign ); // step4 = step4*step3*step2*step1
                        }
                    } else if ( inverseToWGS != null ) {
                        resultMatrix = inverseToWGS;
                        if ( forwardToWGS != null ) {
                            resultMatrix.mul( forwardToWGS ); // step4 = step3*step2*step1
                            LOG.logDebug( "resultMatrix (after mul with forwardToWGS2): \n " + resultMatrix );
                        }
                        if ( forwardAxisAlign != null ) {
                            resultMatrix.mul( forwardAxisAlign ); // step4 = step3*step2*step1
                        }
                    } else if ( forwardToWGS != null ) {
                        resultMatrix = forwardToWGS;
                        if ( forwardAxisAlign != null ) {
                            resultMatrix.mul( forwardAxisAlign ); // step4 = step2*step1
                        }
                    } else {
                        resultMatrix = forwardAxisAlign;
                    }
                }

                LOG.logDebug( "The resulting helmert transformation matrix: from( " + sourceCRS.getIdentifier()
                              + ") to(" + targetCRS.getIdentifier() + ")\n " + resultMatrix );
                result = new MatrixTransform( sourceCRS, targetCRS, resultMatrix, "Helmert-Transformation" );

            }
        }
        if ( result == null ) {
            /*
             * Swap axis order, and rotate the longitude coordinate if prime meridians are different.
             */
            final Matrix matrix = swapAndScaleAxis( sourceCRS, targetCRS );
            result = createAffineTransform( sourceCRS, targetCRS, matrix );
        }
        return result;
    }

    /**
     * Concatenates two existing transforms.
     * 
     * @param first
     *            The first transform to apply to points.
     * @param second
     *            The second transform to apply to points.
     * @return The concatenated transform.
     * 
     */
    private CRSTransformation createConcatenatedTransform( CRSTransformation first, CRSTransformation second ) {
        if ( first == null ) {
            return second;
        }
        if ( second == null ) {
            return first;
        }
        // if one of the two is an identity transformation, just return the other.
        if ( first.isIdentity() ) {
            return second;
        }
        if ( second.isIdentity() ) {
            return first;
        }

        /*
         * If one transform is the inverse of the other, just return an identitiy transform.
         */
        if ( first.areInverse( second ) ) {
            LOG.logDebug( "Transformation1 and Transformation2 are inverse operations, returning null" );
            return null;
        }

        /*
         * If both transforms use matrix, then we can create a single transform using the concatened matrix.
         */
        if ( first instanceof MatrixTransform && second instanceof MatrixTransform ) {
            GMatrix m1 = ( (MatrixTransform) first ).getMatrix();
            GMatrix m2 = ( (MatrixTransform) second ).getMatrix();
            if ( m1 == null ) {
                if ( m2 == null ) {
                    // both matrices are null, just return the identiy matrix.
                    return new MatrixTransform(
                                                first.getSourceCRS(),
                                                first.getTargetCRS(),
                                                new GMatrix( second.getTargetDimension() + 1, first.getSourceDimension() + 1 ) );
                }
                return second;
            } else if ( m2 == null ) {
                return first;
            }
            m2.mul( m1 );
            return new MatrixTransform( first.getSourceCRS(), second.getTargetCRS(), m2 );
        }

        /*
         * If one or both math transform are instance of {@link ConcatenatedTransform}, then concatenate <code>tr1</code>
         * or <code>tr2</code> with one of step transforms.
         */
        if ( first instanceof ConcatenatedTransform ) {
            final ConcatenatedTransform ctr = (ConcatenatedTransform) first;
            first = ctr.getFirstTransform();
            second = createConcatenatedTransform( ctr.getSecondTransform(), second );
        }
        if ( second instanceof ConcatenatedTransform ) {
            final ConcatenatedTransform ctr = (ConcatenatedTransform) second;
            first = createConcatenatedTransform( first, ctr.getFirstTransform() );
            second = ctr.getSecondTransform();
        }

        return new ConcatenatedTransform( first, second );
    }

    /**
     * Creates an affine transform from a matrix.
     * 
     * @param matrix
     *            The matrix used to define the affine transform.
     * @return The affine transform.
     * @throws TransformationException
     *             if the matrix is not affine.
     * 
     */
    private MatrixTransform createAffineTransform( CoordinateSystem sourceCRS, CoordinateSystem targetCRS,
                                                   final Matrix matrix )
                            throws TransformationException {
        if ( matrix == null ) {
            return null;
        }
        if ( matrix.isAffine() ) {// Affine transform are square.
            if ( matrix.getNumRow() == 3 && matrix.getNumCol() == 3 && !matrix.isIdentity() ) {
                LOG.logDebug( "Creating affineTransform of incoming matrix:\n" + matrix );
                Matrix3d tmp = matrix.toAffineTransform();
                LOG.logDebug( "Resulting AffineTransform is:\n" + tmp );
                return new MatrixTransform( sourceCRS, targetCRS, tmp );
            }
            return new MatrixTransform( sourceCRS, targetCRS, matrix );
        }
        throw new TransformationException( "Given matrix is not affine, cannot continue" );
    }

    /**
     * @return the WGS84 parameters as an affine transform, or <code>null</code> if not available.
     */
    private Matrix4d getWGS84Parameters( final GeodeticDatum datum ) {
        final WGS84ConversionInfo info = datum.getWGS84Conversion();
        if ( info != null && info.hasValues() ) {
            return info.getAsAffineTransform();
        }
        return null;
    }

    /**
     * Concatenate two transformation steps.
     * 
     * @param step1
     *            The first step, or <code>null</code> for the identity transform.
     * @param step2
     *            The second step, or <code>null</code> for the identity transform.
     * @return A concatenated transform, or <code>null</code> if all arguments was nul.
     */
    private CRSTransformation concatenate( final CRSTransformation step1, final CRSTransformation step2 ) {
        if ( step1 == null )
            return step2;
        if ( step2 == null )
            return step1;
        return createConcatenatedTransform( step1, step2 );
    }

    /**
     * Concatenate three transformation steps.
     * 
     * @param step1
     *            The first step, or <code>null</code> for the identity transform.
     * @param step2
     *            The second step, or <code>null</code> for the identity transform.
     * @param step3
     *            The third step, or <code>null</code> for the identity transform.
     * @return A concatenated transform, or <code>null</code> if all arguments were <code>null</code>.
     */
    private CRSTransformation concatenate( final CRSTransformation step1, final CRSTransformation step2,
                                           final CRSTransformation step3 ) {
        if ( step1 == null ) {
            return concatenate( step2, step3 );
        }
        if ( step2 == null ) {
            return concatenate( step1, step3 );
        }
        if ( step3 == null ) {
            return concatenate( step1, step2 );
        }
        return createConcatenatedTransform( step1, createConcatenatedTransform( step2, step3 ) );
    }

    /**
     * @return an affine transform between two coordinate systems. Only units and axis order (e.g. transforming from
     *        (NORTH,WEST) to (EAST,NORTH)) are taken in account. Other attributes (especially the datum) must be
     *        checked before invoking this method.
     * 
     * @param sourceCRS
     *            The source coordinate system.
     * @param targetCRS
     *            The target coordinate system.
     */
    private Matrix swapAndScaleAxis( final CoordinateSystem sourceCRS, final CoordinateSystem targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating swap matrix from: " + sourceCRS.getIdentifier() + " to: " + targetCRS.getIdentifier() );
        final Matrix matrix;
        try {
            matrix = new Matrix( sourceCRS.getAxis(), targetCRS.getAxis() );
        } catch ( RuntimeException e ) {
            throw new TransformationException( sourceCRS, targetCRS, e.getMessage() );
        }
        return matrix.isIdentity() ? null : matrix;
    }

    /**
     * @return an affine transform between two geographic coordinate systems. Only units, axis order (e.g. transforming
     *         from (NORTH,WEST) to (EAST,NORTH)) and prime meridian are taken in account. Other attributes (especially
     *         the datum) must be checked before invoking this method.
     * 
     * @param sourceCRS
     *            The source coordinate system.
     * @param targetCRS
     *            The target coordinate system.
     */
    private Matrix swapAndScaleGeoAxis( final GeographicCRS sourceCRS, final GeographicCRS targetCRS )
                            throws TransformationException {
        LOG.logDebug( "Creating geo swap matrix from: " + sourceCRS.getIdentifier() + " to: "
                      + targetCRS.getIdentifier() );
        final Matrix matrix = swapAndScaleAxis( sourceCRS, targetCRS );
        if ( matrix != null ) {
            Axis[] targetAxis = targetCRS.getAxis();
            final int lastMatrixColumn = matrix.getNumCol() - 1;
            for ( int i = 1; --i >= 0; ) {
                // Find longitude, and apply a translation if prime meridians are different.
                final int orientation = targetAxis[i].getOrientation();
                if ( Axis.AO_EAST == Math.abs( orientation ) ) {
                    final Unit unit = targetAxis[i].getUnits();
                    final double sourceLongitude = sourceCRS.getGeodeticDatum().getPrimeMeridian().getLongitude( unit );
                    final double targetLongitude = targetCRS.getGeodeticDatum().getPrimeMeridian().getLongitude( unit );
                    if ( Math.abs( sourceLongitude - targetLongitude ) > EPS11 ) {
                        double translation = targetLongitude - sourceLongitude;
                        if ( Axis.AO_WEST == orientation ) {
                            translation = -translation;
                        }
                        // add the translation to the matrix translate element of this axis
                        matrix.setElement( i, lastMatrixColumn, matrix.getElement( i, lastMatrixColumn ) - translation );
                    }
                }
            }
        }
        return matrix;
    }
}
