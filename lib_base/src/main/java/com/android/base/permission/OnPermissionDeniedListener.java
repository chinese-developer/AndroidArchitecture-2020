package com.android.base.permission;

import java.util.List;

public interface OnPermissionDeniedListener {
    void onPermissionDenied(List<String> permissions);
}
