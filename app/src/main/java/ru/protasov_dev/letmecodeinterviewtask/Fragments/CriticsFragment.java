package ru.protasov_dev.letmecodeinterviewtask.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Activity.CriticPage;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.CriticsListAdapter;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.Result;
import ru.protasov_dev.letmecodeinterviewtask.R;

import static android.R.color.holo_blue_bright;
import static android.R.color.holo_green_light;
import static android.R.color.holo_orange_light;
import static android.R.color.holo_red_light;

public class CriticsFragment extends Fragment implements CriticsListAdapter.CriticsListener {

    private EditText editTextSearchByName;
    private CriticsListAdapter criticsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String nameCritics = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.critics_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_critics);
        editTextSearchByName = view.findViewById(R.id.criticName);

        editTextSearchByName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    nameCritics = editTextSearchByName.getText().toString();
                    getData(true);
                    return true;
                }
                return false;
            }
        });

        ImageButton imageButtonClearName = view.findViewById(R.id.clear_critics_name);
        imageButtonClearName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSearchByName.setText(null);
                nameCritics = null;
                getData(true);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(holo_blue_bright,
                holo_green_light,
                holo_orange_light,
                holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_critics);
        criticsListAdapter = new CriticsListAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(criticsListAdapter);

        getData(true);
    }

    private void getData(boolean clear) {
        if (clear) {
            criticsListAdapter.clearItems();
        }

        if (nameCritics == null) {
            nameCritics = "all";
        }

        App.getApi().getCritic(nameCritics, getString(R.string.api_key_nyt)).enqueue(new Callback<PostModelCritics>() {
            @Override
            public void onResponse(Call<PostModelCritics> call, Response<PostModelCritics> response) {
                if(response.body() != null) {
                    criticsListAdapter.addItems(response.body().getResults());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PostModelCritics> call, Throwable t) {
                Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCriticsItemClick(Result item) {
        String URL = null;
        if(item.getMultimedia() != null){
            URL = item.getMultimedia().getResource().getSrc();
        }
        Intent startCriticPage = new Intent(getContext(), CriticPage.class)
                .putExtra("NAME", item.getDisplayName())
                .putExtra("STATUS", item.getStatus())
                .putExtra("BIO", item.getBio())
                .putExtra("URL_IMG", URL);
        getContext().startActivity(startCriticPage);
    }
}
