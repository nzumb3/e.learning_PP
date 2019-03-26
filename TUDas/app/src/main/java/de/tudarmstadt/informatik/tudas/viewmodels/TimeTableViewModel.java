package de.tudarmstadt.informatik.tudas.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;
import de.tudarmstadt.informatik.tudas.model.AppointmentContent;
import de.tudarmstadt.informatik.tudas.repositories.DataRepository;
import de.tudarmstadt.informatik.tudas.utils.CalendarConverter;
import de.tudarmstadt.informatik.tudas.utils.LiveDataTransformations;

public class TimeTableViewModel extends AndroidViewModel {

    private LiveData<List<List<Appointment>>> appointmentsForView;

    private MutableLiveData<Calendar> startDate;

    private MutableLiveData<Integer> numDays;

    private LiveData<List<Calendar>> timeSlots;

    private DataRepository repository;

    public static final int PIXEL_PER_MINUTE = 3;

    public TimeTableViewModel(Application application) {
        super(application);

        //Decomment if Log is needed
        //Timber.plant(new Timber.DebugTree());

        repository = new DataRepository(application);

        startDate = new MutableLiveData<>();
        Calendar _startDate = Calendar.getInstance();
        //_startDate.set(2018, Calendar.DECEMBER, 25, 0, 0, 0);
        //_startDate.set(Calendar.MILLISECOND, 0);
        startDate.setValue(_startDate);
    }

    public void setNumDays(int num){
        numDays = new MutableLiveData<>();
        numDays.setValue(num);
        LiveData<LiveDataTransformations.Tuple2<Calendar, Integer>> intermediate = LiveDataTransformations.ifNotNull(startDate, numDays);

        LiveData<List<Appointment>> appointmentsInPeriod = Transformations.switchMap(intermediate, (tuple) -> repository.getAppointmentsInPeriod(CalendarConverter.fromCalendar(tuple.first), CalendarConverter.fromCalendar(getEndDate(tuple.first, tuple.second))));

        LiveData<LiveDataTransformations.Tuple3<Calendar, Integer, List<Appointment>>> intermediate2 = LiveDataTransformations.ifNotNull(startDate, numDays, appointmentsInPeriod);

        LiveData<List<List<Appointment>>> appointmentsFromDatabase = Transformations.map(intermediate2, (tuple) -> {
            Calendar startDate = (Calendar) tuple.first.clone();
            int numDays = tuple.second;
            List<Appointment> _appointmentsInPeriod = tuple.third;

            List<List<Appointment>> output = new LinkedList<>();

            for(int dayOffset = 0; dayOffset < numDays; dayOffset++) {
                List<Appointment> listForDay = new ArrayList<>();
                for(Appointment appointment : _appointmentsInPeriod) {
                    if(onSameDay(appointment.getStartDate(), startDate) || onSameDay(appointment.getEndDate(), startDate) || (getDate(appointment.getStartDate()).compareTo(startDate) < 0 && getDate(appointment.getEndDate()).compareTo(startDate) > 0)) {
                        listForDay.add(appointment);
                    }
                }
                output.add(listForDay);
                startDate.add(Calendar.DATE, 1);
            }

            return output;
        });

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

            for(Appointment appointment : tuple.third) {
                if(getDate(appointment.getStartDate()).compareTo(tuple.first) >= 0 && !appointment.atSameDay()) {
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

            return output;
        });

        LiveData<LiveDataTransformations.Tuple2<Calendar, Calendar>> intermediate4 = LiveDataTransformations.ifNotNull(earliestBeginning, latestEnding);

        timeSlots = Transformations.map(intermediate4, (tuple) -> {
            Calendar firstHour = tuple.first;
            Calendar lastHour = tuple.second;

            int hourOffset = lastHour.get(Calendar.HOUR_OF_DAY) == 0 ? 24 - firstHour.get(Calendar.HOUR_OF_DAY) : lastHour.get(Calendar.HOUR_OF_DAY) - firstHour.get(Calendar.HOUR_OF_DAY);

            List<Calendar> output = new ArrayList<>();

            output.add(firstHour);

            for(int hour = 1; hour < hourOffset; hour++) {
                Calendar hourToAdd = (Calendar) firstHour.clone();
                hourToAdd.set(Calendar.HOUR_OF_DAY, firstHour.get(Calendar.HOUR_OF_DAY) + hour);
                output.add(hourToAdd);
            }

            return output;
        });

        LiveData<LiveDataTransformations.Tuple5<Calendar, Integer, Calendar, Calendar, List<List<Appointment>>>> intermediate3 = LiveDataTransformations.ifNotNull(startDate, numDays, earliestBeginning, latestEnding, appointmentsFromDatabase);

        appointmentsForView = Transformations.map(intermediate3, (tuple) -> {
            Calendar startDate = tuple.first;
            Integer numDays = tuple.second;
            Calendar _earliestBeginning = tuple.third;
            Calendar _latestEnding = tuple.fourth;
            List<List<Appointment>> _appointmentsFromDatabase = tuple.fifth;

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

            return output;
        });
    }

    public LiveData<Calendar> getStartDate() {
        return startDate;
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

    public void setStartDate(Calendar startDate) {
        this.startDate.setValue(startDate);
    }

    public LiveData<String> getPeriodString() {
        LiveData<LiveDataTransformations.Tuple2<Calendar, Integer>> intermediate = LiveDataTransformations.ifNotNull(startDate, numDays);

        return Transformations.map(intermediate, tuple -> {
            Calendar endDate = getEndDate(tuple.first, tuple.second);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd. MMM");

            return format.format(tuple.first.getTime()) + " - " + format.format(endDate.getTime());
        });
    }

    public LiveData<List<Calendar>> getTimeSlots() {
        return timeSlots;
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

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }

    private static boolean onSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DATE) == date2.get(Calendar.DATE);
    }

    private static int getDaysBetweenDates(Calendar date1, Calendar date2) {
        return (int) (date2.getTimeInMillis() - date1.getTimeInMillis()) / 1000 / 60 / 60 / 24;
    }

    public static String getComplementaryColor(String hexColor) {
        if(hexColor.indexOf('#') == 0)
            hexColor = hexColor.substring(1);

        if(hexColor.length() == 3)
            hexColor = (new StringBuilder()).append(hexColor.charAt(0)).append(hexColor.charAt(0)).append(hexColor.charAt(1)).append(hexColor.charAt(1)).append(hexColor.charAt(2)).append(hexColor.charAt(2)).toString();

        int r = Integer.parseInt(hexColor.substring(0, 2), 16), g = Integer.parseInt(hexColor.substring(2, 4), 16), b = Integer.parseInt(hexColor.substring(4, 6), 16);

        return (r * 0.299 + g * 0.587 + b * 0.114) > 186 ? "#000000" : "#FFFFFF";
    }
}
