package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.controller.viewobject.ItemVO;
import com.example.demo.error.BusinessException;
import com.example.demo.model.ItemModel;
import com.example.demo.response.CommonReturnType;
import com.example.demo.service.ItemService;

/**
 * @author tangqichang
 *
 * 2019年1月30日-下午3:40:10
 */
@Controller("Item")
@RequestMapping("/Item")
@CrossOrigin(allowCredentials="true",allowedHeaders="*")
public class ItemController extends BaseController{
	
	@Autowired
	private ItemService itemService;
	
	//创建商品
	@RequestMapping(value="/createItem",method=RequestMethod.POST,consumes="application/x-www-form-urlencoded")
	@ResponseBody
	public CommonReturnType createItem(@RequestParam(value="title")String title,
			@RequestParam(value="description")String description,
			@RequestParam(value="price")BigDecimal price,
			@RequestParam(value="stock")Integer stock,
			@RequestParam(value="imgUrl")String imgUrl) throws BusinessException {
		
		ItemModel itemModel = new ItemModel();
		itemModel.setDescription(description);
		itemModel.setPrice(price);
		itemModel.setTitle(title);
		itemModel.setStock(stock);
		itemModel.setImgUrl(imgUrl);
		
		ItemModel itemModelFroReturn = itemService.createItem(itemModel);
		ItemVO itemVO = itemVOConvertFrmoItemModel(itemModelFroReturn);
		
		//封装service请求用来创建商品
		return CommonReturnType.create(itemVO);
	}
	
	private ItemVO itemVOConvertFrmoItemModel(ItemModel itemModel) {
		if (itemModel == null) {
			return null;
		}
		ItemVO itemVO = new ItemVO();
		BeanUtils.copyProperties(itemModel,itemVO);
		return itemVO;
	}
	
	//商品列表页面浏览
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public CommonReturnType listItem() {
		
		List<ItemModel> list = itemService.listItem();
		//使用stream api将list内的listModel转化为itemVO
		List<ItemVO> itemVOlist = list.stream().map(itemModel -> {
			ItemVO itemVO = this.itemVOConvertFrmoItemModel(itemModel);
			return itemVO;
		}).collect(Collectors.toList());
		
		return CommonReturnType.create(itemVOlist);
	}
	
	//商品详情页浏览
	@RequestMapping(value="/getItem",method=RequestMethod.GET)
	@ResponseBody
	public CommonReturnType getItem(@RequestParam("id")Integer id) {
		
		ItemModel itemModel = itemService.getItemById(id);
		ItemVO itemVO = itemVOConvertFrmoItemModel(itemModel);
		return CommonReturnType.create(itemVO);
	}
}	
