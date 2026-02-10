package com.example.mymidialist.data
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymidialist.model.Midia

@Database(entities = [Midia::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun midiaDao(): MidiaDao

}