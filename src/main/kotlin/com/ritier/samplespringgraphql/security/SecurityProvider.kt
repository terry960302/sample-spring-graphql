package com.ritier.samplespringgraphql.security

import com.ritier.samplespringgraphql.service.CustomUserDetailsService
import com.ritier.samplespringgraphql.vo.AccountRoleType
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.mindrot.jbcrypt.BCrypt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class SecurityProvider(val customUserDetailsService: CustomUserDetailsService) {

//    @Autowired
//    private lateinit var passowordEncoder: BCryptPasswordEncoder

    companion object {
        val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.HS256
        private const val jwtSecret = "1234"
        const val accessExpiration = 30 * 60 * 1000L // 30min
        const val refreshExpiration = 7 * 24 * 60 * 60 * 1000L // 7day
    }

    fun generateToken(userId: Long, roles: List<String>, expiration: Long): String {
        val claims: Claims = Jwts.claims()
        claims["user_id"] = userId
        claims["roles"] = roles
        return Jwts.builder().setClaims(claims).setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(SIGNATURE_ALG, jwtSecret)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        val claims: Claims = getAllClaims(token)
        val exp: Date = claims.expiration
        return exp.after(Date())
    }

    fun parseUserId(token: String): Long {
        val claims: Claims = getAllClaims(token)
        return claims["user_id"].toString().toLong()
    }

    fun getAuthentication(token: String): Authentication {
        val userId = parseUserId(token)
        val cred = customUserDetailsService.loadCredentialById(userId)
        return UsernamePasswordAuthenticationToken(cred, "", cred.authorities)
    }
//
//    fun hashPassword(password: String): String {
//        println("hello")
//        return passowordEncoder.encode(password)
//    }

    fun resolveToken(request: HttpServletRequest): String {
        val tokenWithType = request.getHeader("Authorization")
        return filterOnlyToken(tokenWithType)
    }

//    fun validatePassword(plainPassword: String, hashedPassword: String): Boolean {
//        return passowordEncoder.matches(plainPassword, hashedPassword)
//    }

    fun getAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
    }

    fun filterOnlyToken(fullToken: String?): String {
        if (fullToken == null) return ""

        val splittedStr = fullToken.split(" ")
        return if (splittedStr.size == 2) {
            splittedStr[1]
        } else {
            ""
        }
    }


}