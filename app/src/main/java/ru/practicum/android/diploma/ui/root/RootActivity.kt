package ru.practicum.android.diploma.ui.root

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}
