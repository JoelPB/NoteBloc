package com.google.joel.coffee.program.notebloc


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.joel.coffee.program.blocodenotas.model.Nota
import com.google.joel.coffee.program.notebloc.databinding.ActivityNotaBinding
import com.google.joel.coffee.program.notebloc.db.DatabaseHandler

class NotaActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotaBinding

    // Base de dados SQLite
    var databaseHandler = DatabaseHandler(this)

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
            val nota = databaseHandler.getNota(position)
            binding.editTitulo.setText(nota.titulo)
            binding.editNota.setText(nota.anotacao)
            binding.btnEditInserir.setText("Editar")
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
                    finish()
                } else {
                    val nota = Nota(position,
                                    binding.editTitulo.text.toString(),
                                    binding.editNota.text.toString()
                            )
                    databaseHandler.addNota(nota)
                    finish()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Deletar")
                    .setMessage("Deseja deletar a anotação?")
                    .setPositiveButton("Sim"){ dialog, which ->
                        databaseHandler.deleteNota(position)
                        Toast.makeText(this, "Item $position Deletado", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    .setNegativeButton("Não"){ dialog, which -> }
                    .show()
            //finish()
        }
    }
}