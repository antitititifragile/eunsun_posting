package com.eunsunzzang.posting.member.security;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refreshToken")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @JsonIgnore
    private String id;

    private String refresh_token;

    @TimeToLive(unit = TimeUnit.SECONDS)
    private Integer expiration;

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
}
