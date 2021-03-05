package mx.com.maiktmp.skilltestphndr.ui.partners.list.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.com.maiktmp.skilltestphndr.R
import mx.com.maiktmp.skilltestphndr.databinding.ItemPartnerBinding
import mx.com.maiktmp.skilltestphndr.ui.models.Partner

class PartnerAdapter(private val items: List<Partner>) :
    RecyclerView.Adapter<PartnerAdapter.ViewHolder>() {

    var onPartnerClick: (employee: Partner) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_partner, parent, false)
        )

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partner = items[position]
        val vBind = holder.vBind
        vBind.partner = partner
        vBind.itemRoot.setOnClickListener { onPartnerClick(partner) }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vBind = ItemPartnerBinding.bind(itemView)
    }
}