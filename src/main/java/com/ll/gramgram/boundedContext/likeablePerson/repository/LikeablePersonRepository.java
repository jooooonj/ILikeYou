package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    Optional<LikeablePerson> findByFromInstaMemberIdAndToInstaMemberId(Long fromInstaMemberId, Long toInstaMemberId);

    @Query("select L from LikeablePerson L inner join L.toInstaMember T inner join L.fromInstaMember F on T.id=:id where (:attractiveTypeCode is null or L.attractiveTypeCode=:attractiveTypeCode) and (:gender is null or F.gender=:gender)")
    List<LikeablePerson> findByIdByCondition(@Param("id") Long id , @Param("gender") String gender, @Param("attractiveTypeCode") Integer attractiveTypeCode);
}
