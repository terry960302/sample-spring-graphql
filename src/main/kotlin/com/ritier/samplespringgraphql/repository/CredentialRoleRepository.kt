package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.CredentialRole
import com.ritier.samplespringgraphql.entity.UserCredential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CredentialRoleRepository : JpaRepository<CredentialRole, Long> {
    @Query(
        "SELECT cr.* FROM credential_roles cr INNER JOIN user_credentials uc ON uc.id = cr.user_credential_id WHERE uc.id IN :ids",
        nativeQuery = true
    )
    fun getCredentialRolesByCredentialIds(ids: List<Long>): List<CredentialRole>
}