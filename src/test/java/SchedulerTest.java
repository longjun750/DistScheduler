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

    public static void main(String[] args) throws Exception {
        //第一种加密方法
        //到druid-1.1.10.jar目录下打开cmd窗口，执行以下命令为密码ZHUwen12加密，随后获得公钥public key
        //java -cp druid-1.1.10.jar com.alibaba.druid.filter.config.ConfigTools ZHUwen12
        //第一种解密方法
        String publicKey ="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ/yE73fLRCdZXR+q22b+TlbLuFLq84nGmJzj3eETJAFZbMszlh3e3Wd39DcvI/55T86/5DNOassR6CXAog47Y8CAwEAAQ==";
        String encryptPassword ="dTEvIIioWX0LJMcrVkGB/BTxaolrSBZCOSvxcjWKkRr+Yug3bG/P5TQeeVw81UlIHJb+Dct+jujNQbibtEK9Ag==";
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
