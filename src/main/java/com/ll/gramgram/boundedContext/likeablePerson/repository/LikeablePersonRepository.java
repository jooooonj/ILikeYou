package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    Optional<LikeablePerson> findByFromInstaMemberIdAndToInstaMemberId(Long fromInstaMemberId, Long toInstaMemberId);

    //최신순
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) order by L.createDate asc")
    List<LikeablePerson> findByIdByConditionOrderByCreateDate(@Param("id") Long id , @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);

    //오래된순
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) order by L.createDate desc")
    List<LikeablePerson> findByIdByConditionOrderByCreateDateDesc(@Param("id") Long id , @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);

    //인기순
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) ORDER BY SIZE(F.toLikeablePeople) desc")
    List<LikeablePerson> findByIdByConditionOrderByHotOfFromInstaMember(@Param("id") Long id , @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);

    //인기적은순
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) ORDER BY SIZE(F.toLikeablePeople) asc")
    List<LikeablePerson> findByIdByConditionOrderByHotOfFromInstaMemberDesc(@Param("id") Long id, @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);

    //성별순
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) order by F.gender asc")
    List<LikeablePerson> findByIdByConditionOrderByGenderOfFromInstaMember(@Param("id") Long id, @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);

    //호감사유순
    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender) order by L.attractiveTypeCode asc")
    List<LikeablePerson> findByIdByConditionOrderByAttractiveTypeCode(@Param("id") Long id, @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);
}
