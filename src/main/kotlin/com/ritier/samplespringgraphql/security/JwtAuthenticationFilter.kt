package com.ritier.samplespringgraphql.security

import com.ritier.samplespringgraphql.vo.AccountRoleType
import com.ritier.samplespringgraphql.vo.CustomUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class JwtAuthenticationFilter() : GenericFilterBean() {

    @Autowired
    private lateinit var securityProvider: SecurityProvider

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val token: String = securityProvider.resolveToken((request as HttpServletRequest))
        println("@@ token : $token  @@")
        if (token.isNotBlank() && securityProvider.validateToken(token)) {
//            val authentication = securityProvider.getAuthentication(token)
//            SecurityContextHolder.getContext().authentication = authentication
            // mock
            val mockUserDetails = CustomUserDetails(email = "tester1@gmail.com", roles = setOf(AccountRoleType.ADMIN))
            println("@@ authorities : ${mockUserDetails.authorities} @@ ")
            val mockAuthentication =
                UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.authorities)
            SecurityContextHolder.getContext().authentication = mockAuthentication
        }
        chain?.doFilter(request, response)
    }


}