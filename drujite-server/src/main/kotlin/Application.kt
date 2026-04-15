package ru.drujite

import configuration.configureDatabases
import db.reposimpls.CharacterRepositoryImpl
import db.reposimpls.ClanRepositoryImpl
import db.reposimpls.ClothingRepositoryImpl
import db.reposimpls.EventRepositoryImpl
import db.reposimpls.GoalRepositoryImpl
import db.reposimpls.NewsRepositoryImpl
import db.reposimpls.SessionRepositoryImpl
import db.reposimpls.TimeTableRepositoryImpl
import db.reposimpls.UserRepositoryImpl
import db.reposimpls.UsersSessionsRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import routing.configureRouting
import ru.drujite.configuration.configureCORS
import ru.drujite.configuration.configureMonitoring
import ru.drujite.configuration.configureSecurity
import ru.drujite.configuration.configureSerialization
import ru.drujite.services.ClothingService
import ru.drujite.services.ImageService
import services.CharacterService
import services.ClanService
import services.GoalService
import services.JwtService
import services.NewsService
import services.SessionService
import services.TimeTableService
import services.UserService
import services.UsersSessionsService

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val userRepository = UserRepositoryImpl()
    val sessionRepository = SessionRepositoryImpl()
    val usersSessionRepository = UsersSessionsRepositoryImpl()
    val characterRepository = CharacterRepositoryImpl()
    val goalRepository = GoalRepositoryImpl()
    val timeTableRepository = TimeTableRepositoryImpl()
    val eventRepository = EventRepositoryImpl()
    val clanRepository = ClanRepositoryImpl()
    val newsRepository = NewsRepositoryImpl()
    val clothingRepository = ClothingRepositoryImpl()

    val userService = UserService(userRepository)
    val jwtService = JwtService(this, userService)
    val sessionService = SessionService(sessionRepository)
    val usersSessionService = UsersSessionsService(usersSessionRepository, characterRepository, sessionRepository)
    val characterService = CharacterService(characterRepository, usersSessionRepository, clanRepository)
    val goalService = GoalService(goalRepository, usersSessionRepository)
    val timeTableService = TimeTableService(timeTableRepository, eventRepository)
    val clanService = ClanService(clanRepository)
    val newsService = NewsService(newsRepository)
    val imageService = ImageService(characterRepository, newsRepository, sessionRepository, clothingRepository)
    val clothingService = ClothingService(clothingRepository)

    configureSerialization()
    configureSecurity(jwtService)
    configureRouting(
        userService,
        jwtService,
        sessionService,
        usersSessionService,
        characterService,
        goalService,
        timeTableService,
        clanService,
        newsService,
        imageService,
        clothingService,
    )
    configureDatabases()
    configureMonitoring()
    configureCORS()
}
