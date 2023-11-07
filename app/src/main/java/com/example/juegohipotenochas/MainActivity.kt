package com.example.juegohipotenochas

import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu)
        setSupportActionBar(toolbar)

        generarTablero()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun generarTablero(){

        val gridLayout = findViewById<GridLayout>(R.id.tablero_gridLayout)

        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val button = Button(this)
                button.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    width = 0
                    height = 0
                    setMargins(5,5,5,5)
                }
                button.text = "" // Puedes establecer el texto del botón si lo deseas
                button.setBackgroundResource(R.color.white) // Puedes personalizar el fondo aquí
                button.setOnClickListener {
                    // Lógica del juego al hacer clic en una celda
                }
                gridLayout.addView(button)
            }
        }

    }
}

