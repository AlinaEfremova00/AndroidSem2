package ru.itis.first

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.itis.first.data.RetrofitClient
import ru.itis.first.data.WeatherResponse
import ru.itis.first.databinding.ActivityWeatherBinding
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val city = intent.getStringExtra("CITY") ?: run {
            Toast.makeText(this, "Город не указан", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.instance.getWeather(city)
                }

                if (response.isSuccessful && response.body() != null) {
                    val weatherData = response.body()!!
                    updateUI(weatherData)
                } else {
                    when (response.code()) {
                        404 -> Toast.makeText(this@WeatherActivity, "Город не найден", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(this@WeatherActivity, "Ошибка сервера: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@WeatherActivity, "Ошибка сети: ${e.message}", Toast.LENGTH_SHORT).show()
                finish()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(weather: WeatherResponse) {
        binding.temperatureText.text = "Температура: ${weather.main.temp}°C"
        binding.descriptionText.text = weather.weather[0].description.capitalize(Locale.ROOT)
        binding.humidityText.text = "Влажность: ${weather.main.humidity}%"
        binding.windText.text = "Ветер: ${weather.wind.speed} м/с"

        Glide.with(this)
            .load("https://openweathermap.org/img/w/${weather.weather[0].icon}.png")
            .into(binding.weatherIcon)
    }
}