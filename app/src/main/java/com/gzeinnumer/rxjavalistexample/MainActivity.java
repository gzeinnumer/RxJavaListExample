package com.gzeinnumer.rxjavalistexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainAct_ivity";

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        sample1();
        sample2();
    }

    private void sample1() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }

        Log.d(TAG, "onCreate: " + list.toString());

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Long count) {
                        if (count == list.size() - 1) {
                            onComplete();
                        } else {
                            list.set(count.intValue(), list.get(count.intValue()) + " Manipulasi dengan RX");
                            Log.d(TAG, "onCreate: index " + count.intValue() + " Maniputaled " + list.get(count.intValue()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        countDownLatch.countDown();

                        Log.d(TAG, "onCreate: " + list.toString());
                    }
                });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void sample2() {
        List<MyModel> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(new MyModel(i, String.valueOf(i)));
        }

        Log.d(TAG, "onCreate Before: " + list.toString());

        Observable.fromIterable(list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MyModel, MyModel>() {
                    @Override
                    public MyModel apply(MyModel item) throws Exception {
                        item.setData("Manipulated");
                        return item;
                    }
                })
                .subscribe(new Observer<MyModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MyModel myModel) {
                        myModel.setData("Manipulated");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        Log.d(TAG, "onCreate After: " + list.toString());
                    }
                });
    }
}