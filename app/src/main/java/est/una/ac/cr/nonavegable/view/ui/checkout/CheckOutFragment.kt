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
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.CheckOutFragmentBinding
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