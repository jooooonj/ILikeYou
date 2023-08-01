package com.ll.gramgram.domain.likeablePerson.repository;

import com.ll.gramgram.domain.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long>, LikeablePersonRepositoryCustom{
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);
    Optional<LikeablePerson> findByFromInstaMemberIdAndToInstaMemberId(Long fromInstaMemberId, Long toInstaMemberId);
}
