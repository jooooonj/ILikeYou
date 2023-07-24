package com.ll.gramgram.domain.instaMember.eventListener;

import com.ll.gramgram.domain.instaMember.service.InstaMemberService;
import com.ll.gramgram.event.EventCanceledLike;
import com.ll.gramgram.event.EventAddLike;
import com.ll.gramgram.event.EventModifiedAttractiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstaMemberEventListener {

    private final InstaMemberService instaMemberService;

    @EventListener
    public void listen(EventCanceledLike event) {
        instaMemberService.eventCanceledLike(event.getLikeablePerson());
    }

    @EventListener
    public void listen(EventAddLike event) {
        instaMemberService.eventLiked(event.getLikeablePerson());
    }

    @EventListener
    public void listen(EventModifiedAttractiveType event) {
        instaMemberService.eventModifiedAttractiveType(event.getLikeablePerson(), event.getOldAttractiveTypeCode(), event.getNewAttractiveTypeCode());
    }


}
