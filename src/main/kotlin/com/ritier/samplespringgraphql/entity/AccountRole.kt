package com.ritier.samplespringgraphql.entity

import javax.persistence.*

@Entity
@Table(name = "account_roles")
class AccountRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "name")
    val name: String = "",

    @Column(name = "description")
    val description: String = "",

    @OneToMany(mappedBy = "accountRole")
    val credentialRoles : List<CredentialRole>? = null,
) {
}