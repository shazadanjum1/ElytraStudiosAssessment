package com.elytrastudios.assessment.data.local


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DogBreedEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao
    abstract fun userDao(): UserDao
}
