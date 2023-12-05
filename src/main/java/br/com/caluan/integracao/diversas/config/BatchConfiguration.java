package br.com.caluan.integracao.diversas.config;

import br.com.caluan.integracao.diversas.processor.LeitorXLSProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing

public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private LeitorXLSProcessor leitorXLSProcessor;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("jobQuartz").incrementer(new RunIdIncrementer()).start(step1()).build();
    }

    @Bean
    public Step step1() {
        return this.stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) -> {
            leitorXLSProcessor.leArquivo();
            return RepeatStatus.FINISHED;
        }).build();
    }
}