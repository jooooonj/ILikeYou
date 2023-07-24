package com.ll.gramgram.event;

import com.ll.gramgram.domain.likeablePerson.entity.LikeablePerson;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EventCanceledLike extends ApplicationEvent {
    private LikeablePerson likeablePerson;
    public EventCanceledLike(Object source, LikeablePerson likeablePerson) {
        super(source);
        this.likeablePerson = likeablePerson;
    }
}
