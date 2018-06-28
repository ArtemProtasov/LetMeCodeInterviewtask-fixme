package ru.protasov_dev.letmecodeinterviewtask.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.CriticsAdapter;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.PostModelCritics;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class CriticsFragmentV2 extends Fragment{

    private EditText nameCritics;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<PostModelCritics> posts;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.critics_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nameCritics = getView().findViewById(R.id.criticName);
        swipeRefreshLayout = getView().findViewById(R.id.swipe_container_critics);
        ImageButton clearNameCritics = getView().findViewById(R.id.clear_critics_name);

        //При нажатии на кнопку "Очистки" поля - очищаем поле и проводим новый поиск
        clearNameCritics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCritics.setText(null);
                getCritics();
            }
        });

        //При нажатии Enter производим поиск по ключевым словам
        nameCritics.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    getCritics();
                    return true;
                }
                return false;
            }
        });

        getCritics(); //При запуске фрагмента прогружаем посты

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

    private void getCritics(){

        posts = new ArrayList<>();

        CriticsAdapter adapter = new CriticsAdapter(posts, getContext());

        recyclerView = getView().findViewById(R.id.recycler_critics);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        App.getApi().getAllCritics(getString(R.string.api_key_nyt)).enqueue(new Callback<List<PostModelCritics>>() {
            @Override
            public void onResponse(@NonNull Call<List<PostModelCritics>> call, @NonNull Response<List<PostModelCritics>> response) {
                posts.clear();
                assert response.body() != null;
                posts.addAll(response.body());
                recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(@NonNull Call<List<PostModelCritics>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                System.out.println(t);
            }
        });


        swipeRefreshLayout.setRefreshing(false);
    }
}
