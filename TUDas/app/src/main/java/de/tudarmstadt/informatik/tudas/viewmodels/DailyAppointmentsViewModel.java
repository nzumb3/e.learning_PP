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
import timber.log.Timber;

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
        //return repository.getAppointmentsForDay(day);
        //return repository.getDailyAppointments(day);
    }

    public LiveData<List<String>> getLabels() {
        return repository.getLabels();
    }

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }

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

            //String[] selectionArgs = new String[]{Long.toString(CalendarConverter.fromString("2019-03-15T23:59:00").getTimeInMillis()), Long.toString(CalendarConverter.fromString("2019-03-15T00:00:00").getTimeInMillis())};


            cur = cr.query(uri, mProjection, selection, selectionArgs, null);

            if(cur != null) {
                //Timber.d("MyLog: numRows = " + cur.getCount());
                appointment.setOverlap(cur.getCount() > 0);
                cur.close();
            }

            /*if(cur != null) {
                Timber.d("MyLog: Drin");
                while (cur.moveToNext()) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTSTART))));
                    Timber.d("MyLog: New:" + cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE)) + "(" + CalendarConverter.fromCalendar(calendar) + ")");
                }
            }*/
        }



        /*if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, 1);
        }*/
    }

    public void setPermissionStatus(int permissionStatus) {
        this.permissionStatus.setValue(permissionStatus);
    }

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

    public LiveData<Calendar> getCurrentDate() {
        return currentDate;
    }
}