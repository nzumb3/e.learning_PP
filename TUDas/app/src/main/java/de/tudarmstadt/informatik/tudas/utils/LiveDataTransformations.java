package de.tudarmstadt.informatik.tudas.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.tudarmstadt.informatik.tudas.model.Appointment;

public class LiveDataTransformations {

    private LiveDataTransformations() {}

    public static <S, T> LiveData<Tuple2<S, T>> ifNotNull(LiveData<S> first, LiveData<T> second) {
        MediatorLiveData<Tuple2<S, T>> mediator = new MediatorLiveData<>();

        mediator.addSource(first, (_first) -> {
            T _second = second.getValue();

            if(_first != null && _second != null)
                mediator.setValue(new Tuple2<>(_first, _second));
        });

        mediator.addSource(second, (_second) -> {
            S _first = first.getValue();

            if(_first != null && _second != null)
                mediator.setValue(new Tuple2<>(_first, _second));
        });

        return mediator;
    }

    public static <S, T, U> LiveData<Tuple3<S, T, U>> ifNotNull(LiveData<S> first, LiveData<T> second, LiveData<U> third) {
        MediatorLiveData<Tuple3<S, T, U>> mediator = new MediatorLiveData<>();

        mediator.addSource(first, (_first) -> {
            T _second = second.getValue();
            U _third = third.getValue();

            if(_first != null && _second != null && _third != null)
                mediator.setValue(new Tuple3<>(_first, _second, _third));
        });

        mediator.addSource(second, (_second) -> {
            S _first = first.getValue();
            U _third = third.getValue();

            if(_first != null && _second != null && _third != null)
                mediator.setValue(new Tuple3<>(_first, _second, _third));
        });

        mediator.addSource(third, (_third) -> {
            S _first = first.getValue();
            T _second = second.getValue();

            if(_first != null && _second != null && _third != null)
                mediator.setValue(new Tuple3<>(_first, _second, _third));
        });

        return mediator;
    }

    public static <S, T, U, V> LiveData<Tuple4<S, T, U, V>> ifNotNull(LiveData<S> first, LiveData<T> second, LiveData<U> third, LiveData<V> fourth) {
        MediatorLiveData<Tuple4<S, T, U, V>> mediator = new MediatorLiveData<>();

        mediator.addSource(first, (_first) -> {
            T _second = second.getValue();
            U _third = third.getValue();
            V _fourth = fourth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null)
                mediator.setValue(new Tuple4<>(_first, _second, _third, _fourth));
        });

        mediator.addSource(second, (_second) -> {
            S _first = first.getValue();
            U _third = third.getValue();
            V _fourth = fourth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null)
                mediator.setValue(new Tuple4<>(_first, _second, _third, _fourth));
        });

        mediator.addSource(third, (_third) -> {
            S _first = first.getValue();
            T _second = second.getValue();
            V _fourth = fourth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null)
                mediator.setValue(new Tuple4<>(_first, _second, _third, _fourth));
        });

        mediator.addSource(fourth, (_fourth) -> {
            S _first = first.getValue();
            T _second = second.getValue();
            U _third = third.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null)
                mediator.setValue(new Tuple4<>(_first, _second, _third, _fourth));
        });

        return mediator;
    }

    public static <S, T, U, V, W> LiveData<Tuple5<S, T, U, V, W>> ifNotNull(LiveData<S> first, LiveData<T> second, LiveData<U> third, LiveData<V> fourth, LiveData<W> fifth) {
        MediatorLiveData<Tuple5<S, T, U, V, W>> mediator = new MediatorLiveData<>();

        mediator.addSource(first, (_first) -> {
            T _second = second.getValue();
            U _third = third.getValue();
            V _fourth = fourth.getValue();
            W _fifth = fifth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null && _fifth != null)
                mediator.setValue(new Tuple5<>(_first, _second, _third, _fourth, _fifth));
        });

        mediator.addSource(second, (_second) -> {
            S _first = first.getValue();
            U _third = third.getValue();
            V _fourth = fourth.getValue();
            W _fifth = fifth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null && _fifth != null)
                mediator.setValue(new Tuple5<>(_first, _second, _third, _fourth, _fifth));
        });

        mediator.addSource(third, (_third) -> {
            S _first = first.getValue();
            T _second = second.getValue();
            V _fourth = fourth.getValue();
            W _fifth = fifth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null && _fifth != null)
                mediator.setValue(new Tuple5<>(_first, _second, _third, _fourth, _fifth));
        });

        mediator.addSource(fourth, (_fourth) -> {
            S _first = first.getValue();
            T _second = second.getValue();
            U _third = third.getValue();
            W _fifth = fifth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null && _fifth != null)
                mediator.setValue(new Tuple5<>(_first, _second, _third, _fourth, _fifth));
        });

        mediator.addSource(fifth, (_fifth) -> {
            S _first = first.getValue();
            T _second = second.getValue();
            U _third = third.getValue();
            V _fourth = fourth.getValue();

            if(_first != null && _second != null && _third != null && _fourth != null && _fifth != null)
                mediator.setValue(new Tuple5<>(_first, _second, _third, _fourth, _fifth));
        });

        return mediator;
    }

    /*public static LiveData<Tuple5> ifNotNull(LiveData<Integer> numDays, LiveData<Calendar> startDate, LiveData<Calendar> earliestBeginning, LiveData<Calendar> latestEnding, List<LiveData<List<Appointment>>> appointments) {
        MediatorLiveData<Tuple5> mediator = new MediatorLiveData<>();

        mediator.addSource(numDays, (_numDays -> {
            Calendar _startDate = startDate.getValue();
            Calendar _earliestBeginning = earliestBeginning.getValue();
            Calendar _latestEnding = latestEnding.getValue();

            if(_numDays != null && _startDate != null && _earliestBeginning != null && _latestEnding != null && !isNull(appointments)) {
                List<List<Appointment>> _appointments = new ArrayList<>();
                for(LiveData<List<Appointment>> listLiveData : appointments)
                    _appointments.add(listLiveData.getValue());
                mediator.setValue(new Tuple5(_numDays, _startDate, _earliestBeginning, _latestEnding, _appointments));
            }
        }));

        mediator.addSource(startDate, (_startDate -> {
            Integer _numDays = numDays.getValue();
            Calendar _earliestBeginning = earliestBeginning.getValue();
            Calendar _latestEnding = latestEnding.getValue();

            if(_numDays != null && _startDate != null && _earliestBeginning != null && _latestEnding != null && !isNull(appointments)) {
                List<List<Appointment>> _appointments = new ArrayList<>();
                for(LiveData<List<Appointment>> listLiveData : appointments)
                    _appointments.add(listLiveData.getValue());
                mediator.setValue(new Tuple5(_numDays, _startDate, _earliestBeginning, _latestEnding, _appointments));
            }
        }));

        mediator.addSource(earliestBeginning, (_earliestBeginning -> {
            Integer _numDays = numDays.getValue();
            Calendar _startDate = startDate.getValue();
            Calendar _latestEnding = latestEnding.getValue();

            if(_numDays != null && _startDate != null && _earliestBeginning != null && _latestEnding != null && !isNull(appointments)) {
                List<List<Appointment>> _appointments = new ArrayList<>();
                for(LiveData<List<Appointment>> listLiveData : appointments)
                    _appointments.add(listLiveData.getValue());
                mediator.setValue(new Tuple5(_numDays, _startDate, _earliestBeginning, _latestEnding, _appointments));
            }
        }));

        mediator.addSource(latestEnding, (_latestEnding -> {
            Integer _numDays = numDays.getValue();
            Calendar _startDate = startDate.getValue();
            Calendar _earliestBeginning = earliestBeginning.getValue();

            if(_numDays != null && _startDate != null && _earliestBeginning != null && _latestEnding != null && !isNull(appointments)) {
                List<List<Appointment>> _appointments = new ArrayList<>();
                for(LiveData<List<Appointment>> listLiveData : appointments)
                    _appointments.add(listLiveData.getValue());
                mediator.setValue(new Tuple5(_numDays, _startDate, _earliestBeginning, _latestEnding, _appointments));
            }
        }));

        for(LiveData<List<Appointment>> listLiveData : appointments) {
            mediator.addSource(listLiveData, (_listLiveData -> {
                Integer _numDays = numDays.getValue();
                Calendar _startDate = startDate.getValue();
                Calendar _earliestBeginning = earliestBeginning.getValue();
                Calendar _latestEnding = latestEnding.getValue();

                if(_numDays != null && _startDate != null && _earliestBeginning != null && _latestEnding != null && !isNull(appointments)) {
                    List<List<Appointment>> _appointments = new ArrayList<>();
                    for(LiveData<List<Appointment>> listLiveDataTmp : appointments)
                        _appointments.add(listLiveDataTmp.getValue());
                    mediator.setValue(new Tuple5(_numDays, _startDate, _earliestBeginning, _latestEnding, _appointments));
                }
            }));
        }

        return mediator;
    }*/

    private static boolean isNull(List<LiveData<List<Appointment>>> appointments) {
        for(LiveData<List<Appointment>> listLiveData : appointments) {
            if(listLiveData.getValue() == null)
                return false;
        }

        return true;
    }

    /*public static class Tuple5 {
        public final Integer numDays;

        public final Calendar startDate;

        public final Calendar earliestBeginning;

        public final Calendar latestEnding;

        public final List<List<Appointment>> appointments;

        public Tuple5(Integer numDays, Calendar startDate, Calendar earliestBeginning, Calendar latestEnding, List<List<Appointment>> appointments) {
            this.numDays = numDays;
            this.startDate = startDate;
            this.earliestBeginning = earliestBeginning;
            this.latestEnding = latestEnding;
            this.appointments = appointments;
        }
    }*/

    public static class Tuple2<S, T> {
        public final S first;

        public final T second;

        public Tuple2(S first, T second) {
            this.first = first;
            this.second = second;
        }
    }

    public static class Tuple3<S, T, U> {
        public final S first;

        public final T second;

        public final U third;

        public Tuple3(S first, T second, U third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

    public static class Tuple4<S, T, U, V> {
        public final S first;

        public final T second;

        public final U third;

        public final V fourth;

        public Tuple4(S first, T second, U third, V fourth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
        }
    }

    public static class Tuple5<S, T, U, V, W> {
        public final S first;

        public final T second;

        public final U third;

        public final V fourth;

        public final W fifth;

        public Tuple5(S first, T second, U third, V fourth, W fifth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
            this.fifth = fifth;
        }
    }

    /*public class Tuple5<LD1, LD2, LD3, LD4, LD5> {
        public final LD1 first;

        public final LD2 second;

        public final LD3 third;

        public final LD4 fourth;

        public final LD5 fifth;

        public Tuple5(LD1 first, LD2 second, LD3 third, LD4 fourth, LD5 fifth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
            this.fifth = fifth;
        }
    }*/
}
