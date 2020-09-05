package com.android.base.rx;

import android.view.View;

import com.jakewharton.rxbinding3.view.RxView;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import kotlin.Unit;
import timber.log.Timber;

public class RxKit {

    /**
     * <pre>
     *     {@code private CompositeSubscription mCompositeSubscription;
     *
     *       protected void addSubscription(Subscription subscription)
     *                     CompositeSubscription newCompositeSubIfUnsubscribed =
     *                     RxUtils .getNewCompositeSubIfUnsubscribed(mCompositeSubscription);
     *                     mCompositeSubscription = newCompositeSubIfUnsubscribed;
     *                     newCompositeSubIfUnsubscribed.add(subscription);
     *
     *        private void unsubscribe() {
     *                    RxUtils.unsubscribeIfNotNull(mCompositeSubscription);
     *        }
     * }
     * </pre>
     */
    public static void unsubscribeIfNotNull(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static CompositeDisposable getNewCompositeSubIfUnsubscribed(CompositeDisposable disposable) {
        if (disposable == null || disposable.isDisposed()) {
            return new CompositeDisposable();
        }
        return disposable;
    }

    private static final Action LOG_ACTION0 = () -> Timber.d("RxUtils LOG_ACTION0 call() called");

    private static final Consumer LOG_ACTION1 = obj -> {
        if (obj instanceof Throwable) {
            Timber.e((Throwable) obj, "RxUtils PRINT_ACTION1 call() called with: error");
        } else {
            Timber.d("RxUtils ERROR_ACTION call() called with: result = [" + obj + "]");
        }
    };

    @SuppressWarnings("unchecked")
    public static Consumer<Throwable> logErrorHandler() {
        return (Consumer<Throwable>) LOG_ACTION1;
    }

    public static Action logCompletedHandler() {
        return LOG_ACTION0;
    }

    @SuppressWarnings("unchecked")
    public static Consumer<Object> logResultHandler() {
        return (Consumer<Object>) LOG_ACTION1;
    }

    private static final Subscriber LOG_SUBSCRIBER = new Subscriber() {
        @Override
        public void onSubscribe(Subscription s) {
            s.request(Long.MAX_VALUE);
            Timber.d("onSubscribe() called with: s = [" + s + "]");
        }

        @Override
        public void onNext(Object o) {
            Timber.d("onNext() called with: o = [" + o + "]");
        }

        @Override
        public void onError(Throwable t) {
            Timber.d("onError() called with: t = [" + t + "]");
        }

        @Override
        public void onComplete() {
            Timber.d("onComplete() called");
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Subscriber<T> logSubscriber() {
        return (Subscriber<T>) LOG_SUBSCRIBER;
    }

    private static final ThreadTransformer IO_2_UI_SCHEDULERS_TRANSFORMER = ThreadTransformer.newInstance(ThreadTransformer.IO_UI);
    private static final ThreadTransformer COMPUTATION_2_UI_SCHEDULERS_TRANSFORMER = ThreadTransformer.newInstance(ThreadTransformer.COMPUTATION_UI);
    private static final ThreadTransformer NEW_THREAD_2_UI_SCHEDULERS_TRANSFORMER = ThreadTransformer.newInstance(ThreadTransformer.NEW_THREAD_UI);

    @SuppressWarnings("unchecked")
    public static <T> ThreadTransformer<T> io2UI() {
        return IO_2_UI_SCHEDULERS_TRANSFORMER;
    }

    @SuppressWarnings("unchecked")
    public static <T> ThreadTransformer<T> computation2UI() {
        return COMPUTATION_2_UI_SCHEDULERS_TRANSFORMER;
    }

    @SuppressWarnings("unchecked")
    public static <T> ThreadTransformer<T> newThread2UI() {
        return NEW_THREAD_2_UI_SCHEDULERS_TRANSFORMER;
    }

    public static Observable<Unit> clicksChecked(View view) {
        return clicksChecked(view, 300);
    }

    public static Observable<Unit> clicksChecked(View view, int milliseconds) {
        return RxView.clicks(view)
                .debounce(milliseconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

}