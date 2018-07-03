package ru.protasov_dev.letmecodeinterviewtask.Fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.ReviewsAdapter;
import ru.protasov_dev.letmecodeinterviewtask.App;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelReviews.PostModelReviews;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewesFragmentV2 extends Fragment implements View.OnClickListener {
    private EditText editTextKeywords;
    private EditText editTextDate;
    private Calendar calendarDate = Calendar.getInstance();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PostModelReviews postModelReviews = new PostModelReviews();
    private ReviewsAdapter reviewsAdapter;

    public int offset = 0;
    public String query;
    public String reviewer;
    public String publicationDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        editTextKeywords = view.findViewById(R.id.keyword);
        //editTextDate = view.findViewById(R.id.date);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        editTextDate.setOnClickListener(this);
        view.findViewById(R.id.clear_keywords).setOnClickListener(this);
        //view.findViewById(R.id.clear_date).setOnClickListener(this);
        //view.findViewById(R.id.next_page).setOnClickListener(this);
        //view.findViewById(R.id.prev_page).setOnClickListener(this);

        reviewsAdapter = new ReviewsAdapter();
        recyclerView = view.findViewById(R.id.recycler_reviews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(reviewsAdapter);

        //Подгружаем рецензии
        getReviews();

        //При нажатии Enter производим поиск по ключевым словам
        editTextKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // обработка нажатия Enter
                    getReviews();
                    return true;
                }
                return false;
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
        String dateForSearch = formatter.format(calendarDate.getTimeInMillis());
        editTextDate.setText(dateForSearch);
        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        publicationDate = formatter.format(calendarDate.getTimeInMillis());
        //Выполняем поиск при выборе даты
        getReviews();

    }

    //Установка обработчика выбора даты
    private DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendarDate.set(Calendar.YEAR, year);
            calendarDate.set(Calendar.MONTH, monthOfYear);
            calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    private void getReviews() {
        query = editTextKeywords.getText().toString();

        App.getApi().getAllReviews(getString(R.string.api_key_nyt), query, reviewer, offset, "publication-date").enqueue(new Callback<PostModelReviews>() {
            @Override
            public void onResponse(@NonNull Call<PostModelReviews> call, @NonNull Response<PostModelReviews> response) {
                assert response.body() != null;
                postModelReviews = response.body();
                reviewsAdapter.setPostModelReviews(postModelReviews);
                recyclerView.getAdapter().notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<PostModelReviews> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                System.out.println(t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.data:
//                new DatePickerDialog(getContext(), d, calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
//                break;
            case R.id.clear_keywords:
                editTextKeywords.setText(null);
                publicationDate = null;
                getReviews();
                break;
//            case R.id.clear_date:
//                editTextDate.setText(null);
//                getReviews();
//                break;
//            case R.id.next_page:
//                offset += 20;
//                getReviews();
//                break;
//            case R.id.prev_page:
//                //Изначально offset == 0
//                if (offset >= 20) {
//                    offset -= 20;
//                    getReviews();
//                } else {
//                    Snackbar.make(getView(), "You reached the top of the list", Snackbar.LENGTH_SHORT).show();
//                }
//                break;
            default:
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
