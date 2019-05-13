package br.edu.ifsp.scl.asynctaskwskt

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedInputStream
import java.io.IOError
import java.io.IOException
import java.lang.Thread.sleep
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    object constantes{
        val URL_BASE = "http://www.nobile.pro.br/sdm/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarInformacoesBt.setOnClickListener{
            val buscarTextoAt = BuscarTextoAt()
            buscarTextoAt.execute(constantes.URL_BASE + "texto.php")
        }
    }

    private inner class BuscarTextoAt: AsyncTask<String, Int, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            Toast.makeText(baseContext,"Buscando String no WebService", Toast.LENGTH_SHORT)
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String {
            val url = params[0]

            val stringBufferResposta: StringBuffer = StringBuffer()

            try {
                val conexao = URL(url).openConnection() as HttpURLConnection

                if(conexao.responseCode == HttpURLConnection.HTTP_OK){
                    val inpuStream = conexao.inputStream

                    val bufferedReader = BufferedInputStream(inpuStream).bufferedReader()

                    val respostaList = bufferedReader.readLines()

                    respostaList.forEach { stringBufferResposta.append(it) }
                }
            } catch (ioe: IOException){
                Toast.makeText(baseContext,"Erro na conexao", Toast.LENGTH_SHORT)
            }

            for(i in 1..10){
                publishProgress(i)
                sleep(500)
            }

            return stringBufferResposta.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            Toast.makeText(baseContext,"Texto recuperado com sucesso", Toast.LENGTH_SHORT)

            textTv.text = result

            progressBar.visibility = View.GONE
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)

            values[0]?.apply { progressBar.progress = this }
        }
    }
}
