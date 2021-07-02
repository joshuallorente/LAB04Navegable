package est.una.ac.cr.nonavegable.view.ui.checkInVuelo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import est.una.ac.cr.nonavegable.model.entities.Vuelo

class CheckInVueloViewModel : ViewModel() {
   val listVuelo:MutableLiveData<List<Vuelo>> = MutableLiveData()
    init {
        listVuelo.value= listOf()
    }

    fun getRutasArrayList():ArrayList<String>{
        var result = ArrayList<String>()
        for(r in listVuelo.value!!){
            result.add(r.getStringruta())
        }
        return result
    }

    fun getVueloByID(id:String):Vuelo?{
        for(v in listVuelo.value!!){
            if(v.id == id.toInt())
                return v;
        }
        return null;
    }

}