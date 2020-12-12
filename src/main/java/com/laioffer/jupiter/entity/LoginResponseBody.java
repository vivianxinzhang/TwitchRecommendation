package com.laioffer.jupiter.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

// Java obj -> Json
public class LoginResponseBody {
    @JsonProperty("user_id")
    private final String userId;

    @JsonProperty("name")
    private final String name;  // 主要需要返回 user 的名字 给前端 （前端要求）

    // session ID


    public LoginResponseBody(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
