package est.una.ac.cr.nonavegable.view.ui.checkin

import androidx.lifecycle.ViewModel

class CheckInViewModel : ViewModel() {
    var map:HashMap<String, Int> =HashMap()
    fun sizeMap():Int{
        return map.size
    }

}