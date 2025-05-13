package com.example.backend;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Agency {
    private String name;

    @JsonProperty("short_name")
    private String shortName;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("sortable_name")
    private String sortableName;

    private String slug;

    private List<Agency> children;

    @JsonProperty("cfr_references")
    private List<CfrReference> cfrReferences;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSortableName() {
        return sortableName;
    }

    public void setSortableName(String sortableName) {
        this.sortableName = sortableName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Agency> getChildren() {
        return children;
    }

    public void setChildren(List<Agency> children) {
        this.children = children;
    }

    public List<CfrReference> getCfrReferences() {
        return cfrReferences;
    }

    public void setCfrReferences(List<CfrReference> cfrReferences) {
        this.cfrReferences = cfrReferences;
    }
}