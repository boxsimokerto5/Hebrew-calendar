package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannerEventDao {

    @Query("SELECT * FROM planner_events WHERE gregorianDate = :date ORDER BY time ASC, id ASC")
    fun getEventsForDate(date: String): Flow<List<PlannerEvent>>

    @Query("SELECT * FROM planner_events ORDER BY gregorianDate ASC, time ASC")
    fun getAllEvents(): Flow<List<PlannerEvent>>

    @Query("SELECT * FROM planner_events WHERE gregorianDate >= :fromDate ORDER BY gregorianDate ASC, time ASC")
    fun getUpcomingEvents(fromDate: String): Flow<List<PlannerEvent>>

    @Query("SELECT * FROM planner_events WHERE id = :id LIMIT 1")
    suspend fun getEventById(id: Long): PlannerEvent?

    @Query("SELECT DISTINCT gregorianDate FROM planner_events")
    fun getDatesWithEvents(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: PlannerEvent): Long

    @Update
    suspend fun updateEvent(event: PlannerEvent)

    @Delete
    suspend fun deleteEvent(event: PlannerEvent)

    @Query("DELETE FROM planner_events WHERE id = :id")
    suspend fun deleteEventById(id: Long)
}
