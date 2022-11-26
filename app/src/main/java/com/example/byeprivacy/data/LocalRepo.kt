package com.example.byeprivacy.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.byeprivacy.data.api.*
import com.example.byeprivacy.data.db.LocalCache
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.utils.hashPassword
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class LocalRepo private constructor(
    private val api : RestApi,
    private val cache : LocalCache
){
    suspend fun _userCreate(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onServiceResponse: (success: UserResponse?)->Unit
    ){
        try {
            val hashed_password = hashPassword(password)
            val response = api.userCreate(UserCreateRequest(name = name,password = hashed_password))
            Log.d("user_sign_nick",name)
            Log.d("user_sign_pwd",password)
            Log.d("user_sign_pwd_hash",hashed_password)
            if (response.isSuccessful){
                Log.d("response",response.body().toString())
                response.body()?.let{ user ->
                    if (user.uid == "-1"){
                        onServiceResponse(null)
                        onError("Name is already in use. Choose another")
                    }else{
                        onServiceResponse(user)
                    }?: onError("Failed to load bars")

                }
            }
            else{
                onError("Failed to sign up, try again later.")
                onServiceResponse(null)
            }
        }
        catch (ex: IOException) {
            ex.printStackTrace()
            onError("Sign up failed, check internet connection")
            onServiceResponse(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Sign up failed, error.")
            onServiceResponse(null)
        }

    }

    suspend fun _userLogIn(
        name: String,
        password: String,
        onError: (error: String) -> Unit,
        onServiceResponse: (success: UserResponse?)->Unit
    ){
        try {
            val hashed_password = hashPassword(password)
            val response = api.userLogin(UserLoginRequest(name = name,password = hashed_password))
            Log.d("user_log_in_nick",name)
            Log.d("user_log_in_pwd",password)
            Log.d("user_log_in_pwd_hash",hashed_password)
            if (response.isSuccessful){
                response.body()?.let{ user ->
                    if (user.uid == "-1"){
                        onServiceResponse(null)
                        onError("Username or password is wrong, try again.")
                    }else{
                        onServiceResponse(user)
                    }?: onError("Failed to load bars")

                }
            }
            else{
                onError("Failed to log in, try again later.")
                onServiceResponse(null)
            }
        }
        catch (ex: IOException) {
            ex.printStackTrace()
            onError("Login failed, check internet connection")
            onServiceResponse(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Login failed, error.")
            onServiceResponse(null)
        }

    }
    suspend fun _barList(
        onError: (error: String) -> Unit
    ){
        try {
            val response = api.barList()
            if (response.isSuccessful){
                Log.d("response_bar",response.body().toString())

                response.body()?.let { bars->
                    val b = bars.map {
                        BarDbItem(
                            it.bar_id,
                            it.bar_name,
                            it.bar_type,
                            it.lat,
                            it.lon,
                            it.users
                        )

                    }
                    cache.deleteBars()
                    cache.insertBars(b)
                }?: onError("Failed to load bars")
            }else {
                onError("Failed to read bars")
            }
        }
        catch (ex: IOException) {
            ex.printStackTrace()
            onError(ex.printStackTrace().toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError(ex.printStackTrace().toString())
        }
    }
    fun _dbBars() : LiveData<List<BarDbItem>?> {
        return cache.getBars()
    }


    companion object{
        @Volatile
        private var INSTANCE: LocalRepo? = null

        fun getInstance(service: RestApi, cache: LocalCache): LocalRepo =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: LocalRepo(service, cache).also { INSTANCE = it }
            }

        @SuppressLint("SimpleDateFormat")
        fun dateToTimeStamp(date: String): Long {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)?.time ?: 0L
        }

        @SuppressLint("SimpleDateFormat")
        fun timestampToDate(time: Long): String{
            val netDate = Date(time*1000)
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(netDate)
        }
    }
}