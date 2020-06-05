package com.bairock.iot.hamaCenter.jwt;

import com.bairock.iot.hamaCenter.service.UserService;
import com.bairock.iot.hamalib.user.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public class JWTShiroRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authcToken;
        String token = jwtToken.getCredentials().toString();

        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            throw new AuthenticationException("token过期，请重新登录");
        }

        User userDB = userService.findById(userId);
        if (userDB == null) {
            throw new UnknownAccountException("No account found for admin [" + userId + "]");
        }

//        if (!JwtUtil.verify(token, username, JwtUtil.SECRET)) {
//            throw new MyException("Token错误");
//        }

        return new SimpleAuthenticationInfo(userDB, userDB.getPassword(), "jwtRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        User user = (User) getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            List<Object> rules = objectMapper.readValue(user.getRules(), new TypeReference<List<Object>>() {
//            });
//            Set<String> conditions = authRuleService.findConditionByIds(rules);
//            info.setStringPermissions(conditions);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        info.setObjectPermissions(user.getAuthorities());

        return info;
    }
}
