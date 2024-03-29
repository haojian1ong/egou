package com.zbdx.egou.controller;

import com.zbdx.egou.pojo.CarInfo;
import com.zbdx.egou.pojo.Store;
import com.zbdx.egou.pojo.User;
import com.zbdx.egou.service.StoreService;
import com.zbdx.egou.service.UserService;
import com.zbdx.egou.utils.CustomizedToken;
import com.zbdx.egou.utils.IPutils;
import com.zbdx.egou.utils.LoginType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final String USER_LOGIN_TYPE = LoginType.USER.toString();
    @Autowired
    UserService userService;
    @Autowired
    StoreService storeService;
    @RequestMapping("/checkLogin")
    @ResponseBody
    public  String checkLogin(String username, String password){
        //获取subject对象
        Subject subject= SecurityUtils.getSubject();
            CustomizedToken customizedToken = new CustomizedToken(username, password, USER_LOGIN_TYPE);
            customizedToken.setRememberMe(false);
            try {
                subject.login(customizedToken);
            }catch (AuthenticationException e){
                return "error";
            }
        return "ok";
    }
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(){
        Subject subject= SecurityUtils.getSubject();
        subject.logout();
        return "ok";
    }
    @RequestMapping("/reg")
    @ResponseBody
    public String reg(String username,String password,String phone) throws IOException {
        String city= IPutils.getAddr();
        String pass= new Md5Hash(password).toString();
        String  result=userService.regUser(username,pass,city,phone);
        return result;
    }
    @RequestMapping("/selectByUsername")
    @ResponseBody
    public String selectByUsername(String username){
        User user = userService.selectByUsername(username);
        if(user!=null){
            return user.getUsername();
        }else {
            return "error";
        }
    }
    @RequestMapping("/selectMyInfo")
    @ResponseBody
    public Object  selectMyInfo( String username, String type){

        if (type.equals("user")){
            User user = userService.selectByUsername(username);
            return user;
        }else {
            Store store = storeService.selectByUsername(username);
            return store;
        }
    }
    @RequestMapping("/updateInfo")
    @ResponseBody
    public String updateInfo(String username,String phone,Integer userId ){
        Integer info = userService.updateInfo(username, phone, userId);
        if (info!=0){
            return "ok";
        }
        return "error";
    }
}
