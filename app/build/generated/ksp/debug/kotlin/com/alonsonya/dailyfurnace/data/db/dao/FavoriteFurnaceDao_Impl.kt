package com.alonsonya.dailyfurnace.`data`.db.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getTotalChangedRows
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.alonsonya.dailyfurnace.`data`.db.entity.FavoriteFurnaceEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class FavoriteFurnaceDao_Impl(
  __db: RoomDatabase,
) : FavoriteFurnaceDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfFavoriteFurnaceEntity: EntityInsertAdapter<FavoriteFurnaceEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfFavoriteFurnaceEntity = object : EntityInsertAdapter<FavoriteFurnaceEntity>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `favorite_furnaces` (`furnaceId`,`addedAt`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: FavoriteFurnaceEntity) {
        statement.bindLong(1, entity.furnaceId.toLong())
        statement.bindLong(2, entity.addedAt)
      }
    }
  }

  public override suspend fun insert(entity: FavoriteFurnaceEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfFavoriteFurnaceEntity.insertAndReturnId(_connection, entity)
    _result
  }

  public override fun isFavoriteFlow(id: Int): Flow<Boolean> {
    val _sql: String = "SELECT EXISTS(SELECT 1 FROM favorite_furnaces WHERE furnaceId = ?)"
    return createFlow(__db, false, arrayOf("favorite_furnaces")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun isFavorite(id: Int): Boolean {
    val _sql: String = "SELECT EXISTS(SELECT 1 FROM favorite_furnaces WHERE furnaceId = ?)"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _result: Boolean
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp != 0
        } else {
          _result = false
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeAllIds(): Flow<List<Int>> {
    val _sql: String = "SELECT furnaceId FROM favorite_furnaces ORDER BY addedAt DESC"
    return createFlow(__db, false, arrayOf("favorite_furnaces")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: MutableList<Int> = mutableListOf()
        while (_stmt.step()) {
          val _item: Int
          _item = _stmt.getLong(0).toInt()
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: Int): Int {
    val _sql: String = "DELETE FROM favorite_furnaces WHERE furnaceId = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
        getTotalChangedRows(_connection)
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
