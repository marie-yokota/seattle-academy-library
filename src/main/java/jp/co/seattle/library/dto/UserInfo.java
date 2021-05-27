package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * ユーザー情報格納DTO
 *
 */
@Configuration
@Data
public class UserInfo {

    public static final String getUserId = null;

    private int usersId;

    private String email;

    private String password;

    private String passwordCheck;

    public UserInfo() {

    }

    public UserInfo(int usersId, String email, String password, String passwordCheck) {
        this.usersId = usersId;
        this.email = email;
        this.password = password;
    }

}