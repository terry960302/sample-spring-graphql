package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.repository.CredentialRepository
import com.ritier.samplespringgraphql.security.SecurityProvider
import com.ritier.samplespringgraphql.types.AuthToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CredentialService {

    @Autowired
    private lateinit var credentialRepository: CredentialRepository

    @Autowired
    private lateinit var securityProvider: SecurityProvider

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    fun signIn(email: String, password: String): AuthToken {
        val userCred = credentialRepository.findByEmail(email)
        if (userCred.isEmpty) throw Error("There's no user whose email is $email")

        val hasUser = passwordEncoder.matches(password, userCred.get().password)
        if(!hasUser)  throw Error("Please check your password.")

        val userId = userCred.get().user!!.id
        val roles = userCred.get().credentialRoles?.map { it.accountRole!!.name }!!

        val accessToken = securityProvider.generateToken(userId, roles, SecurityProvider.accessExpiration)
        val refreshToken = securityProvider.generateToken(userId, roles, SecurityProvider.refreshExpiration)

        return AuthToken(
            accessToken, refreshToken
        )
    }
}