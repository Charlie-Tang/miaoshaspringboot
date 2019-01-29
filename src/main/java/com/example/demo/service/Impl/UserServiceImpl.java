package com.example.demo.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.UserDOMapper;
import com.example.demo.dao.UserPasswordDOMapper;
import com.example.demo.dataobject.UserDO;
import com.example.demo.dataobject.UserPasswordDO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
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
	
	//将我们dao层对象转换为传输层所使用的对象
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


	@Override
	@Transactional
	public void register(UserModel userModel) throws BusinessException {
		if (userModel==null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		
		if (StringUtils.isEmpty(userModel.getName()) 
				|| userModel.getGender() == null 
				|| userModel.getAge() == null
				|| StringUtils.isEmpty(userModel.getTelephone())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
		}
		
		UserDO userDO = convertFromModel(userModel);
		try {
			userDOMapper.insertSelective(userDO);
		} catch (DuplicateKeyException e) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
		}
		
		//尽量避免在数据库中使用null字段
		UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel,userDO);
		userPasswordDOMapper.insertSelective(userPasswordDO);
	}
	
	private UserDO 	convertFromModel(UserModel userModel) {
		if (userModel==null) {
			return null;
		}
		UserDO userDO = new UserDO();
		BeanUtils.copyProperties(userModel, userDO);
		
		return userDO;
	}
	
	private UserPasswordDO convertPasswordFromModel(UserModel userModel,UserDO userDO) {
		if (userModel==null) {
			return null;
		}
		
		UserPasswordDO userPasswordDO = new UserPasswordDO();
		userPasswordDO.setEncrptPassword(userModel.getEncrpt_password());
		userPasswordDO.setUserId(userDO.getId());
		return userPasswordDO;
	}

	@Override
	public UserModel validateLogin(String telephone, String password) throws BusinessException {
		//通过用户手机获取用户信息
		UserDO userDO = userDOMapper.selectByTelePhone(telephone);
		if (userDO==null) {
			throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
		}
		UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
		UserModel userModel = convertFromDataObject(userDO, userPasswordDO);
		
		//比对用户信息内加密
		if (!StringUtils.equals(password, userModel.getEncrpt_password())) {
			throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
		}
		return userModel; 
		
	}
}
