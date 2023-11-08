package com.example.juegohipotenochas

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu)
        setSupportActionBar(toolbar)

        generarTablero()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.gameInstructions -> {
                showInstructions()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun generarTablero() {

        // Generamos el tablero de juego, y creamos values con los valores de las filas, columnas y minas
        val gridLayout = findViewById<GridLayout>(R.id.tablero_gridLayout)
        val numRows = 8
        val numCols = 8
        val numMinas = 10

        // Generamos las celdas del tablero con dos bucles for anidados
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                val button = Button(this)
                button.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    width = 0
                    height = 0
                    setMargins(5, 5, 5, 5)
                }
                button.setBackgroundResource(R.color.white)
                button.setOnClickListener {
                    // Lógica del juego al hacer clic en una celda


                    // Llamamos al metodo de generar minas, y le pasamos los valores de las filas, columnas y minas
                    generarMinas(gridLayout, numRows, numCols, numMinas)
                }
                gridLayout.addView(button)
            }
        }


    }

    private fun generarMinas(gridLayout: GridLayout, numRows: Int, numCols: Int, numMinas: Int) {

        // Genera un conjunto de pares de coordenadas únicas para las minas (Revisar para entender mejor)
        val minasGeneradas = mutableSetOf<Pair<Int, Int>>()

        // Genera minas aleatoriamente mediante un bucle, y al value de randomRow y randomCol le asignamos un valor aleatorio
        // Finalmente le asignamos a nuevaMina el valor de randomRow y randomCol
        while (minasGeneradas.size < numMinas) {
            val randomRow = (0 until numRows).random()
            val randomCol = (0 until numCols).random()
            val nuevaMina = Pair(randomRow, randomCol)

            // Asegúrate de que no haya minas duplicadas en la misma celda
            if (!minasGeneradas.contains(nuevaMina)) {
                minasGeneradas.add(nuevaMina)
            }
        }

        // Coloca las minas en las celdas correspondientes
        for (mina in minasGeneradas) {
            val row = mina.first
            val col = mina.second
            val button = Button(gridLayout.context)
            // Configura el botón como una mina (puedes personalizarlo con un icono o color de mina)
            button.text = "M" // Por ejemplo, podrías usar "M" para representar una mina
            button.setBackgroundColor(ContextCompat.getColor(gridLayout.context, R.color.black))
            gridLayout.addView(button, row * numCols + col)
        }
    }


    private fun showInstructions() {

        // Muestra las instrucciones del juego con AlertDialog, mediante un value llamado builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Instrucciones")
        builder.setMessage("El juego esta basado en el juego del buscaminas, cuando pulsas en una casilla, sale un número que identifica cuántas hipotenochas hay alrededor:\n" +
                "Ten cuidado porque si pulsas en una casilla que tenga una hipotenocha escondida, perderás.\n" +
                "Si crees o tienes la certeza de que hay una hipotenocha, haz un click largo sobre la casilla para señalarla.\n" +
                "No hagas un click largo en una casilla donde no hay una hipotenocha porque perderás. Ganas una vez hayas encontrado todas las hipotenochas.")

        // Botón para cerrar el AlertDialog
        builder.setPositiveButton("Aceptar") { dialog, which ->
            dialog.dismiss()
        }

        // Muestra el dailogo
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

