package de.tudarmstadt.informatik.tudas.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestMain {

    public static void main(String[] args) {
        UserService service = TudasServiceGenerator.createService(UserService.class);
        Call<List<User>> callAsync = service.getUsers();

        callAsync.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                System.out.println(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
