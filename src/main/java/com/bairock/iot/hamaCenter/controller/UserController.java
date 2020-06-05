package com.bairock.iot.hamaCenter.controller;

import com.bairock.iot.hamaCenter.exception.MyException;
import com.bairock.iot.hamaCenter.service.UserService;
import com.bairock.iot.hamaCenter.utils.Result;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.bairock.iot.hamaCenter.data.UserAuthority;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserAuthorityRepo userAuthorityRepo;

	@Autowired
    private UserService userService;

	@PostMapping("/add")
    public Result<?> add(@ModelAttribute User user, @RequestParam String confirmPassword){
	    if(StringUtils.isEmpty(user.getUsername())){
	        throw new MyException("用户名不可为空");
        }
        if(StringUtils.isEmpty(user.getPassword())){
            throw new MyException("密码不可为空");
        }
        if(StringUtils.isEmpty(confirmPassword)){
            throw new MyException("确认密码不可为空");
        }
        if(!user.getPassword().equals(confirmPassword)){
            throw new MyException("两次输入的密码不一致");
        }
    }

    //打开注册页面
    @GetMapping("/page/register")
    public String registerForm() {
        return "register";
    }

    //打开登录页面
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
    
    @GetMapping("/register/success")
    public String registerSuccess(@RequestParam String userid) {
    	UserAuthority ua = new UserAuthority();
		ua.setUserid(userid);
		ua.setAuthority("ROLE_USER");
		userAuthorityRepo.saveAndFlush(ua);
        return "login";
    }

//    //提交登录信息
//    @PostMapping("/login")
//    public String loginCheck(HttpServletResponse httpServletResponse, RedirectAttributes model, @ModelAttribute RegisterUserHelper userHelper) {
//        User userDb = userService.findByName(userHelper.getName());
//        if(null == userDb){
//            userHelper.setUserNameError("用户不存在");
//            return "login";
//        }
//        //userDb.getDevGroups();
//        if(!userDb.getPsd().equals(userHelper.getPassword())){
//            userHelper.setPasswordError("密码错误");
//            return "login";
//        }
//        if(userHelper.isAutoLogin()){
//            Cookie cookie = new Cookie("userId", String.valueOf(userDb.getId()));
//            cookie.setPath("/");//如果需要在跟目录获取,如欢迎页面,必须设置path为"/",否则只能在"/user"路径下获取到,其他路径获取不到
//            cookie.setMaxAge(Integer.MAX_VALUE); //设置cookie的过期时间是10s
//            httpServletResponse.addCookie(cookie);
//        }
//
//        //重定向
//        model.addAttribute("userId", userDb.getId());
//
//        model.addFlashAttribute("user", userDb);
//        return "redirect:/group/list/{userId}";
//    }

//    @PostMapping("/logout")
//    public String logout(HttpServletResponse httpServletResponse, Model model){
//        Cookie userCookie = new Cookie("userId", "");
//        userCookie.setMaxAge(0);
//        userCookie.setPath("/");
//        httpServletResponse.addCookie(userCookie);
//        model.addAttribute("registerUserHelper", new RegisterUserHelper());
//        return "login";
//    }
}
