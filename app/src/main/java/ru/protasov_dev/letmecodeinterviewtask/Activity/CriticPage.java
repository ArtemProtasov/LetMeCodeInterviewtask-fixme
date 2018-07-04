package ru.protasov_dev.letmecodeinterviewtask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.EndlessRecyclerView;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class CriticPage extends AppCompatActivity implements View.OnClickListener, EndlessRecyclerView.OnLoadMoreListener, Callback<PostModelReviews> {// implements SwipeRefreshLayout.OnRefreshListener, ParseTaskReviewes.MyCustomCallBack{
    private SwipeRefreshLayout refreshLayout;

    private String name;
    private int currentPage = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critic_page);

        final Intent getData = getIntent();
        name = getData.getStringExtra("NAME");
        String status = getData.getStringExtra("STATUS");
        String bio = getData.getStringExtra("BIO");
        String url_photo = getData.getStringExtra("URL_IMG");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(name);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView tvNameCritic = findViewById(R.id.txt_name);
        TextView tvStatusCritic = findViewById(R.id.txt_status);
        TextView bioCritic = findViewById(R.id.txt_bio);
        bioCritic.setText(bio);
        tvNameCritic.setText(name);
        tvStatusCritic.setText(status);

        Glide.with(this)
                .load(url_photo)
                .apply(new RequestOptions().placeholder(R.drawable.avatar).centerCrop())
                .into((ImageView) findViewById(R.id.img_photo));

        refreshLayout = findViewById(R.id.swipe_container);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true, currentPage);
            }
        });

        getData(false, currentPage); //При запуске активити прогружаем посты
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
        PostModelReviews postModelReviews = response.body();
        for (Result result : postModelReviews.getResults()) {
        }
    }

    @Override
    public void onFailure(Call<PostModelReviews> call, Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onLoadMore() {
        currentPage += 20;
        getData(false, currentPage);
    }

    private void getData(boolean clear, int offset) {
        if (clear) {
        }
        App.getApi().getCriticPost(getString(R.string.api_key_nyt), name);
    }
}
