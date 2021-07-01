package est.una.ac.cr.nonavegable.view.ui.test

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.databinding.TestFragmentBinding
import est.una.ac.cr.nonavegable.model.*

class TestFragment : Fragment() {

    companion object {
        fun newInstance() = TestFragment()
    }

    private lateinit var viewModel: TestFragmentViewModel
    lateinit var  recyclerView: RecyclerView
    private  var _binding:TestFragmentBinding?=null
    private val binding get()=_binding!!
    private lateinit var adaptador: TestAdapter
    var task:TestAsync?=null

    fun getViewModel():TestFragmentViewModel{
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=ViewModelProvider(this).get(TestFragmentViewModel::class.java)
        _binding = TestFragmentBinding.inflate(inflater,container,false)
        recyclerView=binding.testReclicler
        recyclerView.layoutManager=LinearLayoutManager(recyclerView.context)
        val root:View=binding.root
        adaptador = TestAdapter(listOf(),inflater,root.context)
        this.viewModel.list.observe(this.viewLifecycleOwner, Observer {
            adaptador.setItems(it)
        })
        recyclerView.adapter=adaptador
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(TestFragmentViewModel::class.java)
        cargarLista()
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

    fun cargarLista(){
        if(task?.status==Constant.Status.RUNNING)
            task?.cancel(true)
        task = TestAsync("http://201.200.0.31/LAB001BACKEND/services/Avion",this.viewModel)
        task?.execute(10)
    }

    class TestAsync(private var apiURL:String,
                    private var viewModel:TestFragmentViewModel
                    ):CoroutinesAsyncTask<Int,Int,String>("TestAsync"){
        override fun doInBackground(vararg params: Int?): String {
            return httpRequestGet(apiURL)
        }

        override fun onPostExecute(result: String?) {
            //super.onPostExecute(result)
            var gson:Gson=Gson()
            var sType=object :TypeToken<List<Beto>>() {}.type
            var data=gson.fromJson<List<Beto>>(result,sType)
            viewModel.list.value=data
        }
    }

}