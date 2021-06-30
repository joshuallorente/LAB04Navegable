package est.una.ac.cr.nonavegable.view.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TestFragmentViewModel : ViewModel() {
    val list: MutableLiveData<List<Beto>> =MutableLiveData<List<Beto>>()

    init {
        list.value= listOf()
    }
}
