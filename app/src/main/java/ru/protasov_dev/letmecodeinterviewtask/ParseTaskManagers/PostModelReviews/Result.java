package ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("display_title")
    @Expose
    private String displayTitle;
    @SerializedName("byline")
    @Expose
    private String byline;
    @SerializedName("summary_short")
    @Expose
    private String summaryShort;
    @SerializedName("publication_date")
    @Expose
    private String publicationDate;
    @SerializedName("link")
    @Expose
    private Link link;
    @SerializedName("multimedia")
    @Expose
    private Multimedia multimedia;

    public String getDisplayTitle() {
        return displayTitle;
    }

    public String getByline() {
        return byline;
    }

    public String getSummaryShort() {
        return summaryShort;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }
}
