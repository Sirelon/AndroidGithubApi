package com.sirelon.githubapi.feature.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created on 2019-09-05 20:51 for GithubAPi.
 */
@Entity(tableName = "repository")
data class Repository(
    @PrimaryKey
    val id: Long,
    val name: String,
    val description: String,
    val starCount: Int,
    val url: String
)