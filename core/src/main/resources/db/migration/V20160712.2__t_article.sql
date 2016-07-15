CREATE TABLE `t_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author` varchar(50) DEFAULT NULL,
  `copy_from` varchar(255) DEFAULT NULL,
  `refer_url` varchar(255) DEFAULT NULL,
  `content` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
