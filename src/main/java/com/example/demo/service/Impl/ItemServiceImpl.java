package com.example.demo.service.Impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.ItemDOMapper;
import com.example.demo.dao.ItemstockDOMapper;
import com.example.demo.dataobject.ItemDO;
import com.example.demo.dataobject.ItemstockDO;
import com.example.demo.error.BusinessException;
import com.example.demo.error.EmBusinessError;
import com.example.demo.model.ItemModel;
import com.example.demo.model.PromoModel;
import com.example.demo.service.ItemService;
import com.example.demo.service.PromoService;
import com.example.demo.validator.ValidationResult;
import com.example.demo.validator.ValidatorImpl;

/**
 * @author tangqichang
 *
 * 2019年1月30日-下午2:49:56
 */
@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ValidatorImpl validator;
	
	@Autowired
	private ItemDOMapper itemDOMapper;
	
	@Autowired
	private ItemstockDOMapper itemstockDOMapper;
	
	@Autowired
	private PromoService promoService;
	
	//创建商品
	@Transactional
	@Override
	public ItemModel createItem(ItemModel itemModel) throws BusinessException {
		
		//校验入参 
		ValidationResult result = validator.validate(itemModel);
		if (result.isHasErrors()) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
		}
		
		
		//转化itemmodel->dataobject
		ItemDO itemDO = this.convertItemModelToItemDO(itemModel);
		
		//写入数据库
		itemDOMapper.insertSelective(itemDO);
		itemModel.setId(itemDO.getId());
		
		ItemstockDO itemstockDO = convertItemModelToItemstockDO(itemModel);
		itemstockDOMapper.insertSelective(itemstockDO);
		
		//返回创建完成的对象
		return this.getItemById(itemModel.getId());
	}
	
	private ItemDO convertItemModelToItemDO(ItemModel itemModel) {
		
		if (itemModel==null) {
			return null;
		}
		ItemDO itemDO = new ItemDO();
		BeanUtils.copyProperties(itemModel, itemDO);
		itemDO.setPrice(itemModel.getPrice().doubleValue());
		return itemDO;
	}
	
	private ItemstockDO convertItemModelToItemstockDO(ItemModel itemModel) {
		
		if (itemModel==null) {
			return null;
		}
		ItemstockDO ItemstockDO = new ItemstockDO();
		ItemstockDO.setStock(itemModel.getStock());
		ItemstockDO.setItemId(itemModel.getId());
		return ItemstockDO;
	}
	
	//通过jdk1.8的stream()方法将商品列表展示出来
	@Override
	public List<ItemModel> listItem() {
		
		List<ItemDO> list = itemDOMapper.listItem();
		List<ItemModel> ModelList = list.stream().map(itemDO ->{
			ItemstockDO itemstockDO = itemstockDOMapper.selectByItemid(itemDO.getId());
			ItemModel itemModel = this.ItemModelConvertFromItemOBj(itemDO, itemstockDO);
			return itemModel;
		}).collect(Collectors.toList());
		
		return ModelList;
	}
	
	//通过id获得商品信息
	@Override
	public ItemModel getItemById(Integer id) {
		
		ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
		if (itemDO==null) {
			return null;
		}
		
		//操作获得库存数量
		ItemstockDO itemstockDO = itemstockDOMapper.selectByItemid(itemDO.getId());
		
		//将dataobject转换为model
		ItemModel itemModel = this.ItemModelConvertFromItemOBj(itemDO,itemstockDO);
		
		//通过活动商品id来获取活动商品信息,在调用函数中做判空处理
		PromoModel promoModel = promoService.getPromoById(itemModel.getId());
		if (promoModel!=null && promoModel.getStatus()!=3) {
			itemModel.setPromoModel(promoModel);
		}
		
		return itemModel;
	}
	
	private ItemModel ItemModelConvertFromItemOBj(ItemDO itemDO,ItemstockDO itemstockDO) {
		
		ItemModel itemModel = new ItemModel();
		BeanUtils.copyProperties(itemDO, itemModel);
		itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
		itemModel.setStock(itemstockDO.getStock());
		
		return itemModel;
	}
	
	//减少库存标志位
	@Transactional
	@Override
	public Boolean decreaseStock(Integer itemid, Integer amount) throws BusinessException {
		
		int affectedRow = itemstockDOMapper.decreaseStock(itemid, amount);
		if (affectedRow>0) {
			//更新库存成功
			return true;
		}else {
			return false;
		}
	}

	@Override
	@Transactional
	public void increaseSales(Integer itemid, Integer amount) throws BusinessException {
		itemDOMapper.increaseSales(itemid, amount);
	}

}
