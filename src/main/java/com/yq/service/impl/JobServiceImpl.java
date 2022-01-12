package com.yq.service.impl;

import com.alibaba.druid.filter.config.ConfigTools;
import com.yq.service.IJobService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

/**
 * @author longjun
 * @date 2021/12/30 - 9:45 上午
 */
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    SchedulerFactoryBean schedulerFactory;


    /**
     * @param jobName          任务名
     * @param jobGroupName     任务组名
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass         任务
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 添加一个定时任务
     */
    @Override
    public void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, Class jobClass, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);

            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    @Override
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式一 ：调用 rescheduleJob 开始 */
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                sched.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    @Override
    public void modifyJobTime(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                /** 方式二：先删除，然后在创建一个新的Job  */
                JobDetail jobDetail = sched.getJobDetail(JobKey.jobKey(jobName, jobGroupName));
                Class<? extends Job> jobClass = jobDetail.getJobClass();
                removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                addJob(jobName, jobGroupName, triggerName, triggerGroupName, jobClass, cron);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @Description: 移除一个任务
     */
    @Override
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();

            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);

            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 暂停任务
     *
     * @param jobName
     * @param groupName
     */
    @Override
    public void pauseJob(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        Scheduler sched = schedulerFactory.getScheduler();
        try {
            sched.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务启动
     *
     * @param jobName
     * @param groupName
     */
    @Override
    public void resumeJob(String jobName, String groupName) {
        JobKey jobKey = new JobKey(jobName, groupName);
        Scheduler sched = schedulerFactory.getScheduler();
        try {
            sched.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:启动所有定时任务
     */
    @Override
    public void startJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     */
    @Override
    public void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:暂停所有定时任务
     */
    @Override
    public void standbyJobs() {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            if (!sched.isInStandbyMode()) {
                sched.standby();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        //第一种加密方法
        //到druid-1.1.10.jar目录下打开cmd窗口，执行以下命令为密码ZHUwen12加密，随后获得公钥public key
        //java -cp druid-1.1.10.jar com.alibaba.druid.filter.config.ConfigTools ZHUwen12
        //第一种解密方法
        String publicKey ="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMKVlUFgEJ4l12RlELeM0hpTAJuh0wO9RMxuiPwtGADSfag9zFJRuIZ6+R18+SoZt/lsGSHiEHprkqID1sWT65UCAwEAAQ==";
        String encryptPassword ="LHcl6TXti2/8KZc2S7gcAlgmWuq+9NpuKy4cz4DxBydiwAtfkjeip26ts90Ow/sasbdUwD8M4tdXbLheUe75Xw==";
        String decryptPassword = ConfigTools.decrypt(publicKey, encryptPassword);
        System.out.println("decryptPassword："+decryptPassword);

        //第二种加密方法
        String pwd = "ZHUwen12";
        String encryptPwd = ConfigTools.encrypt(pwd);
        System.out.println("加密后："+encryptPwd);

        //第二种解密方法
        String decryptPwd = ConfigTools.decrypt(encryptPwd);
        System.out.println("解密后："+decryptPwd);

    }
}
