package com.flayone.oaid.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/**
 * 一加手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:27
 */
public interface OnePlusIDInterface extends IInterface {

    abstract class Stub extends Binder implements OnePlusIDInterface {

        public static OnePlusIDInterface genInstance(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.heytap.openid.IOpenID");
            if (iInterface == null || !(iInterface instanceof OnePlusIDInterface)) {
                return new Proxy(iBinder);
            } else {
                return (OnePlusIDInterface) iInterface;
            }
        }
    }

    class Proxy implements OnePlusIDInterface {
        public IBinder iBinder;

        public Proxy(IBinder ib) {
            iBinder = ib;
        }

        public String getSerID(String str1, String str2, String str3) {
            String res = null;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.heytap.openid.IOpenID");
                obtain.writeString(str1);
                obtain.writeString(str2);
                obtain.writeString(str3);
                iBinder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                res = obtain2.readString();
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                obtain.recycle();
                obtain2.recycle();
            }
            return res;
        }

        @Override
        public IBinder asBinder() {
            return iBinder;
        }
    }
}
