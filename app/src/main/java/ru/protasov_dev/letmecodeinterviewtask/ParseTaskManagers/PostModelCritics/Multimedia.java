package ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Multimedia {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("src")
    @Expose
    private String src;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("width")
    @Expose
    private String width;
    @SerializedName("credit")
    @Expose
    private String credit;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
