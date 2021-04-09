package com.google.joel.coffee.program.notebloc.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.google.joel.coffee.program.blocodenotas.model.Nota

class DatabaseHandler(var context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY," +
                "$TITULO TEXT," +
                "$ANOTACAO TEXT" +
                ");"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addNota(nota: Nota): Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(TITULO, nota.titulo)
        values.put(ANOTACAO, nota.anotacao)

        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        return ("$success").toInt() != -1
    }

    fun getNota(id: Int): Nota {
        val nota = Nota()
        val db = writableDatabase
        val select = "SELECT * FROM $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(select, null)
        cursor?.moveToFirst()
        nota.id = cursor.getInt(cursor.getColumnIndex(ID))
        nota.titulo = cursor.getString(cursor.getColumnIndex(TITULO))
        nota.anotacao = cursor.getString(cursor.getColumnIndex(ANOTACAO))

        cursor.close()
        db.close()

        return nota
    }

    fun notas(): ArrayList<Nota> {
        val notasList = ArrayList<Nota>()
        val db = writableDatabase
        val select = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(select, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val nota = Nota()
                    nota.id = cursor.getInt(cursor.getColumnIndex(ID))
                    nota.titulo = cursor.getString(cursor.getColumnIndex(TITULO))
                    nota.anotacao = cursor.getString(cursor.getColumnIndex(ANOTACAO))
                    notasList.add(nota)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()

        return notasList
    }

    fun updataNota(nota: Nota): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TITULO, nota.titulo)
            put(ANOTACAO, nota.anotacao)
        }
        val success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(nota.id.toString())).toLong()
        db.close()
        return ("$success").toInt() != -1
    }

    fun deleteNota(id: Int): Boolean{
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, ID + "=?", arrayOf(id.toString())).toLong()
        db.close()
        return ("$success").toInt() != -1
    }

    fun deleteAllNotas(): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, null, null).toLong()
        return ("$success").toInt() != -1
    }

    
    fun moveToUpDown(up: Boolean, id: Int, size: Int) {
        if (up) {
            if (id > 1) {
                var nota1 = getNota(id)
                var nota2 = getNota( id - 1)

                nota1.id = id -1
                updataNota(nota1)
                nota2.id = id
                updataNota(nota2)
//                Toast.makeText(context, "id1 ${nota1.id}  id2 ${nota2.id}", Toast.LENGTH_SHORT).show()

            }
        } else {
            if (id < size) {
                var nota1 = getNota(id)
                var nota2 = getNota( id + 1)

                nota1.id = id + 1
                updataNota(nota1)
                nota2.id = id
                updataNota(nota2)
//                Toast.makeText(context, "<id1 ${nota1.id}  <id2 ${nota2.id}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val DB_VERSION = 1
        private val DB_NAME = "Bloco"
        private val TABLE_NAME = "Notas"
        private val ID = "Id"
        private val TITULO = "Titulo"
        private val ANOTACAO = "Anotacao"
    }
}