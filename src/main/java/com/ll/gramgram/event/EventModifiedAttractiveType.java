package com.ll.gramgram.event;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventModifiedAttractiveType extends ApplicationEvent {
    private LikeablePerson likeablePerson;
    private int oldAttractiveTypeCode;
    private int newAttractiveTypeCode;
    public EventModifiedAttractiveType(Object source, LikeablePerson likeablePerson, int oldAttractiveTypeCode, int newAttractiveTypeCode) {
        super(source);
        this.likeablePerson = likeablePerson;
        this.oldAttractiveTypeCode = oldAttractiveTypeCode;
        this.newAttractiveTypeCode = newAttractiveTypeCode;
    }
}
