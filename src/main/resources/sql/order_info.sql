CREATE TABLE `order_info` (
  `id` varchar(32) NOT NULL DEFAULT '“',
  `user_id` int(11) DEFAULT '0',
  `item_id` int(11) DEFAULT '0',
  `item_price` double DEFAULT '0',
  `amount` int(11) DEFAULT '0',
  `orderAmount` double DEFAULT '0',
  `promo_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
