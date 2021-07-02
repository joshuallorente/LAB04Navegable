package est.una.ac.cr.nonavegable.view.ui.checkInVuelo

import android.R
import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.databinding.CheckInVueloFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.checkin.CheckInFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import java.lang.IllegalArgumentException


class CheckInVueloFragment : Fragment() {

    companion object {
        fun newInstance() = CheckInVueloFragment()
    }

    private lateinit var viewModel: CheckInVueloViewModel
    private var _binding: CheckInVueloFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinner_vuelos:Spinner
    private lateinit var rutasArr:ArrayList<String>
    private lateinit var rutasAdapter:ArrayAdapter<String>
    private var task:CheckInVueloAsyncTask?=null

    enum class URLS(val service:String) {
        CHECK("http://201.200.0.31/LAB001BACKEND/services/Tiquete/Listar")
    }
    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CheckInVueloViewModel::class.java)
        _binding = CheckInVueloFragmentBinding.inflate(inflater,container,false)
        val root:View = binding.root

        val btn_buscar = binding.btnBuscarCheckin
        spinner_vuelos = binding.spinnerVuelos
        viewModel.listVuelo.observe(this.viewLifecycleOwner, Observer {
            rutasArr = viewModel.getRutasArrayList()
        })
        cargarVuelos(this.requireContext())

        btn_buscar.setOnClickListener(View.OnClickListener {
            var checkInFragment: CheckInFragment = CheckInFragment()
            var fragmenmanager: FragmentManager? = parentFragmentManager
            var fragTransaction: FragmentTransaction?=fragmenmanager?.beginTransaction()
            var stringId = spinner_vuelos.selectedItem as String
            var spliterator = stringId.split(" ").toTypedArray()
            var bundle=Bundle()
            bundle.putSerializable("Vuelo",viewModel.getVueloByID(spliterator[0]))
            checkInFragment.arguments=bundle

            fragTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragTransaction?.replace(est.una.ac.cr.nonavegable.R.id.nav_host_fragment_content_main,checkInFragment)
            fragTransaction?.addToBackStack(null)
            fragTransaction?.commit()
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(CheckInVueloViewModel::class.java)
        ejecutarTarea(viewModel,MethodRequest.POST.meth,3,"{\"user_name\":\"${Model.instance.user_name}\"}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun cargarVuelos(context: Context){
        rutasArr=viewModel.getRutasArrayList()
        rutasAdapter = ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,rutasArr)
        spinner_vuelos.setAdapter(rutasAdapter)
    }

    fun ejecutarTarea(viewModel: CheckInVueloViewModel,method: Int,service: Int,params:String){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = CheckInVueloAsyncTask(viewModel,method,service,this.requireContext(),params)
        task?.execute()
    }

    inner class CheckInVueloAsyncTask(
        private val viewModel: CheckInVueloViewModel,
        private var method:Int,
        private var serv:Int,
        private var context:Context,
        private var parametros:String?=null
    ):CoroutinesAsyncTask<Int,Int,String>("CheckInVueloAsyncTask"){
        var gson= Gson()

        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1 -> when(serv){
                    1-> return httpRequestGet(URLS.CHECK.service)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                2 -> when(serv){
                    3-> return httpRequestPost(URLS.CHECK.service,parametros!!)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                else -> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            when(serv){
                3->{
                    var sType=object : TypeToken<List<Vuelo>>(){}.type
                    var data=gson.fromJson<List<Vuelo>>(result,sType)
                    viewModel.listVuelo.value=data
                    cargarVuelos(context)
                }
            }
        }
    }

}