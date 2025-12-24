package com.example.api.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class PlayerGetByIdRequest {
    private Long playerId;

    public PlayerGetByIdRequest() {
    }

    public PlayerGetByIdRequest(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("playerId", playerId.toString());
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGetByIdRequest that = (PlayerGetByIdRequest) o;
        return Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
