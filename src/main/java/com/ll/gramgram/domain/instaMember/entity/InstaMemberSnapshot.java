package com.ll.gramgram.domain.instaMember.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class InstaMemberSnapshot extends InstaMemberBase {
    private String username;
    @ToString.Exclude
    @ManyToOne
    private InstaMember instaMember;
}
