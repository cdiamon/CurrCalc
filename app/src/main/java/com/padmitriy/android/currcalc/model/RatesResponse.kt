package com.padmitriy.android.currcalc.model

//@Entity
data class RatesResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
) {
//    @PrimaryKey(autoGenerate = true)
//    var currencyId: Int = 0
}
