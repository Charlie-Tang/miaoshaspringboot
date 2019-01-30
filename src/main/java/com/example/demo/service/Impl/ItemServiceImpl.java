/**
 * 
 */
package com.example.demo.service.Impl;

import java.math.BigDecimal;
import java.util.List;

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
import com.example.demo.service.ItemService;
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
	
	
	@Override
	public List<ItemModel> listItem() {
		return null;
	}

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
		
		return itemModel;
	}
	
	private ItemModel ItemModelConvertFromItemOBj(ItemDO itemDO,ItemstockDO itemstockDO) {
		
		ItemModel itemModel = new ItemModel();
		BeanUtils.copyProperties(itemDO, itemModel);
		itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
		itemModel.setStock(itemstockDO.getStock());
		
		return itemModel;
	}

}