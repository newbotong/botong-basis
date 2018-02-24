package com.yunjing.mommon.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunjing.mommon.constant.BaseCodeConstant;
import com.yunjing.mommon.model.ResponseModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller基类
 * @author: zhangx
 * @date 2018/01/18 14:04
 * @version 1.0.0
 * @description
 **/
public abstract class BaseController {

    /** 响应码 */
    private final String code = "code";

    /** 响应信息*/
    private final String msg = "msg";

    /** 响应数据 */
    private final String data = "data";

    /**
     * 响应码定义说明：
     * 1~100 为，请求接收前，数据验证返回，标识为数据不否和接口要求规范
     */

    public final Logger logger = LogManager.getLogger(this.getClass());

    public HttpServletRequest request;

    public HttpServletResponse response;

    /**
     * 获取Http servlet request、response
     * @param request
     * @param response
     */
    @ModelAttribute
    public void before(HttpServletRequest request, HttpServletResponse response){
        logger.info("开始调用请求方法：" + request.getRequestURI() + " 输入参数包含：" + JSON.toJSONString(request.getParameterMap()));
        this.request = request;
        this.response = response;
    }

    /**
     * 定义捕获全局系统异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e){
        logger.error("系统发生异常，异常原因为：" + e.getMessage(), e);
        if(e instanceof MissingServletRequestParameterException){
            MissingServletRequestParameterException exception = (MissingServletRequestParameterException)e;
            return error(BaseCodeConstant.PARAM_NULL, "参数[ " + exception.getParameterName() + " ]未输入");
        }
        return systemError();
    }

    /**
     * 获取Spring 容器中注入的对象
     * @param beanID 实例ID
     * @return
     */
    public Object getBean(String beanID){
        ServletContext servletContext = request.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        return applicationContext.getBean(beanID);
    }

    /**
     * 返回请求成功码
     * @return
     * @ses
     * <p>该方法已经默认放置成功码，在只需要返回成功码的时候调用</p>
     */
    public String success(){
        return result(BaseCodeConstant.SUCCESS, null , null);
    }

    /**
     * 返回请求成功码和成功数据
     * @param data 需要返回的参数
     * @return
     * @ses
     * <p>该方法已经默认放置成功码，需要添加需要返回的数据集合。在需要请求处理成功，并且需要返回数据的时候调用</p>
     */
    public String success(Object data){
        return result(BaseCodeConstant.SUCCESS, null, data);
    }

    /**
     * 系统异常
     * @return
     */
    public String systemError(){
        return result(BaseCodeConstant.ERROR, "系统异常", null);
    }

    /**
     * 返回请求失败信息
     * @param msg 错误信息
     * @return
     * @ses
     * <p>返回系统请求失败信息，在请求失败时调用</p>
     */
    public String error(String msg){
        return result(BaseCodeConstant.ERROR, msg, null);
    }

    /**
     * 返回请求错误码和错误信息
     * @param code 错误码
     * @param msg 错误信息
     * @return
     * @ses
     * <p>返回请求处理中，自定义的错误码和错误信息。用户用户需要自定义错误码和错误信息时调用</p>
     */
    public String error(String code, String msg){
        return result(code, msg, null);
    }

    /**
     * 响应自定义属性模型
     * @param model 响应模型
     * @return
     */
    public String customize(ResponseModel model){
        return JSONObject.toJSONString(model);
    }

    /**
     * 返回客户定制请求信息
     * @param code 响应码
     * @param msg 响应信息
     * @param data 响应数据
     * @return
     * @ses
     * <p>根据客户需求，返回客户需要信息</p>
     */
    public String customize(String code, String msg, Object data){
        return result(code, msg, data);
    }

    /**
     * 系统返回内容信息
     * @param code 响应码
     * @param msg 响应信息
     * @param obj 响应数据
     * @return
     */
    private String result(String code, String msg, Object obj){
        //保留null值写法
        //System.out.printf("========" + JSONObject.toJSONString(json, SerializerFeature.WriteMapNullValue) + "+++++++++++");
        return JSONObject.toJSONString(new ResponseModel(code, msg, obj));
    }

}
