package com.alfa.githubuserapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.alfa.githubuserapp.data.model.UserEntity
import com.alfa.githubuserapp.data.source.local.AlfaDao
import com.alfa.githubuserapp.data.source.local.AlfaDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var dao: AlfaDao?
    private var database: AlfaDatabase? = AlfaDatabase.getDatabase(application)

    init{
        dao = database?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>>?{
        return dao?.getFavoriteUser()
    }
}