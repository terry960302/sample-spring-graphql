package com.ritier.samplespringgraphql.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
class CredentialRoleId(
    val userCredential: Long = 0,
    val accountRole: Long = 0
) : Serializable {}