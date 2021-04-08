import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.zumba.redux.*

class MainActivity: AppCompatActivity() {

    val viewModel: ListViewModel by viewModel { ListViewModel(GetItemsUseCase(Repository())) }
    var adapter: Adapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = Adapter()
        recyclerView.adapter = adapter
        
        viewModel.currentState.observe(this){ render(it) }
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