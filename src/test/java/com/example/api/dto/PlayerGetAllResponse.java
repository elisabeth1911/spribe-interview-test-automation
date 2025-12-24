package com.example.api.dto;

import java.util.List;
import java.util.Objects;

public final class PlayerGetAllResponse {
    private List<PlayerItem> players;

    public List<PlayerItem> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerItem> players) {
        this.players = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGetAllResponse that = (PlayerGetAllResponse) o;
        return Objects.equals(players, that.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }
}
