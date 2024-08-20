package com.metaro.metaro.security.user.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CollectionIdMutability;

@Entity
@Table(name = "TBL_USER")
public class User {

    @Id
    @Column(name = "USER_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "USER_NAME")
    private String userName;


}
