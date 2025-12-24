package com.example.api.data;

import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerUpdateRequest;

import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public final class PlayerRequestFactory {

    private PlayerRequestFactory() {
    }

    public static PlayerCreateRequest validUserCreate() {
        String login = uniqueLogin();
        return new PlayerCreateRequest()
                .setLogin(login)
                .setPassword("pass1234")
                .setScreenName(login)
                .setRole("user")
                .setAge(17 + ThreadLocalRandom.current().nextInt(43))
                .setGender(ThreadLocalRandom.current().nextBoolean() ? "male" : "female");
    }

    public static PlayerCreateRequest validUserCreate(Consumer<PlayerCreateRequest> override) {
        PlayerCreateRequest req = validUserCreate();
        if (override != null) {
            override.accept(req);
        }
        return req;
    }

    public static PlayerUpdateRequest updateFromCreate(PlayerCreateRequest create) {
        return new PlayerUpdateRequest()
                .setAge(create.getAge())
                .setGender(create.getGender())
                .setLogin(create.getLogin())
                .setPassword(create.getPassword())
                .setRole(create.getRole())
                .setScreenName(create.getScreenName());
    }

    public static PlayerUpdateRequest updateFromCreate(PlayerCreateRequest create, Consumer<PlayerUpdateRequest> override) {
        PlayerUpdateRequest req = updateFromCreate(create);
        if (override != null) {
            override.accept(req);
        }
        return req;
    }

    private static String uniqueLogin() {
        long ts = Instant.now().toEpochMilli();
        int salt = 10000 + ThreadLocalRandom.current().nextInt(90000);
        return ("user_" + ts + "_" + salt).toLowerCase(Locale.ROOT);
    }
}
