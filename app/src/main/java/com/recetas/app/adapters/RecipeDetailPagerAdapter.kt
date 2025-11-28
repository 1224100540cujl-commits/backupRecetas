package com.recetas.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.recetas.app.R

class RecipeDetailPagerAdapter(
    private val ingredients: List<String>,
    private val instructions: String
) : RecyclerView.Adapter<RecipeDetailPagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = when (viewType) {
            0 -> R.layout.tab_ingredients
            else -> R.layout.tab_instructions
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            0 -> setupIngredientsView(holder.itemView)
            1 -> setupInstructionsView(holder.itemView)
        }
    }

    override fun getItemCount() = 2

    override fun getItemViewType(position: Int) = position

    private fun setupIngredientsView(view: View) {
        val container = view.findViewById<LinearLayout>(R.id.ingredientsContainer)
        container.removeAllViews()

        ingredients.forEach { ingredient ->
            // Crear un LinearLayout horizontal para cada ingrediente
            val ingredientLayout = LinearLayout(view.context).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(0, 12, 0, 12)
            }

            // Crear CheckBox
            val checkbox = CheckBox(view.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Crear TextView para el texto del ingrediente
            val textView = TextView(view.context).apply {
                text = ingredient.trim()
                textSize = 16f
                setPadding(16, 0, 0, 0)
                setTextColor(ContextCompat.getColor(view.context, R.color.black))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            // Agregar checkbox y texto al layout del ingrediente
            ingredientLayout.addView(checkbox)
            ingredientLayout.addView(textView)

            // Agregar el layout del ingrediente al contenedor principal
            container.addView(ingredientLayout)
        }
    }

    private fun setupInstructionsView(view: View) {
        val container = view.findViewById<LinearLayout>(R.id.instructionsContainer)
        container.removeAllViews()

        val instructionSteps = instructions.split("\n").filter { it.trim().isNotEmpty() }

        instructionSteps.forEachIndexed { index, step ->
            val stepView = TextView(view.context).apply {
                text = "${index + 1}. ${step.trim()}"
                textSize = 16f
                setPadding(0, 16, 0, 16)
                setTextColor(ContextCompat.getColor(view.context, R.color.black))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            container.addView(stepView)

            // Agregar línea divisoria excepto en el último
            if (index < instructionSteps.size - 1) {
                val divider = View(view.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                    )
                    setBackgroundColor(ContextCompat.getColor(view.context, R.color.gray_light))
                }
                container.addView(divider)
            }
        }
    }
}