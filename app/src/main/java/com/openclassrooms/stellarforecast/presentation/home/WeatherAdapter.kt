package com.openclassrooms.stellarforecast.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.stellarforecast.databinding.ItemWeatherBinding
import com.openclassrooms.stellarforecast.domain.model.WeatherReportModel
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<WeatherReportModel, WeatherAdapter.WeatherViewHolder>(DiffCallback) {

    interface OnItemClickListener {
        fun onItemClick(item: WeatherReportModel)
    }

    class WeatherViewHolder(
        private val binding: ItemWeatherBinding,
        private val itemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormatter = SimpleDateFormat("dd/MM - HH:mm", Locale.getDefault())

        fun bind(observation: WeatherReportModel) {
            val formattedDate: String = dateFormatter.format(observation.date.time)
            binding.textViewDateTime.text = formattedDate
            binding.textViewStargazing.text = if (observation.isGoodForStargazing) "⭐️" else "☁️"
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(observation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView =
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<WeatherReportModel>() {
            override fun areItemsTheSame(
                oldItem: WeatherReportModel,
                newItem: WeatherReportModel
            ): Boolean {
                return oldItem.date == newItem.date
            }


            override fun areContentsTheSame(
                oldItem: WeatherReportModel,
                newItem: WeatherReportModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
