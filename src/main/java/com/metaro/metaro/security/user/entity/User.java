package com.metaro.metaro.security.user.entity;

import com.metaro.metaro.security.common.MetaroRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CollectionIdMutability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "TBL_USER")
public class User {

    @Id
    @Column(name = "USER_NO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "USER_NAME")
    private String userName;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "USER_ROLE")
    private MetaroRole role;


    public List<String> getRoleList(){
        if(this.role.getRole().length() > 0){
            return Arrays.asList(this.role.getRole().split(","));
        }
        return new ArrayList<>();
    }

    public User() {
    }

    public User(Long userNo, String userEmail, String userPassword, String userName, MetaroRole role) {
        this.userNo = userNo;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.role = role;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MetaroRole getRole() {
        return role;
    }

    public void setRole(MetaroRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userNo=" + userNo +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", role=" + role +
                '}';
    }
}
