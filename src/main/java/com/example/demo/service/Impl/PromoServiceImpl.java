package com.example.demo.service.Impl;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PromoDOMapper;
import com.example.demo.dataobject.PromoDO;
import com.example.demo.model.PromoModel;
import com.example.demo.service.PromoService;

/**
 * @author tangqichang
 *
 * 2019年2月1日-下午2:51:51
 */
@Service
public class PromoServiceImpl implements PromoService {
	
	@Autowired
	private PromoDOMapper promoDOMapper;

	@Override
	public PromoModel getPromoById(Integer itemid) {
		
		//获取对应商品的秒杀活动信息
		PromoDO promoDO = promoDOMapper.selectByItemId(itemid);
		PromoModel promoModel = promoModelConvertFromPromoDO(promoDO);
		if (promoModel==null) {
			return null;
		}
		
		//判断当前时间是否秒杀
		if (promoModel.getStartDate().isAfterNow()) {
			promoModel.setStatus(1);
		}else if (promoModel.getEndDate().isBeforeNow()) {
			promoModel.setStatus(3);
		}else {
			promoModel.setStatus(2);
		}
		
		return promoModel;
	}
	
	PromoModel promoModelConvertFromPromoDO(PromoDO promoDO) {
		
		PromoModel promoModel = new PromoModel();
		BeanUtils.copyProperties(promoDO, promoModel);
		promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice().doubleValue()));
		promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
		promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
		return promoModel;
	}
	
}
