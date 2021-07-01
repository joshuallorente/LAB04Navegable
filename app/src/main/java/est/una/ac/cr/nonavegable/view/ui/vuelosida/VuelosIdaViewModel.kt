package est.una.ac.cr.nonavegable.view.ui.vuelosida

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import est.una.ac.cr.nonavegable.model.entities.Vuelo

class VuelosIdaViewModel : ViewModel() {
    val listVuelosIda:MutableLiveData<List<Vuelo>> = MutableLiveData<List<Vuelo>>()
    init{
        listVuelosIda.value = listOf(Vuelo())
    }
}