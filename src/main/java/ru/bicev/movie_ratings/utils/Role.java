package ru.bicev.movie_ratings.utils;

public enum Role {
    ADMIN("Admin"), USER("User");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Ivalid role: " + role);
    }

    public String getDisplayName() {
        return displayName;
    }
}
