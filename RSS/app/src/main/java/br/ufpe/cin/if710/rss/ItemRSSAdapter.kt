package br.ufpe.cin.if710.rss

import br.ufpe.cin.if710.rss.ItemRSS

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.item_rss_list.view.*
import android.net.Uri

//Classe ItemRSSAdapter - Adapter da RecyclerView na MainActivity.kt
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

    //Class ViewHolder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Função que realiza o binding dos items para serem exibidos cada propriedade
        //no devido campo correto
        fun bindView(item: ItemRSS) = with(itemView) {
            item_titulo.text = item.title
            item_data.text = item.pubDate
            item_titulo.setOnClickListener {
                openURL(item, itemView)
            }
        }

        //Função que abri a URL numa Activity VIEW no navegador sobre a notícia
        fun openURL(item: ItemRSS, itemView: View) {
            val uri : Uri = Uri.parse(item.link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            itemView.context.startActivity(intent)
        }
    }
}