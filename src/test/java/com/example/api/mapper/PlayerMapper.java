package com.example.api.mapper;

import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerGetByIdResponse;
import com.example.api.dto.PlayerUpdateRequest;
import com.example.api.dto.PlayerUpdateResponse;

public final class PlayerMapper {

    public PlayerGetByIdResponse expectedGetById(long id, PlayerCreateRequest request) {
        PlayerGetByIdResponse response = new PlayerGetByIdResponse();
        response.setId(id);
        response.setLogin(request.getLogin());
        response.setScreenName(request.getScreenName());
        response.setRole(request.getRole());
        response.setAge(request.getAge());
        response.setGender(request.getGender());
        return response;
    }

    public PlayerUpdateResponse expectedUpdate(long id, PlayerUpdateRequest request) {
        PlayerUpdateResponse response = new PlayerUpdateResponse();
        response.setId(id);
        response.setLogin(request.getLogin());
        response.setScreenName(request.getScreenName());
        response.setRole(request.getRole());
        response.setAge(request.getAge());
        response.setGender(request.getGender());
        return response;
    }
}
