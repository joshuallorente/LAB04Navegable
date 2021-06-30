package est.una.ac.cr.nonavegable.view.ui.vuelosregreso

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import est.una.ac.cr.nonavegable.model.entities.Vuelo

class VuelosRegresoViewModel : ViewModel() {
    val listVuelosRegreso:MutableLiveData<List<Vuelo>> = MutableLiveData<List<Vuelo>>()
    init {
        listVuelosRegreso.value= listOf()
    }
}