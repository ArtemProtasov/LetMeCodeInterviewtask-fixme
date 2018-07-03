package ru.protasov_dev.letmecodeinterviewtask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.Result;

public class ReviewesFragment extends Fragment implements View.OnClickListener, EndlessRecyclerView.OnLoadMoreListener, Callback<PostModelReviews> {

    private static final int MAX_ITEM_PER_PAGE = 100;
    private int currentPage = 0;

    private EditText etKeywords;
    private EndlessRecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.clear_keywords).setOnClickListener(this);
        etKeywords = view.findViewById(R.id.keyword);
        etKeywords.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycler_reviews);

        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setOnLoadMoreListener(this);

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
            default:
                Toast.makeText(getContext(), R.string.no_element, Toast.LENGTH_SHORT).show();
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
            recyclerViewAdapter.clearAllItem();
        }
        App.getApi().getAllReviews(getString(R.string.api_key_nyt), etKeywords.getText().toString(), reviewer, offset, order).enqueue(this);
    }

    @Override
    public void onResponse(Call<PostModelReviews> call, Response<PostModelReviews> response) {
        PostModelReviews postModelReviews = response.body();
        for (Result result : postModelReviews.getResults()) {
            recyclerViewAdapter.addItem(result);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<PostModelReviews> call, Throwable t) {
        t.printStackTrace();
    }
}
