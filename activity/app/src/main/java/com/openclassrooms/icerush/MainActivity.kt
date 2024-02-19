package com.openclassrooms.icerush

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.icerush.databinding.ActivityMainBinding
import com.openclassrooms.icerush.domain.model.SnowReportModel
import com.openclassrooms.icerush.presentation.home.HomeViewModel
import com.openclassrooms.icerush.presentation.home.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), WeatherAdapter.OnItemClickListener {


    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private val customAdapter = WeatherAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defineRecyclerView()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateCurrentWeather(it.forecast)
                }
            }
        }
    }


    private fun updateCurrentWeather(forecast: List<SnowReportModel>) {
        customAdapter.submitList(forecast)
    }


    private fun defineRecyclerView() {
        val layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = customAdapter
    }

    override fun onItemClick(item: SnowReportModel) {
        Toast.makeText(
            this,
            "Temperature: ${item.temperatureCelsius}°C - Meteo Type: ${item.weatherTitle}",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}
