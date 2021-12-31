package com.yq.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple to Introduction
 * className: UpdateRunningDaysJob
 *
 * @author EricYang
 * @version 2018/11/17 17:57
 */

@Component
@Slf4j
public class SecondJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("第二个任务运行时间： " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        log.info("UpdateRunningDaysJob at {}", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
    }
}