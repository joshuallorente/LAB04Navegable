package est.una.ac.cr.nonavegable.view.ui.checkin

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.CheckInFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Asiento
import est.una.ac.cr.nonavegable.model.entities.Tiquete
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import okhttp3.*
import java.lang.IllegalArgumentException
import java.text.ParseException

class CheckInFragment : Fragment() {

    companion object {
        fun newInstance() = CheckInFragment()
    }

    private lateinit var viewModel: CheckInViewModel
    private var _binding: CheckInFragmentBinding? = null
    private var asienPorEnviar:ArrayList<Asiento> = arrayListOf()
    private val binding get() = _binding!!
    val AsientosLayout: HashMap<String, TextView> = HashMap()
    private var filas: Int = 0
    private var columnas: Int = 0
    private var cantidad = 0
    private lateinit var vuelo:Vuelo
    private var task1:CheckInAsyncTask?=null
    private var task2:CheckInAsyncTask?=null

    var wsPath:String="ws://201.200.0.31/LAB001BACKEND/websockets/checkin"
    var client = OkHttpClient()
    var request = Request.Builder().url(this.wsPath).build()

    var wsPath2:String="ws://201.200.0.31/LAB001BACKEND/websockets/checkinreserva"
    var client2 = OkHttpClient()
    var request2 = Request.Builder().url(this.wsPath2).build()

    var socketId = 0
    private lateinit var socket:WebSocket
    private lateinit var socket2:WebSocket

    enum class MethodRequest(var meth:Int){
        GET(1),
        POST(2)
    }

    enum class URLS(var service:String){
        ENLISTAR_OCUPADOS("http://201.200.0.31/LAB001BACKEND/services/Tiquete/ListarAsientosVuelo"),
        ENLISTAR_TIQUETES("http://201.200.0.31/LAB001BACKEND/services/Tiquete/ListarTiquetesVuelo"),
        ENVIAR_TIQUETE("http://201.200.0.31/LAB001BACKEND/services/Tiquete/Checkin")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CheckInViewModel::class.java)
        _binding = CheckInFragmentBinding.inflate(inflater,container,false)
        vuelo = arguments?.getSerializable("Vuelo") as Vuelo
        filas = vuelo.tipo_avion!!.max_fila
        columnas = vuelo.tipo_avion!!.max_columna
        cantidad = vuelo.cantidad_pasajeros

        val root:View = binding.root
        var layout = binding.scrollviewHorizontal
        var layoutSeat = LinearLayout(root.context)

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutSeat.orientation=LinearLayout.VERTICAL
        layoutSeat.layoutParams=params

        layoutSeat.setPadding(80,80,80,80)



        for(i in 1..filas) {
            val layoutt = LinearLayout(root.context)
            layoutt.orientation = LinearLayout.HORIZONTAL
            layoutSeat.addView(layoutt)
            for (j in 1..columnas) {
                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(125, 125)
                layoutParams.setMargins(10, 10, 10, 10)
                //espacios
                if(columnas==6&&j==4){
                    layoutt.addView(generarEspacio(root.context,layoutParams))
                }else if(columnas==7&&(j==3||j==6)){
                    layoutt.addView(generarEspacio(root.context,layoutParams))
                }else if(columnas==8&&(j==3||j==7)){
                    layoutt.addView(generarEspacio(root.context,layoutParams))
                }else if(columnas==9&&(j==4||j==7)){
                    layoutt.addView(generarEspacio(root.context,layoutParams))
                }
                var temp:TextView=crearAsiento(i,j,root.context,layoutParams)
                    if(viewModel.matrizAsientos.value!!.get(Char(j+64)+"$i")!=null){
                        temp.setOnClickListener(null)
                        temp.tag="Reservado"
                        temp.setBackgroundResource(R.drawable.ic_seat_reservado)
                    }
                AsientosLayout.put(Char(j+64)+"$i",temp)
                layoutt.addView(temp)

            }
        }

        val layoutt = LinearLayout(root.context)
        layoutt.orientation = LinearLayout.HORIZONTAL
        var button = Button(root.context)
        button.text= "Reservar Asientos"
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        button.layoutParams=layoutParams
        button.setOnClickListener(View.OnClickListener {
            if(cantidad==0) {
                var meme = asienPorEnviar
                for (l in 0..viewModel.listTiquetes.value!!.size - 1) {
                    asienPorEnviar[l].tiquete_id = viewModel.listTiquetes.value!![l].id
                }
                var gson = Gson()
                var string = gson.toJson(asienPorEnviar)
                Toast.makeText(root.context, "Asientos seleccionados", Toast.LENGTH_LONG).show()
                ejecutarTarea1(viewModel, MethodRequest.POST.meth, 5, string)

                var p = parentFragmentManager
                for (i in 1..p.backStackEntryCount) {
                    p.popBackStack()
                }
            }else{
                Toast.makeText(root.context, "Debe seleccionar todos los asientos", Toast.LENGTH_LONG).show()
            }
        })
        layoutt.addView(button)
        layoutSeat.addView(layoutt)

        layout.addView(layoutSeat)

        return root
    }

    fun crearAsiento(i:Int,j:Int,context:Context,layoutParams:LinearLayout.LayoutParams):TextView{
        var seat = TextView(context)
        seat.layoutParams = layoutParams
        seat.gravity = Gravity.CENTER
        seat.text = Char(j+64)+"$i"
        seat.setBackgroundResource(R.drawable.ic_seat_libre)
        seat.tag="libre"
        seat.textSize = 16F
        seat.text
        seat.textAlignment= TextView.TEXT_ALIGNMENT_GRAVITY
        seat.typeface= Typeface.DEFAULT_BOLD
        seat.setOnClickListener(View.OnClickListener {
            socket.send("{\"id\":${vuelo.id},\"asiento\":\"${seat.text.toString()}\",\"socketid\":$socketId,\"usuario\":\"${Model.instance.user_name}\"}")
            if(seat.tag=="libre"&&cantidad>0){
                cantidad--
                seat.setBackgroundResource(R.drawable.ic_seat_seleccionado)
                seat.tag="seleccionado"
                asienPorEnviar.add(Asiento(0,i,Char(j+64)))
                Log.println(Log.INFO,"Asiento",asienPorEnviar.toString())
            }else if(seat.tag=="seleccionado"){
                cantidad++
                seat.setBackgroundResource(R.drawable.ic_seat_libre)
                seat.tag="libre"
                for(a in asienPorEnviar){
                    if(a.fila==i&&a.columna==Char(j+64))
                        asienPorEnviar.remove(a)
                }
                Log.println(Log.INFO,"Asiento",asienPorEnviar.toString())
            }
        })
        return seat
    }

    fun generarEspacio(context: Context,layoutParams:LinearLayout.LayoutParams): TextView{
        val espacio = TextView(context)
        espacio.layoutParams = layoutParams
        espacio.setBackgroundColor(Color.TRANSPARENT)
        espacio.text = ""
        return espacio
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(CheckInViewModel::class.java)
        viewModel.matrizAsientos.observe(this.viewLifecycleOwner, Observer {
            cambiarAReservados()
        })
        /*viewModel.listAsientos.observe(this.viewLifecycleOwner, Observer {
            cambiarAReservados()
        })*/
        var gson=Gson()
        ejecutarTarea1(viewModel,MethodRequest.POST.meth,4,gson.toJson(vuelo))
        ejecutarTarea2(viewModel,MethodRequest.POST.meth,3,"{\"user_name\":\"${Model.instance.user_name} \", \"vuelo1\":\"${vuelo.id}\"}")
        inicializarWebSocket()
    }

    fun inicializarWebSocket(){
        socket=client.newWebSocket(request, object: WebSocketListener(){
            var gson:Gson=Gson()
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                this@CheckInFragment.activity?.runOnUiThread(Runnable {
                })
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.println(Log.INFO,"Se obtuvo","Pasó pora acá")
                Log.println(Log.INFO,"Se obtuvo",text)

                try{
                    if(socketId==0)
                        socketId = text.toInt()
                }catch (e : ParseException){

                }

                if(text=="Actualizar"){
                    ejecutarTarea2(viewModel,MethodRequest.POST.meth,4,gson.toJson(vuelo))
                }

                else {
                    var sType = object : TypeToken<mensajeAsiento>() {}.type
                    var mensaje: mensajeAsiento? = null
                    try {
                        mensaje = gson.fromJson<mensajeAsiento>(text, sType)
                    } catch (e: JsonSyntaxException) {

                    }
                    if (mensaje != null && mensaje.socketid != socketId && mensaje.usuario == Model.instance.user_name)
                        clickearAsiento(mensaje.asiento)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.println(Log.ERROR,"Error:",t.toString())
            }
        })
        socket.send("Hello there checkin")
        //this.socket =client.newWebSocket(request,SocketListener("pepe",this.binding.root))
    }

    fun clickearAsiento(asiento:String){
        var seat = AsientosLayout[asiento] as TextView?
        var numero = asiento.replace(asiento[0].toString(),"",ignoreCase = false)
        if(seat?.tag=="libre"&&cantidad>0){
            cantidad--
            seat.setBackgroundResource(R.drawable.ic_seat_seleccionado)
            seat.tag="seleccionado"
            asienPorEnviar.add(Asiento(0, numero.toInt(),asiento[0]))
        }else if(seat?.tag=="seleccionado"){
            cantidad++
            seat.setBackgroundResource(R.drawable.ic_seat_libre)
            seat.tag="libre"
            for(a in asienPorEnviar){
                if(a.fila==numero.toInt()&&a.columna==asiento[0])
                    asienPorEnviar.remove(a)
            }
        }

    }

    fun cambiarAReservados(){
        for((k,v) in viewModel.matrizAsientos.value!!){
            var o=k
            var temp = AsientosLayout.get(k) as TextView
            temp.setBackgroundResource(R.drawable.ic_seat_reservado)
            temp.setOnClickListener(null)
        }
    }

    fun ejecutarTarea1(viewModel: CheckInViewModel,method: Int,service: Int,params:String?=null){
        if(task1?.status==Constant.Status.RUNNING){
            task1?.cancel(true)
        }
        task1 = CheckInAsyncTask(viewModel,method,service,this.requireContext(),params)
        task1?.execute()
    }

    fun ejecutarTarea2(viewModel: CheckInViewModel,method: Int,service: Int,params:String?=null){
        if(task2?.status==Constant.Status.RUNNING){
            task2?.cancel(true)
        }
        task2 = CheckInAsyncTask(viewModel,method,service,this.requireContext(),params)
        task2?.execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        socket.close(1000,"Exit of the fragment")
    }
    inner class CheckInAsyncTask(
        private var viewModel: CheckInViewModel,
        private var method:Int,
        private var serv:Int,
        private var context: Context,
        private var parametros:String?=null
    ): CoroutinesAsyncTask<Int,Int,String>("CheckInAsyncTask"){
        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1 -> when(serv){
                    1-> return httpRequestGet(URLS.ENLISTAR_OCUPADOS.service)
                    2-> return httpRequestGet(URLS.ENLISTAR_OCUPADOS.service)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                2 -> when(serv){
                    3-> return httpRequestPost(URLS.ENLISTAR_TIQUETES.service,parametros!!)
                    4-> return httpRequestPost(URLS.ENLISTAR_OCUPADOS.service,parametros!!)
                    5-> return httpRequestPost(URLS.ENVIAR_TIQUETE.service,parametros!!)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                else -> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var gson= Gson()
            when(serv){
                3->{
                    var sType=object : TypeToken<List<Tiquete>>(){}.type
                    var data=gson.fromJson<List<Tiquete>>(result,sType)
                    viewModel.listTiquetes.value=data
                    //cargarVuelos(context)
                }
                4->{
                    var sType=object : TypeToken<List<Asiento>>(){}.type
                    var data=gson.fromJson<List<Asiento>>(result,sType)
                    viewModel.setOnMap(data)
                    //cargarVuelos(context)
                }
                5->{

                }
            }
        }

    }

    class mensajeAsiento(var id:Int,var asiento:String,var socketid: Int,var usuario: String?=null)

}