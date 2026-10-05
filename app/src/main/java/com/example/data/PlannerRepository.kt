package com.example.data

import kotlinx.coroutines.flow.Flow

class PlannerRepository(private val plannerEventDao: PlannerEventDao) {

    fun getEventsForDate(date: String): Flow<List<PlannerEvent>> {
        return plannerEventDao.getEventsForDate(date)
    }

    fun getAllEvents(): Flow<List<PlannerEvent>> {
        return plannerEventDao.getAllEvents()
    }

    fun getUpcomingEvents(fromDate: String): Flow<List<PlannerEvent>> {
        return plannerEventDao.getUpcomingEvents(fromDate)
    }

    fun getDatesWithEvents(): Flow<List<String>> {
        return plannerEventDao.getDatesWithEvents()
    }

    suspend fun getEventById(id: Long): PlannerEvent? {
        return plannerEventDao.getEventById(id)
    }

    suspend fun insertEvent(event: PlannerEvent): Long {
        return plannerEventDao.insertEvent(event)
    }

    suspend fun updateEvent(event: PlannerEvent) {
        plannerEventDao.updateEvent(event)
    }

    suspend fun deleteEvent(event: PlannerEvent) {
        plannerEventDao.deleteEvent(event)
    }

    suspend fun deleteEventById(id: Long) {
        plannerEventDao.deleteEventById(id)
    }
}
