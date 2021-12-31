package com.yq.controller;

import com.yq.job.NewJob;
import com.yq.service.IJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author longjun
 * @date 2021/12/30 - 9:23 上午
 */
@RestController
@RequestMapping("/job")
@Api(tags = "调度任务")
public class JobController {

    @Autowired
    IJobService jobService;

    @ApiOperation(value = "添加定时任务")
    @PostMapping(value = "/addJob")
    public void addJob(
            @RequestParam(value = "jobName") String jobName,
            @RequestParam(value = "jobGroupName", required = false, defaultValue = "DEFAULT") String jobGroupName,
            @RequestParam(value = "triggerName") String triggerName,
            @RequestParam(value = "triggerGroupName", required = false, defaultValue = "DEFAULT") String triggerGroupName,
            @RequestParam(value = "cron", required = false) String cron) {
        Class jobClass = NewJob.class;
        jobService.addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);

    }

    @ApiOperation(value = "修改job时间【方式一：直接修改】")
    @PostMapping(value = "/modifyJobTime")
    public void modifyJobTime(
            @RequestParam(value = "triggerName") String triggerName,
            @RequestParam(value = "triggerGroupName", required = false, defaultValue = "DEFAULT") String triggerGroupName,
            @RequestParam(value = "cron", required = false) String cron) {
        jobService.modifyJobTime(triggerName, triggerGroupName, cron);

    }

    @ApiOperation(value = "修改job时间【方式二：先删除再新增】")
    @PostMapping(value = "/modifyJobTime2")
    public void modifyJobTime2(
            @RequestParam(value = "jobName") String jobName,
            @RequestParam(value = "jobGroupName", required = false, defaultValue = "DEFAULT") String jobGroupName,
            @RequestParam(value = "triggerName") String triggerName,
            @RequestParam(value = "triggerGroupName", required = false, defaultValue = "DEFAULT") String triggerGroupName,
            @RequestParam(value = "cron", required = false) String cron) {
        jobService.modifyJobTime(jobName, jobGroupName, triggerName, triggerGroupName, cron);

    }

    @ApiOperation(value = "移除定时任务")
    @PostMapping(value = "/removeJob")
    public void removeJob(
            @RequestParam(value = "jobName") String jobName,
            @RequestParam(value = "jobGroupName", required = false, defaultValue = "DEFAULT") String jobGroupName,
            @RequestParam(value = "triggerName") String triggerName,
            @RequestParam(value = "triggerGroupName", required = false, defaultValue = "DEFAULT") String triggerGroupName) {
        jobService.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
    }

    @ApiOperation(value = "启动所有任务")
    @GetMapping(value = "/startJobs")
    public void startJobs() {
        jobService.startJobs();
    }

    @ApiOperation(value = "关闭所有任务（关闭后不能重启）")
    @GetMapping(value = "/shutdownJobs")
    public void shutdownJobs() {
        jobService.shutdownJobs();
    }

    @ApiOperation(value = "暂停所有任务")
    @GetMapping(value = "/standbyJobs")
    public void standbyJobs() {
        jobService.standbyJobs();
    }

}
