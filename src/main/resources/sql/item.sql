CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT '"',
  `price` double DEFAULT '0',
  `description` varchar(500) DEFAULT '"234567890ABCDEFGHIJKLMNOPQRSTU',
  `sales` int(11) DEFAULT '0',
  `img_url` varchar(500) DEFAULT '"234567890ABCDEFGHIJKLMNOPQRSTU',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
