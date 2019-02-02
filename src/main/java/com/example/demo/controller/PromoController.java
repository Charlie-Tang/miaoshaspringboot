package com.example.demo.controller;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.model.PromoModel;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.PromoService;

/**
 * @author tangqichang
 *
 * 2019年2月2日-上午10:58:12
 */
@Controller("Promo")
@RequestMapping("/Item")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class PromoController extends BaseController{
	
	@Autowired
	private PromoService promoService;
	
		//给商品添加活动信息
		@RequestMapping(value="/AddItemPromo",method=RequestMethod.POST,consumes="application/x-www-form-urlencoded")
		@ResponseBody
		public CommonReturnType AddItemPromo(@RequestParam("promoName")String promoName,
				@RequestParam("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )DateTime startDate,
				@RequestParam("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss" )DateTime endDate,
				@RequestParam(value="itemid")Integer itemid,
				@RequestParam("promoItemPrice")Double promoItemPrice) throws BusinessException{
			
			PromoModel promoModel = new PromoModel();
			promoModel.setPromoName(promoName);
			promoModel.setStartDate(startDate);
			promoModel.setEndDate(endDate);
			promoModel.setItemid(itemid);
			promoModel.setPromoItemPrice(new BigDecimal(promoItemPrice.doubleValue()));
			
			Boolean flag = promoService.AddPromoToItem(promoModel);
			if (flag==false) {
				throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"无法正确存储活动信息");
			}
			
			return CommonReturnType.create(null);
		}
	
}
