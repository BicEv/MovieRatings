package ru.bicev.movie_ratings.utils;

/**
 * Enumeration representing the different roles a user can have in te system.
 * <p>
 * Roles determine the level of access and permissions granted to a user.
 * <p>
 */
public enum Role {
    /**
     * Admin role grants full access to all system features.
     */
    ADMIN("Admin"),
    /**
     * User role grants access to basic system functionality.
     */
    USER("User");

    private String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets a role from a String 
     * @param role the String to get a role from
     * @return a Role based on the provided string
     * @throws IllegalArgumentException if the provided string cannot be converted to a role
     */
    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Ivalid role: " + role);
    }

    /**
     * Return a String value of a role
     * @return a string values of a role
     */
    public String getDisplayName() {
        return displayName;
    }
}
