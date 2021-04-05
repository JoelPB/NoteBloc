package com.google.joel.coffee.program.notebloc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.joel.coffee.program.blocodenotas.model.Nota
import com.google.joel.coffee.program.notebloc.adapter.ListAdapter
import com.google.joel.coffee.program.notebloc.databinding.ActivityMainBinding
import com.google.joel.coffee.program.notebloc.db.DatabaseHandler

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // Inicialização do RecyclerView
    lateinit var listAdapter: ListAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    // Base de dados SQLite
    var notaList = ArrayList<Nota>()
    var databaseHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initScreen()
        btnClick()
    }

    override fun onResume() {
        super.onResume()
        initScreen()
    }

    private fun btnClick() {
        binding.btnInsert.setOnClickListener {
            val intent = Intent(this, NotaActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    private fun initScreen() {
        notaList = databaseHandler.notas()
//        listAdapter = ListAdapter(notaList, this, this::deleteAdapter)
        listAdapter = ListAdapter(notaList, this)
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = listAdapter
    }

//    private fun deleteAdapter(position: Int){
//        notaList.removeAt(position)
//        listAdapter.notifyItemRemoved(position)
//    }
}