package ru.shorin.authenticationservice.service

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.shorin.authenticationservice.repository.UserRepository
import ru.shorin.exception.BusinessException
import ru.shorin.exception.BusinessExceptionEnum

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    @Transactional(readOnly = true)
    override fun loadUserByUsername(email: String): UserDetails {
        val user =
            userRepository.findByEmail(email)
                ?: throw BusinessException(BusinessExceptionEnum.USER_NOT_FOUND_BY_EMAIL)

        if (!user.enabled) {
            throw BusinessException(BusinessExceptionEnum.USER_NOT_ENABLED)
        }

        if (user.expired) {
            throw BusinessException(BusinessExceptionEnum.USER_EXPIRED)
        }

        if (user.deleted) {
            throw BusinessException(BusinessExceptionEnum.USER_DELETED)
        }

        return user
    }
}
