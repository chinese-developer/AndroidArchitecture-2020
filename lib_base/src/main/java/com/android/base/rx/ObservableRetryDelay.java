package com.android.base.rx;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class ObservableRetryDelay implements Function<Observable<Throwable>, ObservableSource<?>> {

    private final int maxRetries;
    private final long retryDelayMillis;
    @Nullable
    private RetryChecker retryChecker;
    private int retryCount = 0;

    @SuppressWarnings("unused")
    public ObservableRetryDelay(final int maxRetries, final long retryDelayMillis) {
        this(maxRetries, retryDelayMillis, null);
    }

    public ObservableRetryDelay(final int maxRetries, final long retryDelayMillis, @Nullable RetryChecker retryChecker) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryChecker = retryChecker != null ? retryChecker : throwable -> true;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) {
        return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (retryChecker != null && !retryChecker.verify(throwable)) {
                return Observable.error(throwable);
            }
            retryCount++;
            Timber.i(new Date() + " 自动重试" + (retryCount + 1) + "次，在" + Thread.currentThread() + "线程");
            if (retryCount <= maxRetries) {
                return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Observable.error(throwable);
        });
    }

}