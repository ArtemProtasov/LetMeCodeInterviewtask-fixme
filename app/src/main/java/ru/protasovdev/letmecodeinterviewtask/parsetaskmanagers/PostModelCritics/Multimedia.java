package ru.protasovdev.letmecodeinterviewtask.parsetaskmanagers.PostModelCritics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Multimedia {

    @SerializedName("resource")
    @Expose
    private Resource resource = null;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}