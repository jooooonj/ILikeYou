package com.ll.gramgram.domain.notification.eventListener;

import com.ll.gramgram.domain.notification.service.NotificationService;
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
public class NotificationEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    public void listen(EventAddLike event) {
        notificationService.afterAddLike(event.getLikeablePerson());
    }
    @TransactionalEventListener
    public void listen(EventModifiedAttractiveType event) {
        notificationService.afterModifyLike(event.getLikeablePerson(), event.getOldAttractiveTypeCode(), event.getNewAttractiveTypeCode());
    }
}
