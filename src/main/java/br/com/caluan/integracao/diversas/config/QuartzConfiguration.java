package br.com.caluan.integracao.diversas.config;

import br.com.caluan.integracao.diversas.job.BatchScheduleJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {
    @Bean
    public JobDetail quartzJobDetail() {
        return JobBuilder.newJob(BatchScheduleJob.class).storeDurably().build();
    }

    @Bean
    public Trigger jobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                //Intervalo de execução após o inicio
                .withIntervalInMinutes(1)
                //Depois de iniciado, executa pra sempre
                .repeatForever();
        return TriggerBuilder
                .newTrigger()
                .forJob(quartzJobDetail())
                .withSchedule(scheduleBuilder)
                .build();
    }
}