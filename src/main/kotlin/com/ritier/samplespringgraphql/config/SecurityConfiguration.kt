package com.ritier.samplespringgraphql.config

import com.ritier.samplespringgraphql.security.JwtAuthenticationFilter
import com.ritier.samplespringgraphql.security.SecurityProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // pre-authorize를 위해 사용
class SecurityConfiguration {

    @Autowired
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        return http
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
            .authorizeRequests(
                Customizer { auth ->
                    auth
                        .antMatchers("**/graphiql/**")
                        .permitAll()
                }
            )
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity?> ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling().and()
            .httpBasic().disable()
            .logout().disable()
            .anonymous().disable()
            .build()
    }

//    @Bean
//    fun users(): InMemoryUserDetailsManager? {
//        return InMemoryUserDetailsManager(
//            User.withUsername("admin")
//                .password("{noop}1234")
//                .authorities("admin")
//                .build()
//        )
//    }

}