package com.app.base.data.api;

class Timestamp {

    static long getTimestamp() {
        return System.currentTimeMillis() + 259200000;
    }

}
