CREATE TABLE `sequence_info` (
  `name` varchar(255) NOT NULL DEFAULT '',
  `current_value` int(11) DEFAULT '0',
  `step` int(11) DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
