package com.example.movieapp.ui.movies

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.domain.models.Movie

class MovieViewHolder(
    parent: ViewGroup,
    private val clickListener: MoviesAdapter.MovieClickListener
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list, parent, false)
    ) {

    private var poster: ImageView = itemView.findViewById(R.id.poster)
    private var title: TextView = itemView.findViewById(R.id.title)
    private var description: TextView = itemView.findViewById(R.id.description)
    private var inFavoriteToggle: ImageView = itemView.findViewById(R.id.favorite)

    fun bind(movie: Movie) {
        Glide.with(itemView)
            .load(movie.image)
            .into(poster)

        title.text = movie.title
        description.text = movie.description
        inFavoriteToggle.setImageDrawable(getFavoriteToggleDrawable(movie.inFavorite))

        itemView.setOnClickListener { clickListener.onMovieClicked(movie) }

        inFavoriteToggle.setOnClickListener { clickListener.onFavoriteToggle(movie) }
    }

    private fun getFavoriteToggleDrawable(inFavorite: Boolean): Drawable? {
        return itemView.context.getDrawable(
            if (inFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_54
        )
    }
}