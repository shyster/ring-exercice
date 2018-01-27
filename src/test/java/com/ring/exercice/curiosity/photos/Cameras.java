package com.ring.exercice.curiosity.photos;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vladislav Kulasov on 27.01.2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "full_name"
})
public class Cameras {

    @JsonProperty("name")
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("full_name")
    public String getFullName() {
        return fullName;
    }

    @JsonProperty("full_name")
    public void setFullName(String fullName) {
        this.fullName = fullName;
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

        Cameras cameras = (Cameras) o;

        if (!name.equals(cameras.name)) return false;
        if (!fullName.equals(cameras.fullName)) return false;
        return additionalProperties != null ? additionalProperties.equals(cameras.additionalProperties) : cameras.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cameras{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}
