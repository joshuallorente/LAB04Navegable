package est.una.ac.cr.nonavegable.view.ui.historial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import est.una.ac.cr.nonavegable.model.entities.Vuelo

class HistorialViewModel : ViewModel() {
    val listHistorial:MutableLiveData<List<Vuelo>> = MutableLiveData()
    init {
        listHistorial.value= listOf()
    }
}