package com.ucs.bucket

import android.app.Application
import com.ucs.bucket.fragment.ApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App : Application(){
     companion object {
                   private lateinit var retrofit: Retrofit
                   private lateinit var apiService: ApiService
                   private lateinit var repositories: Repositories
                   open val ip = "http://178.128.82.36/"
                   fun injectApiService() = repositories
               }    

               override fun onCreate() {
                   super.onCreate()

                   retrofit = Retrofit.Builder()
                           .baseUrl(ip)
                           .addConverterFactory(GsonConverterFactory.create())
                           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                           .build()

                   apiService = retrofit.create(ApiService::class.java)
                   repositories = Repositories(apiService)
               }
}