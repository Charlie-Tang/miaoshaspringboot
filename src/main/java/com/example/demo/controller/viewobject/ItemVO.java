package com.example.demo.controller.viewobject;

import java.math.BigDecimal;

/**
 * @author tangqichang
 *
 * 2019年1月30日-下午3:41:11
 */
public class ItemVO {
	
	private Integer id;
	
	//商品名
	private String title;
	
	//商品价格
	private BigDecimal price;
	
	//商品库存
	private Integer stock;
	
	//商品描述
	private String description;
	
	//商品销量
	private Integer sales;
	
	//商品描述图片
	private String imgUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}
