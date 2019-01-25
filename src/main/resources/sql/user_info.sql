CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '"',
  `gender` tinyint(3) DEFAULT '0' COMMENT '//1代表男性，2代表女性',
  `age` varchar(255) DEFAULT '0',
  `telephone` varchar(255) DEFAULT '"',
  `register_mode` varchar(255) DEFAULT '"27.0.0.1 - miaosha.user_password' COMMENT '//byphone bywechat byalipay',
  `third_party_id` varchar(255) NOT NULL DEFAULT '"27.0.0.1 - miaosha.user_password',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
