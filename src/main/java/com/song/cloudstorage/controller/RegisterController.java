package com.song.cloudstorage.controller;

import com.song.cloudstorage.model.User;
import com.song.cloudstorage.service.MyDiskInfoService;
import com.song.cloudstorage.service.MyFileService;
import com.song.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/register")
public class RegisterController{

    @Autowired
    private UserService userService;

    @Autowired
    private MyDiskInfoService myDiskInfoService;

    @Autowired
    private MyFileService myFileService;
    /**
     * user registration
     * @param user
     * @return
     */
    @RequestMapping("/welcome")
    public String register(User user) {
    	userService.register(user);
        return "redirect:/home/disk";
    }
    
    /**
     * email confirming
     * @param email
     * @return
     */
    @RequestMapping(value="/confirm_email", method=RequestMethod.POST)
    @ResponseBody
    public String confirmEmail(String email) {
    	return userService.confirmEmail(email);
    }
    
    @RequestMapping(value="/confirm_username",method=RequestMethod.POST)
    @ResponseBody
    public String confirmName(String username) {
    	return userService.confirmUsername(username);
    }
}
