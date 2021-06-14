package est.una.ac.cr.nonavegable.view.ui.vuelosida

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.controllers.ListaElementosVueloAdapter
import est.una.ac.cr.nonavegable.databinding.VuelosIdaFragmentBinding

class VuelosIdaFragment : Fragment() {

    companion object {
        fun newInstance() = VuelosIdaFragment()
    }
    private lateinit var viewModel: VuelosIdaViewModel
    lateinit var recycle:RecyclerView
    private var _binding:VuelosIdaFragmentBinding?=null
    private  val binding get() = _binding!!

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

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VuelosIdaViewModel::class.java)
        // TODO: Use the ViewModel
    }

    /*private fun getListOfJobsApps(
    inflater: LayoutInflater,
    context:Context,
    recycle:RecyclerView,
    adapter:RecyclerView_Adapter
    ) {
        val vuelosList = ArrayList<Vuelo>()
        for (v in Model.getListaVuelos()) {
            vuelosList.add(v)
        }
        adapter = ListaElementosVueloAdapter(vuelosList.add)
        recycler.adapter = adaptador
    }*/

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}
