package com.allo.server.domain.lecture.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLecture is a Querydsl query type for Lecture
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLecture extends EntityPathBase<Lecture> {

    private static final long serialVersionUID = -517789035L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLecture lecture = new QLecture("lecture");

    public final com.allo.server.global.entity.QBaseEntity _super = new com.allo.server.global.entity.QBaseEntity(this);

    public final com.allo.server.domain.content.entity.QContent content;

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdAt = _super.createdAt;

    public final StringPath fileUrl = createString("fileUrl");

    public final NumberPath<Long> lectureId = createNumber("lectureId", Long.class);

    public final EnumPath<LectureSubject> lectureSubject = createEnum("lectureSubject", LectureSubject.class);

    public final EnumPath<LectureType> lectureType = createEnum("lectureType", LectureType.class);

    public final NumberPath<Integer> semester = createNumber("semester", Integer.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.sql.Timestamp> updatedAt = _super.updatedAt;

    public final com.allo.server.domain.user.entity.QUserEntity userEntity;

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QLecture(String variable) {
        this(Lecture.class, forVariable(variable), INITS);
    }

    public QLecture(Path<? extends Lecture> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLecture(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLecture(PathMetadata metadata, PathInits inits) {
        this(Lecture.class, metadata, inits);
    }

    public QLecture(Class<? extends Lecture> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.content = inits.isInitialized("content") ? new com.allo.server.domain.content.entity.QContent(forProperty("content"), inits.get("content")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new com.allo.server.domain.user.entity.QUserEntity(forProperty("userEntity")) : null;
    }

}

