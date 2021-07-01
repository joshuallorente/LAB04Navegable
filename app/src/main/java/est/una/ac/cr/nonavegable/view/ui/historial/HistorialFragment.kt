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
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import okhttp3.*
import java.util.concurrent.Executors


class HistorialFragment : Fragment() {

    companion object {
        fun newInstance() = HistorialFragment()
    }

    private lateinit var viewModel: HistorialViewModel
    private lateinit var adaptador: ListaHistorialAdapter
    private  var _binding:HistorialFragmentBinding?=null
    private val wsPath:String = "ws://192.168.178.24:8080/examples/websocket/chat"
    //private val wsPath:String = "ws://echo.websocket.org"
    private lateinit var socket:WebSocket
    private lateinit var recycler:RecyclerView
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
        inicializarWebSocket()
    }

    fun inicializarWebSocket(){
        socket=client.newWebSocket(request, object: WebSocketListener(){
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                this@HistorialFragment.activity?.runOnUiThread(Runnable {
                })
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.println(Log.ERROR,"Se obtuvo","Pasó pora acá")
                Log.println(Log.ERROR,"Se obtuvo",text)
            }
        })
        socket.send("No se si llega")
        //this.socket =client.newWebSocket(request,SocketListener("pepe",this.binding.root))
    }




    /*private inner class SocketListener(
        private var user:String,
        private var view:View
    ) : WebSocketListener(){
        var gson:Gson = Gson()

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
               this@HistorialFragment.activity?.runOnUiThread(Runnable {
                   // Toast.makeText(view.context,"Conexión establecida.",Toast.LENGTH_SHORT)
                    var parametros = gson.toJson(object {var usua:String=user})
                    this@HistorialFragment.socket.send("Pepardo")
            })
            /*Executors.newSingleThreadExecutor().execute(Runnable {
                //Toast.makeText(view.context,"Conexión establecida.",Toast.LENGTH_SHORT)
                var parametros = gson.toJson(object {var usua:String=user})
                this@HistorialFragment.socket.send(parametros)
            })*/
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            var sType=object :TypeToken<List<Vuelo>>(){}.type
            var data=gson.fromJson<List<Vuelo>>(text,sType)
            Log.println(Log.ERROR,"Se obtuvo",text)
            //this@HistorialFragment.viewModel.listHistorial.value=data
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            this@HistorialFragment.socket.close(1000,null)
            Log.println(Log.ERROR,"Se obtuvo","Pasó pora acá")
        }


    }*/


}