package ru.protasov_dev.letmecodeinterviewtask.Elements;

import android.content.Context;

public class CriticsElement {
    private String name;
    private String status;
    private String urlImg;
    private String bio;
    private Context context;

    public CriticsElement(String name, String status, String urlImg, Context context, String bio){
        this.name = name;
        this.status = status;
        this.urlImg = urlImg;
        this.bio = bio;
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Context getContext() {
        return context;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public String getBio() {
        return bio;
    }
}
