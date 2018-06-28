package ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ParseTaskCritics extends AsyncTask<Void, Void, String> {

    private String URL;
    private String resultJson = "";

    private MyCustomCallBack callback;

    public ParseTaskCritics(final MyCustomCallBack callback, String readyMadeURL) {
        this.callback = callback;
        URL = readyMadeURL;
    }

    @Override
    protected String doInBackground(Void... voids) {
        // получаем данные с внешнего ресурса
        try {
            URL url = new URL(URL); //создаем URL

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET"); //используем метод GET
            urlConnection.connect(); //подключаемся

            InputStream inputStream = urlConnection.getInputStream(); //создаем входной поток
            StringBuffer buffer = new StringBuffer(); //и буфер

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line); //считываем в буфер
            }

            resultJson = buffer.toString(); //преобразуем буфер в String

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson; //Полный JSON со страницы в формате String
    }

    //Здесь использовать onPostExecute вообще не вариант, поэтому перенесу его в ReviewesFragment
    //там я смогу заполнить ListView используя данные, полученные здесь
    @Override
    protected void onPostExecute(String strJson) {
        if(callback != null)
            callback.doSomething(strJson);
    }

    public interface MyCustomCallBack //интерфейс
    {
        void doSomething(String someResult);
    }
}
