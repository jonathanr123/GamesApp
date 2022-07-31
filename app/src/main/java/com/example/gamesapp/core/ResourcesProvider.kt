package com.example.gamesapp.core

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getString(@StringRes stringResId: Int, args: ArrayList<Any>? = arrayListOf()): String {
        return when (args) {
            null -> context.getString(stringResId)
            else -> context.getString(stringResId, *args.toArray())
        }
    }
}