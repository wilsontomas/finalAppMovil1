package service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.requestData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import service.DataService


class DataViewModel(): ViewModel() {
    private val _dataList:MutableLiveData<List<requestData>> = MutableLiveData();
    var dataList:LiveData<List<requestData>> = _dataList;
    private var _error:MutableLiveData<String> = MutableLiveData();
    val error:LiveData<String> = _error;

    fun loadData(){
        var service = DataService.getInstance();
        viewModelScope.launch {
            service.getTotalTodo().enqueue(object : Callback<List<requestData>>{
                override fun onResponse(
                    call: Call<List<requestData>>,
                    response: Response<List<requestData>>
                ) {
                   _dataList.postValue(response.body()!!);
                }

                override fun onFailure(call: Call<List<requestData>>, t: Throwable) {
                    _error.postValue(t.message);
                }
            })
        }
    }

}