package est.una.ac.cr.nonavegable.view.ui.buscarvuelos

import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputLayout
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.BuscarVueloFragmentBinding
import est.una.ac.cr.nonavegable.view.ui.checkInVuelo.CheckInVueloFragment
import est.una.ac.cr.nonavegable.view.ui.checkin.CheckInFragment
import est.una.ac.cr.nonavegable.view.ui.checkout.CheckOutFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import java.util.*

class BuscarVuelo : Fragment() {

    companion object {
        fun newInstance() = BuscarVuelo()
    }

    private lateinit var viewModel: BuscarVueloViewModel
    private var _binding: BuscarVueloFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BuscarVueloViewModel::class.java)
        _binding = BuscarVueloFragmentBinding.inflate(inflater,container,false)
        val root:View = binding.root

        val origen: TextInputLayout = binding.textOrigen
        val destino: TextInputLayout = binding.textDestino
        val fechPartida: TextInputLayout = binding.textFechPartida
        val fechRegreso:TextInputLayout = binding.textFechRegreso
        val cantidadAsientos:TextInputLayout= binding.textCantidadAsientos
        val soloIda:CheckBox=binding.checkSoloIda
        val buscar:Button=binding.btnBuscar
        var picker:DatePickerDialog
        fechPartida.editText?.inputType=InputType.TYPE_NULL
        fechRegreso.editText?.inputType=InputType.TYPE_NULL
        fechPartida.editText?.setOnClickListener(
            View.OnClickListener {
                val cldr: Calendar = Calendar.getInstance()
                val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
                val month: Int = cldr.get(Calendar.MONTH)
                val year: Int = cldr.get(Calendar.YEAR)
                // date picker dialog
                picker = DatePickerDialog(it.context,
                    { view, year, monthOfYear, dayOfMonth -> fechPartida.editText?.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    year,
                    month,
                    day
                )
                picker.datePicker.minDate=System.currentTimeMillis()-1000
                picker.show()})
        fechRegreso.editText?.setOnClickListener(
            View.OnClickListener {
                val cldr: Calendar = Calendar.getInstance()
                val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
                val month: Int = cldr.get(Calendar.MONTH)
                val year: Int = cldr.get(Calendar.YEAR)
                // date picker dialog
                picker = DatePickerDialog(it.context,
                    { view, year, monthOfYear, dayOfMonth -> fechRegreso.editText?.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    year,
                    month,
                    day
                )
                picker.datePicker.minDate=System.currentTimeMillis()-1000
                picker.show()})

        soloIda.setOnClickListener(View.OnClickListener {
            if(!soloIda.isActivated){
                fechRegreso.visibility=View.GONE
                soloIda.isActivated=true
            }else if(soloIda.isActivated){
                fechRegreso.visibility=View.VISIBLE
                soloIda.isActivated=false
            }
        })
        buscar.setOnClickListener(View.OnClickListener {
            /*var i = Intent(context,VuelosIdaFragment::class.java)
            i.putExtra("SoloIda",soloIda.isActivated)*/
            //TareaAsigcronica
            var vuelosIdaFragment:CheckInVueloFragment = CheckInVueloFragment()
            var fragmenmanager: FragmentManager? = parentFragmentManager
            var fragTransaction:FragmentTransaction?=fragmenmanager?.beginTransaction()

            fragTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragTransaction?.replace(R.id.nav_host_fragment_content_main,vuelosIdaFragment)
            fragTransaction?.addToBackStack(null)
            fragTransaction?.commit()
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BuscarVueloViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}