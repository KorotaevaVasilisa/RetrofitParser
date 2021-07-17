import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Headers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Main {
    public static final String BASE_URL = "https://fssp.gov.ru/";

    public static void main(String[] args) throws IOException {

        File file = new File("info.html");
        if (file.exists()) {
            file.createNewFile();
        }
        // В идеале у файлов должна быть кодировка UTF_8, т.к. эта кодировка является стандартом
        FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieJarImpl cookieJar = new CookieJarImpl();

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar) // Подключаем механизм который получает cookie, сохраняет их, и передает в последующих запросах
                //.addInterceptor(logging) // Временно отключил логирование, когда потребуется, раскоментировать
                .build();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).build();
        //Создание объекта с помощью которого будем выполнять запрос
        HttpRequest httpRequest = retrofit.create(HttpRequest.class);

        // HTTP заголовки которые передаются серверу вместе с параметрами
        // Что бы узнать какие заголовки передавать, нужно открыть Developer Tools в браузере (F12 или Ctrl+Shift+i)
        // Найти запрос который хотим повторить и скопировать заголовки из блока Request Headers
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept","*/*");
        // Не все заголовки нужно передавать, если например передать этот заголовок, то придет ответ в архиве gzip
        //headers.put("Accept-Encoding","gzip, deflate, br");

        // Не использовать этот заголовок если используется механизм cookieJar()
        //headers.put("Cookie","PHPSESSID=27m1dm9461njhovclhon102v20; _ym_uid=1626456488452199471; _ym_d=1626456488; sputnik_session=1626456488132|1; _ym_visorc=w; _ym_isad=2");
        headers.put("Accept-Language","ru-RU,ru;q=0.9");
        // "На удачу" можно попросить сервер отдавать информацию в нужной нам кодировки,
        // если передача этого заголовка ни как не повлияет на кодировку, придется преобразововать кодировку самостоятельно, после выполнения запроса
        headers.put("Accept-Charset", "utf-8");

        headers.put("Host","fssp.gov.ru");
        headers.put("Referer","https://fssp.gov.ru/torgi");
        headers.put("sec-ch-ua"," Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91");
        headers.put("sec-ch-ua-mobile","?0");
        headers.put("Sec-Fetch-Dest","empty");
        headers.put("Sec-Fetch-Mode","cors");
        headers.put("Sec-Fetch-Site","same-origin");
        headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.put("X-Requested-With","XMLHttpRequest");

        //Объект выполняющий запрос
        Call<ResponseBody> caller = httpRequest.getData(headers, "", "", "", "", "", "5500000000000", "", "", "", "", "");

        //Получает строку запроса
        String requestUrl = caller.request().url().toString();
        System.out.println(requestUrl);

        //синхронное получение данных
        Response<ResponseBody> response = caller.execute();
        String text = response.body().string();

        fileWriter.append(text);
        fileWriter.flush();
        fileWriter.close();
    }
}

