import com.zumba.redux.Repository
import com.zumba.redux.ResultUseCase

data class ViewItem(val id: Int, val name: String)

class GetItemsUseCase(val repository: Repository) : ResultUseCase<Unit, List<ViewItem>>() {
    override suspend fun doWork(parameters: Unit): List<ViewItem> = repository.getItems().map { item ->  ViewItem(id = item.id, name = item.name)}
}
//
//class RemoveItemUseCase(val repository: Repository) : UseCase<Int>() {
//    override suspend fun doWork(parameters: Int) = repository.removeItem(parameters)
//}