package ru.shorin.authenticationservice.config.job

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.shorin.authenticationservice.job.DeleteExpiredTokensJob
import ru.shorin.authenticationservice.properties.JobsProperties

@Configuration
class DeleteExpiredTokensJobConfig(
    val jobsProperties: JobsProperties
) {

    @Bean
    fun deleteExpiredTokensDetail(): JobDetail = JobBuilder
        .newJob(DeleteExpiredTokensJob::class.java)
        .withIdentity("DeleteExpiredTokensJob", jobsProperties.permanentJobsGroupName)
        .storeDurably()
        .requestRecovery(true)
        .requestRecovery(true)
        .build()

    @Bean
    fun deleteExpiredTokensTrigger(): Trigger = TriggerBuilder.newTrigger()
        .forJob(deleteExpiredTokensDetail())
        .withIdentity("DeleteExpiredTokensJobTrigger", jobsProperties.permanentJobsGroupName)
        .withSchedule(CronScheduleBuilder.cronSchedule(jobsProperties.deleteExpiredTokensJobCron))
        .build()
}