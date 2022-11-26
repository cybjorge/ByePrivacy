package com.example.byeprivacy.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.byeprivacy.data.api.*
import com.example.byeprivacy.data.db.LocalCache
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.utils.AppLocation
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
                //Log.d("response_bar",response.body().toString())

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
    suspend fun _barDetail(
        id: String,
        onError: (error: String) -> Unit
    ): BarApiItem?{
        var bar_api_item : BarApiItem? = null
        try {
            val query = "[out:json];node($id);out body;>;out skel;"
            val response=api.barDetail(query)
            if (response.isSuccessful){
                response.body()?.let { bars ->
                    if(bars.elements.isNotEmpty()){
                        val bar = bars.elements.get(0)
                        bar_api_item= BarApiItem(
                            bar.id,
                            bar.tags.getOrDefault("name",""),
                            bar.tags.getOrDefault("amenity",""),
                            bar.lat,
                            bar.lon,
                            bar.tags
                        )
                    }

                }?: onError("Failed to load bar")
            }
            else{
                onError("Failed to read bar")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load bars, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load bars, error.")
        }
        return bar_api_item
    }

    suspend fun _barsInRadius(
        lat:Double,
        lon:Double,
        onError: (error: String) -> Unit
    ):List<BarApiItem>? {
        var nearby_bars = listOf<BarApiItem>()
        try {
            val query = "data=[out:json];node(around:250,$lat, $lon)" +
                    ";(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"]" +
                    ";);out body;>;out skel;"
            val response = api.barsInRadius(query)
            if (response.isSuccessful) {
                response.body()?.let { nearby ->
                    nearby_bars = nearby.elements.map {
                        BarApiItem(
                            it.id,
                            it.tags.getOrDefault("name", ""),
                            it.tags.getOrDefault("amenity", ""),
                            it.lat,
                            it.lon,
                            it.tags
                        ).apply { distance = distanceTo(AppLocation(lat, lon)) }

                    }
                    nearby_bars =
                        nearby_bars.filter { it.name.isNotBlank() }.sortedBy { it.distance }
                } ?: onError("Failed to load bars")
            } else {
                onError("Failed to read bars")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load bars, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load bars, error.")
        }
        return nearby_bars
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