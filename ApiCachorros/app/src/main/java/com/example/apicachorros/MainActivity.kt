package com.example.apicachorros

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var swFiltroAmigavel: Switch
    var id1Encontrado = false
    var id2Encontrado = false
    var precoTotal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swFiltroAmigavel = findViewById(R.id.sw_filtro_amigaveis);
    }

    fun comprar(view: View) {

        val apiCachorros = ConexaoApiCachorros.criar();

        val etId1:EditText = findViewById(R.id.et_id1);
        val etId2:EditText = findViewById(R.id.et_id2);

        val id1 = etId1.text.toString().toInt()
        val id2 = etId2.text.toString().toInt()

        if(swFiltroAmigavel.isChecked){
            val listaCachorrosAmigaveis = mutableListOf<Cachorros>()
            apiCachorros.getTodos().enqueue(object: Callback<List<Cachorros>> {
                override fun onResponse(call: Call<List<Cachorros>>, response: Response<List<Cachorros>>){
                   val dogsFiltrados = response.body()?.filter { c -> c.indicadoCriancas}
                    if(dogsFiltrados !== null){
                        dogsFiltrados.forEach { c -> listaCachorrosAmigaveis.add(c) }
                    }
                }

                override fun onFailure(call: Call<List<Cachorros>>, t: Throwable) {
                    Log.e("api", t.message!!)
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                }
            })

            Toast.makeText(this, "essa é a lista $listaCachorrosAmigaveis", Toast.LENGTH_SHORT).show()

            val cachorrosEncontrados = listaCachorrosAmigaveis.filter { c -> c.id == id1 || c.id == id2 }

            if (cachorrosEncontrados.isEmpty()){
                val telaErro = Intent(this, TelaError::class.java)
                telaErro.putExtra("id1", id1)
                telaErro.putExtra("id2", id2)
                startActivity(telaErro)
            } else {
                val telaResultado = Intent(this, TelaResultado::class.java)
                var valorTotal = 0
                cachorrosEncontrados.forEach { c -> valorTotal += c.precoMedio }

                if(cachorrosEncontrados.size == 2){
                    telaResultado.putExtra("id1", id1)
                    telaResultado.putExtra("id2", id2)
                } else {
                    val cachorroId1 = cachorrosEncontrados.find { c -> c.id == id1 }
                    val cachorroId2 = cachorrosEncontrados.find { c -> c.id == id2 }

                    if (cachorroId1 !== null && cachorroId2 == null){
                        telaResultado.putExtra("id1", id1)
                        telaResultado.putExtra("id2", 0)
                    } else {
                        telaResultado.putExtra("id1", 0)
                        telaResultado.putExtra("id2", id2)
                    }
                }
                telaResultado.putExtra("total", valorTotal)
                startActivity(telaResultado)
            }
        } else {
            val telaResultado = Intent(this, TelaResultado::class.java)
            apiCachorros.getById(id1).enqueue(object : Callback<Cachorros> {
                override fun onResponse(call: Call<Cachorros>, response: Response<Cachorros>) {
                    val cachorro = response.body()
                    if (cachorro != null) {
                        id1Encontrado = true
                        precoTotal += cachorro.precoMedio
                    }
                }

                override fun onFailure(call: Call<Cachorros>, t: Throwable) {
                    Toast.makeText(baseContext, "Erro: ${t.message!!}", Toast.LENGTH_SHORT).show()
                }
            })

            apiCachorros.getById(id2).enqueue(object : Callback<Cachorros> {
                override fun onResponse(call: Call<Cachorros>, response: Response<Cachorros>) {
                    val cachorro = response.body()
                    if (cachorro != null) {
                        id2Encontrado = true
                        precoTotal += cachorro.precoMedio
                    }
                }

                override fun onFailure(call: Call<Cachorros>, t: Throwable) {
                    Toast.makeText(baseContext, "Erro: ${t.message!!}", Toast.LENGTH_SHORT).show()
                }
            })

            if (!id1Encontrado && !id2Encontrado){
                val telaErro = Intent(this, TelaError::class.java)
                telaErro.putExtra("id1", id1)
                telaErro.putExtra("id2", id2)
                startActivity(telaErro)
            } else if (id1Encontrado && !id2Encontrado){
                telaResultado.putExtra("id1", id1)
                telaResultado.putExtra("id2", 0)
            } else if (!id1Encontrado && id2Encontrado) {
                telaResultado.putExtra("id1", 0)
                telaResultado.putExtra("id2", id1)
            } else {
                telaResultado.putExtra("id1", id1)
                telaResultado.putExtra("id2", id2)
            }
            telaResultado.putExtra("total", precoTotal)
            startActivity(telaResultado)
        }

    }
}