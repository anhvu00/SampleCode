package com.goldsentinel;

import static org.eclipse.jdt.annotation.Checks.requireNonEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.security.Principal;
import java.util.Objects;

/**
 * POJO and JPA Entity for user.
 *
 * @author neon
 * @since 0.11
 */
@JsonDeserialize(builder = User.Builder.class)
@Entity
@Table(name = "USERS", schema = "PUBLIC")
@NonNullByDefault
public class User implements Principal {

    /** Username/login name for user. This must be unique across all users. */
    @Id
    private String username;

    /** One way hash/encoding (e.g. BCrypt) of password for user. */
    @Column(name = "password", nullable =  false)
    private String passwordHash;

    /**
     * True if user is enabled/active (allowed to login), otherwise
     * false for disabled (prevents logins and new references to user).
     */
    private boolean enabled;

    /** Default Constructor required for JPA. */
    @SuppressWarnings({ "unused", "null" })
    private User() {
    }

    /**
     * Creates instance from builder.
     * @param builder Builder. Cannot be null.
     * @throws NullPointerException If any required fields are null.
     */
    private User(Builder builder) {
        this.username = requireNonEmpty(builder.username, "username cannot be null or empty");
        this.passwordHash = requireNonEmpty(builder.passwordHash, "passwordHash cannot be null or empty");
        this.enabled = builder.enabled;
    }

    /** @return the name of this principal using the username. Never null. */
    @JsonIgnore
    @Override
    public String getName() {
        return username;
    }

    /**
     * @return the unique username
     */
    public String getUsername() {
        return username;
    }

    /** @return the one way hash of the password. */
    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Set the one way hash/encoding of the password.
     * @param passwordHash The one way encoding/hash of password. Cannot be null.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @return true if enabled, false otherwise
     */
    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(username, other.username);
    }

    /**
     * Creates a builder to build {@link User} and initialize it with the given object.
     *
     * @param user to initialize the builder with. Cannot be null.
     * @return created builder. Never null.
     */
    public static Builder builderFrom(User user) {
        return new Builder(user);
    }

    /**
     * Builder to build {@link User}.
     */
    public static final class Builder {
        private String username;
        private String passwordHash;
        private boolean enabled;

        /**
         * Creates builder from specified required parameters.
         * @param username Unique username.
         * @param passwordHash Hashed/encoded form of the password.
         */
        public Builder(String username, String passwordHash) {
            this.username = username;
            this.passwordHash = passwordHash;
        }

        private Builder(User user) {
            this.username = user.username;
            this.passwordHash = user.passwordHash;
            this.enabled = user.enabled;
        }

        /**
        * Builder method for passwordHash parameter.
        * @param passwordHash field to set
        * @return builder
        */
        public Builder withPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        /**
        * Builder method for enabled parameter.
        * @param enabled field to set
        * @return builder
        */
        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        /**
        * Builder method of the builder.
        * @return built class
        */
        public User build() {
            return new User(this);
        }
    }

}
