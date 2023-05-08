package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.entity.NotificationType;
import com.ll.gramgram.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public List<Notification> getNotificationsAfterSetTimeLapse(InstaMember toInstaMember) {
        List<Notification> notifications = findByToInstaMember(toInstaMember);
        setTimeLapse(notifications);

        return notifications;
    }

    public List<Notification> findByToInstaMember(InstaMember toInstaMember){
        return notificationRepository.findByToInstaMember(toInstaMember);
    }


    public void setReadDate(Notification notification){
        if(notification.getReadDate()==null)
            notification.setReadDate(LocalDateTime.now());
    }

    private void setTimeLapse(List<Notification> notifications){
        for(Notification notification : notifications){
            notification.setTimeLapse(Time.calculateTimeLapse(notification.getCreateDate()));
        }
    }

    @Transactional
    public void afterAddLike(LikeablePerson likeablePerson) {
        Notification notification = Notification
                .builder()
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .toInstaMember(likeablePerson.getToInstaMember())
                .type(NotificationType.LIKE)
                .newAttractiveTypeCode(likeablePerson.getAttractiveTypeCode())
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void afterModifyLike(LikeablePerson likeablePerson, int oldAttractiveTypeCode, int newAttractiveTypeCode) {
        Notification notification = Notification
                .builder()
                .fromInstaMember(likeablePerson.getFromInstaMember())
                .toInstaMember(likeablePerson.getToInstaMember())
                .type(NotificationType.MODIFY)
                .oldAttractiveTypeCode(oldAttractiveTypeCode)
                .newAttractiveTypeCode(newAttractiveTypeCode)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void clearUnreadNotification(InstaMember instaMember) {
        instaMemberService.clearUnreadNotification(instaMember);
    }

    public class Time {
        private static class TIME_MAXIMUM {
            public static final int SEC = 60;
            public static final int MIN = 60;
            public static final int HOUR = 24;
            public static final int DAY = 30;
            public static final int MONTH = 12;
        }

        private static String calculateTimeLapse(LocalDateTime localDateTime) {
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            long curTime = System.currentTimeMillis();
            long regTime = date.getTime();
            long diffTime = (curTime - regTime) / 1000;
            String msg = null;
            if (diffTime < TIME_MAXIMUM.SEC) {
                // sec
                msg = "방금전";
            } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
                // min
                msg = diffTime + "분 전";
            } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
                // hour
                msg = (diffTime) + "시간 전";
            } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
                // day
                msg = (diffTime) + "일 전";
            } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
                // day
                msg = (diffTime) + "달 전";
            } else {
                msg = (diffTime) + "년 전";
            }
            return msg;
        }
    }


}