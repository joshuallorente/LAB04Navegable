package est.una.ac.cr.nonavegable.view.ui.checkInVuelo

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import est.una.ac.cr.nonavegable.databinding.CheckInVueloFragmentBinding
import est.una.ac.cr.nonavegable.view.ui.checkin.CheckInFragment


class CheckInVueloFragment : Fragment() {

    companion object {
        fun newInstance() = CheckInVueloFragment()
    }

    private lateinit var viewModel: CheckInVueloViewModel

    private var _binding: CheckInVueloFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CheckInVueloViewModel::class.java)
        _binding = CheckInVueloFragmentBinding.inflate(inflater,container,false)
        val root:View = binding.root

        val btn_buscar = binding.btnBuscarCheckin as Button
        val spinner_vuelos = binding.spinnerVuelos as Spinner

        val items = arrayOf("SJO-PTY", "SJO-IST", "SJO-MIA")

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(root.context, R.layout.simple_spinner_dropdown_item, items)

        spinner_vuelos.adapter=adapter

        btn_buscar.setOnClickListener(View.OnClickListener {
            var checkInFragment: CheckInFragment = CheckInFragment()
            var fragmenmanager: FragmentManager? = parentFragmentManager
            var fragTransaction: FragmentTransaction?=fragmenmanager?.beginTransaction()

            fragTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragTransaction?.replace(est.una.ac.cr.nonavegable.R.id.nav_host_fragment_content_main,checkInFragment)
            fragTransaction?.addToBackStack(null)
            fragTransaction?.commit()
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckInVueloViewModel::class.java)

        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}