package est.una.ac.cr.nonavegable.view.ui.checkout

import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.CheckOutFragmentBinding
import est.una.ac.cr.nonavegable.model.*
import est.una.ac.cr.nonavegable.model.entities.Reserva
import est.una.ac.cr.nonavegable.model.entities.Tiquete
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import java.util.*

class CheckOutFragment : Fragment() {

    companion object {
        fun newInstance() = CheckOutFragment()
    }

    private lateinit var viewModel: CheckOutViewModel

    private var _binding: CheckOutFragmentBinding?=null
    private val binding get()=_binding!!

    enum class MethodRequest(val meth:Int){
        GET(1),
        POST(2)
    }
    var task: checkOutAsyncTasks?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CheckOutViewModel::class.java)
        _binding = CheckOutFragmentBinding.inflate(inflater,container,false)
        val root: View = binding.root

        var cantidadAsientos = binding.cardCheckoutCantidadAsi as TextView
        var precioXpersona= binding.cardCheckoutPrecioPersona as TextView
        var total = binding.cardCheckoutTotal as TextView
        var descuento = binding.cardCheckoutDescuento as TextView
        var totalpagar = binding.cardCheckoutTotalAPagar as TextView
        var nomPropietario:TextInputLayout = binding.cardCheckoutNomProp
        var numeroTarjeta:TextInputLayout = binding.cardCheckoutNumCard
        var fechVenci:TextInputLayout = binding.cardCheckoutFechVenci
        var cvc:TextInputLayout = binding.cardCheckoutCvv
        var boton : Button = binding.cardCheckoutBtnPagar
        var picker:DatePickerDialog
        fechVenci.editText?.setOnClickListener(View.OnClickListener {
                    val cldr: Calendar = Calendar.getInstance()
                    val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
                    val month: Int = cldr.get(Calendar.MONTH)
                    val year: Int = cldr.get(Calendar.YEAR)
                    // date picker dialog
                    picker = DatePickerDialog(it.context,
                        { view, year, monthOfYear, dayOfMonth -> fechVenci.editText?.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                        year,
                        month,
                        day
                    )
                    picker.datePicker.minDate=System.currentTimeMillis()-1000
                    picker.show()})

        var argument = this.arguments
        var ida : Vuelo = Vuelo()
        var regreso : Vuelo = Vuelo()
        cantidadAsientos.text="Cantidad de asientos: "+argument?.getInt("Cantidad")
        if(argument?.getBoolean("SoloIda") as Boolean){
            ida = argument.getSerializable("VueloIda") as Vuelo
            var precio : Double = ida.ruta?.precio as Double
            precioXpersona.text="Precio por persona: "+precio
            total.text="Total: "+(argument?.getInt("Cantidad")*precio)
            descuento.text="Descuento: "+(ida.descuento*precio)
            totalpagar.text="Total a pagar: "+(precio*(argument?.getInt("Cantidad"))-ida.descuento)
        }else{
            ida = argument.getSerializable("VueloIda") as Vuelo
            regreso = argument.getSerializable("VueloRegreso") as Vuelo
            var precio = (ida.ruta?.precio as Double +regreso.ruta?.precio as Double)
            precioXpersona.text="Precio por persona: "+precio
            total.text="Total: "+(argument?.getInt("Cantidad")*precio)
            descuento.text="Descuento: "+(ida.descuento*precio)
            totalpagar.text="Total a pagar: "+(precio*(argument?.getInt("Cantidad"))-ida.descuento)
        }

        boton.setOnClickListener(View.OnClickListener {
            Toast.makeText(root.context,"Compra Realizada",Toast.LENGTH_LONG).show()

            var gson= Gson()
            ida = argument.getSerializable("VueloIda") as Vuelo


            var filtro : Reserva = Reserva()
            filtro.user_name="joshua"
            filtro.cantidad=argument?.getInt("Cantidad")
            filtro.vuelo1 = ida.id

            var obj : String

            if(!argument?.getBoolean("SoloIda") as Boolean) {
                regreso = argument.getSerializable("VueloRegreso") as Vuelo
                filtro.vuelo2 = regreso.id
                obj = gson.toJson(filtro)
                ejecutarTarea(MethodRequest.POST.meth, 3, obj)
            }else {
                obj = gson.toJson(filtro)
                ejecutarTarea(MethodRequest.POST.meth, 3, obj)
            }

            var p = parentFragmentManager
            for(i in 1..p.backStackEntryCount){
                p.popBackStack()
            }
        })


        return root
    }

    fun ejecutarTarea(method:Int,service:Int,params:String?=null){
        if(task?.status== Constant.Status.RUNNING){
            task?.cancel(true)
        }
        task = checkOutAsyncTasks(viewModel, method, service, params)
        task?.execute()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckOutViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    class checkOutAsyncTasks(private var viewModel: CheckOutViewModel,
                             private var method:Int,
                             private var serv: Int,
                             private var parametros: String?=null
    ): CoroutinesAsyncTask<Int, Int, String>("Vuelos Ida Async"){

        enum class URLS(val service:String) {
            CHECKOUT("http://201.200.0.31/LAB001BACKEND/services/Tiquete/CheckOut"),
            CHECKOUTSOLOIDA("http://201.200.0.31/LAB001BACKEND/services/CheckOutSoloIda")
        }


        override fun doInBackground(vararg params: Int?): String {
            when(method){
                1 -> when(serv){
                    1-> return httpRequestGet(URLS.CHECKOUTSOLOIDA.service)
                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                2 -> when(serv){
                    3-> return httpRequestPost(URLS.CHECKOUT.service,parametros!!)

                    else -> throw IllegalArgumentException("El Servicio no existe")
                }
                else -> throw IllegalArgumentException("El método de petición no existe.")
            }
        }

        override fun onPostExecute(result: String?) {
            var gson: Gson = Gson()
            when(serv){
                3->{
                    //var sType=object : TypeToken<List<Vuelo>>(){}.type
                    //var data=gson.fromJson<List<Vuelo>>(result,sType)
                    //viewModel.listVuelosIda.value=data
                }
            }
        }
    }
}