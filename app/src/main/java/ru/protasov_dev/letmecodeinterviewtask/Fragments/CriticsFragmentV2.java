package ru.protasov_dev.letmecodeinterviewtask.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.CriticsAdapter;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class CriticsFragmentV2 extends Fragment implements View.OnClickListener {

    private EditText editTextNameCritics;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostModelCritics posts;
    private RecyclerView recyclerView;
    private CriticsAdapter criticsAdapter;
    private GridLayoutManager layoutManager;
    private String nameCritic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.critics_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        editTextNameCritics = view.findViewById(R.id.criticName);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_critics);
        view.findViewById(R.id.clear_critics_name).setOnClickListener(this);
        criticsAdapter = new CriticsAdapter();
        recyclerView = view.findViewById(R.id.recycler_critics);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(criticsAdapter);

        //При нажатии Enter производим поиск по ключевым словам
        editTextNameCritics.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    nameCritic = editTextNameCritics.getText().toString();
                    getCritics();
                    return true;
                }
                return false;
            }
        });

        //Подгружаем список критиков и выводим
        getCritics();

        //Устанавливаем слушатель и какими цветами будет переливаться кружочек на
        //Swipe-to-refresh
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCritics();
            }
        });
    }

    private void getCritics() {
        if(nameCritic == null) {
            nameCritic = "all";
        }

        App.getApi().getCritic(nameCritic, getString(R.string.api_key_nyt)).enqueue(new Callback<PostModelCritics>() {
            @Override
            public void onResponse(@NonNull Call<PostModelCritics> call, @NonNull Response<PostModelCritics> response) {
                assert response.body() != null;
                posts = response.body();
                criticsAdapter.setPostModelCritics(posts);
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<PostModelCritics> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_critics_name:
                editTextNameCritics.setText(null);
                nameCritic = null;
                getCritics();
                break;

        }
    }
}
