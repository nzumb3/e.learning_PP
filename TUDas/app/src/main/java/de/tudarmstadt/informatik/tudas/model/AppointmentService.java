package de.tudarmstadt.informatik.tudas.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppointmentService {

    /*@GET("appointments/{label}/{date}")
    Call<List<Appointment>> getAppointments(@Path("label") String label, @Path("date") String date);*/

    @GET("appointments/{date}/{labels}")
    Call<List<Appointment>> getAppointments(@Path("date") String date, @Path(value = "labels", encoded = true) String labels);
}
