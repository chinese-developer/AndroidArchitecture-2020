package com.app.base.data.app

import androidx.room.*

@Entity(tableName = "app")
class AppEntity(val name: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Dao
interface AppDao {
    @Insert
    suspend fun insertAll(vararg appEntity: AppEntity)

    @Query("SELECT COUNT(*) FROM app")
    suspend fun queryAppAll(): Int
}