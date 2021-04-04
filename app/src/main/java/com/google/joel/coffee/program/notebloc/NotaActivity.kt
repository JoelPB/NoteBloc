package com.google.joel.coffee.program.notebloc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.joel.coffee.program.notebloc.databinding.ActivityNotaBinding

class NotaActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editNota()
    }

    private fun editNota() {
        val edit = intent.getBooleanExtra("edit", false)
        val position = intent.getIntExtra("position", 0)

        if (edit){
            //Continuar
        }
    }
}