package com.amnah.weather.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amnah.weather.databinding.DailyStateBinding
import com.amnah.weather.model.Daily
import com.amnah.weather.util.CustomImage
import com.amnah.weather.util.DateFormatWeather

class DailyWeatherAdapter(
    val dailyWeatherList: List<Daily>
) : RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder>() {

    inner class DailyWeatherViewHolder(val binding: DailyStateBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder =
        DailyWeatherViewHolder(
            DailyStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val position = dailyWeatherList[position + 1]
        holder.binding.apply {
            dateItem.text = position.dt.let {
                DateFormatWeather.getDateTime(
                    it.toString(),
                    "EEEE"
                )
            }.toString()
            dailyStateWeatherItem.text = position.weather?.joinToString { it.description.toString() } ?: ""
            dailyTemperatureItem.text = "${position.temp?.day?.toInt()}ْ C"
            position.weather?.joinToString { it.id.toString() }
                .let { CustomImage.getImage(it.toString()) }.also {
                    dailyIconWeatherItem.setImageResource(it)
                }
        }
    }

    override fun getItemCount() = dailyWeatherList.size - 1
}