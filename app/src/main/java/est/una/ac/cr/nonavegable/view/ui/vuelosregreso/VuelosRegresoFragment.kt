package est.una.ac.cr.nonavegable.view.ui.vuelosregreso

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import est.una.ac.cr.nonavegable.R

class VuelosRegresoFragment : Fragment() {

    companion object {
        fun newInstance() = VuelosRegresoFragment()
    }

    private lateinit var viewModel: VuelosRegresoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.vuelos_regreso_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VuelosRegresoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}