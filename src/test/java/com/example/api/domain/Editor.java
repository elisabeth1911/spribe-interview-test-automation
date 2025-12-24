package com.example.api.domain;

public enum Editor {
    ADMIN,
    SUPERVISOR,
    UNKNOWN;

    public String value() {
        switch (this) {
            case ADMIN:
                return "admin";
            case SUPERVISOR:
                return "supervisor";
            case UNKNOWN:
                return "unknownEditor";
            default:
                throw new IllegalStateException("Unsupported editor: " + this);
        }
    }
}
