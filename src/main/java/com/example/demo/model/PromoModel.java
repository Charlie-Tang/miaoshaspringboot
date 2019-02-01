package com.example.demo.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * @author tangqichang
 *
 * 2019年2月1日-下午2:21:16
 */
public class PromoModel {
	
	private Integer id;
	
	//秒杀活动名称
	private String promoName;
	
	//开始时间
	private DateTime startDate;
	
	//结束时间
	private DateTime endDate;
	
	//秒杀活动的适用商品
	private Integer itemid;
	
	//秒杀价格
	private BigDecimal promoItemPrice;
	
	//秒杀活动状态
	private Integer status;//为1还未开始 为2进行中 为3结束

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPromoName() {
		return promoName;
	}

	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}

	public DateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	
	
	public DateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public BigDecimal getPromoItemPrice() {
		return promoItemPrice;
	}

	public void setPromoItemPrice(BigDecimal promoItemPrice) {
		this.promoItemPrice = promoItemPrice;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
