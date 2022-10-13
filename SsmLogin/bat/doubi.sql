/*
Navicat MySQL Data Transfer

Source Server         : localhost_3307
Source Server Version : 50633
Source Host           : localhost:3306
Source Database       : doubi

Target Server Type    : MYSQL
Target Server Version : 50633
File Encoding         : 65001

Date: 2017-03-03 15:21:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for acodedate
-- ----------------------------
DROP TABLE IF EXISTS `acodedate`;
CREATE TABLE `acodedate` (
  `productDate` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `generateDate` bigint(20) DEFAULT NULL COMMENT '生成到的时间',
  `generateNum` int(11) DEFAULT NULL,
  `sign` varchar(10) NOT NULL,
  `gid` varchar(30) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `pid` varchar(30) CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`gid`,`pid`,`sign`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for activecode
-- ----------------------------
DROP TABLE IF EXISTS `activecode`;
CREATE TABLE `activecode` (
  `activeCode` varchar(16) CHARACTER SET utf8 NOT NULL,
  `gid` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `pid` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `zid` int(4) DEFAULT NULL,
  `uid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `status` int(4) DEFAULT NULL,
  `expire` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`activeCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for gameplat
-- ----------------------------
DROP TABLE IF EXISTS `gameplat`;
CREATE TABLE `gameplat` (
  `gid` varchar(30) CHARACTER SET utf8 NOT NULL,
  `pid` varchar(30) CHARACTER SET utf8 NOT NULL,
  `pidNa` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `gameNa` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `gameOnline` date DEFAULT NULL,
  PRIMARY KEY (`gid`,`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for logindata
-- ----------------------------
DROP TABLE IF EXISTS `logindata`;
CREATE TABLE `logindata` (
  `guid` bigint(20) NOT NULL,
  `lgdate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '登录日期',
  `account` varchar(100) DEFAULT NULL,
  `uid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `gid` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `pid` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`lgdate`,`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin2;

-- ----------------------------
-- Table structure for paymement
-- ----------------------------
DROP TABLE IF EXISTS `paymement`;
CREATE TABLE `paymement` (
  `uuid` varchar(255) NOT NULL COMMENT '游戏id',
  `guid` bigint(11) NOT NULL,
  `gid` varchar(11) NOT NULL,
  `pid` varchar(11) DEFAULT NULL,
  `zid` int(11) DEFAULT NULL,
  `account` varchar(255) NOT NULL COMMENT '账号',
  `nickname` varchar(255) DEFAULT NULL,
  `createDate` datetime NOT NULL COMMENT '支付时间',
  `complateDate` datetime DEFAULT NULL,
  `ownOrder` varchar(255) NOT NULL COMMENT '自己订单号',
  `goodsOrder` varchar(255) DEFAULT NULL COMMENT '订单号',
  `shopId` int(10) DEFAULT NULL COMMENT '商品名称',
  `goodsNum` int(5) DEFAULT NULL COMMENT '商品数量',
  `Amount` float(5,0) DEFAULT NULL COMMENT '金额',
  `sendStatus` int(1) DEFAULT NULL COMMENT '发送数据是否成功',
  `sendTarget` varchar(30) DEFAULT NULL COMMENT '发送目标',
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`ownOrder`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for userdata
-- ----------------------------
DROP TABLE IF EXISTS `userdata`;
CREATE TABLE `userdata` (
  `guid` bigint(20) NOT NULL,
  `lgdate` date NOT NULL COMMENT '登录日期',
  `account` varchar(100) DEFAULT NULL,
  `uid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `gid` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `pid` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- View structure for vguser
-- ----------------------------
DROP VIEW IF EXISTS `vguser`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER  VIEW `vguser` AS select `logindata`.`guid` AS `guid`,date_format(`logindata`.`lgdate`,'%Y-%m-%d') AS `lgdate`,`logindata`.`account` AS `account`,`logindata`.`uid` AS `uid`,`logindata`.`gid` AS `gid`,`logindata`.`pid` AS `pid` from `logindata` ;

-- ----------------------------
-- View structure for vlogin
-- ----------------------------
DROP VIEW IF EXISTS `vlogin`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER  VIEW `vlogin` AS select count(`logindata`.`guid`) AS `num`,date_format(`logindata`.`lgdate`,'%Y-%m-%d') AS `lgdate`,`logindata`.`gid` AS `gid`,`logindata`.`pid` AS `pid` from `logindata` group by `logindata`.`guid`,date_format(`logindata`.`lgdate`,'%Y-%m-%d') ;

-- ----------------------------
-- View structure for vtlift
-- ----------------------------
DROP VIEW IF EXISTS `vtlift`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER  VIEW `vtlift` AS select `logindata`.`guid` AS `guid`,date_format(`logindata`.`lgdate`,'%Y-%m-%d') AS `lgdate`,`logindata`.`account` AS `account`,`logindata`.`uid` AS `uid`,`logindata`.`gid` AS `gid`,`logindata`.`pid` AS `pid` from (`logindata` join `userdata` on((`logindata`.`guid` = `userdata`.`guid`))) where (`userdata`.`lgdate` = (date_format(`logindata`.`lgdate`,'%Y-%m-%d') + interval -(1) day)) group by date_format(`logindata`.`lgdate`,'%Y-%m-%d'),`logindata`.`guid` ;

-- ----------------------------
-- View structure for vtlift7day
-- ----------------------------
DROP VIEW IF EXISTS `vtlift7day`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER  VIEW `vtlift7day` AS select `logindata`.`guid` AS `guid`,date_format(`logindata`.`lgdate`,'%Y-%m-%d') AS `lgdate`,`logindata`.`account` AS `account`,`logindata`.`uid` AS `uid`,`logindata`.`gid` AS `gid`,`logindata`.`pid` AS `pid` from (`logindata` join `userdata` on((`logindata`.`guid` = `userdata`.`guid`))) where (`userdata`.`lgdate` = (date_format(`logindata`.`lgdate`,'%Y-%m-%d') + interval -(7) day)) group by date_format(`logindata`.`lgdate`,'%Y-%m-%d'),`logindata`.`guid` ;

-- ----------------------------
-- Procedure structure for P_pg_Count
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_pg_Count`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `P_pg_Count`(IN gid VARCHAR(30),IN pid VARCHAR(30),INOUT upays INT,OUT gName VARCHAR(30) character set utf8,OUT pName VARCHAR(30) character set utf8,OUT onlineDate VARCHAR(30),OUT users INT,OUT tallpay FLOAT,OUT payRate FLOAT,OUT arpu FLOAT,OUT arppu FLOAT)
BEGIN  
  SET @gid = gid;  
  SET @pid = pid;

  SELECT gameNa,pidNa,gameOnline INTO gName,pName,onlineDate FROM gameplat WHERE gameplat.`gid`=@gid AND gameplat.`pid`=@pid;
		 #累计用户数量
	SELECT COUNT(*) INTO users FROM userdata WHERE userdata.`gid`=gid AND userdata.`pid`=pid;
  #累计充值总数
	SELECT SUM(Amount)INTO tallpay FROM paymement WHERE paymement.`gid`=gid AND paymement.`pid`=pid AND paymement.`status`=1;
	IF tallpay IS NULL THEN
		SET tallpay = 0;
  END IF;
	#充值过的用户数量
	SET upays = (SELECT COUNT(*) FROM (select * FROM paymement WHERE paymement.`gid`=gid AND paymement.`pid`=pid AND paymement.`status`=1 GROUP BY paymement.`uuid`)AS tempGroup);
	IF upays IS NULL THEN
		SET upays = 0;
  END IF;

	IF (users>0) THEN #用户付费率
		SET payRate = upays/users;
	ELSE
		SET payRate = 0;
  END IF;
  
	IF(users>0) THEN #ARPU
		SET arpu = tallpay/users;
	ELSE
		SET arpu = 0;
  END IF;

	IF(upays>0) THEN #ARPPU
		SET arppu = tallpay/upays;
	ELSE
		SET arppu = 0;
  END IF;
END
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for P_view_count
-- ----------------------------
DROP PROCEDURE IF EXISTS `P_view_count`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `P_view_count`(IN gid VARCHAR(30),IN pid VARCHAR(30),IN startTime VARCHAR(10),IN endTime VARCHAR(10))
BEGIN 
	SELECT DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AS lgdate,
	#新用户
	(SELECT COUNT(*) FROM userdata WHERE userdata.lgdate= DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ))as newUser,
	 #活跃用户
	(SELECT COUNT(*) FROM vlogin WHERE vlogin.lgdate = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND vlogin.num>=1)AS actUser,
	 #老用户
  (SELECT COUNT(*) FROM vguser,userdata WHERE vguser.uid= userdata.uid AND vguser.lgdate = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND userdata.lgdate < DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" )) AS oldUser,
	#次日留存
	((SELECT COUNT(*) FROM vtlift WHERE vtlift.lgdate = DATE_FORMAT(date_add(logindata.lgdate,interval 1 day),"%Y-%m-%d"))/
	(SELECT COUNT(*) FROM userdata WHERE userdata.lgdate= DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" )))AS time_left,
  #7日留存
	((SELECT COUNT(*) FROM vtlift7day WHERE vtlift7day.lgdate = DATE_FORMAT(date_add(logindata.lgdate,interval 1 day),"%Y-%m-%d"))/
	(SELECT COUNT(*) FROM userdata WHERE userdata.lgdate= DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" )))AS day7_left,
	#当日流水
	(SELECT SUM(paymement.Amount) FROM paymement WHERE DATE_FORMAT(paymement.createDate, "%Y-%m-%d" ) = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND paymement.`status`=1) AS money,
	#当日付费率
	((SELECT SUM(paymement.Amount) FROM paymement WHERE DATE_FORMAT(paymement.createDate, "%Y-%m-%d" ) = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND paymement.`status`=1)
	/(SELECT COUNT(*) FROM userdata WHERE userdata.lgdate <= DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ))) AS payRate,
	#当日apur
	((SELECT SUM(paymement.Amount) FROM paymement WHERE DATE_FORMAT(paymement.createDate, "%Y-%m-%d" ) = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND paymement.`status`=1)
	/(SELECT COUNT(*) FROM vlogin WHERE vlogin.lgdate = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND vlogin.num>=1))AS ARPU,
	#当日arppu
	((SELECT SUM(paymement.Amount) FROM paymement WHERE DATE_FORMAT(paymement.createDate, "%Y-%m-%d" ) = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND paymement.`status`=1)
	 /(SELECT COUNT(*) FROM paymement WHERE DATE_FORMAT(paymement.createDate, "%Y-%m-%d" ) = DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) AND paymement.`status`=1 GROUP BY paymement.guid)) AS ARPPU
	FROM logindata
  WHERE DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" ) BETWEEN startTime AND endTime GROUP BY DATE_FORMAT(logindata.lgdate, "%Y-%m-%d" );
END
;;
DELIMITER ;
