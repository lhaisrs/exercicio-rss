package br.ufpe.cin.if710.rss

import br.ufpe.cin.if710.rss.ItemRSS

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.item_rss_list.view.*


class ItemRSSAdapter (
        private val items: List<ItemRSS>,
        private val context: Context
) : Adapter<ItemRSSAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rss_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindView(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: ItemRSS) = with(itemView) {
            item_titulo.text = item.title
            item_data.text = item.pubDate
        }
    }
}