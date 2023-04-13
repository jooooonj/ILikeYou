package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Getter
    static int maxFromLikeablePeople;

    @Value("${custom.instaMember.fromLikeablePeople.maxSize}")
    public void setMaxFromLikeablePeople(int num){
        AppConfig.maxFromLikeablePeople = num;
    }
}
