- [马士兵 spring 视频笔记](http://www.cnblogs.com/baolibin528/p/3979975.html)

IOC：
- [Spring IOC基础](http://www.cnblogs.com/linjiqin/p/3407047.html)
- [Spring IOC容器基本原理](http://www.cnblogs.com/linjiqin/p/3407126.html)
- [Spring IOC的配置使用](http://www.cnblogs.com/linjiqin/p/3408306.html)

其他：
- [Spring事务的传播行为](http://www.cnblogs.com/yangy608/archive/2010/12/15/1907065.html)
- [Spring Bean 初始化过程](http://m635674608.iteye.com/blog/2149665)
- [Spring事务配置的五种方式](http://www.blogjava.net/robbie/archive/2009/04/05/264003.html)
---

##### Spring加载Properties方式
- 1、PropertyPlaceholderConfigurer：Spring属性占位符方式
应用场景：Properties配置文件不止一个，需要在系统启动时同时加载多个Properties文件
将多个配置文件读取到容器中，交给Spring管理
    - 获取值方式01（xml中）：${mail.host}
    - 获取值方式02（Bean中）：
        ```
        @Value("${mail.sendFrom}")
        private String sendFrom;
        ```
- 2、PropertiesFactoryBean：声明Properties类型的FactoryBean方式【整个配置当做bean的一个property】
只能当做Properties属性注入，而不能获其中具体的值
```
<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="order" value="1"/>
    <property name="fileEncoding" value="utf-8" />
    <property name="ignoreResourceNotFound" value="true" />	
        <!-- 忽略不存在配置文件 -->
        <property name="ignoreUnresolvablePlaceholders" value="true" />	
        <!-- 是否忽略不可解析的Placeholder，当存在多个config时 -->
        <property name="locations">
            <list>
            <value>classpath*:zkprops.properties</value>
            </list>
    </property>
</bean>
```

##### Spring + junit
```
// maven依赖 ：
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>${spring.version}</version>
</dependency>

// <!-- junit -->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.11</version>
    <scope>test</scope>
</dependency>

// 使用demo
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.xxl.core.model.main.AdminMenu;
import com.xxl.dao.IAdminMenuDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:applicationcontext-*.xml"})
public class AdminMenuDaoTest {
@Autowired
private IAdminMenuDao adminMenuDao;

    @Test
    public void test() {
        List<AdminMenu> list = adminMenuDao.getMyMenus(0);
        System.out.println("-------------------------");
        System.out.println(list!=null?list.size():-1);
    }
}
```

##### Spring常用注解，自动扫描装配Bean
```
// 1 引入context命名空间(在Spring的配置文件中)，配置文件如下：
xmlns:context="http://www.springframework.org/schema/context"
http://www.springframework.org/schema/context
http://www.springframework.org/schema/context/spring-context-2.5.xsd

// spring 会自动扫描cn.pic包下面有注解的类，完成Bean的装配。
<context:component-scan base-package="包名(扫描本包及子包)"/>


// --定义Bean的注解
@Controller
@Controller("Bean的名称")
定义控制层Bean,如Action

@Service
@Service("Bean的名称")
定义业务层Bean

@Repository
@Repository("Bean的名称")
定义DAO层Bean

@Component
定义Bean, 不好归类时使用.

// --自动装配Bean（选用一种注解就可以）
// default-autowire：默认值为“no”，表示默认不会对Bean属性进行注入；必须通过ref元素指定依赖；
@Autowired (Srping提供的)
默认按类型匹配,自动装配(Srping提供的)，可以写在成员属性上,或写在setter方法上

@Autowired(required=true) 
一定要找到匹配的Bean，否则抛异常。 默认值就是true

@Autowired
@Qualifier("bean的名字")
按名称装配Bean,与@Autowired组合使用，解决按类型匹配找到多个Bean问题。

@Resource  JSR-250提供的
默认按名称装配,当找不到名称匹配的bean再按类型装配.
可以写在成员属性上,或写在setter方法上
可以通过@Resource(name="beanName") 指定被注入的bean的名称, 要是未指定name属性, 默认使用成员属性的变量名,一般不用写name属性.
@Resource(name="beanName")指定了name属性,按名称注入但没找到bean, 就不会再按类型装配了.

@Inject  是JSR-330提供的
按类型装配，功能比@Autowired少，没有使用的必要。

--定义Bean的作用域和生命过程
@Scope("prototype")
值有:singleton,prototype,session,request,session,globalSession

@PostConstruct
相当于init-method,使用在方法上，当Bean初始化时执行。

@PreDestroy
相当于destory-method，使用在方法上，当Bean销毁时执行。

// --声明式事务
@Transactional 
```
