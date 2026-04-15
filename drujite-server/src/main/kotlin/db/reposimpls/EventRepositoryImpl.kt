package db.reposimpls

import db.mapping.EventDAO
import db.mapping.EventTable
import db.mapping.TimeTableDAO
import db.mapping.suspendTransaction
import db.repos.EventRepository
import models.EventModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EventRepositoryImpl : EventRepository {
    private val logger: Logger = LoggerFactory.getLogger(EventRepositoryImpl::class.java)

    override suspend fun add(event: EventModel): Int? {
        logger.info("Adding event: $event")
        return suspendTransaction {
            val timetable = TimeTableDAO.findById(event.timetableId)
            logger.info("Found timetable: $timetable, event.timetableId: ${event.timetableId}")
            if (timetable != null) {
                EventDAO
                    .new {
                        timetableId = event.timetableId
                        name = event.name
                        time = java.time.LocalTime.parse(event.time)
                        isTitle = event.isTitle
                    }.id.value
            } else {
                null
            }
        }
    }

    override suspend fun get(id: Int): EventModel? =
        suspendTransaction {
            EventDAO.findById(id)?.let {
                EventModel(
                    id = it.id.value,
                    timetableId = it.timetableId,
                    name = it.name,
                    time = it.time.toString(),
                    isTitle = it.isTitle,
                )
            }
        }

    override suspend fun delete(id: Int): Boolean =
        suspendTransaction {
            EventDAO.findById(id)?.delete() != null
        }

    override suspend fun getTimetableEventIds(timetableId: Int): List<Int> =
        suspendTransaction {
            EventDAO
                .find { EventTable.timetableId eq timetableId }
                .map { it.id.value }
        }
}
