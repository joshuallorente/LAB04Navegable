package est.una.ac.cr.nonavegable.view.ui.checkin

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.CheckInFragmentBinding

class CheckInFragment : Fragment() {

    companion object {
        fun newInstance() = CheckInFragment()
    }

    private lateinit var viewModel: CheckInViewModel
    private var _binding: CheckInFragmentBinding? = null

    private val binding get() = _binding!!
    val AsientosLayout: HashMap<String, TextView> = HashMap()
    private var filas = 20
    private var columnas = 7
    var cantidad = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CheckInViewModel::class.java)
        _binding = CheckInFragmentBinding.inflate(inflater,container,false)

        val root:View = binding.root

        var layout = binding.scrollviewHorizontal as HorizontalScrollView

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
                    if(viewModel.map.get(Char(j+64)+"$i")!=null){
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
        button.text= "Seleccionar"
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        button.layoutParams=layoutParams
        button.setOnClickListener(View.OnClickListener {
            Toast.makeText(root.context,"Asientos seleccionados",Toast.LENGTH_LONG).show()
            var p = parentFragmentManager
            for(i in 1..p.backStackEntryCount){
                p.popBackStack()
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
            if(seat.tag=="libre"&&cantidad>0){
                cantidad--
                seat.setBackgroundResource(R.drawable.ic_seat_seleccionado)
                seat.tag="seleccionado"
            }else if(seat.tag=="seleccionado"){
                cantidad++
                seat.setBackgroundResource(R.drawable.ic_seat_libre)
                seat.tag="libre"
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
        viewModel = ViewModelProvider(this).get(CheckInViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}