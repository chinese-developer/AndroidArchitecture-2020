package com.android.base.rx;

import org.reactivestreams.Publisher;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class FlowableRetryDelay implements Function<Flowable<Throwable>, Publisher<?>> {

    private final int maxRetries;
    private final long retryDelayMillis;
    @Nullable
    private RetryChecker retryChecker;
    private int retryCount = 0;

    @SuppressWarnings("unused")
    public FlowableRetryDelay(final int maxRetries, final long retryDelayMillis) {
        this(maxRetries, retryDelayMillis, null);
    }

    public FlowableRetryDelay(final int maxRetries, final long retryDelayMillis, @Nullable RetryChecker retryChecker) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryChecker = retryChecker != null ? retryChecker : throwable -> true;
    }

    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) {
        return throwableFlowable.flatMap((Function<Throwable, Publisher<?>>) throwable -> {
            if (retryChecker != null && !retryChecker.verify(throwable)) {
                return Flowable.error(throwable);
            }
            retryCount++;
            Timber.i(new Date() + " 自动重试" + (retryCount + 1) + "次，在" + Thread.currentThread() + "线程");
            if (retryCount <= maxRetries) {
                return Flowable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Flowable.error(throwable);
        });
    }

}