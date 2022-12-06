package com.example.byeprivacy.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.byeprivacy.data.api.*
import com.example.byeprivacy.data.db.LocalCache
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.data.db.models.FriendItem
import com.example.byeprivacy.utils.AppLocation
import com.example.byeprivacy.utils.hashPassword
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log


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
        app_lat:Double,
        app_lon:Double,
        onError: (error: String) -> Unit
    ):List<BarDbItem>{
        var b= listOf<BarDbItem>()

        try {
            val response = api.barList()
            if (response.isSuccessful){
                Log.d("response_bar",response.body().toString())

                response.body()?.let { bars->
                     b = bars.map {
                        BarDbItem(
                            it.bar_id,
                            it.bar_name,
                            it.bar_type,
                            it.lat,
                            it.lon,
                            it.users
                        ).apply { distance = distanceTo(AppLocation(app_lat, app_lon)) }

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
        return b
    }
    fun _dbBars(sort_switch: Int) : LiveData<List<BarDbItem>?> {
        Log.d("barsfrom_dbBars",cache.getBars().toString())
        when(sort_switch){
            1 -> return cache.getBarsSortDistDesc()
            -1 ->  return cache.getBarsSortDistAsc()
            2-> return cache.getBarsSortNameDesc()
            -2 -> return cache.getBarsSortNameAsc()
            3-> return cache.getBarsSortUserDesc()
            -3-> return cache.getBarsSortUserAsc()
            else -> return cache.getBars()
        }
    }
    fun _dbBarItem(bar: String): String{
        return cache.getBarItem(bar).toString()
    }
    /*
    fun _dbBarsSortDist() : LiveData<List<BarDbItem>?> {
        Log.d("barsfrom_dbBars",cache.getBarsSortDist().toString())
        return cache.getBarsSortDist()
    }
*/
    suspend fun _barDetail(
        id: String,
        onError: (error: String) -> Unit
    ): BarApiItem?{
        var bar_api_item : BarApiItem? = null
        try {
            val query = "[out:json];node($id);out body;>;out skel;"
            val response=api.barDetail(query)
            Log.d("detial_query",query)
            Log.d("detail_response",response.toString())

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
                            bar.tags,
                            _dbBarItem(bar.id)
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
        Log.d("bar api item bars",bar_api_item?.users.toString())

        return bar_api_item
    }



    suspend fun _barsInRadius(
        lat:Double,
        lon:Double,
        onError: (error: String) -> Unit
    ):List<BarApiItem> {
        var nearby_bars = listOf<BarApiItem>()
        try {
            val query = "[out:json];node(around:250,$lat, $lon);(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"];);out body;>;out skel;"
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
                            it.tags,
                            //_dbBarItem(it.id)
                            ""
                        ).apply {
                            //users=_dbBarItem(it.id)
                            distance = distanceTo(AppLocation(lat, lon))
                        }

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
        Log.d("nearby bars",nearby_bars.toString())
        return nearby_bars
    }

    suspend fun _barCheckIn(
        bar: BarApiItem,
        onError: (error: String) -> Unit,
        onServiceResponse: (success: Boolean)->Unit
    ){
        try {
            val response = api.barMessage(barRequest(bar.id,bar.name,bar.type,bar.lat, bar.lon))
            Log.d("message",response.toString())
            if (response.isSuccessful){
                response.body()?.let { user ->
                    onServiceResponse(true)
                }
            }else {
                onError("Failed to check in, try again later.")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Check in failed, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Check in failed, error.")
        }
    }

    suspend fun _getFollowingFriends(
        onError: (error: String) -> Unit,
    ):List<FriendItem>{
        var friends = listOf<FriendItem>()
        try {
            val response = api.friendList()
            Log.d("message_following",response.toString())
            if (response.isSuccessful){
                response.body()?.let { friend->
                     friends = friend.map {
                        FriendItem(
                            it.user_id,
                            it.user_name,
                            it.bar_id,
                            it.bar_name,
                            it.time,
                            it.bar_lat,
                            it.bar_lon
                        )

                    }
                    cache.deleteFriends()
                    cache.insertFriends(friends)
                }?: onError("Failed to load friends")
            }else {
                onError("Failed to read friends")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load friends, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load friends, error.")
        }
        Log.d("friends from api",friends.toString())
        return friends
    }
/*
    suspend fun _getFollowersFriends(
        onError: (error: String) -> Unit,
    ):List<ContactItem>{
        var friends = listOf<ContactItem>()
        try {
            val response = api.friendAddedByMeList()
            Log.d("FOLLOWING",response.toString())
            if (response.isSuccessful){
                response.body()?.let { friend->
                     friends = friend.map {
                        ContactItem(
                            it.user_id,
                            it.user_name,
                            it.bar_id,
                            it.bar_name,
                            it.time,
                            it.bar_lat,
                            it.bar_lon
                        )

                    }
                    cache.deleteContacts()
                    cache.insertContacts(friends)
                }?: onError("Failed to load friends")
            }else {
                onError("Failed to read friends")
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load friends, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load friends, error.")
        }
        Log.d("FRIENDS",friends.toString())
        return friends
    }*/
    fun _dbFriends() : LiveData<List<FriendItem>?> {
        return cache.getFriends()
    }/*
    fun _dbContacts() : LiveData<List<ContactItem>?> {
        return cache.getContacts()
    }

 */
    suspend fun _removeFriend(
        contact: String,
        onError: (error: String) -> Unit,
        onServiceResponse: (success: Boolean)->Unit
        ){
        try {
            val response = api.removeFriend(friendRequest(contact = contact))
            Log.d("message_addFriend",response.toString())

            if (response.isSuccessful){
                response.body()?.let { friend->
                    onServiceResponse(true)
                }?: onError("Failed to remove friend")
            }else {
                onError("Failed to remove friend")
            }
        }catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load friends, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load friends, error.")
        }
    }
    suspend fun _addFriend(
        contact: String,
        onError: (error: String) -> Unit,
        onServiceResponse: (success: Boolean)->Unit
    ){
        try {
            val response = api.addFriend(friendRequest(contact = contact))
            Log.d("message_addFriend",response.toString())

            if (response.isSuccessful){
                response.body()?.let { friend->
                    onServiceResponse(true)
                }?: onError("Failed to add friend")
            }else {
                onError("Failed to add friend")
            }
        }catch (ex: IOException) {
            ex.printStackTrace()
            onError("Failed to load friends, check internet connection")
        } catch (ex: Exception) {
            ex.printStackTrace()
            onError("Failed to load friends, error.")
        }
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