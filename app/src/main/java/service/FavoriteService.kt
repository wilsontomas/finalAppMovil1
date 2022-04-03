package service

import model.requestData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface FavoriteService {
    @GET("most-popular?page=1")
    fun getAllFavorites(@Url url:String,): Call<requestData>

    companion object{
        private var _instance:DataService?=null;
        fun getInstance():DataService{
            if(_instance==null){
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://www.episodate.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().build()).build();
                _instance = retrofit.create(DataService::class.java);
            }
            return _instance!!;
        }
    }
}