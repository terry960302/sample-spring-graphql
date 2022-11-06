package com.ritier.samplespringgraphql.graphql.datafetcher

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument
import com.ritier.samplespringgraphql.DgsConstants
import com.ritier.samplespringgraphql.entity.AccountRole
import com.ritier.samplespringgraphql.entity.Image
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.entity.UserCredential
import com.ritier.samplespringgraphql.graphql.dataloader.AccountRoleDataLoader
import com.ritier.samplespringgraphql.security.SecurityProvider
import com.ritier.samplespringgraphql.service.CredentialService
import com.ritier.samplespringgraphql.service.UserService
import com.ritier.samplespringgraphql.types.AuthToken
import com.ritier.samplespringgraphql.types.SignInInput
import kotlinx.coroutines.runBlocking
import org.dataloader.DataLoader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import java.util.*
import java.util.concurrent.CompletableFuture

@DgsComponent
class UserFetcher {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var credentialService: CredentialService

    @Autowired
    private lateinit var securityProvider: SecurityProvider

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetAllUsers)
    fun getAllUsers(): List<User> {
        return userService.getUsers()
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.GetUser)
    fun getUser(@InputArgument id: Long): Optional<User> {
        return userService.getUser(id)
    }

    @DgsData(parentType = DgsConstants.USERCREDENTIAL.TYPE_NAME, field = DgsConstants.USERCREDENTIAL.Roles)
    fun roles(dfe: DgsDataFetchingEnvironment): CompletableFuture<List<AccountRole>> {
        val dataLoader: DataLoader<Long, List<AccountRole>> = dfe.getDataLoader(AccountRoleDataLoader::class.java)
        val userCred = dfe.getSource<UserCredential>()
        return dataLoader.load(userCred.id)
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateMockUsers)
    fun createMockUsers(): List<Long> = runBlocking {
        userService.createMockUsers()
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.SignIn)
    fun signIn(@InputArgument input: SignInInput): AuthToken {
        return credentialService.signIn(input.email, input.password)
    }

    //    @PreAuthorize("isAuthenticated")
    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.CreateTestToken)
    fun createTestToken(): AuthToken {
        val access = securityProvider.generateToken(1, listOf("NORMAL", "ADMIN"), SecurityProvider.accessExpiration)
        val refresh = securityProvider.generateToken(1, listOf("NORMAL", "ADMIN"), SecurityProvider.refreshExpiration)
        return AuthToken(access, refresh)
    }

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.MockSignUp)
    fun mockSignUp(): AuthToken {
        return userService.mockSignUp()
    }
}