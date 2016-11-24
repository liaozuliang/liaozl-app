USE liaozl_common;

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;


INSERT INTO `t_user` VALUES ('3', 'aa', '33', '33', '2016-09-27 09:38:34');
INSERT INTO `t_user` VALUES ('4', '中国', '44', '北京', '2016-09-29 09:38:46');
INSERT INTO `t_user` VALUES ('5', 'cc', '55', '55', '2016-09-28 09:38:57');
INSERT INTO `t_user` VALUES ('6', '上海', '66', '66', '2016-09-22 09:39:09');
