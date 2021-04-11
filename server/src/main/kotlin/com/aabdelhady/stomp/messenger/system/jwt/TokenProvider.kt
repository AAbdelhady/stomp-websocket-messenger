package com.aabdelhady.stomp.messenger.system.jwt

import com.aabdelhady.stomp.messenger.feature.user.model.User
import com.aabdelhady.stomp.messenger.system.auth.util.JWT_TTL_SECONDS
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec

@Component
class TokenProvider {
    @Value("\${jwt.key}") private val secretKey: String? = null
    private var key: Key? = null

    @PostConstruct
    private fun init() {
        key = SecretKeySpec(secretKey!!.toByteArray(), SignatureAlgorithm.HS512.jcaName)
    }

    fun createToken(user: User): String {
        val now = Date()
        val expiryDate = Date(now.time + JWT_TTL_SECONDS * 1000)
        return Jwts.builder().setSubject(user.id.toString()).setIssuedAt(now).setExpiration(expiryDate).signWith(key).compact()
    }

    fun getUserIdFromToken(token: String) = Jwts.parser().setSigningKey(key).parseClaimsJws(token).body.subject.toLong()
}
