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
		
		//获取对应商品的秒杀活动信息   做双重判空处理  不过我个人认为后面部分可以省略。。。
		PromoDO promoDO = promoDOMapper.selectByItemId(itemid);
		if (promoDO==null) {
			return null;
		}
		PromoModel promoModel = promoModelConvertFromPromoDO(promoDO);
//		if (promoModel==null) {
//			return null;
//		}
		
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
		//其实可以在传入的时间做判断的
		if (promoDO==null) {
			return null;
		}
		
		PromoModel promoModel = new PromoModel();
		BeanUtils.copyProperties(promoDO, promoModel);
		promoModel.setItemid(promoDO.getItemId());
		promoModel.setPromoItemPrice(new BigDecimal(promoDO.getPromoItemPrice().doubleValue()));
		promoModel.setStartDate(new DateTime(promoDO.getStartDate()));
		promoModel.setEndDate(new DateTime(promoDO.getEndDate()));
		return promoModel;
	}
	
	//传输一个标志位来判断该商品是否正确存入数据库中
	@Override
	public Boolean AddPromoToItem(PromoModel promoModel) {
		if (promoModel==null) {
			return null;
		}
		
		//将promoModel转型为promoDO
		PromoDO promoDO = PromoDoConvertFromPromoModel(promoModel);
		promoDOMapper.insertSelective(promoDO);
		
		return true;
	}

	/**
	 * @param promoModel
	 * promoModel 转换为 PromoDo
	 */
	private PromoDO PromoDoConvertFromPromoModel(PromoModel promoModel) {
		
		PromoDO promoDO = new PromoDO();
		BeanUtils.copyProperties(promoModel, promoDO);
		promoDO.setItemId(promoModel.getItemid());
		promoDO.setPromoItemPrice(promoModel.getPromoItemPrice().doubleValue());
		promoDO.setStartDate(promoModel.getStartDate().toDate());
		promoDO.setEndDate(promoModel.getEndDate().toDate());
		
		//TODO
		return promoDO;
		
	}
	
}
