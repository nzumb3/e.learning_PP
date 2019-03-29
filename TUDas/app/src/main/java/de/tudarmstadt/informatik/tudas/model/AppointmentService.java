package de.tudarmstadt.informatik.tudas.model;

import java.util.List;

import de.tudarmstadt.informatik.tudas.repositories.DataRepository;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * This interface provides the calls, that are sent to the REST API on the shared database.
 */
public interface AppointmentService {

    /**
     * Returns a list of appointments at the given date, that are related to the given labels.
     * The label parameter is a string that contains all labels concatenated together separated by a
     * "/". If you want to get all appointments for the label "foo" and the label "bar", the label
     * string would be "foo/bar".
     *
     * @param date      the date, where the appointments are queried
     * @param labels    the labels, the appointments shall be related to
     * @return          a call object with the resulting list
     */
    @GET("appointments/{date}/{labels}")
    Call<List<Appointment>> getAppointments(@Path("date") String date, @Path(value = "labels", encoded = true) String labels);

    /**
     * Returns a wrapper class for a boolean value, that is true, if the given label exists in the
     * shared database.
     *
     * @param label the label, that's existence in the database should be checked
     * @return      a call object with the boolean wrapper
     */
    @GET("labelExists/{label}")
    Call<DataRepository.BooleanResult> labelExists(@Path("label") String label);
}
