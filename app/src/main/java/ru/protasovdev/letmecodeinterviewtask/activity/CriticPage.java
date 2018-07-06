package ru.protasovdev.letmecodeinterviewtask.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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
import ru.protasovdev.letmecodeinterviewtask.App;
import ru.protasovdev.letmecodeinterviewtask.Utils;
import ru.protasovdev.letmecodeinterviewtask.endlessrecyclereiew.EndlessRecyclerViewCriticPage;
import ru.protasovdev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.PostModelReviews;
import ru.protasovdev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasovdev.letmecodeinterviewtask.adapters.ReviewesListAdapter;

public class CriticPage extends AppCompatActivity implements EndlessRecyclerViewCriticPage.OnLoadMoreListener, ReviewesListAdapter.ReviewesListener {
    private SwipeRefreshLayout refreshLayout;

    private String name;
    private int currentPage = 0;
    private ReviewesListAdapter reviewesListAdapter;
    private EndlessRecyclerViewCriticPage recyclerView;
    private String url_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critic_page);

        final Intent getData = getIntent();
        name = getData.getStringExtra("NAME");
        String status = getData.getStringExtra("STATUS");
        String bio = getData.getStringExtra("BIO");
        try {
            url_photo = getData.getStringExtra("URL_IMG");
        } catch (NullPointerException e) {
            url_photo = null;
        }

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
                getData(true);
            }
        });

        reviewesListAdapter = new ReviewesListAdapter(this);
        recyclerView = findViewById(R.id.recycler_critics_page);
        recyclerView.setAdapter(reviewesListAdapter);
        recyclerView.setOnLoadMoreListener(this);

        getData(false); //При запуске активити прогружаем посты
    }

    @Override
    public void onLoadMore() {
        currentPage += 20;
        getData(false);
    }

    private void getData(boolean clear) {
        if (clear) {
            reviewesListAdapter.clearItems();
        }
        App.getApi().getCriticPost(getString(R.string.api_key_nyt), name, currentPage).enqueue(new Callback<PostModelReviews>() {
            @Override
            public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
                if (response.body() != null) {
                    reviewesListAdapter.addItems(response.body().getResults());
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostModelReviews> call, Throwable t) {
                t.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onReviewesItemClick(Result item) {
        Intent startReviewPage = new Intent(this, ReviewPage.class)
                .putExtra("URL", item.getLink().getUrl())
                .putExtra("ARTICLE_TITLE", item.getDisplayTitle());
        startActivity(startReviewPage);
    }

    private ImageView imageView;
    private String title;
    private Bitmap bitmap;

    @Override
    public void onReviewesLongImageClick(final ImageView imageView, final String title) {
        this.imageView = imageView;
        this.title = title;
        this.bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        new AlertDialog.Builder(this)
                .setTitle(R.string.save_image)
                .setMessage(R.string.save_or_cancel)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!savePictures()) {
                            requestPermission();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(imageView, R.string.image_not_saved, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private boolean savePictures() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                Utils.savePicture(getApplicationContext(), title, bitmap);
                Snackbar.make(imageView,
                        getString(R.string.image_saved),
                        Snackbar.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(imageView,
                        getString(R.string.image_not_saved),
                        Snackbar.LENGTH_SHORT).show();
            }
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.no_permission)
                .setMessage(R.string.allow_save_pictures)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(imageView,
                                R.string.image_not_saved,
                                Snackbar.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        Utils.savePicture(getApplicationContext(), title, bitmap);
                        Snackbar.make(imageView,
                                getString(R.string.image_saved),
                                Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar.make(imageView,
                                getString(R.string.image_not_saved),
                                Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(imageView,
                            R.string.no_permission,
                            Snackbar.LENGTH_SHORT).show();
                }
                return;
            }
        }
        Snackbar.make(imageView,
                R.string.no_permission,
                Snackbar.LENGTH_SHORT).show();
    }
}
