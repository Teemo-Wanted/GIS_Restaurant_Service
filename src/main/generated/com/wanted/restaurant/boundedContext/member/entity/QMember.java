package com.wanted.restaurant.boundedContext.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -532858578L;

    public static final QMember member = new QMember("member1");

    public final StringPath accessToken = createString("accessToken");

    public final StringPath account = createString("account");

    public final EnumPath<AlarmType> alarmType = createEnum("alarmType", AlarmType.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath lat = createString("lat");

    public final StringPath lon = createString("lon");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> tempCode = createNumber("tempCode", Integer.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

