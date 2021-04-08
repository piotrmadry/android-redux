package com.zumba.redux

import GetItemsUseCase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity2 : AppCompatActivity() {
    
    val viewModel: ListViewModel by viewModel { ListViewModel(GetItemsUseCase(Repository())) }
    var adapter: Adapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

    
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter()
        recyclerView.adapter = adapter
    
        viewModel.currentState.observe(this){ render(it) }
    
        val button = findViewById<Button>(R.id.main_resfresh)
        button.setOnClickListener {
            viewModel.refresh()
        }
    
        
    }
    fun render(listState: ListState) {
        adapter?.bindList(listState.list)
    }
}
inline fun <reified T : ViewModel> Fragment.viewModel(crossinline provider: () -> T): Lazy<T> =
    viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
        }
    }

inline fun <reified T : ViewModel> ComponentActivity.viewModel(crossinline provider: () -> T): Lazy<T> =
    viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider() as T
        }
    }