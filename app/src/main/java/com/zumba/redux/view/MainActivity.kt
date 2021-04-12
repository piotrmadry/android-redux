package com.zumba.redux.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zumba.redux.R
import com.zumba.redux.repository.Repository
import com.zumba.redux.usecase.FlowGetItemsUseCase
import com.zumba.redux.view.list.MainAdapter
import com.zumba.redux.viewModel
import com.zumba.redux.viewmodel.ListState
import com.zumba.redux.viewmodel.ListViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: ListViewModel by viewModel { ListViewModel(FlowGetItemsUseCase(Repository)) }
    private var mainAdapter: MainAdapter? = null
    private val progressbar: ProgressBar by lazy { findViewById(R.id.progress_bar) }

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

    private fun render(listState: ListState) {
        mainAdapter?.bindList(listState.list)
        progressbar.visibility = if (listState.progress) View.VISIBLE else View.GONE
    }
}
