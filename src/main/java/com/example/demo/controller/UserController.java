package com.example.demo.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

import com.alibaba.druid.util.StringUtils;
import com.example.demo.controller.viewobject.UserVO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.model.UserModel;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.UserService;

import sun.misc.BASE64Encoder;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class UserController extends BaseController{
	
	@Autowired
	private UserService UserService;
	
	@Autowired
	private HttpServletRequest HttpServletRequest;
	
	//用户获取otp短信接口
	@RequestMapping(value="/getotp",method=RequestMethod.POST,consumes="application/x-www-form-urlencoded")
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
	
	//用户注册接口
	@RequestMapping(value="/register",method=RequestMethod.POST,consumes="application/x-www-form-urlencoded")
	@ResponseBody
	public CommonReturnType register(@RequestParam(value="telephone")String telephone,
			@RequestParam(value="otpCode")String otpCode,
			@RequestParam(value="name")String name,
			@RequestParam(value="gender")Byte gender,
			@RequestParam(value="age")String age,
			@RequestParam(value="password")String password) throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		//验证手机号和对应的otpcode是否相符合
		String inSessionotpcode = (String) this.HttpServletRequest.getSession().getAttribute(telephone);
		if (!StringUtils.equals(otpCode, inSessionotpcode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
		}
		
		//用户注册流程
		UserModel userModel = new UserModel();
		userModel.setName(name);
		userModel.setGender(gender);
		userModel.setAge(age);
		userModel.setTelephone(telephone);
		userModel.setRegisterMode("byphone");
		userModel.setEncrpt_password(this.EncodeByMD5(password));
		
		UserService.register(userModel);
		
		return CommonReturnType.create(null);
	}
	
	//将密码转码为MD5格式
	public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		//确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64Encoder = new BASE64Encoder();
		//加密字符串
		String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
	
	//用户登录接口
	@RequestMapping(value="/login",method=RequestMethod.POST,consumes="application/x-www-form-urlencoded")
	@ResponseBody
	public CommonReturnType login(@RequestParam(value="telephone")String telephone,
			@RequestParam(value="password")String password) throws BusinessException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		//入参校验
		if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)
				||org.apache.commons.lang3.StringUtils.isEmpty(password)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		
		UserModel userModel = UserService.validateLogin(telephone, this.EncodeByMD5(password));
		
		//将登陆凭证加入到用户登陆成功的session内
		HttpServletRequest.getSession().setAttribute("IS_LOGIN", true);
		HttpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
		
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
