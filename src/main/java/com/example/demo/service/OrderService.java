package com.example.demo.service;

import com.example.demo.error.BusinessException;
import com.example.demo.model.OrderModel;

/**
 * @author tangqichang
 *
 * 2019年1月31日-下午5:13:59
 */
public interface OrderService {
	OrderModel createOrder(Integer userid,Integer itemid,Integer amount) throws BusinessException;
}
