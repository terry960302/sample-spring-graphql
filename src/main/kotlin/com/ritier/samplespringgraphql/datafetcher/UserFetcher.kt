package com.ritier.samplespringgraphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.InputArgument
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.service.UserService
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@DgsComponent
class UserFetcher {

    @Autowired
    private lateinit var userService: UserService

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetAllUsers)
    fun getAllUsers(): List<User> {
        return userService.getUsers()
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetUser)
    fun getUser(@InputArgument id: Long): Optional<User> {
        return userService.getUser(id)
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateMockUsers)
    fun createMockUsers(): List<Long> = runBlocking {
        userService.createMockUsers()
    }
}