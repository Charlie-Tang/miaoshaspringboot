package com.example.demo.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.controller.viewobject.UserVO;
import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;


@Controller("user")
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService UserService;
	
	@RequestMapping("/get")
	@ResponseBody
	public UserVO hello(@RequestParam("id")Integer id)
	{
		//调用service服务获取id的对应用户对象并返回给前端
		UserModel userModel = UserService.getUserById(id);
		//将核心领域模型用户对象转为可供UI所使用的viewobject
		System.out.println(userModel.getThirdPartyId());
		System.out.println(userModel.getRegisterMode());
		return convertFromModel(userModel);
	}
	
	private UserVO convertFromModel(UserModel userModel) {
		if (userModel==null) {
			return null;
		}
		
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userModel, userVO);
		
		return userVO;
	}
}
