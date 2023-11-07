package com.wanted.restaurant.boundedContext.sigungu.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSigungu is a Querydsl query type for Sigungu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSigungu extends EntityPathBase<Sigungu> {

    private static final long serialVersionUID = 1726771360L;

    public static final QSigungu sigungu1 = new QSigungu("sigungu1");

    public final StringPath dosi = createString("dosi");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath sigungu = createString("sigungu");

    public QSigungu(String variable) {
        super(Sigungu.class, forVariable(variable));
    }

    public QSigungu(Path<? extends Sigungu> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSigungu(PathMetadata metadata) {
        super(Sigungu.class, metadata);
    }

}

