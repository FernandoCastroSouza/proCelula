package estacio.br.com.procelula.ws;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebService {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String BASE_URL = "http://www.vidasnoaltar.com/outros/sistema-celulas/api/";

    private static OkHttpClient client;
    private static final int TIMEOUT = 5000;

    public static OkHttpClient getClient(){
        if(client == null){
            client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build();
        }
        return client;
    }


    private static String sendRequest(String endpoint) {
        String urlFormed = BASE_URL + endpoint;
        URL url = null;
        try {
            url = new URL(urlFormed);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Request.Builder builder = new Request.Builder().url(url);
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Accept", "application/json");
        builder.get();
        Request request = builder.build();

        try {
            Response response = getClient().newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.i(String.format("sendRequest(%s)", request.url().toString()), "Error closing InputStream");
        }

        return null;
    }
    public static String listarCelulas(){
        return sendRequest("celulas");
    }
    public static String listarUsuarios(){
        return sendRequest("usuarios");
    }
    public static String listarGES(){
        return sendRequest("ges");
    }
    public static String listarProgramacoes(){
        return sendRequest("programacoes");
    }
    public static String listarAvisos(){
        return sendRequest("avisos");
    }

}
