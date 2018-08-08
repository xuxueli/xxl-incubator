package com.tmp.demo.ssh;

import co.paralleluniverse.fibers.Fiber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FiberHelper {
    private static Logger logger = LoggerFactory.getLogger(FiberHelper.class);

    static {
        /**
         * Fiber#defaultUncaughtExceptionHandler 使用 System.err 打印输出协程执行异常的信息，同步阻塞，影响性能，屏蔽
         *
         * 尝试重新实现，但是依赖底层方法太多，放弃；
          */
        Fiber.setDefaultUncaughtExceptionHandler(null);

    }


}
