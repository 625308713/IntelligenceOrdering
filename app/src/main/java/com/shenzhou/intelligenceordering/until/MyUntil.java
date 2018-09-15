package com.shenzhou.intelligenceordering.until;

import android.app.ActivityManager;
import android.content.Context;

import java.util.Collection;
import java.util.List;

public class MyUntil {
    /**
     * 当前运行的Service
     */
    public static boolean getServiceClassName(Context context, String packagename) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = am
                .getRunningServices(100);
        for (int i = 0; i < mServiceList.size(); i++) {
            if(packagename.equals(mServiceList.get(i).service.getClassName())){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isNotNullCollect(Collection<?> collection) {
        if (null != collection && !collection.isEmpty() && collection.size() > 0) {
            return true;
        }
        return false;
    }
}
