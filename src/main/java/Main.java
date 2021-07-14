import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static final String BASE_URL = "https://fssp.gov.ru/";

    public static void main(String[] args) throws IOException {

        File file = new File("info.html");
        if (file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        Document doc = Jsoup.connect("https://fssp.gov.ru/torgi").get();
        fileWriter.append(doc.toString());


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging )
                .build();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).build();
        //Создание объекта с помощью которого будем выполнять запрос
        HttpRequest httpRequest = retrofit.create(HttpRequest.class);
        //Объект выполняющий запрос
        Call<ResponseBody> caller = httpRequest.getData("","","","","","5500000000000","","","","","");
       //синхронное получение данных
        Response<ResponseBody> response = caller.execute();
        String text = response.raw().toString();
        System.out.println(text+" lijjk");


    }
}

