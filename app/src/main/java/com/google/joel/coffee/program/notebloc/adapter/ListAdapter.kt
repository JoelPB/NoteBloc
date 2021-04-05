package com.google.joel.coffee.program.notebloc.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.joel.coffee.program.blocodenotas.model.Nota
import com.google.joel.coffee.program.notebloc.NotaActivity
import com.google.joel.coffee.program.notebloc.R
import kotlin.reflect.KFunction1

class ListAdapter(
    val notaList: List<Nota>,
    internal var context: Context
//    private val callbacks: KFunction1<Int, Unit>
    ): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    // Criação do ViewHolder com base no layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_list, parent, false)
        return ViewHolder(view)
    }

    // Alterações no item do ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nota = notaList[position]

        holder._titulo.text = nota.titulo
        if (position % 2 ==  0) holder._titulo.setBackgroundColor(Color.GRAY)
        else holder._titulo.setBackgroundColor(Color.DKGRAY)
        holder._titulo.setOnClickListener {
            val intent = Intent(context, NotaActivity::class.java)
            intent.putExtra("edit", true)
            intent.putExtra("position", nota.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = notaList.size


    inner class ViewHolder(intemView: View): RecyclerView.ViewHolder(intemView) {
        val _titulo = intemView.findViewById<TextView>(R.id.btnTituloList)
    }


}