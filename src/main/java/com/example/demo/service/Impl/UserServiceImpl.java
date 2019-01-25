package com.example.demo.service.Impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDOMapper;
import com.example.demo.dao.UserPasswordDOMapper;
import com.example.demo.dataobject.UserDO;
import com.example.demo.dataobject.UserPasswordDO;
import com.example.demo.model.UserModel;
import com.example.demo.service.UserService;
/**
 * 
 * @author tangqichang
 * 2019年1月25日-下午4:55:56
 * 为什么不将这个对象直接返回呢
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDOMapper userDOMapper;
	
	@Autowired
	private UserPasswordDOMapper userPasswordDOMapper;
	
	
	@Override
	public UserModel getUserById(Integer id) {
		UserDO userDO = userDOMapper.selectByPrimaryKey(id);
		if (userDO==null) {
			return null;
		}
		//通过用户id获取用户加密的密码信息
		UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
		
		return convertFromDataObject(userDO,userPasswordDO);
	}
	
	
	private UserModel convertFromDataObject(UserDO userDO,UserPasswordDO userPasswordDO)
	{
		if (userDO==null) {
			return null;
		}
		UserModel userModel = new UserModel();
		//将数据层的字段复制到传输层的对象上去
		//我们需要知道的是  BeanUtils.copyProperties复制两个类对象中的参数的时候  需要两个类对象参数名称和类型都是一致的  否则无法传输
		//debug了以后发现了这个问题 需要注意
		BeanUtils.copyProperties(userDO, userModel);
		if (userPasswordDO!=null) {
			userModel.setEncrpt_password(userPasswordDO.getEncrptPassword());
		}
		return userModel;
		
	}
	
}
