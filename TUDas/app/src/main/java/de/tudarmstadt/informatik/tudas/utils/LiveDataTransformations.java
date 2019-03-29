package de.tudarmstadt.informatik.tudas.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

/**
 * This class is a utility class to simplify the usage of LiveData.
 * The methods combine multiple LiveDatas as sources for a MediatorLiveData.
 * For this purpose the class TupleX are used, where X is the number of LiveDatas that shall be
 * combined. The methods ifNotNull return a LiveData wrapping a tuple with the values of the
 * LiveDatas to be combined. The methods assure, that the resulting values of the tuple are not
 * null. The returned LiveData fires when one of the to be combined LiveData is changed.
 */
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

    public static class Tuple2<S, T> {
        public final S first;

        public final T second;

        Tuple2(S first, T second) {
            this.first = first;
            this.second = second;
        }
    }

    public static class Tuple3<S, T, U> {
        public final S first;

        public final T second;

        public final U third;

        Tuple3(S first, T second, U third) {
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

        Tuple4(S first, T second, U third, V fourth) {
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

        Tuple5(S first, T second, U third, V fourth, W fifth) {
            this.first = first;
            this.second = second;
            this.third = third;
            this.fourth = fourth;
            this.fifth = fifth;
        }
    }
}
