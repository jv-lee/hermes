package com.lee.hermes;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.lee.hermes.annotion.ClassId;
import com.lee.hermes.bean.RequestBean;
import com.lee.hermes.bean.RequestParameter;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 *  进程管理器
 *   主进程
 * @author jv.lee
 */
public class ProcessManager {

    private static ProcessManager instance;
    CacheCenter mCacheCenter = CacheCenter.getInstance();
    ProcessInterface processinterface;
    Gson gson = new Gson();
    public static ProcessManager getInstance() {
        if (instance == null) {
            synchronized (ProcessManager.class) {
                if (instance == null) {
                    instance = new ProcessManager();
                }
            }
        }
        return instance;
    }

    private ProcessManager() {
    }

    /**
     * 注册进去缓存中心
     * @param clazz
     */
    public void register(Class<?> clazz) {
        mCacheCenter.register(clazz);
    }


    //````````````````````````````````````````另一个对象 调用的方法  第二个单列 ```````````````````````````````````````````````````
    public <T> T getInstance(Class<T> claszz, Object... params) {
        //发送请求
        sendRequest(ProcessService.GET_INSTANCE, claszz, null, params);
        //返回代理对象
        T proxy = (T) Proxy.newProxyInstance(claszz.getClassLoader(), new Class[]{claszz}, new ProcessInvocationHandler(claszz));
        return proxy;
    }

    public String sendRequest(int type, Class<?> clazz, Method method, Object[] parameters) {
        Log.e("------- >>>>", "sendRequest()");
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = gson.toJson(parameter);
                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i]=requestParameter;
            }
        }
        String className = clazz.getAnnotation(ClassId.class).value();
        String methodName = method == null ? "" : method.getName();
        RequestBean requestBean = new RequestBean(type, className, methodName, requestParameters);
        String request = gson.toJson(requestBean);
        Log.e("------ >>>> ", "className:" + className + " methodName:" + methodName + " request:" + request);
        try {
            //返回到远端服务 的onBind（）；  ProcessService- 主进程
            return processinterface.send(request);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    Context context;
    public void connect(Context context) {
        this.context = context;
        bind(context,null,ProcessService.class);
    }

    public void connect(Context context, String packageName) {
        bind(context, packageName, ProcessService.class);
    }

    private void bind(Context context, String packageName, Class<ProcessService> serviceClass) {
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, serviceClass);
        }else{
            //多APP通信
            intent = new Intent();
            intent.setPackage(packageName);
            //获取service注册时的Action  name标签
            intent.setAction(serviceClass.getName());
        }

        //绑定跨进程服务 IBinder连接通信  初始化 AIDL接口
        context.bindService(intent, new ProcessConnection(), Context.BIND_AUTO_CREATE);
    }

    private class ProcessConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            processinterface = ProcessInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
