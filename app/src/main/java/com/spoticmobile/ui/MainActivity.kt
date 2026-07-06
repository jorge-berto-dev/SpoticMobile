package com.spoticmobile.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.card.MaterialCardView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.spoticmobile.R

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("spoticmobile", Context.MODE_PRIVATE)

        val statusText = findViewById<TextView>(R.id.statusText)
        val statusDetail = findViewById<TextView>(R.id.statusDetail)

        // Check if running inside Spotify via LSPatch
        val isInSpotify = try {
            Class.forName("com.spotify.music.MainActivity")
            true
        } catch (_: Throwable) {
            false
        }

        if (isInSpotify) {
            statusText.text = "✓ Ativo no Spotify"
            statusDetail.text = "Módulo carregado com sucesso!"
        } else {
            statusText.text = "○ Modo Configuração"
            statusDetail.text = "Use LSPatch para ativar no Spotify"
        }

        // Setup toggles
        setupSwitch(R.id.switchPremium, "premium_enabled", true)
        setupSwitch(R.id.switchAdBlock, "adblock_enabled", true)
        setupSwitch(R.id.switchQuality, "quality_enabled", true)
    }

    private fun setupSwitch(id: Int, key: String, default: Boolean) {
        val switch = findViewById<SwitchMaterial>(id)
        switch.isChecked = prefs.getBoolean(key, default)
        switch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(key, isChecked).apply()
        }
    }

    companion object {
        fun isEnabled(context: Context, key: String, default: Boolean = true): Boolean {
            return context.getSharedPreferences("spoticmobile", Context.MODE_PRIVATE)
                .getBoolean(key, default)
        }
    }
}
