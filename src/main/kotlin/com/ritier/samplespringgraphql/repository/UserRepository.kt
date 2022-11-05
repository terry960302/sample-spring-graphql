package com.ritier.samplespringgraphql.repository

import com.ritier.samplespringgraphql.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
}