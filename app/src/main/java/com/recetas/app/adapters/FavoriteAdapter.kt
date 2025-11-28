package com.recetas.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.recetas.app.R
import com.recetas.app.data.model.Receta

class FavoriteAdapter(
    private val onItemClick: (Receta) -> Unit,
    private val onRemoveFavorite: (Receta) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var recipes = emptyList<Receta>()

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emojiTextView: TextView = itemView.findViewById(R.id.favoriteRecipeEmoji)
        val nameTextView: TextView = itemView.findViewById(R.id.favoriteRecipeName)
        val categoryTextView: TextView = itemView.findViewById(R.id.favoriteRecipeCategory)
        val timeTextView: TextView = itemView.findViewById(R.id.favoriteRecipeTime)
        val servingsTextView: TextView = itemView.findViewById(R.id.favoriteRecipeServings)
        val removeFavoriteButton: ImageButton = itemView.findViewById(R.id.removeFavoriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_recipe, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentRecipe = recipes[position]

        holder.emojiTextView.text = currentRecipe.imageUrl ?: "🍽️"
        holder.nameTextView.text = currentRecipe.name
        holder.categoryTextView.text = currentRecipe.category
        holder.timeTextView.text = currentRecipe.time
        holder.servingsTextView.text = currentRecipe.servings.toString()

        holder.itemView.setOnClickListener {
            onItemClick(currentRecipe)
        }

        holder.removeFavoriteButton.setOnClickListener {
            onRemoveFavorite(currentRecipe)
        }
    }

    override fun getItemCount() = recipes.size

    fun setRecipes(recipes: List<Receta>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}