package com.ll.gramgram.domain.likeablePerson.entity.dto;
import com.ll.gramgram.global.util.Ut;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class LikeablePersonResponse {
    private LocalDateTime createDate;
    private Long id;
    private Long fromInstaMemberId;
    private Long toInstaMemberId;
    private String fromInstaMemberGender;
    private int attractiveTypeCode; // 매력포인트(1=외모, 2=성격, 3=능력)
    public String getAttractiveTypeDisplayName() {
        return switch (attractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public String getAttractiveTypeDisplayNameWithIcon() {
        return switch (attractiveTypeCode) {
            case 1 -> "<i class=\"fa-solid fa-person-rays\"></i>";
            case 2 -> "<i class=\"fa-regular fa-face-smile\"></i>";
            default -> "<i class=\"fa-solid fa-people-roof\"></i>";
        } + "&nbsp;" + getAttractiveTypeDisplayName();
    }

    public String getJdenticon() {
        return Ut.hash.sha256(fromInstaMemberId + "_likes_" + toInstaMemberId);
    }

    public String getGenderDisplayNameWithIcon() {
        return switch (fromInstaMemberGender) {
            case "W" -> "<i class=\"fa-solid fa-person-dress\"></i>";
            default -> "<i class=\"fa-solid fa-person\"></i>";
        } + "&nbsp;" + getGenderDisplayName();
    }

    public String getGenderDisplayName() {
        return switch (fromInstaMemberGender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }
    @QueryProjection
    public LikeablePersonResponse(LocalDateTime createDate, Long id, Long fromInstaMemberId, Long toInstaMemberId, String fromInstaMemberGender, int attractiveTypeCode) {
        this.createDate = createDate;
        this.id = id;
        this.fromInstaMemberId = fromInstaMemberId;
        this.toInstaMemberId = toInstaMemberId;
        this.fromInstaMemberGender = fromInstaMemberGender;
        this.attractiveTypeCode = attractiveTypeCode;
    }

}
