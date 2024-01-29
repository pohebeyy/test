package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.modules.DatabaseFactory
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRecord): BudgetRecord = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.authorId = body.authorId?.let { EntityID(it, DatabaseFactory.AuthorTable) }
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            val query = BudgetTable
                .join(DatabaseFactory.AuthorTable, JoinType.LEFT, BudgetTable.authorId, DatabaseFactory.AuthorTable.id)
                .slice(BudgetTable.columns, DatabaseFactory.AuthorTable.fullName)
                .select { BudgetTable.year eq param.year }
                .limit(param.limit, param.offset)

            val total = query.count()
            val data = query.map {
                BudgetEntity.wrapRow(it).toResponse()
            }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data.sortedWith(compareBy({ it.month }, { -it.amount }))
            )
        }
    }
}

private fun Join.slice(columnList: List<Column<*>>, columns: Column<String>): FieldSet {
    TODO("Not yet implemented")
}
