package com.abc.todo.core.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.abc.todo.data.db.AppDatabase
import com.abc.todo.data.model.local.dao.TodoDao
import com.abc.todo.data.repository.TodoRepositoryImpl
import com.abc.todo.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Define migration as a public or internal property if needed, but for Dagger validation,
    // explicitly typing it or just moving it can help. 
    // The error usually happens because Dagger tries to inspect the anonymous class.
    
//    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//        override fun migrate(db: SupportSQLiteDatabase) {
//            // Add createdAt column
//            db.execSQL("ALTER TABLE todos ADD COLUMN date INTEGER NOT NULL DEFAULT 0")
//
//            // Update existing rows
//            val currentTime = System.currentTimeMillis()
//            db.execSQL("UPDATE todos SET createdAt = $currentTime, date = $currentTime")
//        }
//    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "todo.db")
            //.addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase) = db.todoDao()

    @Provides
    @Singleton
    fun provideRepository(dao: TodoDao): TodoRepository = TodoRepositoryImpl(dao)

}
