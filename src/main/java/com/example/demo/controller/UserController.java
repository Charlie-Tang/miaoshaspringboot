package com.example.demo.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.controller.viewobject.UserVO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.model.UserModel;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.UserService;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController extends BaseController{
	
	@Autowired
	private UserService UserService;
	
	@Autowired
	private HttpServletRequest HttpServletRequest;
	
	//用户获取otp短信接口
	
	@RequestMapping(value="/getotp",method=RequestMethod.GET,consumes="application/json")
	@ResponseBody
	public CommonReturnType getOtp(@RequestParam(value="telephone",required=false)String telephone) {
		
		//需要一定的规则生成OTP验证码
		Random random = new Random();
		int randomInt = random.nextInt(99999);
		randomInt += 10000;
		String otpCode = String.valueOf(randomInt);
		
		//将OTP验证码同对应用户的手机号关联,普遍是使用redis绑定
		HttpServletRequest.getSession().setAttribute(telephone, otpCode);
		
		
		//将OTP验证码通过短信通道发送给用户,省略
		System.out.println("telephone= "+telephone +" &otpCode= "+otpCode);
		
		return CommonReturnType.create(null);
	}
	
	
	@RequestMapping("/get")
	@ResponseBody
	public CommonReturnType hello(@RequestParam("id")Integer id) throws BusinessException
	{
		//调用service服务获取id的对应用户对象并返回给前端
		UserModel userModel = UserService.getUserById(id);
		
		if (userModel==null) {
			//抛出异常
			throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
		}
		
		//将核心领域模型用户对象转为可供UI所使用的viewobject
		UserVO userVO = convertFromModel(userModel);
		return CommonReturnType.create(userVO);
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
