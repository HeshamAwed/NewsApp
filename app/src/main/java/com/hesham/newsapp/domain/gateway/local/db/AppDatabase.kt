package com.hesham.newsapp.domain.gateway.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hesham.newsapp.domain.entities.Article
import com.hesham.newsapp.domain.entities.SourceTypeConverter
import com.hesham.newsapp.domain.gateway.local.dao.ArticleDao

@Database(entities = [Article::class], version = 3)
@TypeConverters(SourceTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}