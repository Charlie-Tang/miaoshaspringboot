/**
 * 
 */
package com.example.demo.validator;

import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;


/**
 * @author tangqichang
 *
 * 2019年1月29日-下午5:58:15
 */
@Component
public class ValidatorImpl implements InitializingBean{
	
	private Validator validator;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		//将hibernate validator 通过工厂的初始化方式使其实例化
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	
}
