package com.allo.server.domain.content.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QContent is a Querydsl query type for Content
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QContent extends EntityPathBase<Content> {

    private static final long serialVersionUID = 2042033461L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QContent content1 = new QContent("content1");

    public final com.allo.server.global.entity.QBaseEntity _super = new com.allo.server.global.entity.QBaseEntity(this);

    public final StringPath content = createString("content");

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    //inherited
    public final DateTimePath<java.sql.Timestamp> createdAt = _super.createdAt;

    public final StringPath keywords = createString("keywords");

    public final com.allo.server.domain.lecture.entity.QLecture lecture;

    public final StringPath summary = createString("summary");

    public final StringPath translatedContent = createString("translatedContent");

    public final StringPath translatedSummary = createString("translatedSummary");

    //inherited
    public final DateTimePath<java.sql.Timestamp> updatedAt = _super.updatedAt;

    public QContent(String variable) {
        this(Content.class, forVariable(variable), INITS);
    }

    public QContent(Path<? extends Content> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QContent(PathMetadata metadata, PathInits inits) {
        this(Content.class, metadata, inits);
    }

    public QContent(Class<? extends Content> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.lecture = inits.isInitialized("lecture") ? new com.allo.server.domain.lecture.entity.QLecture(forProperty("lecture"), inits.get("lecture")) : null;
    }

}

