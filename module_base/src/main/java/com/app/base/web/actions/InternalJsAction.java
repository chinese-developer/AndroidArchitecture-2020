package com.app.base.web.actions;

import com.app.base.web.BaseWebFragment;
import com.app.base.web.ResultReceiver;

import androidx.annotation.Nullable;

public class InternalJsAction {

    private static final String SAVE_IMAGE_METHOD = "savePhoto";

    public static void doAction(BaseWebFragment fragment, String method, @Nullable String[] args, @Nullable ResultReceiver resultReceiver) {
        if (SAVE_IMAGE_METHOD.equals(method)) {
            SaveImageAction.action(fragment, args);
        }
        CommonActionsKt.doAction(method, args, resultReceiver, fragment);
    }

}
