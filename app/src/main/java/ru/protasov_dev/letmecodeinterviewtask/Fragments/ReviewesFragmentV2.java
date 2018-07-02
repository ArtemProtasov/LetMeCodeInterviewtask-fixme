package ru.protasov_dev.letmecodeinterviewtask.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.ReviewsAdapter;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewesFragmentV2 extends Fragment {

    //
    private EditText keywords;
    private EditText date;
    private int offset = 0;
    private Calendar Date = Calendar.getInstance();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PostModelReviews posts = new PostModelReviews();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        keywords = view.findViewById(R.id.keyword);
        date = getView().findViewById(R.id.data);
        date.setInputType(InputType.TYPE_NULL); //Не выводим клавиатуру
        swipeRefreshLayout = getView().findViewById(R.id.swipe_container);
        ImageButton clearKeywords = getView().findViewById(R.id.clear_keywords);
        ImageButton clearDate = getView().findViewById(R.id.clear_date);
        ImageButton nextPage = getView().findViewById(R.id.next_page);
        ImageButton prevPage = getView().findViewById(R.id.prev_page);

        //posts = new ArrayList<>();

        //Вызываем парсес
        getReviews();

        //При клике на поле ввода даты - отображаем диалог выбора даты
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), d, Date.get(Calendar.YEAR), Date.get(Calendar.MONTH), Date.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //При нажатии Enter производим поиск по ключевым словам
        keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    // обработка нажатия Enter
                    getReviews();
                    return true;
                }
                return false;
            }
        });

        //При нажатии на "Корзину" в Keywords - очищать Keywords
        clearKeywords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywords.setText(null);
                getReviews();
            }
        });

        //При нажатии на "Корзину" в Date - очищать Date
        clearDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.setText(null);
                getReviews();
            }
        });

        //При нажатии на кнопку "Следующая страница" - подгружать следующую страницу
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset += 20;
                getReviews();
            }
        });

        //При нажатии на кнопку "Предыдущая страница" - подгружать предыдующу страницу
        prevPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Изначально offset == 0
                if(offset >= 20){
                    offset -= 20;
                    getReviews();
                } else {
                    Snackbar.make(getView(), "You reached the top of the list", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getReviews();
            }
        });
    }

    //Установка даты
    private void setInitialDate() {
        //Преобразуем с помощью SimpleDateFormat дату в миллисекундах в следующий формат: ГОД/МЕСЯЦ/ДЕНЬ (так задано в ТЗ)
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        //И сразу устанавливаем в поле ввода форматированную дату
        String dateForSearch = formatter.format(Date.getTimeInMillis());
        date.setText(dateForSearch);
        //Выполняем поиск при выборе даты
        getReviews();

    }

    //Установка обработчика выбора даты
    private DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date.set(Calendar.YEAR, year);
            Date.set(Calendar.MONTH, monthOfYear);
            Date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    private void getReviews() {
        App.getApi().getAllReviews(getString(R.string.api_key_nyt)).enqueue(new Callback<PostModelReviews>() {
            @Override
            public void onResponse(@NonNull Call<PostModelReviews> call, @NonNull Response<PostModelReviews> response) {
                assert response.body() != null;
                posts = response.body();
                ReviewsAdapter adapter = new ReviewsAdapter(posts);

                recyclerView = getView().findViewById(R.id.recycler_reviews);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.setAdapter(adapter);
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<PostModelReviews> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                System.out.println(t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }


}
