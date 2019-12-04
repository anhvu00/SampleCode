package com.goldsentinel;

import static org.eclipse.jdt.annotation.Checks.requireNonEmpty;
import static org.eclipse.jdt.annotation.Checks.requireNonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


import org.eclipse.jdt.annotation.DefaultLocation;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * POJO and JPA Entity for Project concept.
 *
 * @author neon
 * @since 0.8
 */
@JsonDeserialize(builder = Project.Builder.class)
@JsonIgnoreProperties(
        value = { "read", "write", "manage" },
        allowGetters = true)
@Entity
@NonNullByDefault
public class Project  {

    // ========== required fields ==========
    /** Unique Identifier for project. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /** Name for project. */
    private String name;

    /** Description for project. */
    private String description;

    /**
     * User permissions for a project.
     * @since 0.11
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectPermission> permissions;

    /**
     * Searched sites for this project
     * @since 0.11
     */
    @ElementCollection
    @CollectionTable(name = "project_site")
    private List<String> searchedSites;

    /**
     * Transient field if requesting user has read permission to project and it's board.
     * @since 0.11
     */
    @Transient
    private boolean read;

    /**
     * Transient field if requesting user has write permission to project and it's board.
     * @since 0.11
     */
    @Transient
    private boolean write;

    /**
     * Transient field if requesting user has manage permission to project and it's board.
     * @since 0.11
     */
    @Transient
    private boolean manage;

    /** Default Constructor required for JPA. */
    @SuppressWarnings({ "null", "unused" })
    private Project() {
    }

    /**
     * Constructs project from Builder.
     *
     * @param builder Builder to make project. Cannot be null.
     * @throws NullPointerException if any required fields are null.
     * @throws IllegalArgumentException if any required non empty fields is an empty string.
     */
    private Project(Builder builder) {
        this.id = requireNonNull(builder.id, "id cannot be null");
        this.name = requireNonEmpty(builder.name, "name cannot be null or empty");
        this.description = requireNonEmpty(builder.description, "description cannot be null or empty");
        this.read = builder.read;
        this.write = builder.write;
        this.manage = builder.manage;
        this.searchedSites = builder.searchedSites;


    }

    /** @return Identifier for project */
    public UUID getId() {
        return id;
    }

    /** @return Name for project. */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set.
     * @since 0.11
     */
    public void setName(String name) {
        this.name = name;
    }

    /** @return Description for project. */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     * @since 0.11
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return True if requesting user only has read access to project and it's boards.
     * @since 0.11
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read True if requesting user has read access on project and it's boards.
     * @since 0.11
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return True if requesting user only has read access to project and it's boards.
     * @since 0.11
     */
    public boolean isWrite() {
        return write;
    }

    /**
     * @param write True if requesting user has read access on project and it's boards.
     * @since 0.11
     */
    public void setWrite(boolean write) {
        this.write = write;
    }

    /**
     * @return True if requesting user only has manage permission to project and it's boards.
     * @since 0.11
     */
    public boolean isManage() {
        return manage;
    }

    /**
     * @param manage True to give requesting user manage permission to project and it's boards.
     * @since 0.11
     */
    public void setManage(boolean manage) {
        this.manage = manage;
    }

    /**
     * @return The list of searched sites
     * @since 0.11
     */
    public List<String> getSearchedSites() {
        return searchedSites;
    }

    /**
     * @param searchedSites the searched sites to update with
     * @since 0.11
     */
    public void setSearchedSites(List<String> searchedSites) {
        this.searchedSites = searchedSites;
    }

    /**
     * @return list of project permissions
     * @since 0.11
     */
    @JsonIgnore
    public List<ProjectPermission> getPermissions() {
        return this.permissions;
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
        if (!(obj instanceof Project)) {
            return false;
        }
        Project other = (Project) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(name, other.name)
                && Objects.equals(description, other.description);
    }

    @Override
    public String toString() {
        return "Project ["
                + "id=" + id
                + ", name=" + name
                + ", description=" + description
                + ", read=" + read
                + ", write=" + write
                + ", manage=" + manage
                + ", updatedOn=" + "2019"
                + ", searchedSites=" + searchedSites
                + "]";
    }

    /**
     * Creates a builder to build {@link Project} and initialize it with the given object.
     *
     * @param project to initialize the builder with. Cannot be null.
     * @return created builder. Never null.
     */
    public static Builder builderFrom(Project project) {
        return new Builder(project);
    }

    /**
     * Builder to build {@link Project}.
     */
    @NonNullByDefault({ DefaultLocation.PARAMETER, DefaultLocation.RETURN_TYPE })
    public static final class Builder {
        private UUID id;
        private String name;
        private String description;
        private List<String> searchedSites;
        // transient fields
        private boolean read = true;
        private boolean write = true;
        private boolean manage;

        private String createdBy;
        private Date createdOn;
        private String updatedBy;
        private Date updatedOn;

        /**
         * Constructs builder for {@link Board} based on all required fields.
         *
         * @param id Identifier for project. Cannot be null.
         * @param name Name for project. Cannot be null.
         * @param description Description for project. Cannot be null.
         */
        public Builder(UUID id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        /**
         * Constructs builder for {@link Board} based on all required fields.
         *
         * @param project Project to initialize this builder with. Cannot be null.
         */
        private Builder(Project project) {
            this.id = project.id;
            this.name = project.name;
            this.description = project.description;
            this.searchedSites = project.searchedSites;
            this.read = project.read;
            this.write = project.write;
            this.manage = project.manage;

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
         * Builder method for description parameter.
         * @param description field to set. Cannot be null.
         * @return builder
         */
        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Builder method for specified parameter.
         * @param read field to set. Cannot be null.
         * @return builder
         * @since 0.11
         */
        public Builder withRead(boolean read) {
            this.read = read;
            return this;
        }

        /**
         * Builder method for specified parameter.
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
         * Builder method for searchedSites parameter.
         * @param searchedSites the set of searched sites. Cannot be null, but can be empty.
         * @return builder
         * @since 0.11
         */
        public Builder withSearchedSites(List<String> searchedSites) {
            this.searchedSites = searchedSites;
            return this;
        }

        /**
         * Builder method of the builder.
         * @return built class
         */
        public Project build() {
            return new Project(this);
        }
    }

}
