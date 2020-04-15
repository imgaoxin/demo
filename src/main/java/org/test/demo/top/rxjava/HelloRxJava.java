package org.test.demo.top.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HelloRxJava {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("Hello!");
                emitter.onNext("RxJava!");
            }
        });

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(Thread.currentThread().getName() + " - consumer - " + s);
            }
        };

        // observable.subscribe(consumer);
        observable.observeOn(Schedulers.newThread()).subscribe(consumer);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println(Thread.currentThread().getName() + " - onSubscribe - " + d.toString());
            }

            @Override
            public void onNext(String s) {
                System.out.println(Thread.currentThread().getName() + " - observer - " + s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(Thread.currentThread().getName() + " - onError - " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println(Thread.currentThread().getName() + " - onComplete");
            }
        };

        observable.subscribe(observer);
        observer.onComplete();

        // try {
        // Thread.sleep(2000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
    }
}