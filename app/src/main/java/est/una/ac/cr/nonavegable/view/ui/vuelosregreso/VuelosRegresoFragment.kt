package est.una.ac.cr.nonavegable.view.ui.vuelosregreso

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapter
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapterRegreso
import est.una.ac.cr.nonavegable.databinding.VuelosIdaFragmentBinding
import est.una.ac.cr.nonavegable.databinding.VuelosRegresoFragmentBinding
import est.una.ac.cr.nonavegable.model.Model
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.checkout.CheckOutFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaViewModel
import java.io.Serializable

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
        var adapter:ListaElementosVueloAdapter
        val root:View = binding.root

        getListOfVuelos(inflater,root.context,recycle)

        binding.buttonSeleccionarRegreso.setOnClickListener(View.OnClickListener {
            jumpFragment(root.context)
        })
        return root
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
        adaptador = ListaElementosVueloAdapterRegreso(vuelosList,inflater,context)
        recycle.adapter = adaptador
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VuelosRegresoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}