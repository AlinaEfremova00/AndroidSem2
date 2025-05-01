package ru.itis.first

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.itis.first.data.model.WeatherResponse
import ru.itis.first.databinding.ActivityWeatherBinding
import androidx.activity.viewModels
import ru.itis.first.ui.weather.WeatherUiState
import ru.itis.first.ui.weather.WeatherViewModel


class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()


    private fun finishWithError(message: String): Nothing {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
        throw IllegalStateException(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra("CITY") ?: return finishWithError("Город не указан")

        viewModel.loadWeather(city)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is WeatherUiState.Loading -> binding.progressBar.visibility = View.VISIBLE

                    is WeatherUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        updateUI(state.data)
                    }

                    is WeatherUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@WeatherActivity, state.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(weather: WeatherResponse) {
        binding.temperatureText.text = "Температура: ${weather.main.temp}°C"
        binding.descriptionText.text = weather.weather.firstOrNull()?.description
            ?.replaceFirstChar { it.uppercase() } ?: "Нет описания"
        binding.humidityText.text = "Влажность: ${weather.main.humidity}%"
        binding.windText.text = "Ветер: ${weather.wind.speed ?: 0.0} м/с"

        val icon = weather.weather.firstOrNull()?.icon
        if (icon != null) {
            Glide.with(this)
                .load("https://openweathermap.org/img/w/$icon.png")
                .into(binding.weatherIcon)
        }
    }
}