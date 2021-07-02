package est.una.ac.cr.nonavegable.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import est.una.ac.cr.nonavegable.R
import est.una.ac.cr.nonavegable.model.entities.Vuelo

class ListaHistorialAdapter(
    private var mData: List<Vuelo>,
    private val mInflater: LayoutInflater,
    private val context:Context
): RecyclerView.Adapter<ListaHistorialAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = mInflater.inflate(R.layout.historial_item_template,null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setItems(items:List<Vuelo>){
        mData=items
        this.notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var vuelo_id: TextView
        var vuelo_ruta: TextView
        var vuelo_hora_partida: TextView
        var vuelo_hora_llegada: TextView
        var vuelo_fecha: TextView
        var vuelo_precio: TextView
        var tiquete: ImageView
        var flecha: ImageView
        var asientos: TextView

        fun bindData(item: Vuelo?) {
            this.vuelo_id.text="Vuelo Id: ${item?.id}"
            this.vuelo_fecha.text="Fecha: ${item?.calcularFechaDespegue()}"
            this.vuelo_hora_partida.text="${item?.ruta?.calcularHora()}"
            this.vuelo_hora_llegada.text="${item?.ruta?.calcularHoraLlegada()}"
            this.vuelo_ruta.text="Ruta: ${item?.origen} - ${item?.destino}"
            this.asientos.text="Asientos: ${item?.cantidad_pasajeros}"
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
            asientos=itemView.findViewById(R.id.card_Cantidad)
            vuelo_precio = itemView.findViewById(R.id.card_precio)
        }
    }
}