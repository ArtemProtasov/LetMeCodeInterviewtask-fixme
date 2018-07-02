package ru.protasov_dev.letmecodeinterviewtask.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.ReviewsAdapter;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class CriticPage extends AppCompatActivity {// implements SwipeRefreshLayout.OnRefreshListener, ParseTaskReviewes.MyCustomCallBack{
    private SwipeRefreshLayout refreshLayout;

    private String name;
    private int offset = 0;

    private RecyclerView recyclerView;
    private PostModelReviews posts = new PostModelReviews();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critic_page);

        Intent getData = getIntent();
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

        CardView cardCritic = findViewById(R.id.card_critic);
        TextView nameCritic = findViewById(R.id.txt_name);
        nameCritic.setText(name);
        TextView statusCritic = findViewById(R.id.txt_status);
        statusCritic.setText(status);
        ImageView imageView = findViewById(R.id.img_photo);
        Glide.with(this)
                .load(url_photo)
                .into(imageView);
        TextView bioCritic = findViewById(R.id.txt_bio);
        bioCritic.setText(bio.replace("<br/>", " ")); //У A. O. Scott встречаются html теги в БИО. Заменю на пробелы

        ImageButton nextPage = findViewById(R.id.next_critic_page_post);
        ImageButton prevPage = findViewById(R.id.prev_critic_page_post);


        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset += 20;
                //URL += "&offset=" + offset;
                getCriticPosts();
            }
        });

        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(offset >= 20){
                    offset -= 20;
                    //URL += "&offset=" + offset;
                    getCriticPosts();
                }
            }
        });


        refreshLayout = findViewById(R.id.swipe_container);
        //Устанавливаем слушатель и какими цветами будет переливаться кружочек на
        //Swipe-to-refresh
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCriticPosts();
            }
        });

        getCriticPosts(); //При запуске активити прогружаем посты


    }

    private void getCriticPosts(){
        App.getApi().getCriticPost(getString(R.string.api_key_nyt), name).enqueue(new Callback<PostModelReviews>() {
            @Override
            public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
                assert response.body() != null;
                posts = response.body();
                ReviewsAdapter adapter = new ReviewsAdapter(posts);

                recyclerView = findViewById(R.id.recycler_critic_page);

                LinearLayoutManager layoutManager = new LinearLayoutManager(CriticPage.this);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostModelReviews> call, Throwable t) {

            }
        });
        refreshLayout.setRefreshing(false);
    }

//    //FIXME можно было распарсить и в AsyncTask
//    //FIXME инициализация  RecyclerView после каждой загрузки
//    @Override
//    public void doSomething(String strJson) {
//        // выводим целиком полученную json-строку
//        Log.d("JSON: ", strJson);
//
//        //С помощью Gson будем разбирать json на составляющие
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//
//        //Заполняем ParseTaskTwo нашими данными из JSON
//        parseTaskTwo = gson.fromJson(strJson, ParseTaskTwo.class);
//
//        //В List получаем наш Result, основное, с чем будем работать
//        results = parseTaskTwo.getResults();
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_critic_page);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        MyCustomAdapterReviewes adapterReviewes = new MyCustomAdapterReviewes(initData());
//        recyclerView.setAdapter(adapterReviewes);
//    }

    //FIXME абсолюто ненужный метод
    //FIXME данные распарсить (Retrofit в помощь) сразу в требуюмую модель
    //FIXME для фомата дат использовать DateFormat, а не replace и делать это можно в адаптере
    //FIXME try/catch не нужен - в адаптере проверяещь не пуста ли url и в случае успеха грузишь картинку
}
