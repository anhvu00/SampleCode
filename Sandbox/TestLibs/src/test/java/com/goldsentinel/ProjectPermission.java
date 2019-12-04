package com.goldsentinel;



import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Project permission for a user.
 *
 * @author neon
 * @since 0.11
 */
@Entity
@JsonDeserialize(builder = ProjectPermission.Builder.class)
@NonNullByDefault
public class ProjectPermission  {

    /** Composite key. */

    private ProjectUserId id;

    /** The project for this permission. */
    @MapsId("projectId") // maps to the projectId attribute in ProjectUserId (composite key)
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    /** The user that this permission applies to. */
    @MapsId("username") // maps to the username attribute in ProjectUserId (composite key)
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    /** User can only read/view project and it's boards. */
    private boolean read;

    /** User can write/change project and it's boards. */
    private boolean write;

    /** User can manage project and it's boards. */
    private boolean manage;

    /** Default Constructor required for JPA. */
    @SuppressWarnings({ "unused", "null" })
    private ProjectPermission() {
    }

    /**
     * Constructs instance from builder.
     * @param builder Builder to use. Cannot be null.
     */
    private ProjectPermission(@NonNull Builder builder) {
        this.id = builder.id;
        this.project = builder.project; // cannot requireNonNull to allow JSON deserialization
        this.user = builder.user; // cannot requireNonNull to allow JSON deserialization

        this.read = builder.read;
        this.write = builder.write;
        this.manage = builder.manage;

    }

    /**
     * @return the read permission restriction
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read permission to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return the write permission restriction
     */
    public boolean isWrite() {
        return write;
    }

    /**
     * @param write the write permission to set
     */
    public void setWrite(boolean write) {
        this.write = write;
    }

    /**
     * @return the manage permission
     */
    public boolean isManage() {
        return manage;
    }

    /**
     * @param manage the manage permission to set
     */
    public void setManage(boolean manage) {
        this.manage = manage;
    }

    /**
     * @return the project id
     */
    public UUID getProjectId() {
        return id.getProjectId();
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return id.getUsername();
    }

    @JsonIgnore
    /** @return project for this permission */
    public Project getProject() {
        return project;
    }

    @JsonIgnore
    /** @return user for this permission */
    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        ProjectPermission other = (ProjectPermission) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "ProjectPermission "
                + "[ projectId=" + id.getProjectId()
                + ", username=" + id.getUsername()
                + (project != null ? ", project=" + project : "")
                + (user != null ? ", user=" + user : "")
                + ", read=" + read
                + ", write=" + write
                + ", manage=" + manage + "]";
    }

    /**
     * Creates a builder to build {@link ProjectPermission} and initialize it with the given object.
     * @param projectPermission to initialize the builder with. Cannot be null.
     * @return created builder
     */
    public static Builder builderFrom(ProjectPermission projectPermission) {
        return new Builder(projectPermission);
    }

    /**
     * Builder to build {@link ProjectPermission}.
     */
    @NonNullByDefault({ DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE })
    public static final class Builder {
        @NonNull
        private ProjectUserId id;
        private Project project;
        private User user;

        private boolean read;
        private boolean write;
        private boolean manage;

        private String createdBy;
        private Date createdOn;
        private String updatedBy;
        private Date updatedOn;

        /**
         * Constructs builder for JSON deserialization.
         * @param projectId the project id. Cannot be null.
         * @param username the username. Cannot be null.
         */
        @JsonCreator
        private Builder(UUID projectId, String username) {
            this.id = new ProjectUserId(projectId, username);
        }

        /**
         * Constructs builder from following required fields.
         * @param project the project. Cannot be null.
         * @param user the user. Cannot be null.
         */
        public Builder(@NonNull Project project, @NonNull User user) {
            this.project = project;
            this.user = user;
            this.id = new ProjectUserId(project.getId(), user.getUsername());
        }

        private Builder(ProjectPermission projectPermission) {
            this.id = projectPermission.id;
            this.read = projectPermission.read;
            this.write = projectPermission.write;
            this.manage = projectPermission.manage;

        }

        /**
         * Builder method for read permission parameter.
         * @param read field to set. Cannot be null.
         * @return builder
         */
        public Builder withRead(boolean read) {
            this.read = read;
            return this;
        }

        /**
         * Builder method for write permission parameter.
         * @param write field to set. Cannot be null.
         * @return builder
         */
        public Builder withWrite(boolean write) {
            this.write = write;
            return this;
        }

        /**
         * Builder method for manage permission parameter.
         * @param manage field to set. Cannot be null.
         * @return builder
         */
        public Builder withManage(boolean manage) {
            this.manage = manage;
            return this;
        }

        /**
         * Builder method for createdBy parameter.
         * @param createdBy field to set. Cannot be null.
         * @return builder
         */
        public Builder withCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        /**
         * Builder method for createdOn parameter.
         * @param createdOn field to set. Cannot be null.
         * @return builder
         */
        public Builder withCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        /**
         * Builder method for updatedBy parameter.
         * @param updatedBy field to set. Cannot be null.
         * @return builder
         */
        public Builder withUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        /**
         * Builder method for updatedOn parameter.
         * @param updatedOn field to set. Cannot be null.
         * @return builder
         */
        public Builder withUpdatedOn(Date updatedOn) {
            this.updatedOn = updatedOn;
            return this;
        }

        public ProjectPermission build() {
            return new ProjectPermission(this);
        }
    }
}
