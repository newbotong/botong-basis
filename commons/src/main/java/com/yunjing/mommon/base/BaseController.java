package com.yunjing.mommon.base;

import com.alibaba.fastjson.JSON;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller基类
 *
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/01/18 14:04
 * @description
 **/
public class BaseController {
    public final Logger logger = LogManager.getLogger(this.getClass());

    private HttpServletRequest request;

    private HttpServletResponse response;

    /**
     * 获取Http servlet request、response
     *
     * @param request
     * @param response
     */
    @ModelAttribute
    public void before(HttpServletRequest request, HttpServletResponse response) {
        logger.info("开始调用请求方法：" + request.getRequestURI() + " 输入参数包含：" + JSON.toJSONString(request.getParameterMap()));
        this.request = request;
        this.response = response;
    }

    /**
     * 获取Spring 容器中注入的对象
     *
     * @param beanID 实例ID
     * @return
     */
    public Object getBean(String beanID) {
        ServletContext servletContext = request.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return applicationContext.getBean(beanID);
    }

    /**
     * 返回请求成功码
     *
     * @return ResponseEntityWrapper
     * @ses <p>该方法已经默认放置成功码，在只需要返回成功码的时候调用</p>
     */
    public ResponseEntityWrapper success() {
        return success(null);
    }

    /**
     * 返回请求成功码和成功数据
     *
     * @param data 需要返回的参数
     * @return
     * @ses <p>该方法已经默认放置成功码，需要添加需要返回的数据集合。在需要请求处理成功，并且需要返回数据的时候调用</p>
     */
    public <T> ResponseEntityWrapper success(T data) {
        return result(StatusCode.SUCCESS, data);
    }

    /**
     * 系统异常
     *
     * @return
     */
    public ResponseEntityWrapper error() {
        return result(StatusCode.ERROR, null);
    }

    /**
     * 返回请求失败信息
     *
     * @param statusCode 错误信息
     * @return
     * @ses <p>返回系统请求失败信息，在请求失败时调用</p>
     */
    public ResponseEntityWrapper error(StatusCode statusCode) {
        return result(statusCode, null);
    }

    /**
     * 返回请求错误码和错误信息
     *
     * @param code 错误码
     * @param msg  错误信息
     * @return
     * @ses <p>返回请求处理中，自定义的错误码和错误信息。用户用户需要自定义错误码和错误信息时调用</p>
     */
    public ResponseEntityWrapper error(int code, String msg) {
        return result(code, msg, null);
    }

    /**
     * 系统返回内容信息
     *
     * @param code 响应码
     * @param msg  响应信息
     * @param data 响应数据
     * @return
     */
    private <T> ResponseEntityWrapper result(int code, String msg, T data) {
        //保留null值写法
        return new ResponseEntityWrapper.Builder().statusCode(code).statusMessage(msg).data(data).build();
    }


    /**
     * 系统返回内容信息
     *
     * @param statusCode 响应码
     * @param data       响应数据
     * @return
     */
    private <T> ResponseEntityWrapper result(StatusCode statusCode, T data) {
        //保留null值写法
        return new ResponseEntityWrapper.Builder().with(statusCode, data).build();
    }
}
