package com.example.byeprivacy.data.api.helpers

import android.content.Context
import com.example.byeprivacy.data.api.RestApi
import com.example.byeprivacy.data.api.UserRefreshRequest
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Route

class TokenAuth(val context: Context) : Authenticator {
    override fun authenticate(route: Route?, response: okhttp3.Response): Request? {
        synchronized(this) {
            if (response.request.header("mobv-auth")
                    ?.compareTo("accept") == 0 && response.code == 401
            ) {
                val userItem = PreferenceData.getInstance().getUserItem(context)

                if (userItem == null) {
                    PreferenceData.getInstance().clearData(context)
                    return null
                }

                val tokenResponse = RestApi.create(context).userRefresh(
                    UserRefreshRequest(
                        userItem.refresh
                    )
                ).execute()

                if (tokenResponse.isSuccessful) {
                    tokenResponse.body()?.let {
                        PreferenceData.getInstance().putUserItem(context, it)
                        return response.request.newBuilder()
                            .header("authorization", "Bearer ${it.access}")
                            .build()
                    }
                }

                PreferenceData.getInstance().clearData(context)
                return null


            }
        }
        return null
    }
}