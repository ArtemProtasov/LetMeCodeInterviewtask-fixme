package ru.protasov_dev.letmecodeinterviewtask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Activity.ReviewPage;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

public class ReviewesFragment extends Fragment implements View.OnClickListener, EndlessRecyclerView.OnLoadMoreListener, Callback<PostModelReviews>, ReviewesListAdapter.ReviewesListener {

    private int currentPage = 0;

    private EditText etKeywords;
    private ReviewesListAdapter reviewesListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EndlessRecyclerView recyclerView;
    private List<Result> results;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.clear_keywords).setOnClickListener(this);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        etKeywords = view.findViewById(R.id.keyword);
        etKeywords.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_reviews);
        reviewesListAdapter = new ReviewesListAdapter(results, this);
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
        getData(null, null, false, currentPage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_keywords:
                etKeywords.setText(null);
                getData(null, null, true, 0);
                break;
        }
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
        App.getApi().getAllReviews(getString(R.string.api_key_nyt), etKeywords.getText().toString(), reviewer, offset, order).enqueue(this);
    }

    @Override
    public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
        results = response.body().getResults();

        reviewesListAdapter.updateItems(results);

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<PostModelReviews> call, Throwable t) {
        t.printStackTrace();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onReviewesItemClick(int position) {
        Intent startReviewPage = new Intent(getContext(), ReviewPage.class)
                .putExtra("URL", results.get(position).getLink().getUrl())
                .putExtra("ARTICLE_TITLE", results.get(position).getDisplayTitle());
        getContext().startActivity(startReviewPage);
    }
}
