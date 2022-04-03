package service

import model.favoriteModel
import model.requestData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UrlService {
   // @GET("most-popular?page=1")
    @GET
    fun getFavoriteList(@Url direccion:String,@Field("userId") user:String): Call<List<favoriteModel>>

    @GET
    fun verifyExist(@Url direccion:String,@Field("userId") user:String, @Field("position") position:String):Call<Boolean>

    @POST
    fun addFavorite(@Url direccion:String,@Field("favorite") data:favoriteModel)

    @DELETE
    fun removeFavorite(@Url direccion: String,@Field("userId") user:String, @Field("position") position:String )

    companion object{
        private var _instance:UrlService?=null;
        fun getInstance():UrlService{
            if(_instance==null){
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().build()).build();
                _instance = retrofit.create(UrlService::class.java);
            }
            return _instance!!;
        }
    }
}