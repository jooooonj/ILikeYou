package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class AppConfig {

    @Getter
    static int maxFromLikeablePeople;

    @Value("${custom.instaMember.fromLikeablePeople.maxSize}")
    public void setMaxFromLikeablePeople(int num) {
        AppConfig.maxFromLikeablePeople = num;
    }

    @Getter
    static int modifyCoolTime;

    @Value("${custom.likeablePerson.modifyCoolTime}")
    public void setModifyCoolTime(int modifyCoolTime) {
        AppConfig.modifyCoolTime = modifyCoolTime;
    }

    public static LocalDateTime genLikeModifyCoolTime(){
        return LocalDateTime.now().plusSeconds(modifyCoolTime);
    }
}
