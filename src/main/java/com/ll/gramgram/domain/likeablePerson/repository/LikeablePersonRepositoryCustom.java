package com.ll.gramgram.domain.likeablePerson.repository;

import com.ll.gramgram.domain.likeablePerson.entity.dto.LikeablePersonResponse;

import java.util.List;

public interface LikeablePersonRepositoryCustom {
    List<LikeablePersonResponse> findByIdFilteredAndSorted(Long instaMemberId, Integer sortCode, String gender, Integer attractiveTypeCode);
}
