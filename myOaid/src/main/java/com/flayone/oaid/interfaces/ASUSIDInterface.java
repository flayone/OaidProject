package com.flayone.oaid.interfaces;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;


/**
 * 华硕手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:24
 */
public interface ASUSIDInterface extends IInterface {

    String getID();

    public final class ASUSID implements ASUSIDInterface {
        private IBinder iBinder;

        public ASUSID(IBinder ib) {
            iBinder = ib;
        }

        @Override
        public IBinder asBinder() {
            return iBinder;
        }

        @Override
        public String getID() {
            String result = null;
            Parcel v1 = Parcel.obtain();
            Parcel v2 = Parcel.obtain();

            try {
                v1.writeInterfaceToken("com.asus.msa.SupplementaryDID.IDidAidlInterface");
                iBinder.transact(3, v1, v2, 0);
                v2.readException();
                result = v2.readString();
            } catch (Throwable e) {
                v1.recycle();
                v2.recycle();
                e.printStackTrace();
            }

            v1.recycle();
            v2.recycle();
            return result;
        }
    }
}
