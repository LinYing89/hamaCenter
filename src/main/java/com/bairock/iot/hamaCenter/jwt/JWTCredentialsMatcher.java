package com.bairock.iot.hamaCenter.jwt;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

public class JWTCredentialsMatcher implements CredentialsMatcher {
    /**
     * Matcher中直接调用工具包中的verify方法即可
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
//        String token = (String) authenticationToken.getCredentials();
//        User user = (User) authenticationInfo.getPrincipals().getPrimaryPrincipal();

//        return JwtUtil.verify(token, user.getId(), user.getCompanyId(), JwtUtil.SECRET);
        return true;
    }
}