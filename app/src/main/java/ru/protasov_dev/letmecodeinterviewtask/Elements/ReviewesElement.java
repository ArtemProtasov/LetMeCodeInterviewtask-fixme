package ru.protasov_dev.letmecodeinterviewtask.Elements;

import android.content.Context;

public class ReviewesElement {
    private String title;
    private String summaryShort;
    private String date;
    private String byline;
    private String urlImg;
    //FIXME убрать Context и никогда больше так не делать
    private Context context;
    private String urlPage;
    private String suggestedLinkText;

    public ReviewesElement(String title, String summaryShort, String date, String byline, String urlImg, Context context, String urlPage, String suggestedLinkText){
        this.title = title;
        this.summaryShort = summaryShort;
        this.date = date;
        this.byline = byline;
        this.urlImg = urlImg;
        this.context = context;
        this.urlPage = urlPage;
        this.suggestedLinkText = suggestedLinkText;
    }

    public String getTitle() {
        return title;
    }

    public String getSummaryShort() {
        return summaryShort;
    }

    public String getDate() {
        return date;
    }

    public String getByline() {
        return byline;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public Context getContext() {
        return context;
    }

    public String getSuggestedLinkText() {
        return suggestedLinkText;
    }

    public String getUrlPage() {
        return urlPage;
    }
}
