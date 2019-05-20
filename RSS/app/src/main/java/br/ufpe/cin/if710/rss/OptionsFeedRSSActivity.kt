package br.ufpe.cin.if710.rss

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.preference.DropDownPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import org.jetbrains.anko.support.v4.find

class OptionsFeedRSSActivity : AppCompatActivity() {

    //Valores para RSS_FEED
    //http://leopoldomt.com/if1001/g1brasil.xml
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_feed_rss)

        //Carregando o layout do Fragment
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.options_rss_container, OptionsRSSURLPreferenceFragment())
                .commit()
    }

    //Criando Fragment para mostrar as opções de URL RSS
    class OptionsRSSURLPreferenceFragment : PreferenceFragmentCompat() {

        //Carregando o XML da ScreenPreferences
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preferencias)
        }

        private var listener : SharedPreferences.OnSharedPreferenceChangeListener? = null

        //Setando para receber o Dropdown Preference
        private var mRSSOptionURLPreference : Preference? = null

        //Obtendo e Setando as informações das Preferências
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            //Obtendo os dados da preferência
            mRSSOptionURLPreference = preferenceManager.findPreference(RSSFEED)

            //Obtendo os dados para mostrar no summary (resumo) do DropDownPreference
            listener = SharedPreferences.OnSharedPreferenceChangeListener {
                sharedPreferences, key ->
                mRSSOptionURLPreference!!.summary = sharedPreferences.getString(
                        RSSFEED, "http://leopoldomt.com/if1001/g1brasil.xml"
                )
            }

            //Obtendo as SharedPreferences e Registrando o Listener
            val preferences = preferenceManager.sharedPreferences
            preferences.registerOnSharedPreferenceChangeListener(listener)

            //Salvando as mudanças do valor de RSSFEED nas SharedPreferences através do callback
            listener!!.onSharedPreferenceChanged(preferences, RSSFEED)

        }
    }

    companion object {
        private val RSSFEED = "rssFeed"
    }
}
