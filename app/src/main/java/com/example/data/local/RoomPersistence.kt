package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "ride_history")
data class RideHistoryEntity(
    @PrimaryKey
    val id: String,
    val pickupAddress: String,
    val dropoffAddress: String,
    val vehicleName: String,
    val totalFareBDT: Double,
    val paymentMethod: String,
    val timestamp: Long,
    val status: String,
    val driverName: String,
    val rating: Int?
)

@Dao
interface RideHistoryDao {
    @Query("SELECT * FROM ride_history ORDER BY timestamp DESC")
    fun getAllRides(): Flow<List<RideHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRide(ride: RideHistoryEntity)

    @Query("DELETE FROM ride_history")
    suspend fun clearHistory()
}

@Database(entities = [RideHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rideHistoryDao(): RideHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "call_and_go_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
