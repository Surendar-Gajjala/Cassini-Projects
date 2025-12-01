package com.cassinisys.erp.repo.common;

import com.cassinisys.erp.model.common.ERPObjectGeoLocation;
import com.cassinisys.erp.model.core.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by reddy on 11/1/15.
 */
@Repository
public interface ObjectGeoLocationRepository extends JpaRepository<ERPObjectGeoLocation, Integer> {
    ERPObjectGeoLocation findByIdAndObjectType(Integer id, ObjectType objectType);

    @Query(
            value = "SELECT * FROM erp_objectgeolocation WHERE earth_box(ll_to_earth(:latitude, :longitude), :radius) " +
                    "   @> ll_to_earth(erp_objectgeolocation.latitude, erp_objectgeolocation.longitude)",
            nativeQuery = true
    )
    List<Object[]> radiusSearch(@Param("latitude") Double latitude,
                                @Param("longitude") Double longitude,
                                @Param("radius") float radius);

    @Query(
            value = "SELECT * FROM erp_objectgeolocation WHERE ((latitude BETWEEN :swLatitude AND :neLatitude) AND " +
                    "(longitude BETWEEN :swLongitude AND :neLongitude))",
            nativeQuery = true
    )
    List<Object[]> boxSearch(@Param("neLatitude") Double neLatitude,@Param("neLongitude") Double neLongitude,
                             @Param("swLatitude") Double swLatitude,@Param("swLongitude") Double swLongitude);

    @Query(
            value = "SELECT * FROM erp_objectgeolocation WHERE earth_box(ll_to_earth(:latitude, :longitude), :radius) " +
                    "   @> ll_to_earth(erp_objectgeolocation.latitude, erp_objectgeolocation.longitude) " +
                    " AND object_type=CAST(:objectType AS OBJECT_TYPE)",
            nativeQuery = true
    )
    List<Object[]> radiusSearch(@Param("latitude") Double latitude,
                                @Param("longitude") Double longitude,
                                @Param("radius") float radius,
                                @Param("objectType") String objectType);

    @Query(
            value = "SELECT * FROM erp_objectgeolocation WHERE ((latitude BETWEEN :swLatitude AND :neLatitude) AND " +
                    "(longitude BETWEEN :swLongitude AND :neLongitude)) " +
                    " AND object_type=CAST(:objectType AS OBJECT_TYPE)",
            nativeQuery = true
    )
    List<Object[]> boxSearch(@Param("neLatitude") Double neLatitude,@Param("neLongitude") Double neLongitude,
                             @Param("swLatitude") Double swLatitude,@Param("swLongitude") Double swLongitude,
                             @Param("objectType") String objectType);
}
