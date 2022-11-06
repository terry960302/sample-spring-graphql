package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.entity.*
import com.ritier.samplespringgraphql.repository.AccountRoleRepository
import com.ritier.samplespringgraphql.repository.CredentialRepository
import com.ritier.samplespringgraphql.repository.CredentialRoleRepository
import com.ritier.samplespringgraphql.repository.UserRepository
import com.ritier.samplespringgraphql.security.SecurityProvider
import com.ritier.samplespringgraphql.types.AuthToken
import com.ritier.samplespringgraphql.types.SignUpInput
import com.ritier.samplespringgraphql.vo.AccountRoleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import javax.persistence.EntityManager

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var credentialRepository: CredentialRepository

    @Autowired
    lateinit var accountRoleRepository: AccountRoleRepository

    @Autowired
    lateinit var credentialRoleRepository: CredentialRoleRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var securityProvider: SecurityProvider

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder

    fun getUser(id: Long): Optional<User> = userRepository.findById(id)

    fun getUsers(): List<User> = userRepository.findAll()

    fun createUser(user: User) = userRepository.save(user)

    fun getAccountRolesByCredentialIds(ids: List<Long>): MutableMap<Long, List<AccountRole>> {
        val roles: List<CredentialRole> = credentialRoleRepository.getCredentialRolesByCredentialIds(ids)
        return roles.groupBy { it.userCredential!!.user!!.id }
            .map { uc -> uc.key to uc.value.map { cred -> cred.accountRole!! } }.toMap().toMutableMap()
    }

    fun mockSignUp(): AuthToken {
        val input = SignUpInput(
            nickname = "tester123",
            email = "tester123@gmail.com",
            password = passwordEncoder.encode("test123")
        )

        // role
        val role1 = AccountRole(
            name = AccountRoleType.NORMAL.toString(),
            description = "asdasd",
        )
        val role2 = AccountRole(
            name = AccountRoleType.ADMIN.toString(),
            description = "123123123",
        )
        val role3 = AccountRole(
            name = AccountRoleType.MANAGER.toString(),
            description = "jldffjklafjkllasfk",
        )

        val createdRoles = accountRoleRepository.saveAllAndFlush(listOf(role1, role2, role3))

        println("@@ role saved")

        // user with credential
        val user = User(
            nickname = input.nickname,
            age = (1..40).random(),
            profileImg = null,
            postings = null,
        )

        val createdUser = userRepository.saveAndFlush(user)

        println("@@ user saved")

        val cred = UserCredential(
            user = entityManager.getReference(User::class.java, createdUser.id),
            email = input.email,
            password = input.password,
        )

        val createdCred = credentialRepository.saveAndFlush(cred)

        println("@@ credential saved")

        val credRole1 = CredentialRole(
            userCredential = createdCred,
            accountRole = createdRoles[0]
        )
        val credRole2 = CredentialRole(
            userCredential = createdCred,
            accountRole = createdRoles[1]
        )

        credentialRoleRepository.saveAllAndFlush(listOf(credRole1, credRole2))

        println("@@ credential role saved")

        val resultUser = entityManager.getReference(User::class.java, createdUser.id)

        println(resultUser)

        val roles: List<String> = resultUser.credential!!.credentialRoles?.map { it.accountRole!!.name }!!
        val accessToken =
            securityProvider.generateToken(resultUser.id, roles, expiration = SecurityProvider.accessExpiration)
        val refreshToken =
            securityProvider.generateToken(resultUser.id, roles, expiration = SecurityProvider.refreshExpiration)

        return AuthToken(accessToken, refreshToken)
    }

    suspend fun createMockUsers() = withContext(Dispatchers.IO) {
        val users = (1..50).map {
            User(
                nickname = "tester$it",
                age = (1..40).random(),
                profileImg = entityManager.getReference(Image::class.java, it.toLong()),
                postings = null,
            )
        }

        val asyncJob1 = async { userRepository.saveAll(users) }
        val createdUsers = asyncJob1.await()

        createdUsers.map { it.id }
    }
}