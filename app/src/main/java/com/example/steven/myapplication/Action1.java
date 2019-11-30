package com.example.steven.myapplication;

import rx.Observer;

public abstract class Action1<T> implements Observer<T> {

    abstract void call(T t);

    @Override
    public void onNext(T t) {
        call(t);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
}
