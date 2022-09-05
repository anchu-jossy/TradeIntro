package com.techxform.tradintro.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.techxform.tradintro.core.utils.Contants.PREFERENCE
import com.techxform.tradintro.core.utils.Contants.PREF_FCM_TOKEN_KEY
import com.techxform.tradintro.core.utils.Contants.PREF_FCM_TOKEN_SYNC_KEY
import com.techxform.tradintro.core.utils.Contants.PREF_REFRESH_TOKEN_KEY
import com.techxform.tradintro.core.utils.Contants.PREF_TOKEN_KEY
import com.techxform.tradintro.core.utils.Contants.USER_ID_KEY

object PreferenceHelper {



    fun customPreference(context: Context): SharedPreferences  {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


        return EncryptedSharedPreferences.create(
            context,
            PREFERENCE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.refreshToken
        get() = getString(PREF_REFRESH_TOKEN_KEY, "")
        set(value) {
            editMe {
                it.putString(PREF_REFRESH_TOKEN_KEY, value)
            }
        }

    var SharedPreferences.token
        get() = getString(PREF_TOKEN_KEY, "")
        set(value) {
            editMe {
                it.putString(PREF_TOKEN_KEY, value)
            }
        }
    var SharedPreferences.userId
        get() = getInt(USER_ID_KEY, 0)
        set(value) {
            editMe {
                it.putInt(USER_ID_KEY, value)
            }
        }
    var SharedPreferences.fcmToken
        get() = getString(PREF_FCM_TOKEN_KEY, "")
        set(value) {
            editMe {
                it.putString(PREF_FCM_TOKEN_KEY, value)
            }
        }

    var SharedPreferences.isFcmTokenSync
        get() = getBoolean(PREF_FCM_TOKEN_SYNC_KEY, false)
        set(value) {
            editMe {
                it.putBoolean(PREF_FCM_TOKEN_SYNC_KEY, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.clear()
            }
        }
}