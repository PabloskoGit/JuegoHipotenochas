package com.example.juegohipotenochas

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Spinner
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
            R.id.switchChar-> {
                selectCharacter()
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
                        comprobarMinas(row, col, button, minas, numRows, numCols, gridLayout)

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
        row: Int,
        col: Int,
        button: Button,
        minas: List<Pair<Int, Int>>,
        numRows: Int,
        numCols: Int,
        gridLayout: GridLayout) {

        // Verifica si la celda ya ha sido procesada
        if (!button.isEnabled) {
            button.setBackgroundResource(R.color.gray)
            return
        }
        // Marca la celda como procesada y deshabilita el botón
        button.isEnabled = false

        var contadorMinasAlrededor = 0

        // Verifica las celdas alrededor de la actual
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = row + i
                val newCol = col + j

                // Verifica si la nueva posición está dentro del tablero
                if (newRow in 0 until numRows && newCol in 0 until numCols) {
                    val currentPosition = Pair(newRow, newCol)

                    // Verifica si hay una mina en la posición actual
                    if (minas.contains(currentPosition)) {
                        contadorMinasAlrededor++
                    }
                }
            }
        }

        // Actualiza la apariencia del botón según el resultado
        if (contadorMinasAlrededor > 0) {
            // Hay minas alrededor, mostrar el número de minas
            button.text = contadorMinasAlrededor.toString()
            // Cambiar de color al texto del boton
            button.setTextColor(ContextCompat.getColor(this, R.color.black))
            button.setBackgroundResource(R.color.gray)

        } else {
            // No hay minas alrededor, realizar comprobación recursiva
            for (i in -1..1) {
                for (j in -1..1) {
                    val newRow = row + i
                    val newCol = col + j

                    // Verificar si la nueva posición está dentro del tablero
                    if (newRow in 0 until numRows && newCol in 0 until numCols) {
                        val adjacentButton = gridLayout.getChildAt(newRow * numCols + newCol) as Button
                        comprobarMinas(newRow, newCol, adjacentButton, minas, numRows, numCols, gridLayout)
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

    private fun selectCharacter() {

        val dialogView = layoutInflater.inflate(R.layout.select_character, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.select_character_spinner)
        //val iconoSelect = spinner.findViewById<ImageView>(R.id.switchChar)


        // Lista de recursos de imágenes para el Spinner
        val imageList = listOf(
            R.drawable.flagicon_blue,
            R.drawable.flagicon_green,
            R.drawable.flagicon_yellow,
            R.drawable.flagicon_red
        )

        val adapter = AdaptadorSpinnerImagenes(this, imageList)
        spinner.adapter = adapter

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona un personaje")
        builder.setView(dialogView)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            // Lógica a realizar cuando se hace clic en Aceptar
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}


class AdaptadorSpinnerImagenes(context: Context, private val images: List<Int>) :
    ArrayAdapter<Int>(context, android.R.layout.simple_spinner_item, images) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getImageForPosition(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getImageForPosition(position)
    }

    private fun getImageForPosition(position: Int): ImageView {
        val imageView = ImageView(context)
        imageView.setImageResource(images[position])
        return imageView
    }
}





