/**
 * 
 */
package com.example.demo.service;

import java.util.List;

import com.example.demo.error.BusinessException;
import com.example.demo.model.ItemModel;

/**
 * @author tangqichang
 *
 * 2019年1月30日-下午2:47:55
 */
public interface ItemService {
	
	//创建商品
	ItemModel createItem(ItemModel itemModel) throws BusinessException;
	
	//商品列表浏览
	List<ItemModel> listItem();
	
	//商品详情浏览
	ItemModel getItemById(Integer id);
}
