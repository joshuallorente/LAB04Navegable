package est.una.ac.cr.nonavegable.view.ui.buscarvuelos

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.controllers.MainActivity
import est.una.ac.cr.nonavegable.databinding.BuscarVueloFragmentBinding
import est.una.ac.cr.nonavegable.model.Model
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.checkInVuelo.CheckInVueloFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosregreso.VuelosRegresoFragment
import java.util.*
import kotlin.collections.ArrayList


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

        Model.instance.init()
        cargarVuelos(root.context,binding)

        val origen: TextInputLayout = binding.textOrigen
        origen.editText?.setText(viewModel.origen.value)
        origen.editText?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            viewModel.setOrigen(origen.editText?.text.toString())
            true})
        val destino: TextInputLayout = binding.textDestino
        destino.editText?.setText(viewModel.destino.value)
        destino.editText?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            viewModel.setDestino(destino.editText?.text.toString())
            true
        })
        val fechPartida: TextInputLayout = binding.textFechPartida
        fechPartida.editText?.setText(viewModel.fechaPart.value)
        fechPartida.editText?.setText(viewModel.fechaPart.value)
        val fechRegreso:TextInputLayout = binding.textFechRegreso
        fechRegreso.editText?.setText(viewModel.fechaReg.value)
        val cantidadAsientos:TextInputLayout= binding.textCantidadAsientos
        /*cantidadAsientos.editText?.setText(viewModel.cantidadPas.value.toString())
        cantidadAsientos.editText?.setOnKeyListener { v, keyCode, event ->
            viewModel.setCantidadPas(cantidadAsientos.editText?.text.toString())
            true
        }*/
        val soloIda:CheckBox=binding.checkSoloIda
        soloIda.isActivated= viewModel.soloIda.value == true
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
                    { view, year, monthOfYear, dayOfMonth ->
                        var month= if((monthOfYear+1)<9) "0"+(monthOfYear+1) else ""+(monthOfYear+1)
                        var day= if((dayOfMonth)<9) "0"+(dayOfMonth) else ""+(dayOfMonth)
                        fechPartida.editText?.setText("$year-" + month + "-"+day)
                        viewModel.setFechPart(fechPartida.editText?.text.toString())},
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
                    { view, year, monthOfYear, dayOfMonth ->
                        var month= if((monthOfYear+1)<9) "0"+(monthOfYear+1) else ""+(monthOfYear+1)
                        var day= if((dayOfMonth)<9) "0"+(dayOfMonth) else ""+(dayOfMonth)
                        fechRegreso.editText?.setText("$year-" + month + "-"+day)
                        viewModel.setfechRegreso(fechRegreso.editText?.text.toString())},
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
                viewModel.setSoloIda(soloIda.isActivated)
            }else if(soloIda.isActivated){
                fechRegreso.visibility=View.VISIBLE
                soloIda.isActivated=false
                viewModel.setSoloIda(soloIda.isActivated)
            }
        })
        buscar.setOnClickListener(View.OnClickListener {
            //var i = Intent(context,MainActivity::class.java)
            var b = Bundle()
            b.putBoolean("SoloIda",soloIda.isActivated)
            b.putString("Origen",origen.editText?.text.toString())
            b.putString("Destino",destino.editText?.text.toString())
            b.putString("Fecha_partida",fechPartida.editText?.text.toString())
            b.putInt("Cantidad",cantidadAsientos.editText?.text.toString().toInt())
            if(soloIda.isActivated)
                b.putString("Fecha_regreso",fechRegreso.editText?.text.toString())


            //TareaAsigcronica

            var vuelosIdaFragment: VuelosIdaFragment = VuelosIdaFragment()
            vuelosIdaFragment.arguments=b
            var fragmenmanager: FragmentManager? = parentFragmentManager
            var fragTransaction: FragmentTransaction?=fragmenmanager?.beginTransaction()

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

    fun cargarVuelos(context: Context, binding:BuscarVueloFragmentBinding){
        var vuelos : ArrayList<Vuelo> = Model.instance.listaVuelo

        var origenes : ArrayList<String> = ArrayList()
        var destinos : ArrayList<String> = ArrayList()

        for(vuelo in vuelos){
            if(origenes.find {it -> it == vuelo.origen.toString()}==null)
                origenes.add(vuelo.origen.toString())
            if(destinos.find {it -> it == vuelo.destino.toString()}==null)
                destinos.add(vuelo.destino.toString())
        }

        val adapter_origen: ArrayAdapter<String> = ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, origenes)
        val adapter_destino: ArrayAdapter<String> = ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, destinos)

        val origen: MultiAutoCompleteTextView = binding.multiOrigen
        val destino: MultiAutoCompleteTextView = binding.multiDestino

        origen.setAdapter(adapter_origen)
        destino.setAdapter(adapter_destino)

        origen.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        destino.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
    }

}