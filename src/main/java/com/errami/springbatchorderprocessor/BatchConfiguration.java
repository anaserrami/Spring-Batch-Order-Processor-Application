package com.errami.springbatchorderprocessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public FlatFileItemReader<Order> reader() {
        return new FlatFileItemReaderBuilder<Order>()
                .name("orderItemReader")
                .resource(new ClassPathResource("orders.csv"))
                .delimited()
                .names("orderId", "customerName", "amount")
                .linesToSkip(1)
                .targetType(Order.class)
                .build();
    }

    @Bean
    public OrderItemProcessor processor() {
        return new OrderItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Order> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Order>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO orders (customer_name, amount) VALUES (:customerName, :amount)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager dataSourceTransactionManager,
                      FlatFileItemReader<Order> reader, OrderItemProcessor processor,
                      JdbcBatchItemWriter<Order> writer){
        return  new StepBuilder("step1",jobRepository)
                .<Order,Order> chunk(3,dataSourceTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    @Bean
    public Job importOrderJob(JobRepository jobRepository, Step step, JobCompletionNotificationListener listener){
        return new JobBuilder("impotOrderJob",jobRepository)
                .listener(listener)
                .start(step)
                .build();
    }
}