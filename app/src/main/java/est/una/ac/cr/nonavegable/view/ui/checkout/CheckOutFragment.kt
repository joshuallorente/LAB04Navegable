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
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.CheckOutFragmentBinding
import est.una.ac.cr.nonavegable.model.Model
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

            Model.instance.listaReserva.add(Reserva(3,"joshua.llor"))
            Model.instance.listaTiquete.add(Tiquete(3,ida.id,"joshua.llor",3))
            if(!argument?.getBoolean("SoloIda") as Boolean)
                Model.instance.listaTiquete.add(Tiquete(4,regreso.id,"joshua.llor",3))

            var p = parentFragmentManager
            for(i in 1..p.backStackEntryCount){
                p.popBackStack()
            }
        })


        return root
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

}