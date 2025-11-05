package com.alonsonya.dailyfurnace.`data`.db

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.alonsonya.dailyfurnace.`data`.db.dao.FavoriteFurnaceDao
import com.alonsonya.dailyfurnace.`data`.db.dao.FavoriteFurnaceDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _favoriteFurnaceDao: Lazy<FavoriteFurnaceDao> = lazy {
    FavoriteFurnaceDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "7146e9d965d17b7907ee6e4fdad88b66", "591f59e0833ffe0620a2eddbc97afc9a") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `favorite_furnaces` (`furnaceId` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`furnaceId`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7146e9d965d17b7907ee6e4fdad88b66')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `favorite_furnaces`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsFavoriteFurnaces: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFavoriteFurnaces.put("furnaceId", TableInfo.Column("furnaceId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFavoriteFurnaces.put("addedAt", TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFavoriteFurnaces: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFavoriteFurnaces: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoFavoriteFurnaces: TableInfo = TableInfo("favorite_furnaces", _columnsFavoriteFurnaces, _foreignKeysFavoriteFurnaces, _indicesFavoriteFurnaces)
        val _existingFavoriteFurnaces: TableInfo = read(connection, "favorite_furnaces")
        if (!_infoFavoriteFurnaces.equals(_existingFavoriteFurnaces)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |favorite_furnaces(com.alonsonya.dailyfurnace.data.db.entity.FavoriteFurnaceEntity).
              | Expected:
              |""".trimMargin() + _infoFavoriteFurnaces + """
              |
              | Found:
              |""".trimMargin() + _existingFavoriteFurnaces)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "favorite_furnaces")
  }

  public override fun clearAllTables() {
    super.performClear(false, "favorite_furnaces")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(FavoriteFurnaceDao::class, FavoriteFurnaceDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun favoriteFurnaceDao(): FavoriteFurnaceDao = _favoriteFurnaceDao.value
}
