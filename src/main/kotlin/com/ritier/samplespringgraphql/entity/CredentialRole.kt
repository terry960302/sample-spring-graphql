package com.ritier.samplespringgraphql.entity

import javax.persistence.*


@Entity
@Table(name="credential_roles")
@IdClass(CredentialRoleId::class)
class CredentialRole(
    @Id
    @ManyToOne
    @JoinColumn(name="user_credential_id")
    val userCredential : UserCredential? = null,

    @Id
    @ManyToOne
    @JoinColumn(name = "account_role_id")
    val accountRole : AccountRole? = null,
) {
}