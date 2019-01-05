package de.tudarmstadt.informatik.tudas.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("users")
    public Call<List<User>> getUsers();
}
