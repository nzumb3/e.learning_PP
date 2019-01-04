package de.tudarmstadt.informatik.tudas.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class AppointmentViewModel extends AndroidViewModel {

    private AppRepository repository;

    private LiveData<Calendar> earliestBeginning;

    private LiveData<Calendar> latestEnding;

    public static final int pixelPerMinute = 3;

    public AppointmentViewModel(Application application) {
        super(application);
        repository = new AppRepository(application);
        earliestBeginning = new MutableLiveData<>();
        latestEnding = new MutableLiveData<>();
    }

    public void setEarliestBeginning(String startDate, String endDate) {
        earliestBeginning = Transformations.map(repository.getAppointmentsInPeriod(startDate, endDate), (appointments -> {
            Calendar output = getDate(CalendarConverter.fromString(startDate));

            if(appointments != null && !appointments.isEmpty()) {
                for(Appointment appointment : appointments) {
                    if(appointment.atSameDay()) {
                        output.set(Calendar.HOUR_OF_DAY, 0);
                        break;
                    } else {
                        int newHour = Math.max(0, appointment.getStartDate().get(Calendar.HOUR_OF_DAY) - 1);
                        if(newHour < output.get(Calendar.HOUR_OF_DAY))
                            output.set(Calendar.HOUR_OF_DAY, newHour);
                    }
                }
            }

            return output;
        }));
    }

    public void setLatestEnding(String startDate, String endDate) {
        latestEnding = Transformations.map(repository.getAppointmentsInPeriod(startDate, endDate), (appointments -> {
            Calendar start = getDate(CalendarConverter.fromString(startDate));
            Calendar output = getDate(CalendarConverter.fromString(endDate));
            output.add(Calendar.DATE, 1);

            if(appointments != null && !appointments.isEmpty()) {
                for(Appointment appointment : appointments) {
                    if(getDate(appointment.getStartDate()).compareTo(start) >= 0 && !appointment.atSameDay()) {
                        output.set(Calendar.HOUR_OF_DAY, 0);
                        break;
                    } else {
                        int newHour = Math.min(24, appointment.getEndDate().get(Calendar.HOUR_OF_DAY + 1)) % 24;
                        if (newHour == 0) {
                            output.set(Calendar.HOUR_OF_DAY, 0);
                            break;
                        } else if (newHour > output.get(Calendar.HOUR_OF_DAY))
                            output.set(Calendar.HOUR_OF_DAY, newHour);
                    }
                }
            }

            return output;
        }));
    }

    public LiveData<List<Appointment>> getAppointmentsForDay(String day) {
        LiveData<List<Appointment>> appointmentsFromDatabase = repository.getAppointmentsForDay(day);

        MediatorLiveData<List<Appointment>> appointmentsForView = new MediatorLiveData<>();
        appointmentsForView.addSource(appointmentsFromDatabase, (appointments -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && latestEnding != null && latestEnding.getValue() != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue(), latestEnding.getValue(), day));
            }
        }));
        appointmentsForView.addSource(earliestBeginning, (calendar -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && latestEnding != null && latestEnding.getValue() != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue(), latestEnding.getValue(), day));
            }
        }));
        appointmentsForView.addSource(latestEnding, (calendar -> {
            if(earliestBeginning != null && earliestBeginning.getValue() != null && latestEnding != null && latestEnding.getValue() != null && appointmentsFromDatabase.getValue() != null && !appointmentsFromDatabase.getValue().isEmpty()) {
                appointmentsForView.setValue(fillList(appointmentsFromDatabase.getValue(), earliestBeginning.getValue(), latestEnding.getValue(), day));
            }
        }));
        return appointmentsForView;
    }

    public LiveData<List<Calendar>> getTimeSlots() {
        return Transformations.map(earliestBeginning, (calendar -> {
            List<Calendar> output = new ArrayList<>();
            if(earliestBeginning != null && earliestBeginning.getValue() != null) {
                Calendar firstHour = getMaxTimeBeforeAppointment(earliestBeginning.getValue());
                output.add(firstHour);
                for(int hour = firstHour.get(Calendar.HOUR_OF_DAY) + 1; hour < 24; hour++) {
                    Calendar hourToAdd = (Calendar) firstHour.clone();
                    hourToAdd.set(Calendar.HOUR_OF_DAY, hour);
                    output.add(hourToAdd);
                }
            }
            return output;
        }));
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

    public void insert(AppointmentContent content, Appointment... appointments) {
        repository.insert(content, appointments);
    }

    private static long diff(Calendar first, Calendar second) {
        return second.getTimeInMillis() - first.getTimeInMillis();
    }

    private static Calendar getMaxTimeBeforeAppointment(Calendar earliestBeginning) {
        Calendar output = (Calendar) earliestBeginning.clone();
        output.set(Calendar.MINUTE, 0);
        output.set(Calendar.HOUR_OF_DAY, Math.max(0, earliestBeginning.get(Calendar.HOUR_OF_DAY) - 1));
        return output;
    }

    public LiveData<Calendar> getEarliestBeginning() {
        return earliestBeginning;
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

    public static String getComplementaryColor(String hexColor) {
        if(hexColor.indexOf('#') == 0)
            hexColor = hexColor.substring(1);

        if(hexColor.length() == 3)
            hexColor = (new StringBuilder()).append(hexColor.charAt(0)).append(hexColor.charAt(0)).append(hexColor.charAt(1)).append(hexColor.charAt(1)).append(hexColor.charAt(2)).append(hexColor.charAt(2)).toString();

        int r = Integer.parseInt(hexColor.substring(0, 2), 16), g = Integer.parseInt(hexColor.substring(2, 4), 16), b = Integer.parseInt(hexColor.substring(4, 6), 16);

        return (r * 0.299 + g * 0.587 + b * 0.114) > 186 ? "#000000" : "#FFFFFF";
    }
}