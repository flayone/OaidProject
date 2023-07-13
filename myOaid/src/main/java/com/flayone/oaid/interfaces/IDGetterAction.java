package com.flayone.oaid.interfaces;

import com.flayone.oaid.AppIdsUpdater;

public interface IDGetterAction {
    //是否支持获取oaid
    boolean supported();

    void getID(AppIdsUpdater _listener);
}
