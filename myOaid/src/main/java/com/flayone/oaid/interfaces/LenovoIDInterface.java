package com.flayone.oaid.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;


/**
 * 联想手机获取OAid
 *
 * @author AF
 * @time 2020/4/14 18:25
 */
public interface LenovoIDInterface extends IInterface {

    String a();

    String a(String arg1);

    String b();

    String b(String arg1);

    boolean c();

    public abstract class Stub extends Binder implements LenovoIDInterface {

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String str = "com.zui.deviceidservice.IDeviceidInterface";
            switch (code) {
                case 1:
                    data.enforceInterface(str);
                    str = a();
                    reply.writeNoException();
                    reply.writeString(str);
                    return true;
                case 2:
                    data.enforceInterface(str);
                    str = b();
                    reply.writeNoException();
                    reply.writeString(str);
                    return true;
                case 3:
                    data.enforceInterface(str);
                    boolean c = c();
                    reply.writeNoException();
                    reply.writeInt(c ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(str);
                    str = a(data.readString());
                    reply.writeNoException();
                    reply.writeString(str);
                    return true;
                case 5:
                    data.enforceInterface(str);
                    str = b(data.readString());
                    reply.writeNoException();
                    reply.writeString(str);
                    return true;
                case 1598968902:
                    reply.writeString(str);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);

            }

        }

        public static LenovoIDInterface getInstance(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.zui.deviceidservice.IDeviceidInterface");
            if (iInterface == null || !(iInterface instanceof LenovoIDInterface)) {
                return new Proxy(iBinder);
            } else {
                return (LenovoIDInterface) iInterface;
            }
        }
    }

    class Proxy implements LenovoIDInterface {

        private IBinder iBinder;

        public Proxy(IBinder ib) {
            iBinder = ib;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }

        @Override
        public String a() {
            String readString = null;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.zui.deviceidservice.IDeviceidInterface");
                iBinder.transact(1, obtain, obtain2, 0);
                obtain2.readException();
                readString = obtain2.readString();
                return readString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
            return readString;
        }

        @Override
        public String a(String arg1) {
            String readString = null;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.zui.deviceidservice.IDeviceidInterface");
                iBinder.transact(4, obtain, obtain2, 0);
                obtain2.readException();
                readString = obtain2.readString();
                return readString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
            return readString;
        }

        @Override
        public String b() {
            String readString = null;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.zui.deviceidservice.IDeviceidInterface");
                iBinder.transact(2, obtain, obtain2, 0);
                obtain2.readException();
                readString = obtain2.readString();
                return readString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
            return readString;
        }

        @Override
        public String b(String arg1) {
            String readString = null;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.zui.deviceidservice.IDeviceidInterface");
                iBinder.transact(5, obtain, obtain2, 0);
                obtain2.readException();
                readString = obtain2.readString();
                return readString;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                obtain2.recycle();
                obtain.recycle();
            }
            return readString;
        }

        @Override
        public boolean c() {
            boolean z = false;
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            try {
                obtain.writeInterfaceToken("com.zui.deviceidservice.IDeviceidInterface");
                iBinder.transact(3, obtain, obtain2, 0);
                obtain2.readException();
                if (obtain2.readInt() != 0) {
                    z = true;
                }
                obtain2.recycle();
                obtain.recycle();
            } catch (Throwable th) {
                obtain2.recycle();
                obtain.recycle();
            }
            return z;
        }
    }
}
