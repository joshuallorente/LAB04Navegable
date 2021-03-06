package est.una.ac.cr.nonavegable.view.ui.vuelosida

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapter
import est.una.ac.cr.nonavegable.databinding.VuelosIdaFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.buscarvuelos.BuscarVuelo
import est.una.ac.cr.nonavegable.view.ui.checkin.CheckInFragment
import est.una.ac.cr.nonavegable.view.ui.checkout.CheckOutFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosregreso.VuelosRegresoFragment
import okhttp3.*
import java.lang.Exception
import java.lang.IllegalArgumentException

class VuelosIdaFragment : Fragment() {

    private object HOLDER{
        var INSTANCE = VuelosIdaFragment()
    }

    companion object {
        val instance : VuelosIdaFragment by lazy { HOLDER.INSTANCE }
        fun newInstance() = VuelosIdaFragment()
    }
    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2)
    }


    private lateinit var viewModel: VuelosIdaViewModel
    lateinit var recycle:RecyclerView
    private var _binding:VuelosIdaFragmentBinding?=null
    private  val binding get() = _binding!!
    private lateinit var adaptador: ListaElementosVueloAdapter
    var task:vuelosIdaAsyncTasks?=null
    var wsPath:String="ws://201.200.0.31/LAB001BACKEND/websockets/vuelos"
    var client = OkHttpClient()
    var request = Request.Builder().url(this.wsPath).build()
    private lateinit var socket:WebSocket
    private lateinit var filtro:Vuelo


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(VuelosIdaViewModel::class.java)
        _binding = VuelosIdaFragmentBinding.inflate(inflater,container,false)
        recycle=binding.fragmentViewRecycleVuelosIda
        recycle.layoutManager=LinearLayoutManager(recycle.context)
        recycle.setHasFixedSize(true)
        val root:View = binding.root
        adaptador=ListaElementosVueloAdapter(listOf(),inflater,root.context)
        viewModel.listVuelosIda.observe(this.viewLifecycleOwner, Observer {
            it
            adaptador.setItems(it)
        })
        recycle.adapter=adaptador
        binding.buttonSeleccionarIda.setOnClickListener(View.OnClickListener {
            jumpFragment(root.context)
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(VuelosIdaViewModel::class.java)
        filtro = Vuelo()
        filtro.origen=this.arguments?.get("Origen")as String
        filtro.destino=this.arguments?.get("Destino")as String
        filtro.fecha_despegue=this.arguments?.get("Fecha_partida") as String
        var gson=Gson()
        var obj = gson.toJson(filtro)
        ejecutarTarea(MethodRequest.POST.meth,3,obj)
        inicializarWebSocket()
    }

    /*private fun getListOfVuelos(

    ) {
        val vuelosList = ArrayList<Vuelo>()
        for (v in Model.instance.listaVuelo) {
            vuelosList.add(v)
        }
        adaptador = ListaElementosVueloAdapter(vuelosList,inflater,context)
        recycle.adapter = adaptador
    }*/

    fun jumpFragment(context:Context){

        var argument = this.arguments

        var soloIda : Boolean = argument?.get("SoloIda") as Boolean

        var seleccionado : Vuelo = this.adaptador.seleccionado

        var fragment : Fragment
        if(soloIda){
            fragment = CheckOutFragment()
        }else{
            fragment = VuelosRegresoFragment()
        }

        var b = Bundle()
        b.putBoolean("SoloIda",soloIda)
        b.putSerializable("VueloIda",seleccionado)
        b.putInt("Cantidad",argument.getInt("Cantidad"))
        if(!soloIda)
            b.putString("Fecha_partida",argument.getString("Fecha_regreso"));
        fragment.arguments=b

        var fragmenmanager: FragmentManager? = parentFragmentManager
        var fragTransaction: FragmentTransaction?=fragmenmanager?.beginTransaction()

        fragTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragTransaction?.replace(R.id.nav_host_fragment_content_main,fragment)
        fragTransaction?.addToBackStack(null)
        fragTransaction?.commit()
    }

    fun inicializarWebSocket(){
        socket=client.newWebSocket(request, object: WebSocketListener(){
            var gson:Gson=Gson()
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                this@VuelosIdaFragment.activity?.runOnUiThread(Runnable {
                })
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.println(Log.INFO,"Se obtuvo","Pas?? pora ac??")
                Log.println(Log.INFO,"Se obtuvo",text)
                if(text=="Actualizar"){
                    var para= gson.toJson(filtro)
                    ejecutarTarea(MethodRequest.POST.meth,3,para)
                }
            }
        })
        socket.send("Hello there")
        //this.socket =client.newWebSocket(request,SocketListener("pepe",this.binding.root))
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        socket.close(1000,"Exit of the fragment")
    }

     fun ejecutarTarea(method:Int,service:Int,params:String?=null){
        if(task?.status==Constant.Status.RUNNING){
            task?.cancel(true)
        }
         task = vuelosIdaAsyncTasks(viewModel,method,service,params)
         task?.execute()
    }

    class vuelosIdaAsyncTasks(private var viewModel:VuelosIdaViewModel,
                              private var method:Int,
                              private var serv: Int,
                              private var parametros: String?=null
                              ):CoroutinesAsyncTask<Int,Int,String>("Vuelos Ida Async"){

        enum class URLS(val service:String) {
            ENLISTAR("http://201.200.0.31/LAB001BACKEND/services/Vuelo"),
            ELIMINAR("")
        }


        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1 -> when(serv){
                        1-> return httpRequestGet(URLS.ENLISTAR.service)
                        else -> throw IllegalArgumentException("El Servicio no existe")
                    }
                2 -> when(serv){
                        3-> return httpRequestPost(URLS.ENLISTAR.service,parametros!!)
                        else -> throw IllegalArgumentException("El Servicio no existe")
                    }
                else -> throw IllegalArgumentException("El m??todo de petici??n no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            var gson:Gson=Gson()
            when(serv){
                3->{
                    var sType=object :TypeToken<List<Vuelo>>(){}.type
                    var data=gson.fromJson<List<Vuelo>>(result,sType)
                    viewModel.listVuelosIda.value=data
                }
            }
        }
    }

}
