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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapter
import est.una.ac.cr.nonavegable.databinding.VuelosIdaFragmentBinding
import est.una.ac.cr.nonavegable.model.Model
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.buscarvuelos.BuscarVuelo
import est.una.ac.cr.nonavegable.view.ui.checkin.CheckInFragment
import est.una.ac.cr.nonavegable.view.ui.checkout.CheckOutFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosregreso.VuelosRegresoFragment

class VuelosIdaFragment : Fragment() {

    private object HOLDER{
        var INSTANCE = VuelosIdaFragment()
    }

    companion object {
        val instance : VuelosIdaFragment by lazy { HOLDER.INSTANCE }
        fun newInstance() = VuelosIdaFragment()
    }





    private lateinit var viewModel: VuelosIdaViewModel
    lateinit var recycle:RecyclerView
    private var _binding:VuelosIdaFragmentBinding?=null
    private  val binding get() = _binding!!
    private lateinit var adaptador: ListaElementosVueloAdapter

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
        var adapter:ListaElementosVueloAdapter
        val root:View = binding.root

        getListOfVuelos(inflater,root.context,recycle)

        binding.buttonSeleccionarIda.setOnClickListener(View.OnClickListener {
            jumpFragment(root.context)
        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VuelosIdaViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun getListOfVuelos(
        inflater: LayoutInflater,
        context: Context,
        recycle:RecyclerView,
    ) {
        val vuelosList = ArrayList<Vuelo>()
        for (v in Model.instance.listaVuelo) {
            vuelosList.add(v)
        }
        adaptador = ListaElementosVueloAdapter(vuelosList,inflater,context)
        recycle.adapter = adaptador
    }

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
        fragment.arguments=b

        var fragmenmanager: FragmentManager? = parentFragmentManager
        var fragTransaction: FragmentTransaction?=fragmenmanager?.beginTransaction()

        fragTransaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragTransaction?.replace(R.id.nav_host_fragment_content_main,fragment)
        fragTransaction?.addToBackStack(null)
        fragTransaction?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
