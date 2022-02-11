package com.example.navigationcomponentstutorials.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post (
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
): Parcelable