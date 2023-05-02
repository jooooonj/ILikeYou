package com.ll.gramgram.boundedContext.likeablePerson.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Getter
@Builder
public class LikeablePerson extends BaseEntity {

    private LocalDateTime modifyUnlockDate;

    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember; // 호감을 표시한 사람(인스타 멤버)
    private String fromInstaMemberUsername; // 혹시 몰라서 기록
    @ManyToOne
    @ToString.Exclude
    private InstaMember toInstaMember; // 호감을 받은 사람(인스타 멤버)
    private String toInstaMemberUsername; // 혹시 몰라서 기록

    private int attractiveTypeCode; // 매력포인트(1=외모, 2=성격, 3=능력)

    public boolean isModifyUnlocked() {
        return modifyUnlockDate.isBefore(LocalDateTime.now());
    }

    public String getModifyUnlockDateRemainStrHuman() {
        LocalDateTime tmpDateTime = modifyUnlockDate;
        int second = tmpDateTime.getSecond();

        if (second != 0) {
            int nanos = tmpDateTime.getNano();
            int roundedNanos = (int) Math.round(nanos / 1e9) * 1_000_000_000;
            tmpDateTime.withSecond(0).withNano(0).plusMinutes(1).withSecond(0).withNano(roundedNanos);
        }

        int hour = tmpDateTime.getHour();
        int minute = tmpDateTime.getMinute();
        return String.format("%d시 %d분", hour, minute);
    }

    public void modifyAttractiveTypeCode(int attractiveTypeCode) {
        this.attractiveTypeCode = attractiveTypeCode;
    }

    public void genModifyUnlockDate(LocalDateTime dateTime){
        modifyUnlockDate = dateTime;
    }

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
}
