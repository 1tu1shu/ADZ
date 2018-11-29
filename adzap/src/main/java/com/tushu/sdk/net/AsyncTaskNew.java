package com.tushu.sdk.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by A03 on 2018/3/24.
 */

public abstract class AsyncTaskNew<T> {

    private static final int CORE_POOL_SIZE = 20;
    private static final int MAXIMUM_POOL_SIZE = 40;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<>(128);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);

    public void execute(String... jsonStr) {
        new android.os.AsyncTask<String, Void, T>() {
            @Override
            protected void onPreExecute() {
                AsyncTaskNew.this.onPreExecute();
            }

            @Override
            protected void onPostExecute(T t) {
                AsyncTaskNew.this.onPostExecute(t);
            }

            @Override
            protected T doInBackground(String... params) {
                return AsyncTaskNew.this.doInBackground(params);
            }
        }.executeOnExecutor(threadPoolExecutor,jsonStr);
    }

    public void execute(Executor executor, String... jsonStr) {

        new android.os.AsyncTask<String, Void, T>() {

            @Override
            protected void onPreExecute() {
                AsyncTaskNew.this.onPreExecute();
            }

            @Override
            protected void onPostExecute(T t) {
                AsyncTaskNew.this.onPostExecute(t);
            }

            @Override
            protected T doInBackground(String... params) {
                return AsyncTaskNew.this.doInBackground(params);
            }
        }.executeOnExecutor(executor,jsonStr);
//        }.executeOnExecutor(Executors.newSingleThreadScheduledExecutor(),jsonStr);
//        Executors.callable()
    }


    protected abstract T doInBackground(String... params);
    protected void onPreExecute() {}
    protected void onPostExecute(T result) {}


}

