package me.javac.blog.vo;

import lombok.Data;

@Data
public class LoginVo {

    private String username;
    private String password;
    private boolean isRemember;
}
