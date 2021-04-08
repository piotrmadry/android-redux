package com.zumba.redux.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zumba.redux.*
import com.zumba.redux.repository.Repository
import com.zumba.redux.usecase.FlowGetItemsUseCase
import com.zumba.redux.view.list.MainAdapter
import com.zumba.redux.viewmodel.ListState
import com.zumba.redux.viewmodel.ListViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: ListViewModel by viewModel { ListViewModel(FlowGetItemsUseCase(Repository)) }
    var mainAdapter: MainAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mainAdapter = MainAdapter()
        recyclerView.adapter = mainAdapter

        viewModel.currentState.observe(this) { render(it) }

        val button = findViewById<Button>(R.id.main_resfresh)
        button.setOnClickListener {
            viewModel.refresh()
        }
    }

    fun render(listState: ListState) {
        mainAdapter?.bindList(listState.list)
    }
}
