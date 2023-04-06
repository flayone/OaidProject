package com.flayone.oaid;


public enum DeviceTypeEnum {

    Unknown("未知手机", "UNKNOWN"),
    HuaShuo("华硕手机", "ASUS"),
    HuaWei("华为手机", "HUAWEI"),
    Honor("荣耀手机", "HONOR"),
    Lenove("联想手机", "LENOVO"),
    Motolora("摩托罗拉手机", "MOTOLORA"),
    Nubia("努比亚手机", "NUBIA"),
    Meizu("魅族手机", "MEIZU"),
    Oppo("Oppo手机", "OPPO"),
    Samsung("三星手机", "SAMSUNG"),
    Vivo("Vivo手机", "VIVO"),
    XiaoMi("小米手机", "XIAOMI"),
    BlackShark("小米手机", "BLACKSHARK"),
    OnePlus("OnePlus手机", "ONEPLUS"),
    ZTE("中兴手机", "ZTE"),
    Phone360("360手机", "360"),
    Coolpad("酷派手机", "COOLPAD"),
    Gionee("金立手机", "GIONEE"),
    Google("谷歌手机", "GOOGLE"),
    Htc("HTC手机", "HTC"),
    Leeco("乐视手机", "LEECO"),
    Lg("LG手机", "LG"),
    Sony("索尼手机", "SONY"),
    Smartisan("锤子手机", "SMARTISAN");


    private String name;
    private String flag;

    DeviceTypeEnum(String name, String flag) {
        this.name = name;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public static DeviceTypeEnum getInstance(String flag) {
        DeviceTypeEnum[] list = values();
        try {
            for (DeviceTypeEnum item : list) {
                if (item.getFlag().equals(flag)) {
                    return item;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return getDeviceType2();
    }

    public static DeviceTypeEnum getDeviceType2() {
        try {
            if (RomUtils.is360()) {
                return Phone360;
            } else if (RomUtils.isCoolpad()) {
                return Coolpad;
            } else if (RomUtils.isGionee()) {
                return Gionee;
            } else if (RomUtils.isGoogle()) {
                return Google;
            } else if (RomUtils.isHtc()) {
                return Htc;
            } else if (RomUtils.isHuawei()) {
                return HuaWei;
            } else if (RomUtils.isLeeco()) {
                return Leeco;
            } else if (RomUtils.isLenovo()) {
                return Lenove;
            } else if (RomUtils.isLg()) {
                return Lg;
            } else if (RomUtils.isMeizu()) {
                return Meizu;
            } else if (RomUtils.isMotorola()) {
                return Motolora;
            } else if (RomUtils.isNubia()) {
                return Nubia;
            } else if (RomUtils.isOneplus()) {
                return OnePlus;
            } else if (RomUtils.isOppo()) {
                return Oppo;
            } else if (RomUtils.isSamsung()) {
                return Samsung;
            } else if (RomUtils.isSmartisan()) {
                return Smartisan;
            } else if (RomUtils.isSony()) {
                return Sony;
            } else {
                return Unknown;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return Unknown;
        }
    }

}
