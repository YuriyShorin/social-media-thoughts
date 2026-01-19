package ru.shorin.authenticationservice.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.PersistJobDataAfterExecution
import org.springframework.scheduling.quartz.QuartzJobBean
import ru.shorin.authenticationservice.service.RefreshTokenService

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class DeleteRevokedTokensJob(
    private val refreshTokenService: RefreshTokenService,
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        refreshTokenService.deleteRevoked()
    }
}