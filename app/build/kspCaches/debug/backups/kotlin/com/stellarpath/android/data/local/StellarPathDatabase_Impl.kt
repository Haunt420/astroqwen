package com.stellarpath.android.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
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
public class StellarPathDatabase_Impl : StellarPathDatabase() {
  private val _profileDao: Lazy<ProfileDao> = lazy {
    ProfileDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "7d1bdd3cf229f258aeb37f938ba3df65", "16dd4566989ef4731cf7f6e372226f64") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `profiles` (`id` TEXT NOT NULL, `displayName` TEXT NOT NULL, `profileType` TEXT NOT NULL, `birthDate` TEXT NOT NULL, `birthTime` TEXT, `birthTimePrecision` TEXT NOT NULL, `city` TEXT NOT NULL, `region` TEXT, `countryCode` TEXT, `latitude` REAL, `longitude` REAL, `timezoneId` TEXT, `houseSystem` TEXT NOT NULL, `notes` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7d1bdd3cf229f258aeb37f938ba3df65')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `profiles`")
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

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsProfiles: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsProfiles.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("displayName", TableInfo.Column("displayName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("profileType", TableInfo.Column("profileType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("birthDate", TableInfo.Column("birthDate", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("birthTime", TableInfo.Column("birthTime", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("birthTimePrecision", TableInfo.Column("birthTimePrecision", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("city", TableInfo.Column("city", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("region", TableInfo.Column("region", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("countryCode", TableInfo.Column("countryCode", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("latitude", TableInfo.Column("latitude", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("longitude", TableInfo.Column("longitude", "REAL", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("timezoneId", TableInfo.Column("timezoneId", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("houseSystem", TableInfo.Column("houseSystem", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsProfiles.put("notes", TableInfo.Column("notes", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysProfiles: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesProfiles: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoProfiles: TableInfo = TableInfo("profiles", _columnsProfiles, _foreignKeysProfiles,
            _indicesProfiles)
        val _existingProfiles: TableInfo = read(connection, "profiles")
        if (!_infoProfiles.equals(_existingProfiles)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |profiles(com.stellarpath.android.data.local.ProfileEntity).
              | Expected:
              |""".trimMargin() + _infoProfiles + """
              |
              | Found:
              |""".trimMargin() + _existingProfiles)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "profiles")
  }

  public override fun clearAllTables() {
    super.performClear(false, "profiles")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ProfileDao::class, ProfileDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun profileDao(): ProfileDao = _profileDao.value
}
