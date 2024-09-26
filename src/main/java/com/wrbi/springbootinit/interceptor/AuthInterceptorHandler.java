package com.wrbi.springbootinit.interceptor;

import cn.hutool.json.JSONObject;
import com.wrbi.springbootinit.common.ErrorCode;
import com.wrbi.springbootinit.common.JwtContact;
import com.wrbi.springbootinit.common.JwtProperties;
import com.wrbi.springbootinit.common.UserContext;
import com.wrbi.springbootinit.exception.BusinessException;
import com.wrbi.springbootinit.model.entity.User;
import com.wrbi.springbootinit.model.enums.UserRoleEnum;
import com.wrbi.springbootinit.service.UserService;
import com.wrbi.springbootinit.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class AuthInterceptorHandler implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final UserService userService;

    /**
     * 前置拦截器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x_requested_with,x-requested-with,Authorization,Content-Type,token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 获取到JWT的Token
        String jwtToken = request.getHeader(JwtContact.TOKEN_HEADER);
        // 创建json对象
        JSONObject jsonObject = new JSONObject();

        if (StringUtils.hasLength(jwtToken)) {
            // 解析Token，获取Claims = Map
            Claims claims = null;
            try {
                claims = JwtUtils.parseJwtToken(jwtToken);
            } catch (BusinessException e) {
                jsonObject.put("code", e.getCode());
                jsonObject.put("msg", e.getMessage());
                String json = jsonObject.toJSONString(1);
                renderJson(response, json);
            }

            long userId = JwtUtils.getUserIdByToken(jwtToken);
            UserContext.setUserId(userId);

            User user = userService.lambdaQuery().eq(User::getId, userId).one();
            if (user.getUserRole().equals(UserRoleEnum.BAN.getValue())){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "账号被封，禁止访问！");
            }
            return claims != null;
        }
        //如果token不存在
        jsonObject.put("code", ErrorCode.PARAMS_ERROR.getCode());
        jsonObject.put("msg", "登录非法");
        String json = jsonObject.toJSONString(1);
        renderJson(response, json);

        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUserId();
    }

    private void renderJson(HttpServletResponse response, String json) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
