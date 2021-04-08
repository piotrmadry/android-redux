package com.zumba.redux

import RemoveItemUseCase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class Details : AppCompatActivity() {
    
    private val viewModel by viewModel { DetailsViewModel(intent.getIntExtra("id", -1), RemoveItemUseCase(Repository)) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        
        val id: Int = intent.getIntExtra("id", -1)
        
        val text_view = findViewById<TextView>(R.id.details_text_view)
        text_view.text = id.toString()
    
        findViewById<Button>(R.id.details_button).setOnClickListener {
            viewModel.remove()
        }
        
        viewModel.currentEffects.observe(this) {
            finish()
        }
    }
}