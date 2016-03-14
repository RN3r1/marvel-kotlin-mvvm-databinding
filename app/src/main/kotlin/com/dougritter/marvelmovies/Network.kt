package com.dougritter.marvelmovies

import rx.Observable

import retrofit.http.*
import retrofit.Retrofit
import retrofit.GsonConverterFactory
import retrofit.RxJavaCallAdapterFactory

import com.google.gson.GsonBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor

interface MarvelService {

    @Headers("Accept: */*")
    @GET("/v1/public/characters")
    public fun getCharacters(@Query("ts") ts: String,
                             @Query("apikey") apiKey: String,
                             @Query("hash") hash: String,
                             @Query("limit") limit: Int)
            : Observable<Model.CharacterResponse>

    companion object {
        fun create() : MarvelService {
            val gsonBuilder = GsonBuilder()

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient()
            client.interceptors().add(loggingInterceptor)

            val restAdapter = Retrofit.Builder()
                    .baseUrl("http://gateway.marvel.com")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .client(client)
                    .build()

            return restAdapter.create(MarvelService::class.java)
        }
    }

}
