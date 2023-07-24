package com.ll.gramgram.domain.notification.repository;

import com.ll.gramgram.domain.instaMember.entity.InstaMember;
import com.ll.gramgram.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToInstaMember(InstaMember toInstaMember);
}