package ru.shorin.authenticationservice.config.job

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.shorin.authenticationservice.job.DeleteRevokedTokensJob
import ru.shorin.authenticationservice.properties.JobsProperties

@Configuration
class DeleteRevokedTokensJobConfig(
    val jobsProperties: JobsProperties
) {

    @Bean
    fun deleteRevokedTokensDetail(): JobDetail = JobBuilder
        .newJob(DeleteRevokedTokensJob::class.java)
        .withIdentity("DeleteRevokedTokensJob", jobsProperties.permanentJobsGroupName)
        .storeDurably()
        .requestRecovery(true)
        .build()

    @Bean
    fun deleteRevokedTokensTrigger(): Trigger = TriggerBuilder.newTrigger()
        .forJob(deleteRevokedTokensDetail())
        .withIdentity("DeleteRevokedTokensJobTrigger", jobsProperties.permanentJobsGroupName)
        .withSchedule(CronScheduleBuilder.cronSchedule(jobsProperties.deleteRevokedTokensJobCron))
        .build()
}