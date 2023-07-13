package com.flayone.oaid.imp;

import com.flayone.oaid.AppIdsUpdater;
import com.flayone.oaid.interfaces.IDGetterAction;

//默认的空获取结果
public class DefaultHelper implements IDGetterAction {
    public DefaultHelper() {
    }

    @Override
    public boolean supported() {
        return false;
    }

    @Override
    public void getID(AppIdsUpdater _listener) {

        //默认空
        if (_listener != null) {
            _listener.OnIdsAvalid("");
        }
    }
}
