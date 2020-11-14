package com.app.base.utils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.base.utils.network.annotation.NetWork;
import com.app.base.utils.network.utils.Constants;
import com.app.base.utils.network.utils.NetworkUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetworkStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private Map<Object, List<MethodManager>> networkList;

    public NetworkStateReceiver() {
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equalsIgnoreCase(Constants.ANDROID_NET_ACTION)) {
            netType = NetworkUtils.getNetType();
            post(netType);
        }
    }

    private void post(NetType netType) {
        Set<Object> set = networkList.keySet();
        for (final Object object : set) {
            List<MethodManager> methodManagers = networkList.get(object);
            if (methodManagers != null) {
                for (final MethodManager method : methodManagers) {
                    if (method.getType().isAssignableFrom(netType.getClass())) {
                        switch (method.getNetType()) {
                            case AUTO:
                                invoke(method, object, netType);
                                break;
                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(method, object, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(method, object, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(method, object, netType);
                                }
                                break;
                            case NONE:
                                break;
                            default:
                                break;
                        }
                    }

                }
            }
        }
    }

    private void invoke(MethodManager method, Object object, NetType netType) {
        Method me = method.getMethod();
        try {
            me.invoke(object, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void registerObserver(Object register) {
        List<MethodManager> methodManagers = networkList.get(register);
        if (methodManagers == null) {
            methodManagers = findAnnotation(register);
            networkList.put(register, methodManagers);
        }
    }

    private List<MethodManager> findAnnotation(Object register) {
        List<MethodManager> methodManagers = new ArrayList<>();
        Class<?> aClass = register.getClass();
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            NetWork annotation = method.getAnnotation(NetWork.class);
            if (annotation == null) {
                continue;
            }
            Type genericReturnType = method.getGenericReturnType();
            if (!genericReturnType.toString().equals("void")) {
                throw new RuntimeException(method.getName() + "Method return must be void");
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + "Method can only have one parameter");
            }

            MethodManager methodManager = new MethodManager(parameterTypes[0], annotation.netType(), method);
            methodManagers.add(methodManager);
        }
        return methodManagers;
    }

    public void unRegisterObserver(Object register) {
        if (!networkList.isEmpty()) {
            networkList.remove(register);
        }
    }

    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
        }
        NetworkManager.getInstance().getApplication().unregisterReceiver(this);
        networkList = null;
    }
}
