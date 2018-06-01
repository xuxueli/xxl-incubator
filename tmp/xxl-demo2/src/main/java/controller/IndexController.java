package controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


/**
 * https://blog.csdn.net/lxhjh/article/details/70237473
 */

@Controller
public class IndexController {
    private static Logger logger = LoggerFactory.getLogger(IndexController.class);


    /**
     * 阻塞的Controller
     *
     * servlet request不会被释放，直到长时间的任务执行完
     *
     * @return
     */
    @RequestMapping(value = "/block", method = RequestMethod.GET)
    @ResponseBody
    public String block() {

        logger.info("Request received");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Servlet thread released");

        return "block";
    }

    /**
     * 返回Callable
     *
     * 返回Callable意味着Spring MVC将调用在不同的线程中执行定义的任务。Spring将使用TaskExecutor来管理线程。在等待完成的长期任务之前，servlet线程将被释放。
     *
     * 长时间运行的任务执行完毕之前就已经从servlet返回了。这并不意味着客户端收到了一个响应。与客户端的通信仍然是开放的等待结果，但接收到的请求的线程已被释放，并可以服务于另一个客户的请求。
     *
     * @return
     */
    @RequestMapping(value = "/callable", method = RequestMethod.GET)
    @ResponseBody
    public Callable<String> callable() {
        logger.info("Request received");
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return "callable:" + System.currentTimeMillis();
            }
        };
        logger.info("Servlet thread released");

        return callable;
    }


    /**
     * 返回DeferredResult
     *
     * 创建一个deferredresult对象。此对象将由控制器返回。我们将完成和Callable相同的事，当我们在另一个线程处理长时间运行的任务的时候释放servlet线程。
     *
     * Callable和Deferredresult做的是同样的事情——释放容器线程，在另一个线程上异步运行长时间的任务。
     *
     *      Callable执行线程完毕即返回；
     *      Deferredresult通过设置返回对象值（deferredResult.setResult(result));）返回，可以在任何地方控制返回。
     *
     * @return
     */
    @RequestMapping(value = "/deferred", method = RequestMethod.GET)
    @ResponseBody
    public DeferredResult<String> executeSlowTask() {

        logger.info("Request received");
        deferredResult = new DeferredResult<String>();
        logger.info("Servlet thread released");

        return deferredResult;
    }


    private DeferredResult<String> deferredResult;

    @RequestMapping(value = "/deferredStop", method = RequestMethod.GET)
    @ResponseBody
    public String deferredStop(String word) {
        deferredResult.setResult(word);
        return "done";
    }


}
