package est.una.ac.cr.nonavegable.view.ui.checkin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import est.una.ac.cr.nonavegable.model.entities.Asiento
import est.una.ac.cr.nonavegable.model.entities.Tiquete

class CheckInViewModel : ViewModel() {
    fun sizeMap():Int{
        return matrizAsientos.value!!.size
    }
    val matrizAsientos:MutableLiveData<HashMap<String,Int>> = MutableLiveData()
    val listTiquetes:MutableLiveData<List<Tiquete>> = MutableLiveData()
    val listAsientos:MutableLiveData<List<Asiento>> = MutableLiveData()

    fun setOnMap(list: List<Asiento>){
        listAsientos.value=list
        var temp = HashMap<String,Int>()
        for(a in list){
            temp.put("${a.columna}${a.fila}",1)
        }
        matrizAsientos.value=temp
    }

    init {
        matrizAsientos.value= HashMap()
        listTiquetes.value = listOf()
        listAsientos.value = listOf()
    }

}