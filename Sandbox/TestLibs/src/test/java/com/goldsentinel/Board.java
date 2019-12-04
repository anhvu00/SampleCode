package com.goldsentinel;

import static org.eclipse.jdt.annotation.Checks.requireNonEmpty;
import static org.eclipse.jdt.annotation.Checks.requireNonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * POJO and JPA Entity for "Connections" Board concept.
 *
 * @author neon
 * @since 0.8
 */

@JsonIgnoreProperties(
        value = { "read", "write", "manage" },
        allowGetters = true)
@NonNullByDefault
public class Board {

    // ========== required fields ==========
    /** Unique Identifier for board. */

    private UUID id;

    /** Name of board. */
    private String name;

    /** JSON data for board. */
    private JsonNode data;

    /** Owner's id (e.g. username) for board. */
    private String owner;

    /** Is board sharable. */
    private boolean allowSharing;

    /** Share board to project (only available if #allowSharing is true). */
    private boolean sharedToProject;

    /** Version of the board. */
    private int version;

    /** Project this belongs to. */

    private Project project;

    /**
     * Transient field if requesting user has read permission to board.
     * @since 0.11
     */

    private boolean read;

    /**
     * Transient field if requesting user has write permission to board.
     * @since 0.11
     */

    private boolean write;

    /**
     * Transient field if requesting user has manage permission to board.
     * @since 0.11
     */

    private boolean manage;

    // ========== optional fields ==========
    /** Description of board. */
    @Nullable
    private String description;

    /** Default Constructor required for JPA. */
    @SuppressWarnings({ "unused", "null" })
    private Board() {
    }

    /**
     * Constructs a board from builder.
     *
     * @param builder Builder to make Board. Cannot be null.
     * @throws NullPointerException if any required fields are null.
     * @throws IllegalArgumentException if any required non empty fields is an empty string.
     */
    private Board(Builder builder) {
        this.id = requireNonNull(builder.id, "id cannot be null");

        // Allow client to send Board JSON without project, name, data, and owner,
        // a requireNonNull prevent deserialize to Board without those fields.
        // The null checks are done in the Builder(with all the require fields)
        // from public API.
        this.name = builder.name;
        this.data = builder.data;
        this.owner =builder.owner;
        this.project = builder.project;

        this.allowSharing = builder.allowSharing;
        this.sharedToProject = builder.sharedToProject;
        this.description = builder.description;
        this.version = builder.version;
        this.read = builder.read;
        this.write = builder.write;
        this.manage = builder.manage;


    }

    /** @return Unique Identifier for board. */
    public UUID getId() {
        return id;
    }

    /** @return Name of board. */
    public String getName() {
        return name;
    }

    /** @param name Name of board. */
    public void setName(String name) {
        this.name = name;
    }

    /** @return JSON data for board. */
    public JsonNode getData() {
        return data;
    }

    /** @param data JSON data to set for board. Cannot be null. */
    public void setData(JsonNode data) {
        this.data = data;
    }

    /** @return Owner of this board. */
    public String getOwner() {
        return owner;
    }

    /** @return Project this board belongs to. */
    public Project getProject() {
        return project;
    }

    /** @return true if board is sharable, otherwise false */
    public boolean isAllowSharing() {
        return allowSharing;
    }

    /** @param allowSharing Set to true if board is sharable, otherwise false. */
    public void setAllowSharing(boolean allowSharing) {
        this.allowSharing = allowSharing;
    }

    /** @return true if this board is shared to a project */
    public boolean isSharedToProject() {
        return sharedToProject;
    }

    /** @param sharedToProject Set to true if this board is shared to a project. */
    public void setSharedToProject(boolean sharedToProject) {
        this.sharedToProject = sharedToProject;
    }

    /** @return Version of the board */
    public int getVersion() {
        return version;
    }

    /** @param version Version for board. */
    public void setVersion(int version) {
        this.version = version;
    }

    /** Increment the version of this board by one. */
    public void incrementVersion() {
        setVersion(this.version + 1);
    }

    /** @return Description of board. */
    @Nullable
    public String getDescription() {
        return description;
    }

    /** @param description Description for board. Can be null for none. */
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    /**
     * @return True if requesting user only has read access to board.
     * @since 0.11
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read True if requesting user has read access to board.
     * @since 0.11
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return True if requesting user only has write access to board.
     * @since 0.11
     */
    public boolean isWrite() {
        return write;
    }

    /**
     * @param write True if requesting user has write access on board.
     * @since 0.11
     */
    public void setWrite(boolean write) {
        this.write = write;
    }

    /**
     * @return True if requesting user only has manage access to board.
     * @since 0.11
     */
    public boolean isManage() {
        return manage;
    }

    /**
     * @param manage True if requesting user has manage access on board.
     * @since 0.11
     */
    public void setManage(boolean manage) {
        this.manage = manage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                version);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Board)) {
            return false;
        }
        Board other = (Board) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(owner, other.owner)
                && Objects.equals(data, other.data)
                && Objects.equals(description, other.description)
                && Objects.equals(project, other.project)
                && allowSharing == other.allowSharing
                && sharedToProject == other.sharedToProject
                && version == other.version;
    }

    @Override
    public String toString() {
        return "Board ["
                + "id=" + id
                + ", name=" + name
                + ", owner=" + owner
                + ", data=" + (data != null ? StringUtils.abbreviate(data.toString(), 1000) : data)
                + ", allowSharing=" + allowSharing
                + ", sharedToProject=" + sharedToProject
                + ", read=" + read
                + ", write=" + write
                + ", manage=" + manage
                + (description != null ? ", description=" + description : "")
                + (project != null ? ", project=" + project : "")
                + ", version=" + version
                + "]";
    }

    /**
     * Creates a builder to build {@link Board} and initialize it with the given object.
     *
     * @param board to initialize the builder with. Cannot be null.
     * @return created builder. Never null.
     */
    public static Builder builderFrom(Board board) {
        return new Builder(board);
    }

    /**
     * Builder to build {@link Board}.
     */
    @NonNullByDefault({ DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE })
    public static final class Builder {
        private UUID id;
        private String name;
        private JsonNode data;
        private String owner;
        private boolean allowSharing;
        private boolean sharedToProject;
        private int version;
        private Project project;
        private String description;
        // transient fields
        private boolean read = true;
        private boolean write = true;
        private boolean manage;

        private String createdBy;
        private Date createdOn;
        private String updatedBy;
        private Date updatedOn;

        /** Used by Jackson to deserialize (without project, name, data, description fields). */
        @SuppressWarnings("unused")
        private Builder() {
        }

        /**
         * Constructs builder for {@link Board} based on all required fields.
         *
         * @param id Id for board
         * @param name Name for board.
         * @param data Data for board.
         * @param owner Owner/creator of board.
         * @param project Project board belongs to.
         * @throws NullPointerException if any required parameters are null.
         */
        public Builder(
                UUID id,
                String name,
                JsonNode data,
                String owner,
                Project project) {

            this.id = requireNonNull(id, "id cannot be null");
            this.name = requireNonEmpty(name, "name cannot be null or empty");
            this.data = requireNonNull(data, "data cannot be null");
            this.owner = requireNonNull(owner, "owner cannot be null");
            this.project = requireNonNull(project, "project cannot be null");
        }

        /**
         * Constructs builder for {@link Board} based specified board.
         *
         * @param board Board to initialize this builder with. Cannot be null.
         */
        private Builder(Board board) {
            this.id = board.id;
            this.name = board.name;
            this.data = board.data;
            this.owner = board.owner;
            this.allowSharing = board.allowSharing;
            this.sharedToProject = board.sharedToProject;
            this.version = board.version;
            this.project = board.project;
            this.description = board.description;
            this.read = board.read;

        }

        /**
         * Builder method for id parameter.
         * @param id field to set. Cannot be null.
         * @return builder
         */
        public Builder withId(UUID id) {
            this.id = id;
            return this;
        }

        /**
         * Builder method for name parameter.
         * @param name field to set. Cannot be null.
         * @return builder
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Builder method for data parameter.
         * @param data field to set. Cannot be null.
         * @return builder
         */
        public Builder withData(JsonNode data) {
            this.data = data;
            return this;
        }

        /**
         * Builder method for owner parameter.
         * @param owner field to set. Cannot be null.
         * @return builder
         */
        public Builder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Builder method for project parameter.
         * @param project field to set. Cannot be null.
         * @return builder
         */
        public Builder withProject(Project project) {
            this.project = project;
            return this;
        }

        /**
         * Builder method for allowSharing parameter.
         * @param allowSharing field to set. Cannot be null.
         * @return builder
         */
        public Builder withAllowSharing(boolean allowSharing) {
            this.allowSharing = allowSharing;
            return this;
        }

        /**
         * Builder method for sharedToProject parameter.
         * @param sharedToProject field to set. Cannot be null.
         * @return builder
         */
        public Builder withSharedToProject(boolean sharedToProject) {
            this.sharedToProject = sharedToProject;
            return this;
        }

        /**
         * Builder method for version parameter.
         * @param version field to set. Cannot be null.
         * @return builder
         */
        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        /**
         * Builder method for description parameter.
         * @param description field to set. Can be null.
         * @return builder
         */
        public Builder withDescription(@Nullable String description) {
            this.description = description;
            return this;
        }

        /**
         * Builder method for read parameter.
         * @param read field to set. Cannot be null.
         * @return builder
         * @since 0.11
         */
        public Builder withRead(boolean read) {
            this.read = read;
            return this;
        }

        /**
         * Builder method for write parameter.
         * @param write field to set. Cannot be null.
         * @return builder
         * @since 0.11
         */
        public Builder withWrite(boolean write) {
            this.write = write;
            return this;
        }

        /**
         * Builder method for manage parameter.
         * @param manage field to set. Cannot be null.
         * @return builder
         * @since 0.11
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

        /**
         * Builder method of the builder.
         * @return built class
         */
        public Board build() {
            return new Board(this);
        }
    }

}
