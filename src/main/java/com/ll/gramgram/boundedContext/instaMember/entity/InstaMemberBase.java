package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public abstract class InstaMemberBase extends BaseEntity {
    String gender;

    long likesCountByWomanAndAttractiveTypeCode1;
    long likesCountByWomanAndAttractiveTypeCode2;
    long likesCountByWomanAndAttractiveTypeCode3;
    long likesCountByManAndAttractiveTypeCode1;
    long likesCountByManAndAttractiveTypeCode2;
    long likesCountByManAndAttractiveTypeCode3;

    public Long getLikesCountByWoman() {
        return likesCountByWomanAndAttractiveTypeCode1 + likesCountByWomanAndAttractiveTypeCode2 + likesCountByWomanAndAttractiveTypeCode3;
    }

    public Long getLikesCountByMan() {
        return likesCountByManAndAttractiveTypeCode1 + likesCountByManAndAttractiveTypeCode2 + likesCountByManAndAttractiveTypeCode3;
    }

    public Long getLikesCountByAttractionTypeCode1() {
        return likesCountByWomanAndAttractiveTypeCode1 + likesCountByManAndAttractiveTypeCode1;
    }

    public Long getLikesCountByAttractionTypeCode2() {
        return likesCountByWomanAndAttractiveTypeCode2 + likesCountByManAndAttractiveTypeCode2;
    }

    public Long getLikesCountByAttractionTypeCode3() {
        return likesCountByWomanAndAttractiveTypeCode3 + likesCountByManAndAttractiveTypeCode3;
    }

    public Long getLikesCountAll() {
        return getLikesCountByWoman() + getLikesCountByMan();
    }
}
