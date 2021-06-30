package est.una.ac.cr.nonavegable.view.ui.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import est.una.ac.cr.nonavegable.R
import org.w3c.dom.Text

class TestAdapter(
    private var mData: List<Beto>,
    private var mInflater: LayoutInflater,
    private var context: Context
): RecyclerView.Adapter<TestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.test_template,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mData[position])
        var item=mData.get(position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setItems(items: List<Beto>){
        mData= items
        this.notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(itemView:View): RecyclerView.ViewHolder(itemView) {

        var text_name: TextView
        var text_last: TextView

        fun bindData(item:Beto?){
            this.text_name.text=item?.name
            this.text_last.text=item?.lastName
        }
        init {
            text_last = itemView.findViewById(R.id.apellidos)
            text_name = itemView.findViewById(R.id.nombre)
        }
    }
}