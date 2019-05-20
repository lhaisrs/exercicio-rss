package br.ufpe.cin.if710.rss

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.content.Intent

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import android.util.Log

//Alterado para AppCompatActivity - Para utilizar a Toolbar
class MainActivity : AppCompatActivity() {

    //Valores para RSS_FEED
    //http://leopoldomt.com/if1001/g1brasil.xml
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    //Setando variáveis
    private lateinit var conteudoRSS: RecyclerView //Setando a variável para ser inicializada depois em onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setando a Toolbar
        val toolbar : Toolbar = findViewById(R.id.toolbar_rss)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        conteudoRSS = findViewById(R.id.conteudoRSS) //Setando o conteudoRSS

        //Configurando o LayoutManager da RecyclerView
        conteudoRSS.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            conteudoRSS.layoutManager = layoutManager
        }
    }

    //Configurando o Menu da ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Configurando ações do Menu ActionBar
    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_edit -> {
            val intent = Intent(this, OptionsFeedRSSActivity::class.java)
            startActivity(intent)
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            loadRSS() //Chamando a função para carregar o RSS
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Retorna da lista dos Itens do RSS
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

            rssFeed = ParserRSS.parse(feedXML) //Fazendo o parse do FeedXML para retornar a lista (Feed) do RSS

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
            //Obtendo a URL via SharedPreferences
            val urlRSS : String = getRSSPreference()
            //Obtendo o FeedRSS
            val listFeedRSS: List<ItemRSS> = getRssFeed(urlRSS)
            uiThread {
                //Setando o adapter da RecyclerView para receber a lista do FeedRSS e passando
                //applicationContext que no caso é a content (view) onde será apresentado
                //o Adapter
                conteudoRSS.adapter = ItemRSSAdapter(listFeedRSS, applicationContext)
            }
        }
    }

    //Função para obter o URL RSS da OptionFeedRSS Fragment
    private fun getRSSPreference() : String {
        val preferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val urlRSS = preferences.getString(RSSFEED, "http://leopoldomt.com/if1001/g1brasil.xml")

        return urlRSS
    }

    companion object {
        private val RSSFEED = "rssFeed"
    }
}
