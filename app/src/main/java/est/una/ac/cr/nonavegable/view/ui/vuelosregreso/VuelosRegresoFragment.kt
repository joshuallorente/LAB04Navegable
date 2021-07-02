package est.una.ac.cr.nonavegable.view.ui.vuelosregreso

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapter
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapterRegreso
import est.una.ac.cr.nonavegable.databinding.VuelosIdaFragmentBinding
import est.una.ac.cr.nonavegable.databinding.VuelosRegresoFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.checkout.CheckOutFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaViewModel
import okhttp3.*
import java.io.Serializable
import java.lang.IllegalArgumentException

class VuelosRegresoFragment : Fragment() {

    private object HOLDER{
        var INSTANCE = VuelosRegresoFragment()
    }

    companion object {
        val instance : VuelosRegresoFragment by lazy { HOLDER.INSTANCE }
        fun newInstance() = VuelosRegresoFragment()
    }

    private lateinit var viewModel: VuelosRegresoViewModel
    lateinit var recycle: RecyclerView
    private var _binding: VuelosRegresoFragmentBinding?=null
    private  val binding get() = _binding!!
    private lateinit var adaptador: ListaElementosVueloAdapterRegreso
    var task:VuelosRegresoAsyncTasks?=null
    var wsPath:String="ws://201.200.0.31/LAB001BACKEND/websockets/vuelos"
    var client = OkHttpClient()
    var request = Request.Builder().url(this.wsPath).build()
    private lateinit var socket: WebSocket
    private lateinit var filtro:Vuelo

    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(VuelosRegresoViewModel::class.java)
        _binding = VuelosRegresoFragmentBinding.inflate(inflater,container,false)
        recycle=binding.recyclerVuelosRegreso
        recycle.layoutManager= LinearLayoutManager(recycle.context)
        recycle.setHasFixedSize(true)
        val root:View = binding.root

        adaptador= ListaElementosVueloAdapterRegreso(listOf(),inflater,root.context)

        viewModel.listVuelosRegreso.observe(this.viewLifecycleOwner, Observer {
            adaptador.setItems(it)
        })

        recycle.adapter=adaptador

        binding.buttonSeleccionarRegreso.setOnClickListener(View.OnClickListener {
            jumpFragment(root.context)
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(VuelosRegresoViewModel::class.java)
        filtro=Vuelo()
        var vuelo = Vuelo()
        vuelo = this.arguments?.getSerializable("VueloIda") as Vuelo
        filtro.origen= vuelo.destino
        filtro.destino=vuelo.origen
        filtro.fecha_despegue=this.arguments?.getString("Fecha_partida") as String
        var gson=Gson()
        var obj = gson.toJson(filtro)
        ejecutarTarea(MethodRequest.POST.meth,3,obj)
        inicializarWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        socket.close(1000,"Exit from fragment")
    }

    fun inicializarWebSocket() {
        socket = client.newWebSocket(request, object : WebSocketListener() {
            var gson: Gson = Gson()
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                this@VuelosRegresoFragment.activity?.runOnUiThread(Runnable {
                })
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.println(Log.INFO, "Se obtuvo", "Pasó pora acá")
                Log.println(Log.INFO, "Se obtuvo", text)
                if (text == "Actualizar") {
                    var para = gson.toJson(filtro)
                    ejecutarTarea(MethodRequest.POST.meth, 3, para)
                }
            }
        })
        socket.send("Hello there")
        //this.socket =client.newWebSocket(request,SocketListener("pepe",this.binding.root))
    }

    /*private fun getListOfVuelos(
        inflater: LayoutInflater,
        context: Context,
        recycle:RecyclerView,
    ) {
        val vuelosList = ArrayList<Vuelo>()
        for (v in Model.instance.listaVuelo) {
            vuelosList.add(v)
        }
        adaptador = ListaElementosVueloAdapterRegreso(vuelosList,inflater,context)
        recycle.adapter = adaptador
    }*/

    fun ejecutarTarea(method:Int,service:Int,params:String?=null){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = VuelosRegresoAsyncTasks(viewModel, method, service, params)
        task?.execute()
    }

    fun jumpFragment(context:Context){
        var fragment : CheckOutFragment = CheckOutFragment()

        var argument = this.arguments

        var seleccionado : Vuelo = this.adaptador.seleccionado

        var b = Bundle()
        b.putSerializable("VueloRegreso",seleccionado)
        b.putSerializable("VueloIda",argument?.get("VueloIda") as Vuelo)
        b.putBoolean("SoloIda",false)
        b.putInt("Cantidad",argument.getInt("Cantidad"))
        fragment.arguments=b

        var fragmenmanager: FragmentManager? = parentFragmentManager
        var fragTransaction: FragmentTransaction?=fragmenmanager?.beginTransaction()

        fragTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragTransaction?.replace(R.id.nav_host_fragment_content_main,fragment)
        fragTransaction?.addToBackStack(null)
        fragTransaction?.commit()
    }



    class VuelosRegresoAsyncTasks(private var viewModel: VuelosRegresoViewModel,
                                  private var method:Int,
                                  private var serv: Int,
                                  private var parametros: String?=null
    ): CoroutinesAsyncTask<Int, Int, String>("Vuelos Regreso Async"){

        enum class URLS(val service:String) {
            ENLISTAR("http://201.200.0.31/LAB001BACKEND/services/Vuelo"),
            ELIMINAR("")
        }

        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1 -> when(serv){
                    1-> return httpRequestGet(VuelosIdaFragment.vuelosIdaAsyncTasks.URLS.ENLISTAR.service)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                2 -> when(serv){
                    3-> return httpRequestPost(VuelosIdaFragment.vuelosIdaAsyncTasks.URLS.ENLISTAR.service,parametros!!)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                else -> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            var gson: Gson = Gson()
            when(serv){
                3->{
                    var sType=object : TypeToken<List<Vuelo>>(){}.type
                    var data=gson.fromJson<List<Vuelo>>(result,sType)
                    viewModel.listVuelosRegreso.value=data
                }
            }
        }

    }

}