package com.stellarpath.android.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ProfileDao_Impl(
  __db: RoomDatabase,
) : ProfileDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfProfileEntity: EntityInsertAdapter<ProfileEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfProfileEntity = object : EntityInsertAdapter<ProfileEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `profiles` (`id`,`displayName`,`profileType`,`birthDate`,`birthTime`,`birthTimePrecision`,`city`,`region`,`countryCode`,`latitude`,`longitude`,`timezoneId`,`houseSystem`,`notes`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ProfileEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.displayName)
        statement.bindText(3, entity.profileType)
        statement.bindText(4, entity.birthDate)
        val _tmpBirthTime: String? = entity.birthTime
        if (_tmpBirthTime == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpBirthTime)
        }
        statement.bindText(6, entity.birthTimePrecision)
        statement.bindText(7, entity.city)
        val _tmpRegion: String? = entity.region
        if (_tmpRegion == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpRegion)
        }
        val _tmpCountryCode: String? = entity.countryCode
        if (_tmpCountryCode == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpCountryCode)
        }
        val _tmpLatitude: Double? = entity.latitude
        if (_tmpLatitude == null) {
          statement.bindNull(10)
        } else {
          statement.bindDouble(10, _tmpLatitude)
        }
        val _tmpLongitude: Double? = entity.longitude
        if (_tmpLongitude == null) {
          statement.bindNull(11)
        } else {
          statement.bindDouble(11, _tmpLongitude)
        }
        val _tmpTimezoneId: String? = entity.timezoneId
        if (_tmpTimezoneId == null) {
          statement.bindNull(12)
        } else {
          statement.bindText(12, _tmpTimezoneId)
        }
        statement.bindText(13, entity.houseSystem)
        val _tmpNotes: String? = entity.notes
        if (_tmpNotes == null) {
          statement.bindNull(14)
        } else {
          statement.bindText(14, _tmpNotes)
        }
      }
    }
  }

  public override suspend fun upsertProfile(profile: ProfileEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfProfileEntity.insert(_connection, profile)
  }

  public override fun observeProfiles(): Flow<List<ProfileEntity>> {
    val _sql: String = "SELECT * FROM profiles ORDER BY displayName COLLATE NOCASE ASC"
    return createFlow(__db, false, arrayOf("profiles")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfProfileType: Int = getColumnIndexOrThrow(_stmt, "profileType")
        val _columnIndexOfBirthDate: Int = getColumnIndexOrThrow(_stmt, "birthDate")
        val _columnIndexOfBirthTime: Int = getColumnIndexOrThrow(_stmt, "birthTime")
        val _columnIndexOfBirthTimePrecision: Int = getColumnIndexOrThrow(_stmt,
            "birthTimePrecision")
        val _columnIndexOfCity: Int = getColumnIndexOrThrow(_stmt, "city")
        val _columnIndexOfRegion: Int = getColumnIndexOrThrow(_stmt, "region")
        val _columnIndexOfCountryCode: Int = getColumnIndexOrThrow(_stmt, "countryCode")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfTimezoneId: Int = getColumnIndexOrThrow(_stmt, "timezoneId")
        val _columnIndexOfHouseSystem: Int = getColumnIndexOrThrow(_stmt, "houseSystem")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: MutableList<ProfileEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ProfileEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpDisplayName: String
          _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          val _tmpProfileType: String
          _tmpProfileType = _stmt.getText(_columnIndexOfProfileType)
          val _tmpBirthDate: String
          _tmpBirthDate = _stmt.getText(_columnIndexOfBirthDate)
          val _tmpBirthTime: String?
          if (_stmt.isNull(_columnIndexOfBirthTime)) {
            _tmpBirthTime = null
          } else {
            _tmpBirthTime = _stmt.getText(_columnIndexOfBirthTime)
          }
          val _tmpBirthTimePrecision: String
          _tmpBirthTimePrecision = _stmt.getText(_columnIndexOfBirthTimePrecision)
          val _tmpCity: String
          _tmpCity = _stmt.getText(_columnIndexOfCity)
          val _tmpRegion: String?
          if (_stmt.isNull(_columnIndexOfRegion)) {
            _tmpRegion = null
          } else {
            _tmpRegion = _stmt.getText(_columnIndexOfRegion)
          }
          val _tmpCountryCode: String?
          if (_stmt.isNull(_columnIndexOfCountryCode)) {
            _tmpCountryCode = null
          } else {
            _tmpCountryCode = _stmt.getText(_columnIndexOfCountryCode)
          }
          val _tmpLatitude: Double?
          if (_stmt.isNull(_columnIndexOfLatitude)) {
            _tmpLatitude = null
          } else {
            _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          }
          val _tmpLongitude: Double?
          if (_stmt.isNull(_columnIndexOfLongitude)) {
            _tmpLongitude = null
          } else {
            _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          }
          val _tmpTimezoneId: String?
          if (_stmt.isNull(_columnIndexOfTimezoneId)) {
            _tmpTimezoneId = null
          } else {
            _tmpTimezoneId = _stmt.getText(_columnIndexOfTimezoneId)
          }
          val _tmpHouseSystem: String
          _tmpHouseSystem = _stmt.getText(_columnIndexOfHouseSystem)
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          _item =
              ProfileEntity(_tmpId,_tmpDisplayName,_tmpProfileType,_tmpBirthDate,_tmpBirthTime,_tmpBirthTimePrecision,_tmpCity,_tmpRegion,_tmpCountryCode,_tmpLatitude,_tmpLongitude,_tmpTimezoneId,_tmpHouseSystem,_tmpNotes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getProfile(id: String): ProfileEntity? {
    val _sql: String = "SELECT * FROM profiles WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfDisplayName: Int = getColumnIndexOrThrow(_stmt, "displayName")
        val _columnIndexOfProfileType: Int = getColumnIndexOrThrow(_stmt, "profileType")
        val _columnIndexOfBirthDate: Int = getColumnIndexOrThrow(_stmt, "birthDate")
        val _columnIndexOfBirthTime: Int = getColumnIndexOrThrow(_stmt, "birthTime")
        val _columnIndexOfBirthTimePrecision: Int = getColumnIndexOrThrow(_stmt,
            "birthTimePrecision")
        val _columnIndexOfCity: Int = getColumnIndexOrThrow(_stmt, "city")
        val _columnIndexOfRegion: Int = getColumnIndexOrThrow(_stmt, "region")
        val _columnIndexOfCountryCode: Int = getColumnIndexOrThrow(_stmt, "countryCode")
        val _columnIndexOfLatitude: Int = getColumnIndexOrThrow(_stmt, "latitude")
        val _columnIndexOfLongitude: Int = getColumnIndexOrThrow(_stmt, "longitude")
        val _columnIndexOfTimezoneId: Int = getColumnIndexOrThrow(_stmt, "timezoneId")
        val _columnIndexOfHouseSystem: Int = getColumnIndexOrThrow(_stmt, "houseSystem")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: ProfileEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpDisplayName: String
          _tmpDisplayName = _stmt.getText(_columnIndexOfDisplayName)
          val _tmpProfileType: String
          _tmpProfileType = _stmt.getText(_columnIndexOfProfileType)
          val _tmpBirthDate: String
          _tmpBirthDate = _stmt.getText(_columnIndexOfBirthDate)
          val _tmpBirthTime: String?
          if (_stmt.isNull(_columnIndexOfBirthTime)) {
            _tmpBirthTime = null
          } else {
            _tmpBirthTime = _stmt.getText(_columnIndexOfBirthTime)
          }
          val _tmpBirthTimePrecision: String
          _tmpBirthTimePrecision = _stmt.getText(_columnIndexOfBirthTimePrecision)
          val _tmpCity: String
          _tmpCity = _stmt.getText(_columnIndexOfCity)
          val _tmpRegion: String?
          if (_stmt.isNull(_columnIndexOfRegion)) {
            _tmpRegion = null
          } else {
            _tmpRegion = _stmt.getText(_columnIndexOfRegion)
          }
          val _tmpCountryCode: String?
          if (_stmt.isNull(_columnIndexOfCountryCode)) {
            _tmpCountryCode = null
          } else {
            _tmpCountryCode = _stmt.getText(_columnIndexOfCountryCode)
          }
          val _tmpLatitude: Double?
          if (_stmt.isNull(_columnIndexOfLatitude)) {
            _tmpLatitude = null
          } else {
            _tmpLatitude = _stmt.getDouble(_columnIndexOfLatitude)
          }
          val _tmpLongitude: Double?
          if (_stmt.isNull(_columnIndexOfLongitude)) {
            _tmpLongitude = null
          } else {
            _tmpLongitude = _stmt.getDouble(_columnIndexOfLongitude)
          }
          val _tmpTimezoneId: String?
          if (_stmt.isNull(_columnIndexOfTimezoneId)) {
            _tmpTimezoneId = null
          } else {
            _tmpTimezoneId = _stmt.getText(_columnIndexOfTimezoneId)
          }
          val _tmpHouseSystem: String
          _tmpHouseSystem = _stmt.getText(_columnIndexOfHouseSystem)
          val _tmpNotes: String?
          if (_stmt.isNull(_columnIndexOfNotes)) {
            _tmpNotes = null
          } else {
            _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          }
          _result =
              ProfileEntity(_tmpId,_tmpDisplayName,_tmpProfileType,_tmpBirthDate,_tmpBirthTime,_tmpBirthTimePrecision,_tmpCity,_tmpRegion,_tmpCountryCode,_tmpLatitude,_tmpLongitude,_tmpTimezoneId,_tmpHouseSystem,_tmpNotes)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun profileCount(): Int {
    val _sql: String = "SELECT COUNT(*) FROM profiles"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _result: Int
        if (_stmt.step()) {
          val _tmp: Int
          _tmp = _stmt.getLong(0).toInt()
          _result = _tmp
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteProfile(id: String) {
    val _sql: String = "DELETE FROM profiles WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM profiles"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
