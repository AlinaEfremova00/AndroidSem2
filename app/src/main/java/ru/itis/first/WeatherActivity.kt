package ru.itis.first

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
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

class WeatherActivity() : AppCompatActivity(), Parcelable {
    private lateinit var binding: ActivityWeatherBinding

    constructor(parcel: Parcel) : this() {

    }

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
                        404 -> Toast.makeText(
                            this@WeatherActivity,
                            "Город не найден",
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> Toast.makeText(
                            this@WeatherActivity,
                            "Ошибка сервера: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@WeatherActivity,
                    "Ошибка сети: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherActivity> {
        override fun createFromParcel(parcel: Parcel): WeatherActivity {
            return WeatherActivity(parcel)
        }

        override fun newArray(size: Int): Array<WeatherActivity?> {
            return arrayOfNulls(size)
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