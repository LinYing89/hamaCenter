package com.bairock.iot.hamaCenter.jwt;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public Realm jwtRealm() {
        JWTShiroRealm realm = new JWTShiroRealm();
        realm.setCacheManager(new MemoryConstrainedCacheManager()); // 开启内存缓存
        realm.setCredentialsMatcher(new JWTCredentialsMatcher());
        return realm;
    }

    //权限管理，配置主要是Realm的管理认证
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(jwtRealm());
//        List<Realm> list = new ArrayList<>();
//        list.add(jwtRealm());
//        securityManager.setRealms(list);

        // 关闭自带session
//        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
//        evaluator.setSessionStorageEnabled(false);
//
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        subjectDAO.setSessionStorageEvaluator(evaluator);
//
//        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }

//    @Bean
//    public ShiroPermissionsFilter shiroLoginFilter(){
//        return new ShiroPermissionsFilter();
//    }

    //Filter工厂，设置对应的过滤条件和跳转条件
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
        //将自定义 的ShiroFilterFactoryBean注入shiroFilter
        filters.put("jwt", new JwtFilter());

        Map<String,String> map = new HashMap<>();
        //登出
        map.put("/logout","logout");
//        map.put("/login","anon");
        //对所有用户认证anon
//        map.put("/**","authc");
        map.put("/**","jwt");
//        map.put("/**","anon");
        //登录
//        shiroFilterFactoryBean.setLoginUrl("/login");
        //首页
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        //错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
        return shiroFilterFactoryBean;
    }

    //加入注解的使用，不加入这个注解不生效
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        /*
          setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
          在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
          加入这项配置能解决这个bug
         */
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

}
