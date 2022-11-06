package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.AccountRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountRoleRepository : JpaRepository<AccountRole, Long> {
//    @Query(
//        "SELECT * FROM account_roles ar INNER JOIN credential_roles cr ON cr.account_role_id = ar.id WHERE cr.user_credential_id IN :ids",
//        nativeQuery = true
//    )
//    fun getAccountRolesByCredentialIds(ids: Set<Long>): List<AccountRole>
}