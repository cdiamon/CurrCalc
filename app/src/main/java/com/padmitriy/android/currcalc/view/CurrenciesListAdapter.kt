package com.padmitriy.android.currcalc.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
    var cursorPosition = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)

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
    }

    override fun getItemCount() = ratesResponses.size

    inner class CurrencyViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun init(rateModel: RateModel) {

            var resourceName = rateModel.name.toLowerCase(Locale.getDefault())

            // workaround because try is java reserved name
            if (rateModel.name == "TRY") resourceName = "z_try"

            val resourceId = containerView.context.resources.getIdentifier(
                resourceName, "drawable",
                containerView.context.packageName
            )

            with(containerView) {
                GlideApp.with(this)
                    .load(resourceId)
                    .error(ContextCompat.getDrawable(context, R.drawable.placeholder_circle))
                    .apply(RequestOptions.circleCropTransform())
                    .into(currImage)

                // BigDecimal to work with floating point TODO refactor BigDecimal to string in adapter, move logic to presenter
                currName.text = rateModel.name
                val trimmedValue =
                    BigDecimal(rateModel.value.toString()).setScale(2, RoundingMode.HALF_DOWN)
                        .stripTrailingZeros()
                currValueInput.setText(trimmedValue.toPlainString())
                try {
                    currValueInput.setSelection(cursorPosition)
                } catch (ignore: Exception) {
                }
            }

            //moving active item to top logic and listen to currency value change
            currValueInput.setOnFocusChangeListener { v, hasFocus ->

                if (adapterPosition != 0) {
                    currencyValueListener.onFocusChanged(rateModel.name)
                }

                currValueInput.addTextChangedListener { text ->
                    if (adapterPosition == 0) {
                        cursorPosition = currValueInput.selectionStart
                        if (!text.isNullOrBlank()) {
                            currencyValueListener.onValueChanged(
                                rateModel.name,
                                text.toString().toDouble()
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * @property onFocusChanged called when we click on non-top input
     * @property onValueChanged called when we change value in top input
     */
    interface CurrencyValueListener {
        fun onFocusChanged(name: String)
        fun onValueChanged(name: String, value: Double)
    }
}
