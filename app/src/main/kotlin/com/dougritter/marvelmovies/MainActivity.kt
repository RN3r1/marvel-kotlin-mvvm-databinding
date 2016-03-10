package com.dougritter.marvelmovies

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timestamp = "1457563230"
        val privateKey = "083203d3fad457db9c2733611f21a71e1f8a78fd"
        val publicKey = "a4734679191fb19cf3573a65926dd720"

        Utils.md5(timestamp+privateKey+publicKey)

        Log.e(MainActivity::class.java.simpleName, Utils.md5(timestamp+privateKey+publicKey))

    }
}
