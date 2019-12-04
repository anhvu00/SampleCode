package com.goldsentinel;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Composite foreign key for {@link ProjectPermission}.
 *
 * @author neon
 * @since 0.11
 */
@Embeddable
@NonNullByDefault
public class ProjectUserId implements Serializable {

    /** NOTE: This MUST be updated if the serialized structure of this class changes. */
    private static final long serialVersionUID = -2575643521305626622L;

    /** Project Id */
    private UUID projectId;

    /** Username */
    private String username;

    /** Default Constructor required for JPA. */
    @SuppressWarnings({ "null", "unused" })
    private ProjectUserId() {
    }

    /** Constructs object from required fields.
     *
     * @param projectId The project id. Cannot be null.
     * @param username The user's username. Cannot be null.
     */
    public ProjectUserId(UUID projectId, String username) {
        this.projectId = projectId;
        this.username = username;
    }

    /**
     * @return the projectId
     */
    public UUID getProjectId() {
        return projectId;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, username);
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
        ProjectUserId other = (ProjectUserId) obj;
        return Objects.equals(projectId, other.projectId)
                && Objects.equals(username, other.username);
    }

    @Override
    public String toString() {
        return "ProjectUserId [projectId=" + projectId + ", username=" + username + "]";
    }

}
