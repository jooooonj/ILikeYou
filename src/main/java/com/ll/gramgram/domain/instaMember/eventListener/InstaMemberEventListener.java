package com.ll.gramgram.domain.instaMember.eventListener;

import com.ll.gramgram.domain.instaMember.service.InstaMemberService;
import com.ll.gramgram.event.EventCanceledLike;
import com.ll.gramgram.event.EventAddLike;
import com.ll.gramgram.event.EventModifiedAttractiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class InstaMemberEventListener {

    private final InstaMemberService instaMemberService;

    //호감 취소 이벤트 발생
    @TransactionalEventListener
    public void listen(EventCanceledLike event) {
        instaMemberService.eventCanceledLike(event.getLikeablePerson());
    }

    //호감표시 이벤트 발생
    @TransactionalEventListener
    public void listen(EventAddLike event) {
        instaMemberService.eventLiked(event.getLikeablePerson());
    }

    //호감 포인트 변경 이벤트 발생
    @TransactionalEventListener
    public void listen(EventModifiedAttractiveType event) {
        instaMemberService.eventModifiedAttractiveType(event.getLikeablePerson(), event.getOldAttractiveTypeCode(), event.getNewAttractiveTypeCode());
    }


}
