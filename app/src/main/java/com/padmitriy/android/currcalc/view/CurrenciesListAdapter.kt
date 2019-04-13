package com.padmitriy.android.currcalc.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.padmitriy.android.currcalc.R
import com.padmitriy.android.currcalc.model.RateModel
import com.padmitriy.android.currcalc.util.CurrenciesDiffUtil
import com.padmitriy.android.currcalc.util.GlideApp
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_currency.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


class CurrenciesListAdapter(val currencyValueListener: CurrencyValueListener) :
    RecyclerView.Adapter<CurrenciesListAdapter.CurrencyViewHolder>() {

    private var ratesResponses: List<RateModel> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)

        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.init(ratesResponses[position])
    }

    fun setCurrencies(ratesResponses: List<RateModel>) {


        val diffUtilCallback = CurrenciesDiffUtil(
            this.ratesResponses, ratesResponses
        )
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)

        this.ratesResponses = ratesResponses
        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }

    override fun getItemCount() = ratesResponses.size

    inner class CurrencyViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun init(rateModel: RateModel) {

            var resourceName = rateModel.name.toLowerCase()
            // try is java reserved name
            if (rateModel.name == "TRY") resourceName = "z_try"
            val resourceId = containerView.context.resources.getIdentifier(
                resourceName, "drawable",
                containerView.context.packageName
            )

            with(containerView) {
                GlideApp.with(this)
                    .load(resourceId)
                    .error(resources.getDrawable(R.drawable.placeholder_circle))
                    .apply(RequestOptions.circleCropTransform())
                    .into(currImage)

                currName.text = rateModel.name
                val trimmedValue =
                    BigDecimal(rateModel.value.toString()).setScale(2, RoundingMode.HALF_DOWN).stripTrailingZeros()
                currValueInput.setText(trimmedValue.toPlainString())
                try {
                    currValueInput.setSelection(cursorPosition)
                } catch (ignore: Exception) {
                }
            }

            currValueInput.setOnFocusChangeListener { v, hasFocus ->

                //                Collections.swap(ratesResponses, position, 0)
//                notifyItemMoved(position, 0)

                if (position != 0) {
                    currencyValueListener.onFocusChanged(rateModel.name)
                }
//                v.onFocusChangeListener = null

                currValueInput.addTextChangedListener { text ->
                    if (position == 0) {
                        cursorPosition = currValueInput.selectionStart
                        if (!text.isNullOrBlank()) {
                            currencyValueListener.onValueChanged(rateModel.name, text.toString().toDouble())
                        }
//                        cache = text.toString().toDouble()
                    }
                }
            }
        }
    }

    var cursorPosition = 1
//    var cache: Double = 0.0

    interface CurrencyValueListener {
        fun onFocusChanged(name: String)
        fun onValueChanged(name: String, value: Double)
    }
}
