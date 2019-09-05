package com.sirelon.githubapi.feature.ui.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created on 2019-09-05 20:51 for GithubAPi.
 */
@Entity(tableName = "repository")
data class Repository(
    @PrimaryKey
    val id: String,
    val name: String
)