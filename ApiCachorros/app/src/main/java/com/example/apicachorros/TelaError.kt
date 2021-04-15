package com.example.apicachorros

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TelaError : AppCompatActivity() {

    lateinit var tvError: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_error)

        tvError = findViewById(R.id.tv_descricao_erro)

        val id1 = intent.getIntExtra("id1", 0)
        val id2 = intent.getIntExtra("id2", 0)

        tvError.text = "Deu ruim... Nenhum cachorro encontrado com os ids $id1 e $id2"

    }
}