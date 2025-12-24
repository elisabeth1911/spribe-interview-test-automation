package com.example.api.dto;

import java.util.Objects;

public final class PlayerDeleteRequest {
    private Long playerId;

    public PlayerDeleteRequest() {
    }

    public PlayerDeleteRequest(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDeleteRequest that = (PlayerDeleteRequest) o;
        return Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
