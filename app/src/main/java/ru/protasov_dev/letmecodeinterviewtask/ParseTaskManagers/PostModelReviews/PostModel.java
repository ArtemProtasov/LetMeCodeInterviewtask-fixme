package ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Result> getResults() {
        return results;
    }
}
