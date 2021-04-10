package com.google.joel.coffee.program.notebloc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

        setSupportActionBar(binding.toolbar)

        initScreen()
        btnClick()
    }

    override fun onResume() {
        super.onResume()
        initScreen()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.search -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun btnClick() {
        binding.btnInsert.setOnClickListener {
            val intent = Intent(this, NotaActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    private fun initScreen() {
        notaList = databaseHandler.notas()
        listAdapter = ListAdapter(notaList, this, this::statusNotaAdapter)
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = listAdapter
    }

    private fun statusNotaAdapter(up: Boolean, position: Int){
        if (up) {
            listAdapter.notifyItemMoved(position, position - 1)
        } else {
            listAdapter.notifyItemMoved(position, position + 1)
        }

        onResume()

    }
}