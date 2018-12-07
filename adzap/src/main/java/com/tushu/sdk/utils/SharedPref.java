package com.tushu.sdk.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class SharedPref {

    public static final String AD_SERVICE_CONFIG = "ad_service_config";
    public static final String AD_FACEBOOK_ID = "ad_facebook_id";
    public static final String AD_GOOGLE_ID = "ad_google_id";
    public static final String AD_ADT_ID = "ad_adt_id";
    public static final String NET_STATE = "net_state";
    public static final String CHARGE_STATE = "charge_state";
    public static final String LAST_SHOW_TIME = "last_show_time";
    public static final String SHOW_FIRST_TIME = "show_first_time";//第一次显示时间，24小时后刷新显示次数
    public static final String INSTALL_TIME = "install_time";//安装时间


    //广告逻辑
    public static final String LOAD_AD_CODE = "load_ad_code";
    public static final String LOAD_AD_FULL_SCREEN = "load_ad_full_screen";//加载广告类型  基数为插屏广告  偶数为原生全屏广告
    public static final String LOAD_AD_PRELOAD = "load_ad_preload";//加载广告类型  基数为facebook  偶数为admob

    private static final HashMap<String, SharedPreferences> sSharedPrefs = new HashMap<String, SharedPreferences>();

    private static SharedPreferences getSharedPreferences(Context context, final String fileName) {
        SharedPreferences sp;
        if (!TextUtils.isEmpty(fileName)) {
            synchronized (sSharedPrefs) {
                sp = sSharedPrefs.get(fileName);
                if (sp == null) {
                    sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                    sSharedPrefs.put(fileName, sp);
                }
            }
        } else {
            synchronized (sSharedPrefs) {
                String defualt = context.getPackageName() + "_preferences";
                sp = sSharedPrefs.get(defualt);
                if (sp == null) {
                    sp = context.getSharedPreferences(defualt, Context.MODE_PRIVATE);
                    sSharedPrefs.put(defualt, sp);
                }
            }
        }
        return sp;
    }

    public static void removeKey(Context c, String key) {
        removeKeyPrivate(c, key, null);
    }

    public static void removeKeyPrivate(Context c, String key, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.remove(key);
        editor.commit();
    }

    public static void removeKeys(Context context, String[] keys) {
        removeKeysPrivate(context, keys, null);
    }

    public static void removeKeysPrivate(Context context, String[] keys, String fileName) {
        Editor editor = getSharedPreferences(context, fileName).edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

    public static void setLong(Context c, String key, long value) {
        setLongPrivate(c, key, value, null);
    }

    public static void setLongPrivate(Context c, String key, long value, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context c, String key, long defaultValue) {
        return getLongPrivate(c, key, defaultValue, null);
    }

    public static long getLongPrivate(Context c, String key, long defaultValue,
                                      final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        try {
            return sp.getLong(key, defaultValue);
        } catch (Exception e) {
            try {
                // compatible with old preferences XML file
                String str = sp.getString(key, null);
                return Long.valueOf(str);
            } catch (Exception ex) {
                return defaultValue;
            }
        }
    }

    public static void setFloat(Context c, String key, float value) {
        setFloatPrivate(c, key, value, null);
    }

    public static void setFloatPrivate(Context c, String key, float value, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(Context c, String key, float defaultValue) {
        return getFloatPrivate(c, key, defaultValue, null);
    }

    public static float getFloatPrivate(Context c, String key, float defaultValue,
                                        final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        try {
            return sp.getFloat(key, defaultValue);
        } catch (Exception e) {
            try {
                String str = sp.getString(key, null);
                return Float.valueOf(str);
            } catch (Exception x) {
                return defaultValue;
            }
        }
    }

    public static void setInt(Context c, String key, int value) {
        setIntPrivate(c, key, value, null);
    }

    public static void setIntPrivate(Context c, String key, int value, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static int getInt(Context c, String key, int defaultValue) {
        return getIntPrivate(c, key, defaultValue, null);
    }

    public static int getIntPrivate(Context c, String key, int defaultValue, final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        try {
            return sp.getInt(key, defaultValue);
        } catch (Exception e) {
            try {
                String str = sp.getString(key, null);
                return Integer.valueOf(str);
            } catch (Exception x) {
                return defaultValue;
            }
        }
    }


    public static double getDouble(Context c, String key, double defaultValue) {
        return getDoublePrivate(c, key, defaultValue, null);
    }

    public static double getDoublePrivate(Context c, String key, double defaultValue,
                                          final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        String str = sp.getString(key, String.valueOf(defaultValue));
        try {
            return Double.valueOf(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void setDouble(Context c, String key, double value) {
        setDoublePrivate(c, key, value, null);
    }

    public static void setDoublePrivate(Context c, String key, double value, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    public static boolean getBoolean(Context c, String key, boolean defaultValue) {
        return getBooleanPrivate(c, key, defaultValue, null);
    }

    public static boolean getBooleanPrivate(Context c, String key, boolean defaultValue,
                                            final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        return sp.getBoolean(key, defaultValue);
    }

    public static boolean setBoolean(Context c, String key, boolean value) {
        return setBooleanPrivate(c, key, value, null);
    }

    public static boolean setBooleanPrivate(Context c, String key, boolean value,
                                            final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static String getString(Context c, String key) {
        return getStringPrivate(c, key, null);
    }

    public static String getStringPrivate(Context c, String key, final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        return sp.getString(key, null);
    }

    public static String getString(Context c, String key, String defValue) {
        return getStringPrivate(c, key, defValue, null);
    }

    public static String getStringPrivate(Context c, String key, String defValue,
                                          final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        return sp.getString(key, defValue);
    }

    public static void setString(Context c, String key, String value) {
        setStringPrivate(c, key, value, null);
    }

    public static void setStringPrivate(Context c, String key, String value, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setValues(Context c, ContentValues values) {
        setValuesPrivate(c, values, null);
    }

    public static void setValuesPrivate(Context c, ContentValues values, final String fileName) {
        Editor editor = getSharedPreferences(c, fileName).edit();

        Set<Entry<String, Object>> set = values.valueSet();
        for (Entry<String, Object> entry : set) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                continue;
            }

            put(editor, key, value);
        }

        editor.commit();
    }

    public static String[] getKeysPrivate(Context c, final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        Map<String, ?> all = sp.getAll();
        String values[] = null;
        if (all != null) {
            Set<String> keys = all.keySet();
            if (keys != null) {
                values = new String[keys.size()];
                int i = 0;
                for (String key : keys) {
                    values[i++] = key;
                }
            }
        }
        return values;
    }

    /**
     * 添加一项到Editor中。 注意该方法会判断Value的类型
     */
    private static void put(Editor editor, String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
    }

    /**
     * @param c
     * @param key
     * @return
     */
    public static boolean contains(Context c, String key) {
        return containsPrivate(c, key, null);
    }

    /**
     * @param c
     * @param key
     * @param fileName
     * @return
     */
    public static boolean containsPrivate(Context c, String key, final String fileName) {
        SharedPreferences sp = getSharedPreferences(c, fileName);
        return sp.contains(key);
    }
}