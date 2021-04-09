package com.google.joel.coffee.program.notebloc.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.joel.coffee.program.blocodenotas.model.Nota
import com.google.joel.coffee.program.notebloc.NotaActivity
import com.google.joel.coffee.program.notebloc.R
import com.google.joel.coffee.program.notebloc.db.DatabaseHandler
import kotlin.reflect.KFunction2

class ListAdapter(
        val notaList: List<Nota>,
        internal var context: Context,
        private val callbacks: KFunction2<Boolean, Int, Unit>
    ): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    // Criação do ViewHolder com base no layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_list, parent, false)
        return ViewHolder(view)
    }

    // Alterações no item do ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        alteraView(holder, position)
//        val nota = notaList[position]
//
//        holder._titulo.text = nota.titulo
//        if (position % 2 ==  0) holder._titulo.setBackgroundColor(Color.GRAY)
//        else holder._titulo.setBackgroundColor(Color.DKGRAY)
        holder._titulo.setOnClickListener {
            val intent = Intent(context, NotaActivity::class.java)
            intent.putExtra("edit", true)
            intent.putExtra("position", notaList[position].id)
            context.startActivity(intent)
        }

        holder._btnUp.setOnClickListener {
            if (position > 0) {
                val databaseHandler = DatabaseHandler(context)
                databaseHandler.moveToUpDown(true, position + 1, notaList.size)   // acrescenta 1 a posição, pois a posição da lista inicia em 0 e o id do dados SQLite inicia em 1
                callbacks(true, position)
            }
        }

        holder._btnDown.setOnClickListener {
            if (position < notaList.size - 1) {
                val databaseHandler = DatabaseHandler(context)
                databaseHandler.moveToUpDown(false, position + 1, notaList.size)
                callbacks(false, position)
            }
        }
    }

    override fun getItemCount() = notaList.size

    private fun alteraView(holder: ListAdapter.ViewHolder, position: Int) {
        val nota = notaList[position]

        holder._titulo.text = nota.titulo
        if (position % 2 ==  0) holder._titulo.setBackgroundColor(Color.GRAY)
        else holder._titulo.setBackgroundColor(Color.DKGRAY)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val _titulo = itemView.findViewById<TextView>(R.id.tvTituloList)
        val _btnUp = itemView.findViewById<Button>(R.id.btnUp)
        val _btnDown = itemView.findViewById<Button>(R.id.btnDown)
    }

}