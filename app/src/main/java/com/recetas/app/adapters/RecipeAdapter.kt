package com.recetas.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recetas.app.R
import com.recetas.app.data.model.Receta

class RecipeAdapter(
    private val onItemClick: (Receta) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Receta>()

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.recipeNameTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.recipeTimeTextView)
        val servingsTextView: TextView = itemView.findViewById(R.id.recipeServingsTextView)
        val emojiTextView: TextView = itemView.findViewById(R.id.recipeEmoji)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]

        holder.nameTextView.text = currentRecipe.name
        holder.timeTextView.text = currentRecipe.time
        holder.servingsTextView.text = currentRecipe.servings.toString()
        holder.emojiTextView.text = currentRecipe.imageUrl ?: "🍽️"

        holder.itemView.setOnClickListener {
            onItemClick(currentRecipe)
        }
    }

    override fun getItemCount() = recipes.size

    fun setRecipes(recipes: List<Receta>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}