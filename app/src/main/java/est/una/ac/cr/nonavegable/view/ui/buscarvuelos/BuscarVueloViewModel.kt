package est.una.ac.cr.nonavegable.view.ui.buscarvuelos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BuscarVueloViewModel : ViewModel() {
    private var _origen = MutableLiveData<String>().apply {
        value = ""
    }
     private var _destino = MutableLiveData<String>().apply {
        value = ""
    }
    private var _fechaPart = MutableLiveData<String>().apply {
        value = ""
    }
    private var _fechaReg = MutableLiveData<String>().apply {
        value = ""
    }
    private var _cantidadPas = MutableLiveData<String>().apply {
        value = ""
    }

    private var _soloIda = MutableLiveData<Boolean>().apply {
        value=false
    }
    fun setOrigen(text:String){
        _origen=MutableLiveData<String>().apply {
            value=text
        }
        origen=_origen
    }
    fun setFechPart(text:String){
        _fechaPart=MutableLiveData<String>().apply {
            value=text
        }
        fechaPart=_fechaPart
    }

    fun setDestino(text:String){
        _destino=MutableLiveData<String>().apply {
            value=text
        }
        destino=_destino
    }
    fun setCantidadPas(text:String){
        _cantidadPas=MutableLiveData<String>().apply {
            value=text
        }
        cantidadPas=_cantidadPas
    }
    fun setSoloIda(valor:Boolean){
        _soloIda=MutableLiveData<Boolean>().apply {
            value=valor
        }
        soloIda=_soloIda
    }
    fun setfechRegreso(text:String){
        _fechaReg = MutableLiveData<String>().apply {
            value=text
        }
        fechaReg=_fechaReg
    }

    var origen: LiveData<String> = _origen
    var destino: LiveData<String> = _destino
    var fechaPart: LiveData<String> = _fechaPart
    var fechaReg: LiveData<String> = _fechaReg
    var cantidadPas: LiveData<String> = _cantidadPas
    var soloIda:LiveData<Boolean> =_soloIda
}