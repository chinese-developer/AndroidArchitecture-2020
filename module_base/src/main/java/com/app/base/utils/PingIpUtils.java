package com.app.base.utils;

import com.android.base.TagsFactory;
import com.app.base.utils.domain.PingIp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import timber.log.Timber;

/**
 * @author Nemo
 * Email: privateemailmaster@gmail.com
 * Date : 2020-11-13 14:45
 */
public class PingIpUtils {

    public static PingIpUtils getInstance() {
        return INSTANCE.instance;
    }

    private static class INSTANCE {
        static PingIpUtils instance = new PingIpUtils();
    }

    private BufferedReader successReader = null;
    private Process process = null;
    private Callback callback;

    public interface Callback {
        void finish(String pingMs);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * @param pingIp 检测网络实体类
     */
    public void ping(PingIp pingIp) {
        String line;
        //ping -c 次数 -w 超时时间（s） ip
        String command = "ping -c " + pingIp.getPingCount() + " -w " + pingIp.getPingTimeOut() + " " + pingIp.getIp();
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                Timber.tag(TagsFactory.debug).d("ping fail:process is null.");
                append(pingIp.getResultBuffer(), "ping fail:process is null.");
                pingIp.setPingTime(null);
                pingIp.setResult(false);
                return;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int count = 0;
            BigDecimal sum = new BigDecimal(0);
            while ((line = successReader.readLine()) != null) {
                append(pingIp.getResultBuffer(), line);
                BigDecimal time = getTime(line);
                if (time != null) {
                    sum = sum.add(time);
                    count++;
                }
            }
            //时间取平均值，四舍五入保留两位小数
            String pingMs = null;
            if (count > 0) {
                pingMs = (sum.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
                pingIp.setPingTime(pingMs);
            } else
                pingIp.setPingTime(null);
            int status = process.waitFor();
            if (status == 0) {
                append(pingIp.getResultBuffer(), "exec cmd success:" + command);
                pingIp.setResult(true);
            } else {
                append(pingIp.getResultBuffer(), "exec cmd fail.");
                pingIp.setPingTime(null);
                pingIp.setResult(false);
            }
            append(pingIp.getResultBuffer(), "exec finished.");
            if (callback != null) {
                callback.finish(pingMs);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Timber.tag(TagsFactory.error).e(pingIp.getResultBuffer().toString());
    }

    public void releaseAll() {
        if (process != null) {
            process.destroy();
        }
        if (successReader != null) {
            try {
                successReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.callback = null;
    }

    private void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text).append("\n");
        }
    }

    /**
     * 获取ping接口耗时
     *
     * @param line readLine
     * @return BigDecimal避免float、double精准度问题
     */
    private BigDecimal getTime(String line) {
        String[] lines = line.split("\n");
        String time = null;
        for (String l : lines) {
            if (!l.contains("time="))
                continue;
            int index = l.indexOf("time=");
            time = l.substring(index + "time=".length());
            index = time.indexOf("ms");
            time = time.substring(0, index);
        }
        return time == null ? null : new BigDecimal(time.trim());
    }
}
