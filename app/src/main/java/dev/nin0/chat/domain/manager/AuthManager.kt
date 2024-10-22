package dev.nin0.chat.domain.manager

import android.content.Context
import androidx.core.content.edit

class AuthManager(
    context: Context
) {

    private val sharedPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    var token: String?
        get() = sharedPrefs.getString("auth_token", null)
        set(value) = sharedPrefs.edit {
            putString("auth_token", value)
        }

    var authedUserId: String?
        get() = sharedPrefs.getString("auth_user_id", null)
        set(value) = sharedPrefs.edit {
            putString("auth_user_id", value)
        }

    val isLoggedIn
        get() = token != null

}