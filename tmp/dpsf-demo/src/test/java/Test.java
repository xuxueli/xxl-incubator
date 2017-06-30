import com.dianping.easyutils.remote.PigeonServiceFactory;
import com.xxl.service.IDpsfService;

/**
 * https://github.com/dianping/pigeon1
 */
public class Test {
	
	
	private static String LOCAL_SERVICE_HOSTS = "127.0.0.1:3022";
	public static void main(String[] args) throws Exception {
		IDpsfService dpsfService = new PigeonServiceFactory<IDpsfService>(){}.getRemoteService("http://service.xxl.com/demo/dpsfService_1.0.0", LOCAL_SERVICE_HOSTS);
		System.out.println(dpsfService.sayHi("jack"));
	}
	
}
