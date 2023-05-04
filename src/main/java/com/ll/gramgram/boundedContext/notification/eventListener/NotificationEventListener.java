package com.ll.gramgram.boundedContext.notification.eventListener;

import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import com.ll.gramgram.event.EventAddLike;
import com.ll.gramgram.event.EventModifiedAttractiveType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener
    public void listen(EventAddLike event) {
        notificationService.afterAddLike(event.getLikeablePerson());
    }
    @EventListener
    public void listen(EventModifiedAttractiveType event) {
        notificationService.afterModifyLike(event.getLikeablePerson(), event.getOldAttractiveTypeCode(), event.getNewAttractiveTypeCode());
    }


}
