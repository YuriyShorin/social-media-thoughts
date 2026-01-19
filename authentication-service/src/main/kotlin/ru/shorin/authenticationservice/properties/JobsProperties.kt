package ru.shorin.authenticationservice.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jobs")
data class JobsProperties(
    var permanentJobsGroupName: String,
    val deleteRevokedTokensJobCron: String,
    val deleteExpiredTokensJobCron: String,
)