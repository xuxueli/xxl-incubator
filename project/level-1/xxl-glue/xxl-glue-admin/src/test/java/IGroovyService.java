

import java.util.Map;

/**
 * groovy service interface
 * @author xuxueli 2016-1-1 17:03:50
 */
public interface IGroovyService {
	
	/**
	 * default invoke method
	 * @param param
	 * @return
	 */
	public Object execute(Map<String, Object> param);
	
}
