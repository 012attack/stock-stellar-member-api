package yi.memberapi.application.provided

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.application.provided.dto.LoginRequest
import yi.memberapi.application.provided.dto.RefreshTokenRequest
import yi.memberapi.application.provided.dto.RegisterRequest
import yi.memberapi.application.provided.dto.RegisterResponse
import yi.memberapi.application.provided.dto.TokenResponse
import yi.memberapi.application.required.MemberRepository
import yi.memberapi.application.required.RefreshTokenRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.member.Member
import yi.memberapi.domain.token.RefreshToken
import java.time.Instant

@Service
@Transactional
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: MemberUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: org.springframework.security.crypto.password.PasswordEncoder
) : AuthService {

    override fun register(request: RegisterRequest): RegisterResponse {
        if (memberRepository.existsByUsername(request.username)) {
            throw AuthException.UsernameAlreadyExistsException()
        }

        val member = Member(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )
        val savedMember = memberRepository.save(member)

        return RegisterResponse(
            id = savedMember.id!!,
            username = savedMember.username!!,
            name = savedMember.name!!
        )
    }

    override fun login(request: LoginRequest): TokenResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (e: AuthenticationException) {
            throw AuthException.InvalidCredentialsException()
        }

        val userDetails = userDetailsService.loadUserByUsername(request.username) as MemberUserDetails
        val accessToken = jwtTokenProvider.generateAccessToken(userDetails)
        val refreshToken = jwtTokenProvider.generateRefreshToken(userDetails)

        refreshTokenRepository.findByUsername(request.username).ifPresent {
            refreshTokenRepository.delete(it)
        }

        val refreshTokenEntity = RefreshToken(
            token = refreshToken,
            username = request.username,
            expiryDate = Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpiration())
        )
        refreshTokenRepository.save(refreshTokenEntity)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtTokenProvider.getAccessTokenExpiration() / 1000
        )
    }

    override fun refresh(request: RefreshTokenRequest): TokenResponse {
        val refreshTokenEntity = refreshTokenRepository.findByToken(request.refreshToken)
            .orElseThrow { AuthException.RefreshTokenNotFoundException() }

        if (refreshTokenEntity.isExpired()) {
            refreshTokenRepository.delete(refreshTokenEntity)
            throw AuthException.TokenExpiredException()
        }

        if (!jwtTokenProvider.validateToken(request.refreshToken)) {
            refreshTokenRepository.delete(refreshTokenEntity)
            throw AuthException.InvalidTokenException()
        }

        val userDetails = userDetailsService.loadUserByUsername(refreshTokenEntity.username) as MemberUserDetails
        val newAccessToken = jwtTokenProvider.generateAccessToken(userDetails)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails)

        refreshTokenRepository.delete(refreshTokenEntity)
        val newRefreshTokenEntity = RefreshToken(
            token = newRefreshToken,
            username = refreshTokenEntity.username,
            expiryDate = Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpiration())
        )
        refreshTokenRepository.save(newRefreshTokenEntity)

        return TokenResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiresIn = jwtTokenProvider.getAccessTokenExpiration() / 1000
        )
    }

    override fun logout(username: String) {
        refreshTokenRepository.deleteByUsername(username)
    }
}
