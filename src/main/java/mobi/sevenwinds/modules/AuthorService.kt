import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.UserDataHolder
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

private var UserDataHolder.creationDate: DateTime?
    get() {
        TODO("Not yet implemented")
    }
    set(value) {}
private var UserDataHolder.fullName: String
    get() {
        TODO("Not yet implemented")
    }
    set(value) {}

class AuthorService {
    companion object {
        suspend fun addAuthor(body: Author, AuthorEntity: Any): Author = withContext(Dispatchers.IO) {
            transaction {
                val entity = AuthorEntity {
                    this.fullName = body.fullName
                    this.creationDate = DateTime.parse(body.creationDate)
                }

                return@transaction entity.toResponse()
            }
        }

        fun addAuthor(body: Author): Author {
            TODO("Not yet implemented")
        }
    }
}

private fun Any.toResponse(): Author {
    TODO("Not yet implemented")
}

private fun UserDataHolder.AuthorEntity(value: () -> Unit): Any {
    TODO("Not yet implemented")
}
