package est.una.ac.cr.nonavegable.view.ui.historial

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.controllers.ListaHistorialAdapter
import est.una.ac.cr.nonavegable.databinding.HistorialFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import okhttp3.*
import java.lang.IllegalArgumentException
import java.util.concurrent.Executors


class HistorialFragment : Fragment() {

    companion object {
        fun newInstance() = HistorialFragment()
    }

    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2)
    }

    private lateinit var viewModel: HistorialViewModel
    private lateinit var adaptador: ListaHistorialAdapter
    private  var _binding:HistorialFragmentBinding?=null
    private val wsPath:String = "ws://201.200.0.31/LAB001BACKEND/websockets/vuelos"
    //private val wsPath:String = "ws://echo.websocket.org"
    private lateinit var socket:WebSocket
    private lateinit var recycler:RecyclerView
    private var task:HistorialFragmentAsyncTask?=null
    private  val binding get() = _binding!!
    var client = OkHttpClient()
    var request = Request.Builder().url(this.wsPath).build()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HistorialViewModel::class.java)
        _binding = HistorialFragmentBinding.inflate(inflater,container,false)
        val root = binding.root
        adaptador= ListaHistorialAdapter(listOf(),inflater,root.context)
        viewModel.listHistorial.observe(this.viewLifecycleOwner, Observer {
            adaptador.setItems(it)
        })
        recycler = binding.recyclerHistorial
        recycler.adapter=adaptador
        recycler.layoutManager=LinearLayoutManager(recycler.context)
        recycler.setHasFixedSize(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(HistorialViewModel::class.java)
        // TODO: Use the ViewMode
        ejecutarTarea(viewModel,MethodRequest.POST.meth,3,"{\"user_name\":\"${Model.instance.user_name}\"}")
        inicializarWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        socket.close(1000,"Exit on Destroy")
    }

    fun inicializarWebSocket(){
        socket=client.newWebSocket(request, object: WebSocketListener(){
            var gson=Gson()
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                this@HistorialFragment.activity?.runOnUiThread(Runnable {
                })
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                if(text=="Actualizar"){
                    ejecutarTarea(viewModel,MethodRequest.POST.meth,3,"{\"user_name\":\"${Model.instance.user_name}\"}")
                }
            }
        })
        socket.send("No se si llega")
        //this.socket =client.newWebSocket(request,SocketListener("pepe",this.binding.root))
    }

    fun ejecutarTarea(viewModel: HistorialViewModel,method: Int,service:Int,params:String?=null){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = HistorialFragmentAsyncTask(viewModel, method, service, params)
        task?.execute()
    }

    class HistorialFragmentAsyncTask(
        private var viewModel: HistorialViewModel,
        private var method:Int,
        private var serv:Int,
        private var parametros:String?=null
    ):CoroutinesAsyncTask<Int,Int,String>("Historial Fragment"){
        enum class URLS(val service:String) {
            ENLISTAR("http://201.200.0.31/LAB001BACKEND/services/Tiquete"),
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
                else -> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var gson=Gson()
            when(serv){
                3->{
                    var sType=object :TypeToken<List<Vuelo>>(){}.type
                    var data=gson.fromJson<List<Vuelo>>(result,sType)
                    viewModel.listHistorial.value=data
                }
            }
        }

    }

}