package com.example.movieapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.data.db.dao.MovieDao
import com.example.movieapp.data.db.entity.MovieEntity

@Database(version = 1, entities = [MovieEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}