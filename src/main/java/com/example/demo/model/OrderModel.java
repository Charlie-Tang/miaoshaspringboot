package com.example.demo.model;

import java.math.BigDecimal;

/**
 * @author tangqichang
 *用户下单的模型
 * 2019年1月31日-下午4:58:21
 */
public class OrderModel {
	//交易号
	private String id;
	
	//用户id
	private Integer userid;
	
	//商品id
	private Integer itemid;
	
	//购买商品的单价
	private BigDecimal itemPrice;
	
	//购买数量
	private Integer amount;
	
	//购买金额
	private BigDecimal orderAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	
}
