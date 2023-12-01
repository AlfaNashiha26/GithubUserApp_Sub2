package com.alfa.githubuserapp.utils

import com.alfa.githubuserapp.data.model.UserEntity
import com.alfa.githubuserapp.data.source.remote.responses.UserResponse

object DataMapper {

    /*
        Ubah Response data ke Entity
     */
    fun mapResponseToEntity(response: UserResponse): UserEntity{
        return UserEntity(
            response.id,
            response.username,
            response.avatarUrl,
            response.htmlUrl
        )
    }

    /*
        Ubah Response data list ke Entity list
    */
    fun mapResponsesToEntities(responses: List<UserResponse>): List<UserEntity>{
        return responses.map {
            mapResponseToEntity(it)
        }
    }
}