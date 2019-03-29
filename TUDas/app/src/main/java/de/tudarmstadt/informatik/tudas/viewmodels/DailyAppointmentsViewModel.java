package de.tudarmstadt.informatik.tudas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;
import de.tudarmstadt.informatik.tudas.utils.LiveDataTransformations;

public class DailyAppointmentsViewModel extends AndroidViewModel {
    private DataRepository repository;

    private MutableLiveData<Integer> permissionStatus;

    private MutableLiveData<Calendar> currentDate;

    public DailyAppointmentsViewModel(Application application) {
        super(application);

        repository = new DataRepository(application);

        permissionStatus = new MutableLiveData<>();
        permissionStatus.setValue(PackageManager.PERMISSION_DENIED);

        currentDate = new MutableLiveData<>();
        Calendar _startDate = Calendar.getInstance();
        _startDate.set(Calendar.HOUR_OF_DAY, 0);
        _startDate.set(Calendar.MINUTE, 0);
        _startDate.set(Calendar.SECOND, 0);
        _startDate.set(Calendar.MILLISECOND, 0);
        currentDate.setValue(_startDate);
    }

    /**
     * Returns the appointments for the current startdate from the local database and from the
     * server.
     */
    public LiveData<List<Appointment>> getAppointmentsForDay() {
        LiveData<List<Appointment>> appointmentsForDay = Transformations.switchMap(currentDate, (calendar) -> repository.getDailyAppointments(CalendarConverter.fromCalendar(calendar)));
        LiveData<LiveDataTransformations.Tuple2<Integer, List<Appointment>>> intermediate = LiveDataTransformations.ifNotNull(permissionStatus, appointmentsForDay);
        return Transformations.map(intermediate, (tuple) -> {
            int permission = tuple.first;
            List<Appointment> appointments = tuple.second;

            if(permission == PackageManager.PERMISSION_GRANTED)
                checkOverlap(appointments);

            return appointments;
        });
    }

    /**
     * Returns the labels from the database.
     */
    public LiveData<List<String>> getLabels() {
        return repository.getLabels();
    }

    /**
     * Checks if one of the given appointments overlaps with an event in another calendar app, that
     * is installed on the user's device. If there is an overlap, it sets a flag in the appointment,
     * so it can be marked in the view.
     */
    private void checkOverlap(List<Appointment> appointments) {
        for(Appointment appointment : appointments) {
            Cursor cur;
            ContentResolver cr = getApplication().getContentResolver();

            String[] mProjection = {"_id", CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};

            Uri uri = CalendarContract.Events.CONTENT_URI;

            String selection = CalendarContract.Events.DTSTART + " <= ? AND " + CalendarContract.Events.DTEND + " >= ?";

            String[] selectionArgs = new String[]{
                    Long.toString(appointment.getEndDate().getTimeInMillis()),
                    Long.toString(appointment.getStartDate().getTimeInMillis())
            };

            cur = cr.query(uri, mProjection, selection, selectionArgs, null);

            if(cur != null) {
                appointment.setOverlap(cur.getCount() > 0);
                cur.close();
            }
        }
    }

    /**
     * Sets the permission status, if the user permits the access on the installed calendar apps.
     */
    public void setPermissionStatus(int permissionStatus) {
        this.permissionStatus.setValue(permissionStatus);
    }

    /**
     * Returns an information code for the current state of the viewModel for showing some
     * informations in the view depending on the current state.
     * There are the following states:
     * 0: There are appointments for the current date
     * 1: There are no appointments for the current date, but the user has labels saved
     * 2: There are no appointments for the current date and the user hasn't saved any labels
     */
    public LiveData<Integer> getInformationCode() {
        LiveData<LiveDataTransformations.Tuple2<List<String>, List<Appointment>>> intermediate = LiveDataTransformations.ifNotNull(repository.getLabels(), getAppointmentsForDay());
        return Transformations.map(intermediate, (tuple) -> {
            if(tuple.second.isEmpty()) {
                if(tuple.first.isEmpty())
                    return 2;
                return 1;
            }
            return 0;
        });
    }

    /**
     * Returns the current date.
     */
    public LiveData<Calendar> getCurrentDate() {
        return currentDate;
    }
}