package com.alfa.githubuserapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alfa.githubuserapp.data.model.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class AlfaDatabase: RoomDatabase(){
    companion object {
        private var INSTANCE : AlfaDatabase? = null

        fun getDatabase(context: Context): AlfaDatabase?{
            if(INSTANCE == null){
                synchronized(AlfaDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AlfaDatabase::class.java, "mk_codehub_db").build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteUserDao(): AlfaDao
}