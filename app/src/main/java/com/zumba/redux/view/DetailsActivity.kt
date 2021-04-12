package com.zumba.redux.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zumba.redux.viewmodel.DetailsViewModel
import com.zumba.redux.R
import com.zumba.redux.repository.Repository
import com.zumba.redux.usecase.RemoveItemUseCase
import com.zumba.redux.viewModel
import kotlinx.coroutines.InternalCoroutinesApi

class DetailsActivity : AppCompatActivity() {
    
    private val viewModel by viewModel { DetailsViewModel(intent.getIntExtra("id", -1), RemoveItemUseCase(
        Repository
    )
    ) }
    
    @InternalCoroutinesApi
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