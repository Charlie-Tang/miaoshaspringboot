package com.example.demo.service.Impl;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderDOMapper;
import com.example.demo.dataobject.OrderDO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.model.ItemModel;
import com.example.demo.model.OrderModel;
import com.example.demo.model.UserModel;
import com.example.demo.service.ItemService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;

/**
 * @author tangqichang
 *
 * 2019年1月31日-下午5:18:32
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderDOMapper orderDOMapper;
	
	@Transactional
	@Override
	public OrderModel createOrder(Integer userid, Integer itemid, Integer amount) throws BusinessException {
		//1.校验下单状态，用户是否存在，用户是否合法，购买数量是否正确等
		ItemModel itemModel = itemService.getItemById(itemid);
		if (itemModel==null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
		}
		if (amount<=0 || amount>99) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
		}
		UserModel userModel = userService.getUserById(userid);
		if (userModel==null) {
			throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
		}
		
		//2.落单减库存,支付减库存
		Boolean result = itemService.decreaseStock(itemid, amount);
		if (!result) {
			throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
		}
		//3.订单入库
		OrderModel orderModel = new OrderModel();
		orderModel.setUserid(userid);
		orderModel.setItemid(itemid);
		orderModel.setAmount(amount);
		orderModel.setItemPrice(itemModel.getPrice());
		//multiply只接受new BigDecimal
		orderModel.setOrderAmount(itemModel.getPrice().multiply(new BigDecimal(amount)));
		
		//生成交易流水号
		generateOrderNo();
		
		OrderDO orderDO = this.OrderDOConvertFromOrderModel(orderModel);
		orderDOMapper.insertSelective(orderDO);
		//4.返回前端
		return null;
	}
	
	/**
	 *订单号生成函数
	 */
	private String generateOrderNo() {
		//订单号一般为16为  前8位为时间信息(年月日)  中间6位为自增序列(保证订单号不重复)  最后两位为分库分表位
		return null;
		
	}

	private OrderDO OrderDOConvertFromOrderModel(OrderModel orderModel) {
		
		if (orderModel==null) {
			return null;
		}
		 
		OrderDO orderDO = new OrderDO();
		BeanUtils.copyProperties(orderModel, orderDO);
		
		return orderDO;
	}

}
