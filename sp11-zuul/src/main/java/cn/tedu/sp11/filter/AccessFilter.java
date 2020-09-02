package cn.tedu.sp11.filter;

import cn.tedu.web.util.JsonResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessFilter extends ZuulFilter {
    /**
     * 指定过滤器类型 pre，post，routing，error;
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 顺序号 :指定过滤器插入位置
     * 第五个过滤器在上下文添加了service-id
     * 在第五个过滤器之后才能从上下文访问service-id
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    /**
     * 对当前请求是否进行过滤,
     * 如果方法返回true,则进行过滤,执行过滤方法run()
     * 如果返回false,则跳过过滤代码,继续执行后面的流程
     * 判断调用的是否是商品服务,如果是返回true,不是返回false
     */
    @Override
    public boolean shouldFilter() {
        //获取上下文对象
        RequestContext ctx = RequestContext.getCurrentContext();
        //从上下文对象获得客户端调用的service id
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        return "item-service".equals(serviceId);
    }

    /**
     * 过滤代码
     * 其返回值在当前zuul版本中没有启用.返回任何数据都无效
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getParameter("token");
        if (StringUtils.isBlank(token)){
        ctx.setSendZuulResponse(false);
        //向客户端响应
        ctx.setResponseStatusCode(JsonResult.NOT_LOGIN);
        ctx.setResponseBody(JsonResult.err().code(JsonResult.NOT_LOGIN).msg("not log in").toString());
        }
        return null;
    }
}
