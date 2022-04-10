package service

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.favoriteModel
import model.requestData
import model.tv_shows_model
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel(): ViewModel() {

    private val _favoriteList: MutableLiveData<List<favoriteModel>> = MutableLiveData();
    var favoriteList: MutableLiveData<List<favoriteModel>> = _favoriteList;
    private var _error: MutableLiveData<String> = MutableLiveData();
    val error: LiveData<String> = _error;
    var service = FavoriteService.getInstance();

    fun loadData(userId:String,context: Context){

        viewModelScope.launch {
            service.getAllFavorites(userId).enqueue(object : Callback<List<favoriteModel>> {
                override fun onResponse(
                    call: Call<List<favoriteModel>>,
                    response: Response<List<favoriteModel>>
                ) {
                    _favoriteList.postValue(response.body()!!);
                    //Toast.makeText(context,response.body()!![0].tvShowId,Toast.LENGTH_LONG).show();
                }

                override fun onFailure(call: Call<List<favoriteModel>>, t: Throwable) {
                    _error.postValue(t.message);
                    Toast.makeText(context,t.message,Toast.LENGTH_LONG).show();
                }
            })
        }
    }

    fun add(favorite:model.favoriteModel,context:Context){

        viewModelScope.launch {
            service.add(favorite).enqueue(object :Callback<List<String>>{
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    Toast.makeText(context,"Se agrego",Toast.LENGTH_SHORT).show();
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    _error.postValue(t.message);
                    //Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show();
                }
            })
        }
    }
    fun remove(userId:String,position:Int,context: Context){

        viewModelScope.launch {
            service.remove(userId,position).enqueue(object : Callback<List<String>>{
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                 // Toast.makeText(context,"Se removio",Toast.LENGTH_SHORT).show();
                    loadData(userId,context);
                    _favoriteList.value = _favoriteList.value!!.filter { x->x.tvShowId !=position.toString() }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    _error.postValue(t.message);
                    Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    fun exist(userId:String,position:Int):Boolean{
        var resultado:Boolean=false;

        viewModelScope.launch {
            service.verifyExist(userId,position).enqueue(object : Callback<Boolean> {
                override fun onResponse(
                    call: Call<Boolean>,
                    response: Response<Boolean>
                ) {
                    resultado=response.body()!!

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    _error.postValue(t.message);
                }
            });

        }
        return resultado;
    }
}