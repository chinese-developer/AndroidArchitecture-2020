package com.app.base.utils.looper;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.android.base.TagsFactory;
import com.android.base.app.mvvm.LifecycleViewModel;
import com.app.base.AppContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;


/**
 * 注意 onPause onResume 会自动暂停和恢复, 如果不需要 resunme 恢复, 需要在 accept 里面拦截.
 */
public final class GlobalLooper {
    private static WeakHandler uiHandler;
    private static GlobalLooper instance;
    private static Map<String, Builder> builderTagsMap = new ConcurrentHashMap<>();

    private GlobalLooper() {
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.obj instanceof Builder) {
                    ((Builder) msg.obj).executeTask();
                }
                return true;
            }
        };
        uiHandler = new WeakHandler(AppContext.get().getMainLooper(), callback);
    }

    public static GlobalLooper getInstance() {
        if (instance == null) {
            synchronized (GlobalLooper.class) {
                if (instance == null) {
                    instance = new GlobalLooper();
                }
            }
        }
        return instance;
    }

    public Builder newBuilder(Object object) {
        return new Builder(object);
    }

    public Builder newBuilder(Fragment fragment) {
        return new Builder(fragment);
    }

    public Builder newBuilder(ViewModel baseViewModel) {
        return new Builder(baseViewModel);
    }

    public Builder newBuilder(AppCompatActivity activity) {
        return new Builder(activity);
    }

    public Builder newBuilder(Object object, String tag) {
        return new Builder(object, tag);
    }

    public Builder newBuilder(Fragment fragment, String tag) {
        return new Builder(fragment, tag);
    }

    public Builder newBuilder(ViewModel baseViewModel, String tag) {
        return new Builder(baseViewModel, tag);
    }

    public Builder newBuilder(AppCompatActivity activity, String tag) {
        return new Builder(activity, tag);
    }

    /**
     * 取消
     */
    public void cancel(Object object) {
        if (object == null) {
            return;
        }
        cancel(object, object.getClass().getName());
    }

    public boolean containsTag(String tag) {
        return builderTagsMap.containsKey(tag);
    }

    public void cancel(Object object, String tag) {
        if (object == null || TextUtils.isEmpty(tag)) {
            return;
        }
        Builder builder = builderTagsMap.get(tag);

        if (builder != null) {
            if (object instanceof LifecycleOwner) {
                builder.cancel((LifecycleOwner) object);
            } else {
                builder.cancel();
            }
        }
    }

    /**
     * 暂停
     */
    public void pause(Object object) {
        if (object == null) {
            return;
        }
        pause(object, object.getClass().getName());
    }

    public void pause(Object object, String tag) {
        if (object == null || TextUtils.isEmpty(tag)) {
            return;
        }
        Builder builder = builderTagsMap.get(tag);
        if (builder != null) {
            builder.pause();
        }
    }

    public boolean isPaused(Object object) {
        if (object == null) {
            return true;
        }
        return isPaused(object, object.getClass().getName());
    }

    public boolean isPaused(Object object, String tag) {
        if (object == null || TextUtils.isEmpty(tag)) {
            return true;
        }
        Builder builder = builderTagsMap.get(tag);
        if (builder != null) {
            return builder.isPaused();
        }
        return true;
    }

    /**
     * 继续
     */
    public void resume(Object object) {
        if (object == null) {
            return;
        }
        resume(object, object.getClass().getName());
    }

    public void resume(Object object, String tag) {
        if (object == null || TextUtils.isEmpty(tag)) {
            return;
        }
        Builder builder = builderTagsMap.get(tag);
        if (builder != null) {
            builder.resume();
        }
    }

    /**
     * 更新 轮训周期
     */
    public void updatePeriod(Object object, long period) {
        if (object == null) {
            return;
        }
        updatePeriod(object, object.getClass().getName(), period);
    }

    public void updatePeriod(Object object, String tag, long period) {
        if (object == null || TextUtils.isEmpty(tag)) {
            return;
        }
        Builder builder = builderTagsMap.get(tag);
        if (builder != null) {
            builder.updatePeriod(period);
        }
    }

    /**
     * 清空所有任务
     */
    public void cancelAll() {
        for (Builder builder : builderTagsMap.values()) {
            builder.cancel();
        }
        builderTagsMap.clear();
    }

    public static class Builder {
        long initialDelay;
        long period;
        TimeUnit unit;
        private Action action;
        String tag;
        //打补丁模式，解决fragment可见性不跟随生命周期的问题
        Boolean fragmentHidden = null;
        TaskType taskType = TaskType.LOOP_EXECUTE;
        static final int RUNNING_WHAT = 0x0001;
        Lifecycle.State status = Lifecycle.State.DESTROYED; // Lifecycle.Event
        IIntervalObserver.Observer observer = null;
        private boolean handlerHasBeenSuspended = false;

        public Builder(Object object) {
            this(object, object.getClass().getName());
        }

        public Builder(Fragment fragment) {
            this(fragment, fragment.getClass().getName());
        }

        public Builder(LifecycleViewModel baseViewModel) {
            this(baseViewModel, baseViewModel.getClass().getName());
        }

        public Builder(AppCompatActivity activity) {
            this(activity, activity.getClass().getName());
        }

        public Builder(Object object, @NonNull String tag) {
            this.tag = tag;
        }

        public Builder(Fragment fragment, String tag) {
            observer = new IIntervalObserver.Observer(this);
            fragment.getLifecycle().addObserver(observer);
            this.tag = tag;
        }

        public Builder(LifecycleViewModel baseViewModel, String tag) {
            observer = new IIntervalObserver.Observer(this);
            baseViewModel.getLifecycle().addObserver(observer);
            this.tag = tag;
        }

        public Builder(AppCompatActivity activity, String tag) {
            observer = new IIntervalObserver.Observer(this);
            activity.getLifecycle().addObserver(observer);
            this.tag = tag;
        }

        /**
         * 一段时间后执行
         */
        public Builder period(long period, TimeUnit unit) {
            this.period = period;
            this.initialDelay = period;
            this.unit = unit;
            return this;
        }

        /**
         * 首次延迟执行时间
         */
        public Builder initialDelay(long period, TimeUnit unit) {
            this.initialDelay = period;
            this.unit = unit;
            return this;
        }

        /**
         * @param period       一段时间后执行
         * @param initialDelay 首次延迟执行时间
         */
        public Builder period(long period, long initialDelay, TimeUnit unit) {
            this.period = period;
            this.initialDelay = initialDelay;
            this.unit = unit;
            return this;
        }

        /**
         * @param action 任务执行完成CallBack
         *               注意: 绑定了 lifecycle 在 onResume 时会自动恢复任务, onPause 时会自动暂停任务.
         *               如果不需要轮训, 请在 accept 拦截 lifecycle 生命周期事件
         */
        public Builder accept(Action action) {
            this.action = action;
            return this;
        }

        /**
         * 循环模式任务
         */
        public Builder loopExecute() {
            this.taskType = TaskType.LOOP_EXECUTE;
            return this;
        }

        /**
         * 延迟模式任务
         */
        public Builder delayExecute() {
            this.taskType = TaskType.DELAY_EXECUTE;
            return this;
        }

        public void updatePeriod(long period) {
            this.period = Math.max(0L, unit.toMillis(period));
        }

        /**
         * 暂停任务
         */
        public void pause() {
            if (!handlerHasBeenSuspended) {
                handlerHasBeenSuspended = true;
                uiHandler.removeCallbacksAndMessages(this);
            }
        }

        public boolean isPaused() {
            return handlerHasBeenSuspended;
        }

        /**
         * 恢复任务
         */
        public void resume() {
            if (handlerHasBeenSuspended) {
                handlerHasBeenSuspended = false;
                if (!uiHandler.hasMessages(RUNNING_WHAT, this)) {
                    uiHandler.sendMessageDelayed(obtainThis(), period);
                }
            }
        }

        /**
         * 取消任务
         */
        public void cancel() {
            handlerHasBeenSuspended = false;
            cancel(null);
        }

        /**
         * 如果当前绑定了 {@link LifecycleOwner} ，应通过该方法取消，需传递 {@link LifecycleOwner}
         *
         * @param lifecycleOwner {@link LifecycleOwner}
         */
        public void cancel(@Nullable LifecycleOwner lifecycleOwner) {
            if (tag != null && builderTagsMap.containsKey(tag)) {
                if (!tag.equals("lottery_detail_handler_fetch_user_info_key")) {
                    Timber.tag(TagsFactory.debug).d("cancel tag is %s", tag);
                }
                builderTagsMap.remove(tag);
            }
            handlerHasBeenSuspended = false;
            status = Lifecycle.State.DESTROYED;
            uiHandler.removeCallbacksAndMessages(this);
            if (checkLifecycleOwnerNotNull(lifecycleOwner)) removeObserver(lifecycleOwner);
        }

        /**
         * 启动任务
         */
        public Builder start() {
            if (taskType == null || tag == null) {
                return this;
            }
            handlerHasBeenSuspended = false;
            builderTagsMap.put(tag, this);
            this.initialDelay = Math.max(0L, unit.toMillis(initialDelay));
            this.period = Math.max(0L, unit.toMillis(period));
            switch (taskType) {
                case LOOP_EXECUTE:
                case DELAY_EXECUTE:
                    if (action == null) {
                        return this;
                    }
                    break;
                default:
                    break;
            }
            status = Lifecycle.State.CREATED;
            uiHandler.sendMessageDelayed(obtainThis(), initialDelay);
            return this;
        }

        Message obtainThis() {
            Message msg = Message.obtain();
            msg.what = RUNNING_WHAT;
            msg.obj = this;
            return msg;
        }

        private void executeTask() {
            if (taskType == null) {
                return;
            }
            switch (taskType) {
                case LOOP_EXECUTE:
                    performLoopExecute();
                    break;
                case DELAY_EXECUTE:
                    performActionExecute();
                    cancel();
                    break;
                default:
                    break;
            }
        }

        private void removeObserver(@NonNull LifecycleOwner lifecycleOwner) {
            lifecycleOwner.getLifecycle().removeObserver(observer);
            observer = null;
        }

        private void performLoopExecute() {
            if (action != null) {
                uiHandler.sendMessageDelayed(obtainThis(), period);
                performActionExecute();
            }
        }

        private boolean checkLifecycleOwnerNotNull(LifecycleOwner lifecycleOwner) {
            return lifecycleOwner != null && observer != null;
        }

        private void performActionExecute() {
            if (action != null) {
                try {
                    action.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
