import com.xxl.push.core.schemas.bdparser.CoreBeanDefinitionParser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by xuxueli on 16/10/8.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml");
        CoreBeanDefinitionParser.Element p = (CoreBeanDefinitionParser.Element)ctx.getBean("xxlPush");
        System.out.println(p.getId());
        System.out.println(p.getAddress());
        System.out.println(p.getBeat());
    }

}
