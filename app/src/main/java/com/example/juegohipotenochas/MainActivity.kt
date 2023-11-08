package com.example.juegohipotenochas

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Random

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
            R.id.select_character_spinner -> {
                //selectCharacter()
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

        // Llamamos al metodo de generar minas, y le pasamos los valores de las filas, columnas y minas
        val minas = generarMinas(numRows, numCols, numMinas)

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

                // Verifica si la coordenada actual es una mina y configura el botón en consecuencia
                val isMina = minas.contains(Pair(row, col))
                if (isMina) {
                    button.text =
                        "M" // Esto es solo un ejemplo, puedes personalizar la apariencia para representar una mina
                }

                button.setOnClickListener {
                    // Lógica del juego al hacer clic en una celda
                    if (isMina) {
                        // El jugador ha perdido
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("HAS PERDIDO")
                        builder.setMessage("¿Quieres volver a jugar?")

                        builder.setNegativeButton("Salir") { dialog, _ ->
                            dialog.dismiss()
                            finish()
                        }

                        builder.setPositiveButton("Reiniciar") { dialog, _ ->
                            dialog.dismiss()
                            // Limpiar el layout actual y volver a crearlo
                            gridLayout.removeAllViews()
                            generarTablero()
                        }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()

                    } else {

                        // El jugador no ha perdido, comprueba si hay minas alrededor
                        comprobarMinas(row, col, button, minas)

                    }

                }
                gridLayout.addView(button)
            }
        }
    }

    private fun generarMinas(numRows: Int, numCols: Int, numMinas: Int): List<Pair<Int, Int>> {

        val minas = mutableListOf<Pair<Int, Int>>()

        // Genera minas aleatoriamente con un random y un bucle while
        val random = Random()
        while (minas.size < numMinas) {
            val randomRow = random.nextInt(numRows)
            val randomCol = random.nextInt(numCols)
            val nuevaMina = Pair(randomRow, randomCol)

            // Comprueba que no haya minas duplicadas
            if (!minas.contains(nuevaMina)) {
                minas.add(nuevaMina)
            }
        }

        return minas
    }

    private fun comprobarMinas(
        numRows: Int,
        numCols: Int,
        button: Button,
        minas: List<Pair<Int, Int>>
    ) {
        // Verificar si la celda actual ya se ha procesado
        if (!button.isEnabled) {
            return
        }

        // Contador para minas adyacentes
        var minasAlrededor = 0

        // Comprueba si hay minas alrededor de la celda actual
        for (i in numRows - 1..numRows + 1) {
            for (j in numCols - 1..numCols + 1) {
                if (minas.contains(Pair(i, j))) {
                    minasAlrededor++
                }
            }
        }

        if (minasAlrededor > 0) {
            // Hay minas adyacentes, mostrar el número de minas
            button.text = minasAlrededor.toString()
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            button.isEnabled = false
        } else {
            // No hay minas adyacentes, explorar las celdas adyacentes recursivamente
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            button.isEnabled = false

            for (i in numRows - 1..numRows + 1) {
                for (j in numCols - 1..numCols + 1) {
                    if (i in 0 until numRows && j in 0 until numCols) {
                        /*val adjacentButton = Pair(i, j) as Button
                        comprobarMinas(i, j, adjacentButton, minas)*/
                    }
                }
            }
        }
    }


    private fun showInstructions() {

        // Muestra las instrucciones del juego con AlertDialog, mediante un value llamado builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Instrucciones")
        builder.setMessage(
            "El juego esta basado en el juego del buscaminas, cuando pulsas en una casilla, sale un número que identifica cuántas hipotenochas hay alrededor:\n" +
                    "Ten cuidado porque si pulsas en una casilla que tenga una hipotenocha escondida, perderás.\n" +
                    "Si crees o tienes la certeza de que hay una hipotenocha, haz un click largo sobre la casilla para señalarla.\n" +
                    "No hagas un click largo en una casilla donde no hay una hipotenocha porque perderás. Ganas una vez hayas encontrado todas las hipotenochas."
        )

        // Botón para cerrar el AlertDialog
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        // Muestra el dailogo
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

    /*private fun selectCharacter() {

        val dialogView = layoutInflater.inflate(R.layout.select_character, null)

        // Muestra las instrucciones del juego con AlertDialog, mediante un value llamado builder
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona un personaje")
        builder.setMessage(
            "Elige un personaje para jugar"
        )

        // Botón para cerrar el AlertDialog
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        // Muestra el dailogo
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }*/

