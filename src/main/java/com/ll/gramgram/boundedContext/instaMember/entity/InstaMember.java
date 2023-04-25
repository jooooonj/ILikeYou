package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
public class InstaMember extends BaseEntity {

    @Column(unique = true)
    private String username;
    @Setter
    private String gender;

    @OneToMany(mappedBy = "fromInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> fromLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> toLikeablePeople = new ArrayList<>();

    private long likesCountByWomanAndAttractiveTypeCode1;
    private long likesCountByWomanAndAttractiveTypeCode2;
    private long likesCountByWomanAndAttractiveTypeCode3;
    private long likesCountByManAndAttractiveTypeCode1;
    private long likesCountByManAndAttractiveTypeCode2;
    private long likesCountByManAndAttractiveTypeCode3;


    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.add(0, likeablePerson);
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.add(0, likeablePerson);
    }

    public void delFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.remove(likeablePerson);
    }

    public void delToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.remove(likeablePerson);
    }

    public String getGenderDisplayName() {
        return switch (gender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }

    public void increaseLikesCount(String gender, int attractiveTypeCode){
        if(gender.equals("W") && attractiveTypeCode == 1) likesCountByWomanAndAttractiveTypeCode1++;
        if(gender.equals("W") && attractiveTypeCode == 2) likesCountByWomanAndAttractiveTypeCode2++;
        if(gender.equals("W") && attractiveTypeCode == 3) likesCountByWomanAndAttractiveTypeCode3++;
        if(gender.equals("M") && attractiveTypeCode == 1) likesCountByWomanAndAttractiveTypeCode1++;
        if(gender.equals("M") && attractiveTypeCode == 2) likesCountByWomanAndAttractiveTypeCode2++;
        if(gender.equals("M") && attractiveTypeCode == 3) likesCountByWomanAndAttractiveTypeCode3++;
    }

    public void decreaseLikesCount(String gender, int attractiveTypeCode){
        if(gender.equals("W") && attractiveTypeCode == 1) likesCountByWomanAndAttractiveTypeCode1--;
        if(gender.equals("W") && attractiveTypeCode == 2) likesCountByWomanAndAttractiveTypeCode2--;
        if(gender.equals("W") && attractiveTypeCode == 3) likesCountByWomanAndAttractiveTypeCode3--;
        if(gender.equals("M") && attractiveTypeCode == 1) likesCountByWomanAndAttractiveTypeCode1--;
        if(gender.equals("M") && attractiveTypeCode == 2) likesCountByWomanAndAttractiveTypeCode2--;
        if(gender.equals("M") && attractiveTypeCode == 3) likesCountByWomanAndAttractiveTypeCode3--;
    }

    public long getLikesCountByMan(){
        return likesCountByManAndAttractiveTypeCode1 + likesCountByManAndAttractiveTypeCode2 + likesCountByManAndAttractiveTypeCode3;
    }

    public long getLikesCountByWoman(){
        return likesCountByWomanAndAttractiveTypeCode1 + likesCountByWomanAndAttractiveTypeCode2 + likesCountByWomanAndAttractiveTypeCode3;
    }

    public long getLikesCountByAll(){
        return getLikesCountByMan() + getLikesCountByWoman();
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



}
