package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.UserCredential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CredentialRepository : JpaRepository<UserCredential, Long>{
    @Query("SELECT c.* FROM user_credentials c WHERE c.email = :email", nativeQuery = true)
    fun getCredentialByEmail(email : String) : Optional<UserCredential>

    @Query("SELECT c.* FROM user_credentials c WHERE c.user_id = :userId", nativeQuery = true)
    fun getCredentialByUserId(userId : Long) : Optional<UserCredential>

    fun findByEmail(email : String) : Optional<UserCredential>

    fun findByEmailAndPassword(email : String, password : String) : Optional<UserCredential>
}