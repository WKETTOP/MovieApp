package com.example.movieapp.data.dto

import android.content.SharedPreferences

class LocalStorage(private val sharedPreferences: SharedPreferences) {
    private companion object {
        const val FAVORITE_KEY = "FAVORITES_KEY"
    }

    fun addToFavorite(moviesId: String) {
        changeFavorites(moviesId = moviesId, remove = false)
    }

    fun removeFromFavorites(moviesId: String) {
        changeFavorites(moviesId = moviesId, remove = true)
    }

    fun getSavedFavorites(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITE_KEY, emptySet()) ?: emptySet()
    }

    private fun changeFavorites(moviesId: String, remove: Boolean) {
        val mutableSet = getSavedFavorites().toMutableSet()
        val modified = if (remove) mutableSet.remove(moviesId) else mutableSet.add(moviesId)
        if (modified) sharedPreferences.edit().putStringSet(FAVORITE_KEY, mutableSet).apply()
    }
}
