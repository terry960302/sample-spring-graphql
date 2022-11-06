package com.ritier.samplespringgraphql.service

import com.ritier.samplespringgraphql.entity.AccountRole
import com.ritier.samplespringgraphql.entity.User
import com.ritier.samplespringgraphql.repository.CredentialRepository
//import com.ritier.samplespringgraphql.entity.UserCredential
//import com.ritier.samplespringgraphql.repository.CredentialRepository
import com.ritier.samplespringgraphql.repository.UserRepository
import com.ritier.samplespringgraphql.vo.AccountRoleType
import com.ritier.samplespringgraphql.vo.CustomUserDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsService : UserDetailsService {
    @Autowired
    private lateinit var credentialRepository: CredentialRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userId = username.toLong() // userId를 받아서 처리할 예정
        val credential = credentialRepository.getCredentialByUserId(userId)

        val userDetails = CustomUserDetails(
            email = credential.get().email,
            roles = credential.get().credentialRoles!!.map { role ->
                AccountRoleType.values().first { it.name == role.accountRole.toString() }
            }.toSet()
        )

        if (credential.isEmpty) {
            throw Error("There's no user mathed to username")
        } else {
            return userDetails
        }
    }

    fun loadCredentialById(userId : Long) : UserDetails {
        val credential = credentialRepository.getCredentialByUserId(userId)

        val userDetails = CustomUserDetails(
            email = credential.get().email,
            roles = credential.get().credentialRoles!!.map { role ->
                AccountRoleType.values().first { it.name == role.accountRole.toString() }
            }.toSet()
        )
        if (credential.isEmpty) {
            throw Error("There's no user matched to username")
        } else {
            return userDetails
        }
    }

//    companion object {
//        private val logger: Logger = LoggerFactory.getLogger(CustomUserDetailsService::class.java)
//    }
}