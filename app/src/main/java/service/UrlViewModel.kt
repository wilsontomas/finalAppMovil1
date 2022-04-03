package service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.requestData

class UrlViewModel():ViewModel() {

    private val _urlContent:MutableLiveData<String> = MutableLiveData();
    var urlContent:LiveData<String> = _urlContent;

    fun setUrl(url:String){
        _urlContent.value=url;
    }

}