package com.filter;

import com.config.CorsFilterConfig;
import com.core.ResultInfo;
import com.core.UserContext;
import com.jedis.JedisUtil;
import com.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: Lanrriet
 * @date: 2020/7/2 11:37 上午
 * @description: 全局过滤器
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "headerFilter")
public class CorsFilter implements Filter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CorsFilterConfig corsFilterConfig;

    @Autowired
    private JedisUtil jedisUtil;

    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            //设置用户信息
            setAuthorization(req);
            //用户认证
            ResultInfo resultInfo = checkUser(((HttpServletRequest) req).getRequestURI());
            // 验证失败
            if (resultInfo.getCode() == HttpServletResponse.SC_UNAUTHORIZED) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "用户授权认证没有通过!");
                return;
            } else {
                chain.doFilter(req, res);
            }
        } finally {
            UserContext.clean();
        }
    }

    /**
     * 根据url拦截配置对当前url进行用户信息校验
     *
     * @param requestUri 当前请求路径
     * @return
     */
    private ResultInfo checkUser(String requestUri) {
        ResultInfo resultInfo = new ResultInfo();
        if (!checkUrl(requestUri)) {
            // 匿名访问
            resultInfo.setCode(HttpServletResponse.SC_OK);
            resultInfo.setMessage("用户授权认证通过!");
        }
        String token = UserContext.getAuthorization();
        String emailAddress = UserContext.getEmailAddress();
        if (null == token || token.isEmpty()) {
            resultInfo.setCode(HttpServletResponse.SC_UNAUTHORIZED);
            resultInfo.setMessage("用户授权认证没有通过!客户端请求参数中无token信息");
        } else {
            try {
                if (jedisUtil.get(emailAddress) != null && jedisUtil.get(emailAddress).equals(token.replace("Bearer ", "")) && jwtTokenUtil.validateToken(token)) {
                    resultInfo.setCode(HttpServletResponse.SC_OK);
                    resultInfo.setMessage("用户授权认证通过!");
                } else {
                    resultInfo.setCode(HttpServletResponse.SC_UNAUTHORIZED);
                    resultInfo.setMessage("用户授权认证没有通过!");
                }
            } catch (MalformedJwtException | ExpiredJwtException e) {
                resultInfo.setCode(HttpServletResponse.SC_UNAUTHORIZED);
                resultInfo.setMessage("用户授权认证没有通过!");
            }
        }
        return resultInfo;
    }

    /**
     * 判断请求servletPath是否为拦截路径
     *
     * @return
     */
    private Boolean checkUrl(String requestUri) {
        String exclusionUrls = corsFilterConfig.getExclusionUrls();
        if (!StringUtils.isEmpty(exclusionUrls)) {
            for (String excludedPath : exclusionUrls.split(",")) {
                String uriPattern = excludedPath.trim();
                if (antPathMatcher.match(uriPattern, requestUri)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setAuthorization(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        //设置token
        String token = request.getHeader("Authorization");
        System.out.println(token);
        UserContext.setAuthorization(token);
        //设置email
        String emailAddress = jwtTokenUtil.parseJWT(token).getSubject();
        System.out.println(emailAddress);
        UserContext.setEmailAddress(emailAddress);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }
}

