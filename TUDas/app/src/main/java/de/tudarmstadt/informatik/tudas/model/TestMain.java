package de.tudarmstadt.informatik.tudas.model;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestMain {

    public static void main(String[] args) {
        AppointmentService service = TudasServiceGenerator.createService(AppointmentService.class);
        Call<List<Appointment>> callAsync = service.getAppointments("2019-03-15", "test/bla");

        callAsync.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                System.out.println(call.request());
                List<Appointment> appointments = response.body();
                System.out.println("Drin: " + appointments);
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                System.out.println("Request: " + call.request());
                System.out.println("Fehler: " + t);
            }
        });
    }
}
