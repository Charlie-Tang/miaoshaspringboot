package com.example.demo.service;

import com.example.demo.model.PromoModel;

/**
 * @author tangqichang
 *
 * 2019年2月1日-下午2:50:52
 */
public interface PromoService {
	
	PromoModel getPromoById(Integer itemid);
	//注册活动信息到商品上
	Boolean AddPromoToItem(PromoModel promoModel);
}
