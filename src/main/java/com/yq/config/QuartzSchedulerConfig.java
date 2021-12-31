package com.yq.config;


import com.alibaba.druid.filter.config.ConfigTools;
import com.yq.job.FirstJob;
import com.yq.job.SecondJob;
import com.yq.job.ThirdJob;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Simple to Introduction
 * className: QuartzSchedulerConfig
 *
 * @author EricYang
 * @version 2018/11/17 17:53
 */
@Configuration
@Slf4j
public class QuartzSchedulerConfig {

    @Autowired
    private DataSource dataSource;

    private static final String QUARTZ_PROPERTIES_NAME = "/quartz.properties";

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory, CronTrigger[] cronTrigger,
                                                     JobDetail[] jobDetails) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        try {
            factoryBean.setQuartzProperties(quartzProperties());
            factoryBean.setDataSource(dataSource);
            factoryBean.setJobFactory(jobFactory);
            factoryBean.setTriggers(cronTrigger);
            factoryBean.setJobDetails(jobDetails);
            // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
            factoryBean.setOverwriteExistingJobs(true);
            // 延长启动
            factoryBean.setStartupDelay(10);
        } catch (Exception e) {
            log.error("Failed to load config file {}.", QUARTZ_PROPERTIES_NAME, e);
            throw new RuntimeException("LoadingConfigFileError", e);
        }

        return factoryBean;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_PROPERTIES_NAME));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean(name = "job1Trigger")
    public CronTriggerFactoryBean job1Trigger(@Qualifier("jobFirstDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression("0 */5 * * * ?");
        return cronTriggerFactoryBean;
    }

    @Bean(name = "jobFirstDetail")
    public JobDetailFactoryBean job1Detail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(FirstJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    @Bean(name = "job2Trigger")
    public CronTriggerFactoryBean job2Trigger(@Qualifier("jobSecondDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression("0 */10 * * * ?");
        return cronTriggerFactoryBean;
    }

    @Bean(name = "jobSecondDetail")
    public JobDetailFactoryBean job2Detail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(SecondJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    @Bean(name = "job3Trigger")
    public CronTriggerFactoryBean job3Trigger(@Qualifier("jobThirdJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression("0 */10 * * * ?");
        return cronTriggerFactoryBean;
    }

    @Bean(name = "jobThirdJobDetail")
    public JobDetailFactoryBean job3Detail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(ThirdJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }



    class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {

        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }


}
