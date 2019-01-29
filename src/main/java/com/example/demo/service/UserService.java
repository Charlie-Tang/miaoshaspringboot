package com.example.demo.service;

import com.example.demo.error.BusinessException;
import com.example.demo.model.UserModel;

public interface UserService {
	//通过用户ID获取用户对象的方法
	UserModel getUserById(Integer id);
	//注册
	void register(UserModel userModel) throws BusinessException;
	//登录
	UserModel validateLogin(String telephone,String password) throws BusinessException;
}
