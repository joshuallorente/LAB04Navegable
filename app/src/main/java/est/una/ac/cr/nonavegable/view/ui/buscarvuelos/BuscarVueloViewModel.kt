package est.una.ac.cr.nonavegable.view.ui.buscarvuelos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BuscarVueloViewModel : ViewModel() {
    private val _origen = MutableLiveData<String>().apply {
        value = ""
    }
    private val _destino = MutableLiveData<String>().apply {
        value = ""
    }
    private val _fechaPart = MutableLiveData<String>().apply {
        value = ""
    }
    private val _fechaReg = MutableLiveData<String>().apply {
        value = ""
    }
    private val _cantidadPas = MutableLiveData<String>().apply {
        value = ""
    }
    val origen: LiveData<String> = _origen
    val destino: LiveData<String> = _destino
    val fechaPart: LiveData<String> = _fechaPart
    val fechaReg: LiveData<String> = _fechaReg
    val cantidadPas: LiveData<String> = _cantidadPas
}