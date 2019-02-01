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
	private Integer userId;
	
	//商品id
	private Integer itemId;
	
	//如果非空，则表示秒杀时的商品单价
	private Integer promoId;
	
	//购买商品的单价，如果promoId则表示秒杀价格
	private BigDecimal itemPrice;
	
	//购买数量
	private Integer amount;
	
	//购买金额，如果promoId则表示秒杀金额
	private BigDecimal orderamount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public BigDecimal getOrderamount() {
		return orderamount;
	}

	public void setOrderamount(BigDecimal orderamount) {
		this.orderamount = orderamount;
	}

	public Integer getPromoId() {
		return promoId;
	}

	public void setPromoId(Integer promoId) {
		this.promoId = promoId;
	}

	
	
}
