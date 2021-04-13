package com.google.joel.coffee.program.notebloc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
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

        // Inicializar a barra de pesquisa do item de menu
        // obtem o objeto search do menu
        val searchViewItem = menu!!.findItem(R.id.search)
        if (searchViewItem != null) {
            val searchView = searchViewItem.actionView as SearchView
            searchView.queryHint = "Pesquisar título"
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    Toast.makeText(this@MainActivity, "submit", Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
//                    Toast.makeText(this@MainActivity, searchView.query.toString(), Toast.LENGTH_SHORT).show()
                    initScreen(searchView.query.toString())
                    return true
                }

            })
        }

        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId){
//            R.id.search -> Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
//        }
//        return true
//    }

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

    private fun initScreen(search: String) {
        notaList = (databaseHandler.notas()).filter {
            (it.titulo).contains(search, true)
        } as ArrayList<Nota>
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