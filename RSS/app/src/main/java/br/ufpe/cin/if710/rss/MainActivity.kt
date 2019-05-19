package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ArrayAdapter
import android.util.Log


import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import br.ufpe.cin.if710.rss.ParserRSS
import br.ufpe.cin.if710.rss.ItemRSSAdapter

class MainActivity : Activity() {

    //Setando variáveis

    private var RSS_FEED: String = "http://leopoldomt.com/if1001/g1brasil.xml"
    //Valores para RSS_FEED
    //http://leopoldomt.com/if1001/g1brasil.xml
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    private lateinit var conteudoRSS: RecyclerView //Setando a variável para ser inicializada depois em onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conteudoRSS = findViewById(R.id.conteudoRSS)

        val layoutManager = LinearLayoutManager(this)
        conteudoRSS.layoutManager = layoutManager
    }

    override fun onStart() {
        super.onStart()
        try {
            loadRSS()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Adaptado getRssFeed para retornar a List de Itens RSS já prontas
    private fun getRssFeed(feed: String): List<ItemRSS> {
        var input: InputStream? = null //Mudando o nome para deixar mais legível e tirar a palavra reservada 'in' de Kotlin
        var rssFeed: List<ItemRSS> = mutableListOf()

        try {

            var url: URL = URL(feed)
            var conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            input = conn.inputStream
            var output: ByteArrayOutputStream = ByteArrayOutputStream()
            var buffer: ByteArray = ByteArray(1024)

            for (count in buffer) {
                val _count = input.read(buffer)
                if (_count != -1) {
                    output.write(buffer, 0, _count)
                }
            }

            var response: ByteArray = output.toByteArray()
            val charset_UTF_8: Charset = Charsets.UTF_8

            val feedXML = String(response, charset_UTF_8)

            rssFeed = ParserRSS.parse(feedXML)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (input != null) {
                input.close()
            }
        }


        return rssFeed
    }

    private fun loadRSS() {
        doAsync {
            val listFeedRSS: List<ItemRSS> = getRssFeed(RSS_FEED)
            uiThread {
                conteudoRSS.adapter = ItemRSSAdapter(listFeedRSS, applicationContext)
            }
        }
    }
}
