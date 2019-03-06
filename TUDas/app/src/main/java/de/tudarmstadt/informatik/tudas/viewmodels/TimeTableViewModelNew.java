package de.tudarmstadt.informatik.tudas.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;
import de.tudarmstadt.informatik.tudas.utils.LiveDataTransformations;
import timber.log.Timber;

public class TimeTableViewModelNew extends AndroidViewModel {

    private LiveData<List<List<Appointment>>> appointmentsForView;

    private MutableLiveData<Calendar> startDate;

    private MutableLiveData<Integer> numDays;

    public TimeTableViewModelNew(Application application) {
        super(application);

        DataRepository repository = new DataRepository(application);

        startDate = new MutableLiveData<>();
        Calendar _startDate = Calendar.getInstance();
        _startDate.set(2018, Calendar.DECEMBER, 25, 0, 0, 0);
        startDate.setValue(_startDate);

        numDays = new MutableLiveData<>();
        numDays.setValue(2);

        LiveData<LiveDataTransformations.Tuple2<Calendar, Integer>> intermediate = LiveDataTransformations.ifNotNull(startDate, numDays);

        MediatorLiveData<List<List<Appointment>>> appointmentsFromDatabase = new MediatorLiveData<>();

        appointmentsFromDatabase.addSource(intermediate, (tuple) -> {
            Calendar startDate = tuple.first;
            int _numDays = tuple.second;

            List<List<Appointment>> appointments = new LinkedList<>();

            for(int dayOffset = 0; dayOffset < _numDays; dayOffset++) {
                Timber.d("MyLog: dayOffset = " + dayOffset);
                appointments.add(new ArrayList<>());
                Calendar day = (Calendar) startDate.clone();
                day.add(Calendar.DATE, dayOffset);
                appointmentsFromDatabase.addSource(repository.getAppointmentsForDay(CalendarConverter.toDateString(day)), new MyObserver(dayOffset, startDate, appointments, appointmentsFromDatabase));
                /*appointmentsFromDatabase.addSource(repository.getAppointmentsForDay(CalendarConverter.toDateString(day)), (_appointments) -> {
                    Calendar tempDay = (Calendar) day.clone();
                    if(_appointments != null) {
                        Calendar date1 = Calendar.getInstance();
                        date1.set(2018, 11, 25, 0, 0, 0);
                        Calendar date2 = Calendar.getInstance();
                        date2.set(2018, 11, 26, 0, 0, 0);
                        Timber.d("MyLog: " + CalendarConverter.fromCalendar(startDate) + " day = " + CalendarConverter.fromCalendar(getDate(tempDay)) + ", days between = " + getDaysBetweenDates(startDate, getDate(tempDay)));
                        appointments.remove(getDaysBetweenDates(startDate, getDate(day)));
                        appointments.add(getDaysBetweenDates(startDate, getDate(day)), _appointments);
                        appointmentsFromDatabase.setValue(appointments);
                    }
                });*/
            }
        });

        LiveData<List<Appointment>> appointmentsInPeriod = Transformations.switchMap(intermediate, (tuple) -> repository.getAppointmentsInPeriod(CalendarConverter.fromCalendar(tuple.first), CalendarConverter.fromCalendar(getEndDate(tuple.first, tuple.second))));

        LiveData<LiveDataTransformations.Tuple3<Calendar, Integer, List<Appointment>>> intermediate2 = LiveDataTransformations.ifNotNull(startDate, numDays, appointmentsInPeriod);

        LiveData<Calendar> earliestBeginning = Transformations.map(intermediate2, (tuple) -> {
            Calendar output = (Calendar) tuple.first.clone();

            int hour = Integer.MAX_VALUE;

            for(Appointment appointment : tuple.third) {
                if(!appointment.atSameDay() && getDate(appointment.getEndDate()).compareTo(getEndDate(tuple.first, tuple.second)) <= 0) {
                    hour = 0;
                    output.set(Calendar.HOUR_OF_DAY, 0);
                    break;
                } else
                    hour = Math.min(hour, Math.max(0, appointment.getStartDate().get(Calendar.HOUR_OF_DAY) - 1));
            }

            output.set(Calendar.HOUR_OF_DAY, hour != Integer.MAX_VALUE ? hour : 0);

            return output;
        });

        LiveData<Calendar> latestEnding = Transformations.map(intermediate2, (tuple) -> {
            Calendar output = (Calendar) getEndDate(tuple.first, tuple.second).clone();

            int hour = Integer.MIN_VALUE;

            Timber.d("MyLog: " + tuple.third.isEmpty());

            for(Appointment appointment : tuple.third) {
                Timber.d("MyLog: " + appointment.getStartDate().compareTo(tuple.first));
                if(getDate(appointment.getStartDate()).compareTo(tuple.first) >= 0 && !appointment.atSameDay()) {
                    Timber.d("MyLog: LatestEnding to null");
                    hour = 0;
                    break;
                } else {
                    int newHour = Math.min(24, appointment.getEndDate().get(Calendar.HOUR_OF_DAY) + 1) % 24;
                    if (newHour == 0) {
                        hour = 0;
                        break;
                    } else
                        hour = Math.max(hour, newHour);
                }
            }

            output.set(Calendar.HOUR_OF_DAY, hour);

            Timber.d("MyLog: LatestEnding = " + CalendarConverter.fromCalendar(output));

            return output;
        });

        LiveData<LiveDataTransformations.Tuple5<Calendar, Integer, Calendar, Calendar, List<List<Appointment>>>> intermediate3 = LiveDataTransformations.ifNotNull(startDate, numDays, earliestBeginning, latestEnding, appointmentsFromDatabase);

        appointmentsForView = Transformations.map(intermediate3, (tuple) -> {
            Calendar startDate = tuple.first;
            Integer numDays = tuple.second;
            Calendar _earliestBeginning = tuple.third;
            Calendar _latestEnding = tuple.fourth;
            List<List<Appointment>> _appointmentsFromDatabase = tuple.fifth;

            //Timber.d("MyLog: appointments " + _appointmentsFromDatabase);

            List<List<Appointment>> output = new LinkedList<>();

            for(int dayOffset = 0; dayOffset < numDays; dayOffset++) {
                Calendar day = (Calendar) startDate.clone();
                day.add(Calendar.DATE, dayOffset);
                String dayString = CalendarConverter.toDateString(day);
                if(_appointmentsFromDatabase.get(dayOffset).isEmpty())
                    output.add(new ArrayList<>());
                else
                    output.add(fillList(_appointmentsFromDatabase.get(dayOffset), _earliestBeginning, _latestEnding, dayString));
            }

            Timber.d("MyLog: " + output);
            return output;
        });
    }

    private static List<Appointment> fillList(List<Appointment> appointments, Calendar earliestBeginning, Calendar latestEnding, String day) {
        Calendar requestedDay = getDate(day);

        AppointmentContent emptyContent = new AppointmentContent();
        emptyContent.setRoom("");
        emptyContent.setAbbreviation("");
        emptyContent.setDescription("");
        emptyContent.setTitle("");
        emptyContent.setColor("#FFFFFF");

        List<Appointment> outputAppointments = new LinkedList<>();
        List<Appointment> outerAppointments = new LinkedList<>();

        earliestBeginning = (Calendar) earliestBeginning.clone();
        earliestBeginning.set(Calendar.DATE, requestedDay.get(Calendar.DATE));
        earliestBeginning.set(Calendar.MONTH, requestedDay.get(Calendar.MONTH));
        earliestBeginning.set(Calendar.YEAR, requestedDay.get(Calendar.YEAR));

        int minutes;
        int position = 0;

        while(appointments.size() > position && getDate(appointments.get(position).getStartDate()).compareTo(requestedDay) < 0) {
            if(getDate(appointments.get(position).getEndDate()).compareTo(requestedDay) > 0)
                outerAppointments.add(appointments.get(position));
            else
                outputAppointments.add(getAppointment(getDate(appointments.get(position).getEndDate()), appointments.get(position).getEndDate(), appointments.get(position).getAppointmentContent()));
            position++;
        }

        if(outputAppointments.isEmpty()) {
            minutes = Appointment.millisToMinute(diff(earliestBeginning, appointments.get(position).getStartDate()));
            if (minutes > 0) {
                outputAppointments.add(getAppointment(earliestBeginning, appointments.get(position).getStartDate(), emptyContent));
            }
        }

        if(appointments.size() > position)
            outputAppointments.add(appointments.get(position));

        for(int i = position + 1; i < appointments.size(); i++) {
            minutes = Appointment.millisToMinute(diff(appointments.get(i - 1).getEndDate(), appointments.get(i).getStartDate()));
            if(minutes > 0) {
                outputAppointments.add(getAppointment(appointments.get(i - 1).getEndDate(), appointments.get(i).getStartDate(), emptyContent));
            }

            outputAppointments.add(appointments.get(i));
        }

        latestEnding = (Calendar) latestEnding.clone();
        latestEnding.set(Calendar.MONTH, requestedDay.get(Calendar.MONTH));
        latestEnding.set(Calendar.YEAR, requestedDay.get(Calendar.YEAR));
        if(latestEnding.get(Calendar.HOUR_OF_DAY) == 0 && latestEnding.get(Calendar.MINUTE) == 0)
            latestEnding.set(Calendar.DATE, requestedDay.get(Calendar.DATE) + 1);
        else
            latestEnding.set(Calendar.DATE, requestedDay.get(Calendar.DATE));

        if(outputAppointments.get(outputAppointments.size() - 1).atSameDay())
            outputAppointments.add(getAppointment(outputAppointments.get(outputAppointments.size() - 1).getEndDate(), latestEnding, emptyContent));

        return outputAppointments;
    }

    private static Appointment getAppointment(Calendar startDate, Calendar endDate, AppointmentContent content) {
        Appointment output = new Appointment();
        output.setStartDate(startDate);
        output.setEndDate(endDate);
        output.setAppointmentContent(content);
        return output;
    }

    private static long diff(Calendar first, Calendar second) {
        return second.getTimeInMillis() - first.getTimeInMillis();
    }

    public LiveData<List<List<Appointment>>> getAppointments() {
        return appointmentsForView;
    }

    private static Calendar getEndDate(Calendar startDate, int numDays) {
        Calendar endDate = (Calendar) startDate.clone();
        endDate.add(Calendar.DATE, numDays - 1);
        return endDate;
    }

    private static Calendar getDate(String date) {
        return CalendarConverter.fromString(date + "T00:00:00");
    }

    private static Calendar getDate(Calendar calendar) {
        Calendar output = (Calendar) calendar.clone();
        output.set(Calendar.HOUR_OF_DAY, 0);
        output.set(Calendar.MINUTE, 0);
        output.set(Calendar.SECOND, 0);
        output.set(Calendar.MILLISECOND, 0);
        return output;
    }

    private static int getDaysBetweenDates(Calendar date1, Calendar date2) {
        return (int) (date2.getTimeInMillis() - date1.getTimeInMillis()) / 1000 / 60 / 60 / 24;
    }

    private class MyObserver implements Observer<List<Appointment>> {
        private int day;
        private Calendar startDate;
        private List<List<Appointment>> appointments;
        private MediatorLiveData<List<List<Appointment>>> appointmentsFromDatabase;

        public MyObserver(int day, Calendar startDate, List<List<Appointment>> appointments, MediatorLiveData<List<List<Appointment>>> liveData) {
            this.day = day;
            this.startDate = startDate;
            this.appointments = appointments;
            appointmentsFromDatabase = liveData;
        }

        @Override
        public void onChanged(@Nullable List<Appointment> appointments) {
            if(appointments != null) {
                this.appointments.remove(day);
                this.appointments.add(day, appointments);
                appointmentsFromDatabase.setValue(this.appointments);
            }
        }
    }
}
