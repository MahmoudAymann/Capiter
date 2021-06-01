package com.capiter.main.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.capiter.main.BuildConfig
import com.capiter.main.constants.ConstString
import com.capiter.user.db.AppDb
import com.capiter.main.data.local.SessionManager
import com.capiter.user.db.UserDao
import com.capiter.main.data.remote.UserApiService
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Suppress("unused")
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPref(app: Application): SessionManager {
        return SessionManager(
            app.getSharedPreferences(
                ConstString.Const_PREFS_NAME,
                Context.MODE_PRIVATE
            )
        )
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room.databaseBuilder(app, AppDb::class.java, "capiter.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: AppDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        val headers = Interceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .header("x-apikey", sessionManager.getApiKey())
                    .build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
                addInterceptor(headers)
                readTimeout(120, TimeUnit.SECONDS)
                connectTimeout(120, TimeUnit.SECONDS)
                writeTimeout(120, TimeUnit.SECONDS)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideUserApiService(okHttpClient: OkHttpClient): UserApiService {
        return getRetrofitInstance(okHttpClient)
            .create(UserApiService::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideDeliveryApiService(okHttpClient: OkHttpClient): DeliveryApiService {
//        return getRetrofitInstance(okHttpClient)
//            .create(DeliveryApiService::class.java)
//    }


    private fun getRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ConstString.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(okHttpClient)
        .build()


}
