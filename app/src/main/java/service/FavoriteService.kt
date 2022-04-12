package service

import model.favoriteModel
import model.requestData
import model.tv_shows_model
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface FavoriteService {
    @GET("{userId}")
    fun getAllFavorites(@Path("userId") userId:String): Call<List<favoriteModel>>

    @GET("exist/{userId}/{position}")
    fun verifyExist(@Path("userId") id:String,@Path("position") position:Int):Call<Boolean>

    @POST("add")
    fun add(@Body valor:favoriteModel):Call<List<String>>

    @DELETE("delete/{userId}/{position}")
    fun remove(@Path("userId") userId:String,@Path("position") position:Int ):Call<List<String>>


    companion object{
        private var _instance:FavoriteService?=null;
        fun getInstance():FavoriteService{
            if(_instance==null){
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://d485-181-36-66-217.ngrok.io/api/favorites/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(OkHttpClient.Builder().build()).build();
                _instance = retrofit.create(FavoriteService::class.java);
            }
            return _instance!!;
        }
    }
}