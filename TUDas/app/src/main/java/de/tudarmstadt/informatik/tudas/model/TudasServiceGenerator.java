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

/**
 * This class is a generator class for a retrofit service provider. An instance of this retrofit
 * service class is needed for an REST API call.
 *
 * The URL of the server, where the REST API is located, has to be set in this class.
 */
public class TudasServiceGenerator {

    /**
     * URL of the server, where the REST API is located
     */
    private static final String BASE_URL = "http://88.152.247.4/tudas/public/api/";

    /**
     * Deserializer of a calendar object
     */
    private static JsonDeserializer<Calendar> deserializer = ((json, typeOfT, context) -> {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
        Calendar output = Calendar.getInstance();
        try {
            output.setTime(formatter.parse(json.getAsString()));
        } catch(ParseException ignored) {}
        return output;
    });

    /**
     * Have to be set for serializing a calendar object
     */
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Calendar.class, deserializer)
            .create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    /**
     * Returns a retrofit service class of the given REST api interface.
     *
     * @param serviceClass  an interface where the api requests are defined
     * @param <S>           class of the interface
     * @return              the retrofit service class for the given request interface
     */
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
