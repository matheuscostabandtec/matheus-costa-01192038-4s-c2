package com.example.apicachorros

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TelaResultado : AppCompatActivity() {
    lateinit var idCachoro1: TextView
    lateinit var idCachoro2: TextView
    lateinit var precoTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_resultado)

        idCachoro1 = findViewById(R.id.tv_result_dog1)
        idCachoro2 = findViewById(R.id.tv_result_dog2)
        precoTotal = findViewById(R.id.tv_result_total)


        val id1 = intent.getIntExtra("id1", 0)
        val id2 = intent.getIntExtra("id2", 0)
        val total = intent.getIntExtra("total", 0)

        if(id1 == 0) {
            idCachoro1.text = "não encontrado"
        } else {
            idCachoro1.text = "$id1"
        }

        if(id2 == 0) {
            idCachoro2.text = "não encontrado"
        } else {
            idCachoro2.text = "$id2"
        }
        precoTotal.text = "R$$total"
    }
}