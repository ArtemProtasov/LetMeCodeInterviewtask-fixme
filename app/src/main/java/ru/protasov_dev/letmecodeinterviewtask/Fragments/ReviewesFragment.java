package ru.protasov_dev.letmecodeinterviewtask.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ru.protasov_dev.letmecodeinterviewtask.Adapters.MyCustomAdapterReviewes;
import ru.protasov_dev.letmecodeinterviewtask.Elements.ReviewesElement;
import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.ParseTaskReviewes;
import ru.protasov_dev.letmecodeinterviewtask.R;

public class ReviewesFragment extends Fragment implements ParseTaskReviewes.MyCustomCallBack, SwipeRefreshLayout.OnRefreshListener {

    private EditText keywords;
    private EditText date;
    private int offset = 0;
    private Calendar Date = Calendar.getInstance();
    private String url;
    private List<ReviewesElement> list;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviewes_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        keywords = getView().findViewById(R.id.keyword);
        date = getView().findViewById(R.id.data);
        date.setInputType(InputType.TYPE_NULL); //Не выводим клавиатуру
        ImageButton clearKeywords = getView().findViewById(R.id.clear_keywords);
        ImageButton clearDate = getView().findViewById(R.id.clear_date);
        ImageButton nextPage = getView().findViewById(R.id.next_page);
        ImageButton prevPage = getView().findViewById(R.id.prev_page);


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

        swipeRefreshLayout = getView().findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    //FIXME фэйковый индикатор загрузки
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getReviews();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 4000);
    }

    //FIXME Retrofit
    private void createURL(){
        //Основной URL имеет вид:
        //https://api.nytimes.com/svc/movies/v2/reviews/search.json?api-key=020eb74eff674e3da8aaa1e8e311edda
        url = getString(R.string.main_url) + "?api-key=" + getString(R.string.api_key_nyt) + "&offset=" + offset;
        //Дальше проверяем, если поле Keywords заполнено, то
        if(keywords.getText().length() != 0){
            //Добавляем в URL "query"
            //https://api.nytimes.com/svc/movies/v2/reviews/search.json?api-key=020eb74eff674e3da8aaa1e8e311edda&query=example+text
            url += "&query=" + keywords.getText().toString().replace(" ", "+");
        }
        //Если поле с датой заполнено, то добавляем в URL "publication-date"
        if(date.getText().length() != 0){
            //И URL становится таким:
            //https://api.nytimes.com/svc/movies/v2/reviews/search.json?api-key=020eb74eff674e3da8aaa1e8e311edda&order=by-publication-date&publication-date=INPUT_DATE
            url += "&order=by-publication-date" + "&publication-date=" + date.getText().toString().replace("/", "-");
        }
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
//        if(list != null && results != null) {
//            list.clear();
//            results.clear();
//        }
//        //Формируем URL
//        createURL();
//        //Вызываем парсес
//        ParseTaskReviewes parseTaskReviewes = new ParseTaskReviewes(this, url);
//        parseTaskReviewes.execute();
    }

    @Override
    public void doSomething(String strJson) {
//        // выводим целиком полученную json-строку
//        Log.d("JSON: ", strJson);
//
//        //С помощью Gson будем разбирать json на составляющие
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();

//        //Заполняем ParseTaskTwo нашими данными из JSON
//        parseTaskTwo = gson.fromJson(strJson, ParseTaskTwo.class);
//
//        //В List получаем наш Result, основное, с чем будем работать
//        results = parseTaskTwo.getResults();
//
//        RecyclerView recyclerView = getView().findViewById(R.id.recycler_reviews);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        MyCustomAdapterReviewes adapterReviewes = new MyCustomAdapterReviewes(initData());
//        recyclerView.setAdapter(adapterReviewes);
    }

//    private List<ReviewesElement> initData() {
////        list = new ArrayList<>();
////        String dateAndTime;
////        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
////        for (int i = 0; i < results.size(); i++) {
////            //Преобразуем дату и время в следующий формат: ГОД/МЕСЯЦ/ДЕНЬ ЧАС:МИНУТА:СЕКУНДА (так задано в ТЗ)
////            //Тут я установил дату публикации. Если нужна дата обновления статьи, то
////            //пишем за место getPublicationDate() -> getDateUpdated()
////            //если нужно дата открытия, то -> getOpeningDate
////            //FIXME что помешало пользовать SimpleDateFormat ?
////            try {
////                System.out.println(dateFormat.parse(results.get(i).getPublicationDate()));
////            } catch (ParseException e) {
////                e.printStackTrace();
////            }
////
////            dateAndTime = results.get(i).getPublicationDate().replace("-", "/");
////            try {
////                list.add(new ReviewesElement(results.get(i).getDisplayTitle(),
////                        results.get(i).getSummaryShort(),
////                        dateAndTime,
////                        results.get(i).getByline(),
////                        results.get(i).getMultimedia().getSrc(),
////                        getContext(),
////                        results.get(i).getLink().getSuggestedLinkText(),
////                        results.get(i).getLink().getUrl()));
////            } catch (NullPointerException e){
////                list.add(new ReviewesElement(results.get(i).getDisplayTitle(),
////                        results.get(i).getSummaryShort(),
////                        dateAndTime,
////                        results.get(i).getByline(),
////                        getString(R.string.src_search),
////                        getContext(),
////                        results.get(i).getLink().getSuggestedLinkText(),
////                        results.get(i).getLink().getUrl()));
////            }
////        }
////        return list;
//    }

}
