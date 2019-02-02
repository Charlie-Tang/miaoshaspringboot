package com.example.demo.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.OrderDOMapper;
import com.example.demo.dao.SequenceDOMapper;
import com.example.demo.dataobject.OrderDO;
import com.example.demo.dataobject.SequenceDO;
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
	
	@Autowired
	private SequenceDOMapper sequenceDOMapper;
	
	
	@Transactional
	@Override
	public OrderModel createOrder(Integer userid, Integer itemId,Integer promoid, Integer amount) throws BusinessException {
		//1.校验下单状态，用户是否存在，用户是否合法，购买数量是否正确等
		ItemModel itemModel = itemService.getItemById(itemId);
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
		
		//通过获取promoid来校验活动信息 
		if (promoid!=null) {
			//校验对应活动是否存在这个适用商品
			if (promoid.intValue()!=itemModel.getPromoModel().getId()) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确"); 
			}
			//校验活动是否处于进行中  可以是1或者是3啊
			else if (itemModel.getPromoModel().getStatus()==1) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"活动还没开始"); 
			}
			else if (itemModel.getPromoModel().getStatus()==3) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该活动已经过期"); 
			}
		}
		
		//2.落单减库存,支付减库存
		Boolean result = itemService.decreaseStock(itemId, amount);
		if (!result) {
			throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
		}
		
		//3.订单入库
		OrderModel orderModel = new OrderModel();
		orderModel.setUserId(userid);
		orderModel.setItemId(itemId);
		orderModel.setAmount(amount);
		orderModel.setPromoId(promoid);
		if (promoid!=null) {
			//如果
			orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
		}
		else {
			orderModel.setItemPrice(itemModel.getPrice());
		}
		//multiply只接受new BigDecimal
		orderModel.setOrderamount(orderModel.getItemPrice().multiply(new BigDecimal(amount)));
		
		//生成交易流水号
		//生成交易流水号的好处：首先可以根据日期去清理往期数据  其次通过自增时间戳去获取  并且可以重置
		//最后两位分库分表位去获取不同信息
		orderModel.setId(generateOrderNo());
		OrderDO orderDO = this.OrderDOConvertFromOrderModel(orderModel);
		orderDOMapper.insertSelective(orderDO);
		
		//4.返回前端
		itemService.increaseSales(itemId, amount);
		
		return orderModel;
	}
	
	/**
	 *订单号生成函数
	 */
	//这么处理是只要这段事务完成，无论之后的事务如何回滚，都会开启一个新的事务
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private String generateOrderNo() {
		//订单号一般为16位  
		//java8之后一般使用该方法获取当前时间
		StringBuilder s1 = new StringBuilder();
		//前8位为时间信息(年月日) 
		LocalDateTime now = LocalDateTime.now();
		String nowdate = now.format(DateTimeFormatter.ISO_DATE).replaceAll("-", "");
		s1.append(nowdate);
		
		//中间6位为自增序列(保证订单号不重复)
		int sequence = 0;
		SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
		sequenceDO.getCurrentValue();
		sequence = sequenceDO.getCurrentValue();
		sequenceDO.setCurrentValue(sequenceDO.getCurrentValue()+sequenceDO.getStep());
		sequenceDOMapper.updateByPrimaryKey(sequenceDO);
		String sequenceStr = String.valueOf(sequence);
		//需要考虑我们的CurrentValue大于6位之后如何处理
		for (int i = 0; i < 6-sequenceStr.length(); i++) {
			s1.append("0");
		}
		s1.append(sequenceStr);
		
		//最后两位为分库分表位
		s1.append("00");
		
		return s1.toString();
		
	}

	private OrderDO OrderDOConvertFromOrderModel(OrderModel orderModel) {
		
		if (orderModel==null) {
			return null;
		}
		 
		OrderDO orderDO = new OrderDO();
		BeanUtils.copyProperties(orderModel, orderDO);
		//记得类型完全匹配
		orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
		orderDO.setOrderamount(orderModel.getOrderamount().doubleValue());
		return orderDO;
	}

}
