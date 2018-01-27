package com.ring.exercice.curiosity.photos;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "landing_date",
        "launch_date",
        "status",
        "max_sol",
        "max_date",
        "total_photos",
        "cameras"
})
public class Rover {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("landing_date")
    private String landingDate;
    @JsonProperty("launch_date")
    private String launchDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("max_sol")
    private Integer maxSol;
    @JsonProperty("max_date")
    private String maxDate;
    @JsonProperty("total_photos")
    private Integer totalPhotos;
    @JsonProperty("cameras")
    private List<Cameras> cameras = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("landing_date")
    public String getLandingDate() {
        return landingDate;
    }

    @JsonProperty("landing_date")
    public void setLandingDate(String landingDate) {
        this.landingDate = landingDate;
    }

    @JsonProperty("launch_date")
    public String getLaunchDate() {
        return launchDate;
    }

    @JsonProperty("launch_date")
    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("max_sol")
    public Integer getMaxSol() {
        return maxSol;
    }

    @JsonProperty("max_sol")
    public void setMaxSol(Integer maxSol) {
        this.maxSol = maxSol;
    }

    @JsonProperty("max_date")
    public String getMaxDate() {
        return maxDate;
    }

    @JsonProperty("max_date")
    public void setMaxDate(String maxDate) {
        this.maxDate = maxDate;
    }

    @JsonProperty("total_photos")
    public Integer getTotalPhotos() {
        return totalPhotos;
    }

    @JsonProperty("total_photos")
    public void setTotalPhotos(Integer totalPhotos) {
        this.totalPhotos = totalPhotos;
    }

    @JsonProperty("cameras")
    public List<Cameras> getCameras() {
        return cameras;
    }

    @JsonProperty("cameras")
    public void setCameras(List<Cameras> cameras) {
        this.cameras = cameras;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rover rover = (Rover) o;

        if (!id.equals(rover.id)) return false;
        if (!name.equals(rover.name)) return false;
        if (!landingDate.equals(rover.landingDate)) return false;
        if (!launchDate.equals(rover.launchDate)) return false;
        if (!status.equals(rover.status)) return false;
        if (!maxSol.equals(rover.maxSol)) return false;
        if (!maxDate.equals(rover.maxDate)) return false;
        if (!totalPhotos.equals(rover.totalPhotos)) return false;
        if (!cameras.equals(rover.cameras)) return false;
        return additionalProperties != null ? additionalProperties.equals(rover.additionalProperties) : rover.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + landingDate.hashCode();
        result = 31 * result + launchDate.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + maxSol.hashCode();
        result = 31 * result + maxDate.hashCode();
        result = 31 * result + totalPhotos.hashCode();
        result = 31 * result + cameras.hashCode();
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Rover{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", landingDate='" + landingDate + '\'' +
                ", launchDate='" + launchDate + '\'' +
                ", status='" + status + '\'' +
                ", maxSol=" + maxSol +
                ", maxDate='" + maxDate + '\'' +
                ", totalPhotos=" + totalPhotos +
                ", cameras=" + cameras +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
