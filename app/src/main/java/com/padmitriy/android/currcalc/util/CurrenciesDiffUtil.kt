package com.padmitriy.android.currcalc.util

import androidx.recyclerview.widget.DiffUtil
import com.padmitriy.android.currcalc.model.RateModel

class CurrenciesDiffUtil(private var oldList: List<RateModel>, private var newList: List<RateModel>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].name == newList[newItemPosition].name

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val new = newList[newItemPosition]
        val old = oldList[oldItemPosition]
        val isNameTheSame = new.name == old.name
        val isValueTheSame = new.value == old.value
        return isNameTheSame && isValueTheSame
    }

}