package de.tudarmstadt.informatik.tudas.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TudasServiceGenerator {

    private static final String BASE_URL = "http://88.152.247.4/tudas/public/api/";

    private static JsonDeserializer<Calendar> deserializer = ((json, typeOfT, context) -> {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        Calendar output = Calendar.getInstance();
        try {
            output.setTime(formatter.parse(json.getAsString()));
        } catch(ParseException ignored) {}
        return output;
    });

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Calendar.class, deserializer)
            .create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
