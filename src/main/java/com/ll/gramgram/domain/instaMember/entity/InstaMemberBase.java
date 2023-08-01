package com.ll.gramgram.domain.instaMember.entity;

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
    //외모가 좋다고 호감표시한 여성의 수
    long likesCountByWomanAndAttractiveTypeCode1;
    //성격이 좋다고 호감표시한 여성의 수
    long likesCountByWomanAndAttractiveTypeCode2;
    //능력이 좋다고 호감표시한 여성의 수
    long likesCountByWomanAndAttractiveTypeCode3;
    //외모가 좋다고 호감표시한 남성의 수
    long likesCountByManAndAttractiveTypeCode1;
    //성격이 좋다고 호감표시한 남성의 수
    long likesCountByManAndAttractiveTypeCode2;
    //능력이 좋다고 호감표시한 남성의 수
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
