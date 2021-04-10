package com.google.joel.coffee.program.notebloc


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.joel.coffee.program.blocodenotas.model.Nota
import com.google.joel.coffee.program.notebloc.databinding.ActivityNotaBinding
import com.google.joel.coffee.program.notebloc.db.DatabaseHandler

class NotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotaBinding

    // Base de dados SQLite
    private var databaseHandler = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar2)

        editNota()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nota, menu)

        // Deixa invisível previous/next
        if (posit == 0) {
            menu!!.findItem(R.id.previous).setVisible(false)
            menu!!.findItem(R.id.next).setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.previous -> {
                if (posit > 1) {
                    val intent = Intent(this, NotaActivity::class.java)
                    intent.putExtra("edit", true)
                    intent.putExtra("position", posit - 1)
                    this.startActivity(intent)
                }
//                Toast.makeText(this, "previous para $posit", Toast.LENGTH_SHORT).show()
            }
            R.id.next -> {
                if (posit < size) {
                    val intent = Intent(this, NotaActivity::class.java)
                    intent.putExtra("edit", true)
                    intent.putExtra("position", posit + 1)
                    this.startActivity(intent)
                }
//                Toast.makeText(this, "next para $posit", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

//    override fun onContextMenuClosed(menu: Menu) {
//        super.onContextMenuClosed(menu)
//    }

    private fun editNota() {
        val edit = intent.getBooleanExtra("edit", false)
        val position = intent.getIntExtra("position", 0)

        if (edit){
            val nota = databaseHandler.getNota(position)
            binding.run {
                editTitulo.setText(nota.titulo)
                editNota.setText(nota.anotacao)
            }
            binding.btnEditInserir.text = "Salvar alteração"
//            binding.previus.visibility = View.VISIBLE
//            binding.next.visibility = View.VISIBLE
            posit = nota.id
            size = databaseHandler.notas().size
        } else {
            binding.btnDelete.visibility = View.GONE
        }

        eventClick(edit, position)
    }

    private fun eventClick(edit: Boolean, position: Int) {
        binding.btnEditInserir.setOnClickListener {
            if (binding.editTitulo.text.toString() == ""){
                Toast.makeText(this, "Não há um título", Toast.LENGTH_SHORT).show()
            } else {
                if (edit){
                    val nota = Nota(position,
                                    binding.editTitulo.text.toString(),
                                    binding.editNota.text.toString()
                            )
                    databaseHandler.updataNota(nota)
//                    finish()
                    startMainActivity()
                } else {
                    val nota = Nota(position,
                                    binding.editTitulo.text.toString(),
                                    binding.editNota.text.toString()
                            )
                    databaseHandler.addNota(nota)
//                    finish()
                    startMainActivity()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
//            finish()
            startMainActivity()
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Deletar")
                    .setMessage("Deseja deletar a anotação?")
                    .setPositiveButton("Sim"){ _, _ ->
                        databaseHandler.deleteNota(position)
                        Toast.makeText(this, "Item $position Deletado", Toast.LENGTH_LONG).show()

//                        finish()
                        startMainActivity()
                    }
                    .setNegativeButton("Não"){ _, _ -> }
                    .show()
        }
    }

    private fun startMainActivity(){
        posit = 0
        size = 0
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)

    }
    companion object {
        var posit: Int = 0
        var size: Int = 0
    }
}