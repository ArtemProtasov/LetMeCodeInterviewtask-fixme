package ru.protasov_dev.letmecodeinterviewtask.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.SavePictures;
import ru.protasov_dev.letmecodeinterviewtask.activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.endlessrecyclereiew.EndlessRecyclerViewReviews;
import ru.protasov_dev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.parsetaskmanagers.PostModelReviews.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasov_dev.letmecodeinterviewtask.adapters.ReviewesListAdapter;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;
import static android.content.pm.PackageManager.*;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ReviewesFragment extends Fragment implements
        EndlessRecyclerViewReviews.OnLoadMoreListener,
        ReviewesListAdapter.ReviewesListener {

    private int currentPage = 0;

    private EditText etKeywords;
    private ReviewesListAdapter reviewesListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        etKeywords = view.findViewById(R.id.keyword);

        EndlessRecyclerViewReviews recyclerView = view.findViewById(R.id.recycler_reviews);
        reviewesListAdapter = new ReviewesListAdapter(this);
        recyclerView.setAdapter(reviewesListAdapter);
        recyclerView.setOnLoadMoreListener(this);

        swipeRefreshLayout.setColorSchemeResources(holo_blue_bright,
                holo_green_light,
                holo_orange_light,
                holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(null, null, true, 0);
            }
        });

        etKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    getData(null, null, true, 0);
                    return true;
                }
                return false;
            }
        });

        ImageButton imageButtonClearKeyword = view.findViewById(R.id.clear_keywords);
        imageButtonClearKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeywords.setText(null);
                getData(null, null, true, 0);
            }
        });

        getData(null, null, false, currentPage);
    }

    @Override
    public void onLoadMore() {
        currentPage += 20;
        getData(null, null, false, currentPage);
    }

    private void getData(String reviewer, String order, boolean clear, int offset) {
        if (clear) {
            reviewesListAdapter.clearItems();
        }

        App.getApi().getAllReviews(getString(R.string.api_key_nyt), etKeywords.getText().toString(),
                reviewer, offset, order).enqueue(new Callback<PostModelReviews>() {
            @Override
            public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
                if(response.body() != null) {
                    reviewesListAdapter.addItems(response.body().getResults());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostModelReviews> call, Throwable t) {
                t.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onReviewesItemClick(Result item) {
        Intent startReviewPage = new Intent(getContext(), ReviewPage.class)
                .putExtra("URL", item.getLink().getUrl())
                .putExtra("ARTICLE_TITLE", item.getDisplayTitle());
        startActivity(startReviewPage);
    }

    @Override
    public void onReviewesLongImageClick(ImageView imageView, String title) {
        new SavePictures(getContext(), getView(), getActivity(), title, imageView);
    }
}
