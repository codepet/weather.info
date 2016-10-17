package com.gc.weather.http.core;

public class HttpConfig {
    private long mConnectedTimeout = 20000L;
    private long mReadTimeout = 15000L;
    private long mWriteTimeout = 20000L;
    private static final String mBaseUrl = "http://apis.baidu.com";

    public long getConnectedTimeout() {
        return mConnectedTimeout;
    }

    public void setConnectedTimeout(long connectedTimeout) {
        this.mConnectedTimeout = connectedTimeout;
    }

    public long getReadTimeout() {
        return mReadTimeout;
    }

    public void setReadTimeout(long readTimeout) {
        this.mReadTimeout = readTimeout;
    }

    public long getWriteTimeout() {
        return mWriteTimeout;
    }

    public void setWriteTimeout(long writeTimeout) {
        this.mWriteTimeout = writeTimeout;
    }

    public static String getBaseUrl() {
        return mBaseUrl;
    }
}
