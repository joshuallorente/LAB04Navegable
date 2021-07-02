package est.una.ac.cr.nonavegable.view.ui.buscarvuelos


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.BuscarVueloFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Ruta
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList


class BuscarVuelo : Fragment() {

    companion object {
        fun newInstance() = BuscarVuelo()
    }

    private lateinit var viewModel: BuscarVueloViewModel

    private var _binding: BuscarVueloFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var origen: Spinner
    private lateinit var destino: Spinner
    private lateinit var origenesArr : ArrayList<String>
    private lateinit var destinosArr : ArrayList<String>
    private lateinit var adapter_origen: ArrayAdapter<String>
    private lateinit var adapter_destino: ArrayAdapter<String>
    private var task1:buscarVuelosAsyncTask?=null
    private var task2:buscarVuelosAsyncTask?=null

    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2)
    }
    enum class URLS(val service:String) {
        ORIGENES("http://201.200.0.31/LAB001BACKEND/services/Vuelo/Origen"),
        DESTINOS("http://201.200.0.31/LAB001BACKEND/services/Vuelo/Destino")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BuscarVueloViewModel::class.java)
        _binding = BuscarVueloFragmentBinding.inflate(inflater,container,false)
        val root:View = binding.root

        Model.instance.init()

        origen= binding.multiOrigen
        destino= binding.multiDestino
        viewModel.destinoList.observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            destinosArr=viewModel.getArrayListDestino()
        })
        viewModel.origenList.observe(this.viewLifecycleOwner, androidx.lifecycle.Observer {
            origenesArr=viewModel.getArrayListOrigen()
        })
        cargarVuelos(root.context)

        val fechPartida: TextInputLayout = binding.textFechPartida
        fechPartida.editText?.setText(viewModel.fechaPart.value)
        fechPartida.editText?.setText(viewModel.fechaPart.value)
        val fechRegreso:TextInputLayout = binding.textFechRegreso
        fechRegreso.editText?.setText(viewModel.fechaReg.value)
        val cantidadAsientos:Spinner= binding.textCatidadAsientos
        /*cantidadAsientos.editText?.setText(viewModel.cantidadPas.value.toString())
        cantidadAsientos.editText?.setOnKeyListener { v, keyCode, event ->
            viewModel.setCantidadPas(cantidadAsientos.editText?.text.toString())
            true
        }*/
        var cantidadAsAdapter=ArrayAdapter<Int>(root.context,android.R.layout.simple_dropdown_item_1line,Model.instance.listaCantidadAsientos)
        cantidadAsientos.adapter = cantidadAsAdapter
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
            b.putString("Origen",origen.selectedItem.toString())
            b.putString("Destino",destino.selectedItem.toString())
            b.putString("Fecha_partida",fechPartida.editText?.text.toString())
            b.putInt("Cantidad",cantidadAsientos.selectedItem.toString().toInt())
            if(!soloIda.isActivated)
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
        ejecutarTarea1(viewModel,MethodRequest.GET.meth,1)
        ejecutarTarea2(viewModel,MethodRequest.GET.meth,2)
        // TODO: Use the ViewModel
    }

    fun ejecutarTarea1(viewModel: BuscarVueloViewModel,method: Int,service: Int,params:String?=null){
        if(task1?.status== Constant.Status.RUNNING){
            task1?.cancel(true)
        }
        task1 = buscarVuelosAsyncTask(viewModel, method, service, this.requireContext())
        task1?.execute()
    }
    fun ejecutarTarea2(viewModel: BuscarVueloViewModel,method: Int,service: Int,params:String?=null){
        if(task2?.status== Constant.Status.RUNNING){
            task2?.cancel(true)
        }
        task2 = buscarVuelosAsyncTask(viewModel, method, service, this.requireContext())
        task2?.execute()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun cargarVuelos(context: Context){
        origenesArr = viewModel.getArrayListOrigen()
        destinosArr = viewModel.getArrayListDestino()
        adapter_origen = ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, origenesArr)
        adapter_destino = ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line, destinosArr)
        origen.setAdapter(adapter_origen)
        destino.setAdapter(adapter_destino)
    }

    inner class buscarVuelosAsyncTask(
        private var viewModel: BuscarVueloViewModel,
        private var method:Int,
        private var serv:Int,
        private var context: Context,
        private var parametros: String?=null,
    ):CoroutinesAsyncTask<Int,Int,String>("Buscar Vuelos Async Task"){


        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1 -> when(serv){
                    1-> return httpRequestGet(URLS.ORIGENES.service)
                    2-> return httpRequestGet(URLS.DESTINOS.service)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                2 -> when(serv){
                    3-> return httpRequestPost(URLS.ORIGENES.service,parametros!!)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                else -> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var gson: Gson = Gson()
            when(serv){
                1->{
                    var sType=object : TypeToken<List<Ruta>>(){}.type
                    var data=gson.fromJson<List<Ruta>>(result,sType)
                    viewModel.origenList.value=data
                    cargarVuelos(context)
                }
                2->{
                    var sType=object : TypeToken<List<Ruta>>(){}.type
                    var data=gson.fromJson<List<Ruta>>(result,sType)
                    viewModel.destinoList.value=data
                    cargarVuelos(context)
                }
            }
        }

    }

}