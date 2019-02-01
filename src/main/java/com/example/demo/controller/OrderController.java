package com.example.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.model.OrderModel;
import com.example.demo.model.UserModel;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.OrderService;

/**
 * @author tangqichang
 *
 * 2019年2月1日-上午9:48:27
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class OrderController extends BaseController{
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpServletRequest httpServletRequest;
	
	
	//封装下单请求
	@RequestMapping(value="/createorder",method=RequestMethod.POST,consumes="application/x-www-form-urlencoded")
	@ResponseBody
	public CommonReturnType createOrder(@RequestParam("itemid")Integer itemId,
			@RequestParam("amount")Integer amount,
			@RequestParam(value="promoid",required=false)Integer promoid) throws BusinessException {
		
		//放进去的都变成Object对象了，都需要强转
		Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
		if (isLogin==null || !isLogin.booleanValue()) {
			throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"用户还未登录，不能下单");
		}
		
		UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");
		OrderModel orderModel = orderService.createOrder(userModel.getId(), itemId, promoid , amount);
		return CommonReturnType.create(orderModel);
	}
	
	
}
