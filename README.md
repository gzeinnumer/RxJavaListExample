# RxJavaListExample

```
implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
implementation 'io.reactivex.rxjava2:rxjava:2.2.9'
```

- With Delay
```java
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
```

- Without Delay
```java
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
```

---

```
Copyright 2021 M. Fadli Zein
```
