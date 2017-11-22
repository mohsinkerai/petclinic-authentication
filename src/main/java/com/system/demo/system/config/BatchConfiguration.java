package com.system.demo.system.config;

import com.system.demo.bulk.volunteer.job.elements.VolunteerFieldSetMapper;
import com.system.demo.volunteer.Volunteer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public DataSource dataSource;

    @Autowired
    LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean(name = "volunteerItemReader")
    @StepScope
    public FlatFileItemReader<Volunteer> volunteerItemCsvReader(@Value("#{jobParameters['sourceFilePath']}") String path) throws Exception {

        FlatFileItemReader<Volunteer> reader = new FlatFileItemReader<Volunteer>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        Resource resource = resourceLoader.getResource("file:" + path);

        DefaultLineMapper<Volunteer> defaultLineMapper = new DefaultLineMapper<>();
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        defaultLineMapper.setFieldSetMapper(new VolunteerFieldSetMapper());
        reader.setLineMapper(defaultLineMapper);

        reader.setLinesToSkip(1);
        reader.setResource(resource);
        reader.setSkippedLinesCallback(headerLine -> {
            delimitedLineTokenizer.setNames(headerLine.split(","));
        });

        return reader;
    }

    @Bean(name = "volunteerItemWriter")
    @StepScope
    public JpaItemWriter<Volunteer> voulunteerItemWriter() throws Exception {

        JpaItemWriter writer = new JpaItemWriter<Volunteer>();
        writer.setEntityManagerFactory(entityManagerFactory.getObject());

        return writer;
    }
}
