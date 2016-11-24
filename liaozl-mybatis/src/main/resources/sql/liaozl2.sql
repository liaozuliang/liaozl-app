USE liaozl2;

CREATE TABLE `t_test2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


INSERT INTO `t_test2` VALUES ('1', 'test2', '中国上海');
INSERT INTO `t_test2` VALUES ('2', '测试2', '中国北京');
