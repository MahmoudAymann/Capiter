package com.capiter.user.di

import android.app.Application
import androidx.room.Room
import com.capiter.main.di.modules.SharedAppModule
import com.capiter.user.api.UserApiService
import com.capiter.user.db.UserAppDb
import com.capiter.user.db.UserDao
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(
    includes = [
        SharedAppModule::class
    ]
)
class UserAppModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): UserAppDb {
        return Room.databaseBuilder(app, UserAppDb::class.java, "user_capiter.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: UserAppDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideUserApiService(retrofit: Retrofit): UserApiService {
        return retrofit
            .create(UserApiService::class.java)
    }

}