package com.yq.service;

/**
 * @author longjun
 * @date 2021/12/30 - 9:42 上午
 */
public interface IJobService {

    void addJob(String jobName, String jobGroupName,
                String triggerName, String triggerGroupName,
                Class jobClass, String cron);

    void modifyJobTime(String triggerName, String triggerGroupName, String cron);

    void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cron);

    void removeJob(String jobName, String jobGroupName,
                   String triggerName, String triggerGroupName);

    void pauseJob(String jobName,String groupName);

    void resumeJob(String jobName,String groupName);

    void startJobs();

    void shutdownJobs();

    void standbyJobs();
}
