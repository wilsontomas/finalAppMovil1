package service

import model.requestData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface DataService {

    @GET("/todos")
    fun getTotalTodo():Call<List<requestData>>

    companion object{
        private var _instance:DataService?=null;
        fun getInstance():DataService{
            if(_instance==null){
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().build()).build();
                _instance = retrofit.create(DataService::class.java);
            }
            return _instance!!;
        }
    }
}