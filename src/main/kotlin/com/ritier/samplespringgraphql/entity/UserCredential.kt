package com.ritier.samplespringgraphql.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "user_credentials")
class UserCredential(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Column(name = "email")
    val email: String = "",

    @Column(name ="password")
    val password : String = "",

    @Column(name = "roles")
    @OneToMany(mappedBy = "userCredential")
    val credentialRoles: List<CredentialRole>? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {

}