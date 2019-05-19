package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle

import android.widget.ListView
import android.widget.TextView

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class MainActivity : Activity() {

    //Setando variáveis

    private var RSS_FEED : String = "http://leopoldomt.com/if1001/g1brasil.xml"
    //Outros valores para RSS_FEED
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    private lateinit var conteudoRSS : TextView //Setando a variável para ser inicializada depois em onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS = findViewById(R.id.conteudoRSS)
    }

    override fun onStart() {
        super.onStart()
        try {
            val feedXML : String = getRssFeed(RSS_FEED)
            conteudoRSS.text = feedXML
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRssFeed(feed: String) : String {
        var input : InputStream? = null //Mudando o nome para deixar mais legível e tirar a palavra reservada 'in' de Kotlin
        var rssFeed : String = ""

        try {

            var url : URL = URL(feed)
            var conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            input = conn.inputStream
            var output : ByteArrayOutputStream = ByteArrayOutputStream()
            var buffer : ByteArray = ByteArray(1024)

            for (count in buffer) {
                val _count = input.read(buffer)
                if(_count != -1) {
                    output.write(buffer, 0, _count)
                }
            }

            var response : ByteArray =  output.toByteArray()
            val charset_UTF_8 : Charset = Charsets.UTF_8
            rssFeed = String(response, charset_UTF_8)

        } finally {
            if(input != null) {
                input.close()
            }
        }

        return rssFeed
    }
}
