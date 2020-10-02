package com.app.base.utils.looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public interface IIntervalObserver extends LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner source);

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume();

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause();

    class Observer implements IIntervalObserver {

        GlobalLooper.Builder builder;

        Observer(GlobalLooper.Builder builder) {
            this.builder = builder;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDestroy(LifecycleOwner source) {
            if (source != null) {
                source.getLifecycle().removeObserver(this);
            }
            builder.cancel();
        }

        @Override
        public void onResume() {
            if (builder.status != Lifecycle.State.DESTROYED) {
                builder.resume();
            }
        }

        @Override
        public void onPause() {
            if (builder.status != Lifecycle.State.DESTROYED) {
                builder.pause();
            }
        }
    }
}
