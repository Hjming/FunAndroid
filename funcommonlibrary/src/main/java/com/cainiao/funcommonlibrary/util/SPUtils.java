package com.cainiao.funcommonlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;

import com.cainiao.funcommonlibrary.Constant;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * time: 2019-12-12
 * Project_Name: FunAndroid
 * Package_Name:
 * Description: 轻缓存工具类 只存储简单的配置信息
 *              默认以apply()方式提交的，如有必要，
 *              可以通过重载方法入参isCommit置为true，选择commit方式提交
 * Created by hjm.
 */
public class SPUtils {
    //存储的sharedpreferences文件名
    private static final String DEFAULT_SP_NAME = "ziroom_sp_file";

    /**
     * 存储多个不同key的SpUtils的map集合
     */
    private static SimpleArrayMap<String, SPUtils> spUtilsMap = new SimpleArrayMap<>();
    private SharedPreferences mSp;

    private SPUtils(final String spName) {
        if(null !=  Constant.sContext) {
            mSp = Constant.sContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
        }
    }

    /**
     * 获取sp对象
     *
     * @return sp对象 默认的存储sp文件名为 ziroom_sp_file
     */
    public static SPUtils getInstance() {
        return getInstance("");
    }

    /**
     * 获取sp对象 存储的sp文件名为spKeyName
     *
     * @param spKeyName 存储的sp文件名
     * @return sp对象
     */
    public static SPUtils getInstance(String spKeyName) {
        if (StringUtil.isNull(spKeyName)) {
            spKeyName = DEFAULT_SP_NAME;
        }
        SPUtils spUtils = spUtilsMap.get(spKeyName);
        if (null == spUtils) {
            spUtils = new SPUtils(spKeyName);
            spUtilsMap.put(spKeyName, spUtils);
        }
        return spUtils;
    }

    /**
     * SP中写入String类型value
     *
     * @param key   key
     * @param value value
     */
    public void putString(final String key, @NonNull final String value) {
        putString(key, value, false);
    }

    /**
     * SP中写入String类型value
     *
     * @param key      key
     * @param value    value
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     * @return isCommit为false时，永远返回false；isCommit为true时，返回commit的结果
     */
    public boolean putString(@NonNull final String key,
                             @NonNull final String value,
                             final boolean isCommit) {
        boolean success = false;
        if (isCommit) {
            success = mSp.edit().putString(key, value).commit();
        } else {
            mSp.edit().putString(key, value).apply();
        }
        return success;
    }

    /**
     * SP中读取String
     *
     * @param key key 键
     * @return 存在返回对应值，不存在返回默认值""
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    /**
     * SP中读取String
     *
     * @param key          key 键
     * @param defaultValue 不存在返回的默认值
     * @return 存在返回对应值, 不存在返回默认值defaultValue
     */
    public String getString(@NonNull final String key, @NonNull final String defaultValue) {
        return mSp.getString(key, defaultValue);
    }


    /**
     * SP中写入int类型value
     *
     * @param key   key
     * @param value value
     */
    public void putInt(final String key, @NonNull final int value) {
        putInt(key, value, false);
    }

    /**
     * SP中写入int类型value
     *
     * @param key      key
     * @param value    value
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     * @return isCommit为false时，永远返回false；isCommit为true时，返回commit的结果
     */
    public boolean putInt(@NonNull final String key,
                          @NonNull final int value,
                          final boolean isCommit) {
        boolean success = false;
        if (isCommit) {
            success = mSp.edit().putInt(key, value).commit();
        } else {
            mSp.edit().putInt(key, value).apply();
        }
        return success;
    }

    /**
     * SP中读取int
     *
     * @param key key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    /**
     * SP中读取int
     *
     * @param key          key 键
     * @param defaultValue 不存在返回的默认值
     * @return 存在返回对应值, 不存在返回默认值defaultValue
     */
    public int getInt(@NonNull final String key, @NonNull final int defaultValue) {
        return mSp.getInt(key, defaultValue);
    }

    /**
     * SP中写入long类型value
     *
     * @param key   key
     * @param value value
     */
    public void putLong(final String key, @NonNull final long value) {
        putLong(key, value, false);
    }

    /**
     * SP中写入long类型value
     *
     * @param key      key
     * @param value    value
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     */
    public void putLong(@NonNull final String key,
                        @NonNull final long value,
                        final boolean isCommit) {
        if (isCommit) {
            mSp.edit().putLong(key, value).commit();
        } else {
            mSp.edit().putLong(key, value).apply();
        }
    }

    /**
     * SP中读取long
     *
     * @param key key 键
     * @return 存在返回对应值，不存在返回默认值-1L
     */
    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          key 键
     * @param defaultValue 不存在返回的默认值
     * @return 存在返回对应值, 不存在返回默认值defaultValue
     */
    public long getLong(@NonNull final String key, @NonNull final long defaultValue) {
        return mSp.getLong(key, defaultValue);
    }


    /**
     * SP中写入boolean类型value
     *
     * @param key   key
     * @param value value
     */
    public void putBoolean(final String key, @NonNull final boolean value) {
        putBoolean(key, value, false);
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key      key
     * @param value    value
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     */
    public void putBoolean(@NonNull final String key,
                           @NonNull final boolean value,
                           final boolean isCommit) {
        if (isCommit) {
            mSp.edit().putBoolean(key, value).commit();
        } else {
            mSp.edit().putBoolean(key, value).apply();
        }
    }

    /**
     * SP中读取boolean
     *
     * @param key key 键
     * @return 存在返回对应值，不存在返回默认值false
     */
    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    /**
     * SP中读取boolean
     *
     * @param key          key 键
     * @param defaultValue 不存在返回的默认值
     * @return 存在返回对应值, 不存在返回默认值defaultValue
     */
    public boolean getBoolean(@NonNull final String key, @NonNull final boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    /**
     * SP中写入float类型value
     *
     * @param key   key
     * @param value value
     */
    public void putFloat(final String key, @NonNull final float value) {
        putFloat(key, value, false);
    }

    /**
     * SP中写入float类型value
     *
     * @param key      key
     * @param value    value
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     */
    public void putFloat(@NonNull final String key,
                         @NonNull final float value,
                         final boolean isCommit) {
        if (isCommit) {
            mSp.edit().putFloat(key, value).commit();
        } else {
            mSp.edit().putFloat(key, value).apply();
        }
    }

    /**
     * SP中读取float
     *
     * @param key key 键
     * @return 存在返回对应值，不存在返回默认值-1f
     */
    public Float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          key 键
     * @param defaultValue 不存在返回的默认值
     * @return 存在返回对应值, 不存在返回默认值defaultValue
     */
    public Float getFloat(@NonNull final String key, @NonNull final float defaultValue) {
        return mSp.getFloat(key, defaultValue);
    }


    /**
     * SP中写入Set<String>
     *
     * @param key   key
     * @param value Set<String> value
     */
    public void putStringSet(@NonNull final String key, @NonNull final Set<String> value) {
        putStringSet(key, value, false);
    }

    /**
     * SP中写入Set<String>
     *
     * @param key      key
     * @param value    Set<String> value
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     */
    public void putStringSet(@NonNull final String key,
                             @NonNull final Set<String> value,
                             final boolean isCommit) {
        if (isCommit) {
            mSp.edit().putStringSet(key, value).commit();
        } else {
            mSp.edit().putStringSet(key, value).apply();
        }
    }

    /**
     * SP中读取Set<String>
     *
     * @param key key 键
     * @return 存在返回对应值, 不存在返回默认值 Collections.<String>emptySet()
     */
    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * SP中读取Set<String>
     *
     * @param key          key 键
     * @param defaultValue 不存在返回的默认值
     * @return 存在返回对应值, 不存在返回默认值defaultValue
     */
    public Set<String> getStringSet(@NonNull final String key,
                                    @NonNull final Set<String> defaultValue) {
        return mSp.getStringSet(key, defaultValue);
    }

    /**
     * 返回所有的存储在sp中的数据
     *
     * @return
     */
    public Map<String, ?> getAll() {
        return mSp.getAll();
    }

    /**
     * 判断sp中是否存在指定存储key的数据
     *
     * @param key key
     * @return 存在返回true 不存在返回false
     */
    public boolean contains(@NonNull final String key) {
        return mSp.contains(key);
    }

    /**
     * 移除指定key的sp数据
     *
     * @param key
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }


    /**
     * 移除指定key的sp数据
     *
     * @param key      要移除的key
     * @param isCommit true 表明以commit()提交  false 以apply()形式提交
     * @return isCommit为false时，永远返回false；isCommit为true时，返回commit的结果
     */
    public boolean remove(@NonNull final String key, final boolean isCommit) {
        boolean success = false;
        if (isCommit) {
            success = mSp.edit().remove(key).commit();
        } else {
            mSp.edit().remove(key).apply();
        }
        return success;
    }

    /**
     * 移除sp中的所有数据
     */
    public void clear() {
        clear(false);
    }

    /**
     * 移除sp中的所有数据
     * @param isCommit   true 表明以commit()提交  false 以apply()形式提交
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            mSp.edit().clear().commit();
        } else {
            mSp.edit().clear().apply();
        }
    }
}
