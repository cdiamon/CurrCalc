package com.padmitriy.android.currcalc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RateModel(
    @PrimaryKey val name: String,
    val value: Double
)