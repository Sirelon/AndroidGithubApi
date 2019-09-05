package com.sirelon.githubapi.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


/**
 * Created on 2019-09-05 20:56 for GithubAPi.
 */
interface CommonDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun replaceAll(list: List<T>)

    @Update
    fun updateAll(list: List<T>): Int

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)

}