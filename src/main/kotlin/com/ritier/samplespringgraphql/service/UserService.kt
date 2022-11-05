package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Optional
import javax.persistence.EntityManager

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var entityManager: EntityManager

    fun getUser(id: Long): Optional<User> = userRepository.findById(id)

    fun getUsers(): List<User> = userRepository.findAll()

    fun createUser(user: User) = userRepository.save(user)

    suspend fun createMockUsers() = withContext(Dispatchers.IO) {
        val user1 = User(
            nickname = "tester1",
            age = 12,
            profileImg = entityManager.getReference(Image::class.java, 1.toLong()),
            postings = null
        )
        val user2 = User(
            nickname = "tester2",
            age = 13,
            profileImg = entityManager.getReference(Image::class.java, 2.toLong()),
            postings = null
        )
        val user3 = User(
            nickname = "tester3",
            age = 14,
            profileImg = entityManager.getReference(Image::class.java, 3.toLong()),
            postings = null
        )
        val user4 = User(
            nickname = "tester4",
            age = 20,
            profileImg = null,
            postings = null
        )
        val mockUsers = arrayListOf<User>(user1, user2, user3, user4)

        val usersAsync = mockUsers.map {
            async { userRepository.save(it) }
        }
        val users = usersAsync.awaitAll()
        users.map { it.id }
    }
}