package com.ritier.samplespringgraphql.vo

import com.ritier.samplespringgraphql.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.stream.Collectors
import javax.persistence.*

class CustomUserDetails(
    val email: String, val roles: Set<AccountRoleType>
) : UserDetails {


    override fun getAuthorities(): Collection<GrantedAuthority> {
//        return arrayListOf(SimpleGrantedAuthority(role.toString()))
        return roles.stream().map { role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
    }

    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired

    override fun isEnabled(): Boolean = isEnabled
}

enum class AccountRoleType {
    NORMAL, ADMIN, MANAGER
}