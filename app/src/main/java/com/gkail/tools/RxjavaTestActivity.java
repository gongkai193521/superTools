package com.gkail.tools;

import android.os.Bundle;
import android.util.Log;

import com.gkail.tools.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by gongkai on 2019/3/21.
 */

public class RxjavaTestActivity extends BaseActivity {
    @Override
    public int setContentView() {
        return R.layout.activity_rxjavatest;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        mObservable.subscribe(mObserver);
        mObservable.subscribeOn(Schedulers.io());
    }

    Observable mObservable = Observable.create(new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter emitter) throws Exception {
            emitter.onNext("你好啊");
        }
    });
    Observer mObserver = new Observer<String>() {
        @Override
        public void onSubscribe(Disposable d) {
            Log.i("----", " onSubscribe ");
        }

        @Override
        public void onNext(String s) {
            Log.i("----", " == " + s);
        }

        @Override
        public void onError(Throwable e) {
            Log.i("----", " onError ");
        }

        @Override
        public void onComplete() {
            Log.i("----", " onComplete ");
        }
    };
}
