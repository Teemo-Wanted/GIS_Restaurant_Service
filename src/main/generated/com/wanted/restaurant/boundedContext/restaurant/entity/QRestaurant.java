package com.wanted.restaurant.boundedContext.restaurant.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRestaurant is a Querydsl query type for Restaurant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurant extends EntityPathBase<Restaurant> {

    private static final long serialVersionUID = 20076340L;

    public static final QRestaurant restaurant = new QRestaurant("restaurant");

    public final StringPath businessClassificationDivisionName = createString("businessClassificationDivisionName");

    public final StringPath businessPlaceName = createString("businessPlaceName");

    public final StringPath businessPlaceNameAndAddress = createString("businessPlaceNameAndAddress");

    public final NumberPath<Integer> businessStatus = createNumber("businessStatus", Integer.class);

    public final DatePath<java.time.LocalDate> closeBusinessDate = createDate("closeBusinessDate", java.time.LocalDate.class);

    public final NumberPath<Integer> femaleEmployeeCount = createNumber("femaleEmployeeCount", Integer.class);

    public final NumberPath<Double> grade = createNumber("grade", Double.class);

    public final StringPath grandDivisionName = createString("grandDivisionName");

    public final StringPath grandFacilityDivisionName = createString("grandFacilityDivisionName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> licenseBusinessDate = createDate("licenseBusinessDate", java.time.LocalDate.class);

    public final NumberPath<Double> locationPlaceArea = createNumber("locationPlaceArea", Double.class);

    public final NumberPath<Integer> maleEmployeeCount = createNumber("maleEmployeeCount", Integer.class);

    public final StringPath multiUseBusiness = createString("multiUseBusiness");

    public final StringPath refinedLocationAddress = createString("refinedLocationAddress");

    public final StringPath refinedLocationZipCode = createString("refinedLocationZipCode");

    public final StringPath refinedRoadNameAddress = createString("refinedRoadNameAddress");

    public final NumberPath<Double> refinedWGS84Latitude = createNumber("refinedWGS84Latitude", Double.class);

    public final NumberPath<Double> refinedWGS84Longitude = createNumber("refinedWGS84Longitude", Double.class);

    public final StringPath sanitationBusinessName = createString("sanitationBusinessName");

    public final StringPath sanitationIndustryTypeName = createString("sanitationIndustryTypeName");

    public final StringPath sigunCode = createString("sigunCode");

    public final StringPath sigunName = createString("sigunName");

    public final NumberPath<Integer> totalEmployeesCount = createNumber("totalEmployeesCount", Integer.class);

    public final NumberPath<Double> totalFacilityScale = createNumber("totalFacilityScale", Double.class);

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QRestaurant(String variable) {
        super(Restaurant.class, forVariable(variable));
    }

    public QRestaurant(Path<? extends Restaurant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRestaurant(PathMetadata metadata) {
        super(Restaurant.class, metadata);
    }

}

