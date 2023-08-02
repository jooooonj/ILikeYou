package com.ll.gramgram.domain.likeablePerson.repository;

import com.ll.gramgram.domain.instaMember.entity.QInstaMember;
import com.ll.gramgram.domain.likeablePerson.entity.dto.LikeablePersonResponse;
import com.ll.gramgram.domain.likeablePerson.entity.dto.QLikeablePersonResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import java.util.List;
import static com.ll.gramgram.domain.likeablePerson.entity.QLikeablePerson.*;

public class LikeablePersonRepositoryImpl implements LikeablePersonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeablePersonRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<LikeablePersonResponse> findByIdFilteredAndSorted(Long instaMemberId, Integer sortCode, String gender, Integer attractiveTypeCode) {

        List<LikeablePersonResponse> result = queryFactory
                .select(new QLikeablePersonResponse(
                        likeablePerson.createDate,
                        likeablePerson.id,
                        likeablePerson.fromInstaMember.id,
                        likeablePerson.toInstaMember.id,
                        likeablePerson.fromInstaMember.gender,
                        likeablePerson.attractiveTypeCode
                        )
                )
                .from(likeablePerson)
                .where(
                        likeablePerson.toInstaMember.id.eq(instaMemberId),
                        eqGender(gender),
                        eqAttractiveTypeCode(attractiveTypeCode)
                )
                .orderBy(orderSelector(sortCode))
                .fetch();

        return result;
    }

    private static OrderSpecifier<?>
    orderSelector(Integer sortCode) {
        if (sortCode == null) {
            return likeablePerson.createDate.desc();
        }

        return switch (sortCode) {
            case 2 -> likeablePerson.fromInstaMember.toLikeablePeopleCount.asc();
            case 3 -> likeablePerson.fromInstaMember.toLikeablePeopleCount.desc();
            case 4 -> likeablePerson.fromInstaMember.gender.asc();
            case 5 -> likeablePerson.attractiveTypeCode.desc();
            default -> likeablePerson.createDate.asc();
        };
    }

    private BooleanExpression eqGender(String gender) {
        if (StringUtils.isEmpty(gender)) {
            return null;
        }
        return likeablePerson.fromInstaMember.gender.eq(gender);
    }

    private BooleanExpression eqAttractiveTypeCode(Integer attractiveTypeCode) {
        if (ObjectUtils.isEmpty(attractiveTypeCode)) {
            return null;
        }
        return likeablePerson.attractiveTypeCode.eq(attractiveTypeCode);
    }

}
