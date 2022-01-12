import com.alibaba.druid.filter.config.ConfigTools;
import com.yq.job.MyJob;
import com.yq.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Test;

/**
 * @author longjun
 * @date 2021/12/30 - 2:29 下午
 */
public class SchedulerTest extends BaseTest {

    public static String JOB_NAME = "动态任务调度";
    public static String TRIGGER_NAME = "动态任务触发器";
    public static String JOB_GROUP_NAME = "XLXXCC_JOB_GROUP";
    public static String TRIGGER_GROUP_NAME = "XLXXCC_JOB_GROUP";

    @Autowired
    IJobService jobService;

    @Test
    public void test() {
        try {
            //System.out.println("【系统启动】开始(每1秒输出一次)...");
            //jobService.addJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, MyJob.class, "0/1 * * * * ?");

            //Thread.sleep(5000);
            //System.out.println("【修改时间】开始(每5秒输出一次)...");
            //jobService.modifyJobTime(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, "0/5 * * * * ?");
            //
            //Thread.sleep(6000);
            System.out.println("【移除定时】开始...");
            jobService.removeJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME);
            System.out.println("【移除定时】成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
