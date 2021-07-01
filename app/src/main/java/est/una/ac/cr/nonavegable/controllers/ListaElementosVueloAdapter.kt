package est.una.ac.cr.nonavegable.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.model.entities.Vuelo
import est.una.ac.cr.nonavegable.view.ui.buscarvuelos.BuscarVuelo
import est.una.ac.cr.nonavegable.view.ui.vuelosida.VuelosIdaFragment

class ListaElementosVueloAdapter(
    private var mData: List<Vuelo>,
    private val mInflater: LayoutInflater,
    private val context: Context
) :
    RecyclerView.Adapter<ListaElementosVueloAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.recycle_item_template, null)
        return ViewHolder(view)
    }

    var seleccionado : Vuelo = Vuelo()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mData[position])

        var item=mData.get(position)
        holder.itemView.setOnClickListener(View.OnClickListener {
            holder.itemView.setBackgroundColor(Color.GREEN)
            Log.println(Log.DEBUG,"Debug",item.toString())
            seleccionado=item
        })
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setItems(items: List<Vuelo>) {
        mData = items
        this.notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var vuelo_id: TextView
        var vuelo_ruta: TextView
        var vuelo_hora_partida: TextView
        var vuelo_hora_llegada: TextView
        var vuelo_fecha: TextView
        var vuelo_precio:TextView
        var tiquete: ImageView
        var flecha: ImageView


        fun bindData(item: Vuelo?) {
            this.vuelo_id.text="Vuelo Id: ${item?.id}"
            this.vuelo_fecha.text="Fecha: ${item?.calcularFechaDespegue()}"
            this.vuelo_hora_partida.text="${item?.ruta?.calcularHora()}"
            this.vuelo_hora_llegada.text="${item?.ruta?.calcularHoraLlegada()}"
            this.vuelo_ruta.text="Ruta: ${item?.origen} - ${item?.destino}"
            this.vuelo_precio.text="$ ${item?.ruta?.precio}"
        }

        init {
            vuelo_id = itemView.findViewById(R.id.card_vuelo_id)
            vuelo_ruta = itemView.findViewById(R.id.card_vuelo_ruta)
            vuelo_hora_partida = itemView.findViewById(R.id.card_hora_partida)
            vuelo_hora_llegada = itemView.findViewById(R.id.card_hora_llegada)
            vuelo_fecha = itemView.findViewById(R.id.card_fecha_vuelo)
            tiquete = itemView.findViewById(R.id.iconImageView)
            flecha = itemView.findViewById(R.id.iconForwardImage)
            vuelo_precio = itemView.findViewById(R.id.card_precio)
        }
    }
}