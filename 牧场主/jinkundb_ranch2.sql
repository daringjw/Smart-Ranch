

1 所有的分表逻辑写完

2 redis引入

3 设备上报数据解析

4 文档的编写


图片服务器okhttp年后写

多包的拆分，滑动
*  增加按牧场进行拆分的设备表， 便于进行统计和查询。在此表进行电量的更新


仪表盘界面可以有 按牛羊进行的个数统计， 按旗、苏木进行的统计，低电设备的统计

位置信息的统计可有：


牧场增加视频源字段

牧场表 存 牧场主账户、苏木id、旗id
其他分表 存 牧场id，苏木id，旗id
需要一个热数据表 记录牧场绑定设备的羊、牛、马、猪、鸡、鹿的个数，认领羊、牛、马、猪、鸡、鹿的个数（热数据 实时加减), 设备总数
每天晚上在用定时任务同步一下

=====================================
需要理清绑定与解绑定的流程
  1 手动解绑定， app提供对应的按钮-扫码->解绑定
  2 自动解绑定，屠宰场完成 装箱步骤后?? 自动自动解绑定
  3 打印 电子档案二维码 功能， 在设备没有绑定到新的牲畜时，从设备表的last_livestock_id获取相关牲畜的电子档案信息，没有则返回无数据或过期标识
  4 打印 电子档案二维码 功能,  在设备已经绑定到新的牲畜时，从设备表的livestock_id获取相关牲畜的电子档案信息，没有则返回无数据标识

=========================================================
?deviceNo=&livestockId=&ranchId=
如果没有牲畜id则通过设备编号到牲畜表查牲畜

=========================================================
图片服务器
web服务器采用okhttp方式将图片上传至图片服务器，
app或web应用直接访问图片服务器

=========================================================
增加google的经纬度字段
mqtt + google  group buffer

cpu 16核，32G内存， 性能瓶颈主要在 数据解析。
loadruner 模拟10W用户很难， 测试讲师提到 曾经用过10个机器去报loadrunner进行压力测试。

-- ----------------------------
-- 创建数据库是需要创建的表
-- 1 t_livestock_[0,1,2,...,197,198,199]
-- 2 t_livestock_photo_[0,1,2,...,197,198,199]
-- 3 t_position_instance_[0,1,2,...,197,198,199]
-- 4 t_device_[00,01,02,...,97,98,99]
-- 5 t_ranch_device_[0,1,2,...,197,198,199]
-- ----------------------------


-- ----------------------------
-- Table structure for t_livestock
-- 牲畜表
-- 需要分表, 采用余数方式： var remainder = ranchId/200 
-- 分表规则：t_livestock_[0,1,2,...,197,198,199]
-- 200张表，按1000w的牲畜总量计算，每张表平均存5W调记录
--
-- 清除原则：1 || (牲畜已经与设备解绑定 并且  解绑时间至少小于当前时间1年)
--           2 || 记录创建时间小于当前时间5年
-- 每月第3天凌晨1点零3分执行清除任务
-- ----------------------------
DROP TABLE IF EXISTS `t_livestock`;
CREATE TABLE `t_livestock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '纪录id',
  `device_no` varchar(32) NOT NULL COMMENT '设备编号(必填项)',
  `device_type` int(2) NOT NULL DEFAULT 2 COMMENT '设备类型(根据设备编号获取)；1：头羊网关，2：小羊耳标，3：牛，4：马',
  `device_no_history` varchar(32) DEFAULT NULL COMMENT '历史设备编??, 用于牲畜解绑设备后记录删除前设备编号的留存',
  `weight` float(4,1) DEFAULT NULL COMMENT '体重，以Kg为单位',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡, 6:鹿',
  `variety` int(4) NOT NULL DEFAULT 100 COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛??, 501：草原绿鸟鸡, 601:梅花鹿',
  `health` int(1) NOT NULL DEFAULT 3 COMMENT '健康情况; 1:优, 2:良, 3：好 ...',
  `color` int(2) NOT NULL DEFAULT 2 COMMENT '颜色; 1:黑白, 2:白色, 3:花色, 4:红棕色, 5:黄白花, 6:淡红白花',
  `img_url` varchar(256) DEFAULT NULL COMMENT '牲畜照片(没有图片时返回一个默认图片)',
  `img_url_qr_code` varchar(256) DEFAULT NULL COMMENT '二维码图片链接',
  `is_claimed` int(1) NOT NULL DEFAULT 0 COMMENT '是否被认领: 0: 否，1：是', 
  `phase` int(2) NOT NULL DEFAULT 1 COMMENT '状态; 1: 出生or接羔(羊)，2：打疫苗，3：剪羊毛(羊)，4 宰杀，5 装箱',
  `bind_status` int(2) NOT NULL DEFAULT 0 COMMENT '绑定状态; 0: 默认状态，1：绑定，2：解绑定',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  `img_url_time` datetime DEFAULT NULL COMMENT '牲畜照片上传时间,  yyyy-MM-dd hh:mm:ss',
  `unbind_time` datetime DEFAULT NULL COMMENT '牲畜与设备解绑时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`id`),
  KEY `key_livestock_device_no` (`device_no`),
  KEY `key_livestock_unbind_time` (`unbind_time`),
  KEY `key_livestock_livestock_type` (`livestock_type`),
  KEY `key_livestock_ranch_id` (`ranch_id`),
  KEY `key_livestock_qi_id` (`qi_id`),
  KEY `key_livestock_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牲畜表';

-- ----------------------------
-- Table structure for t_animal_type
-- 牲畜类型表
-- ----------------------------
DROP TABLE IF EXISTS `t_animal_type`;
CREATE TABLE `t_animal_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '纪录id',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡, 6:鹿',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牲畜类型表';

-- ----------------------------
-- Table structure for t_livestock_photo
-- 牲畜图片表 打疫苗、 剪羊毛、屠宰前、装箱前
-- 需要分表, 采用余数方式： var remainder = ranchId/200 
-- 分表规则：t_livestock_photo_[0,1,2,...,197,198,199]
-- 200张表，按1000w的牲畜总量计算，每头牲畜10张图片计算，每张表平均存50W条记录
-- 
-- 清除原则：1 || (牲畜已经与设备解绑定 并且  解绑时间至少小于当前时间1年)
--           2 || 记录创建时间小于当前时间5年
-- 每月第3天凌晨3点零3分执行清除任务
-- ----------------------------
DROP TABLE IF EXISTS `t_livestock_photo`;
CREATE TABLE `t_livestock_photo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '纪录id',
  `livestock_id` bigint(20) NOT NULL COMMENT '牲畜id(必填项)',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡, 6:鹿',
  `variety` int(4) NOT NULL DEFAULT 100 COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡, 601:梅花鹿',
  `device_no` varchar(32) NOT NULL COMMENT '设备编号(必填项)',
  `phase` int(2) DEFAULT '1' COMMENT '阶段；1，接羔，2 打疫苗，3 剪羊毛，4 屠宰前 5 装箱前',
  `img_url` varchar(256) NOT NULL COMMENT '图片相对路径(必填项)',
  `bind_status` int(2) NOT NULL DEFAULT 0 COMMENT '绑定状态; 0: 默认状态，1：绑定，2：解绑定',
  `unbind_time` datetime DEFAULT NULL COMMENT '牲畜与设备解绑时间,  yyyy-MM-dd hh:mm:ss',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`id`),
  KEY `key_photo_livestock_id` (`livestock_id`),
  KEY `key_photo_device_no` (`device_no`),
  KEY `key_photo_unbind_time` (`unbind_time`),
  KEY `key_photo_livestock_type` (`livestock_type`),
  KEY `key_photo_ranch_id` (`ranch_id`),
  KEY `key_photo_qi_id` (`qi_id`),
  KEY `key_photo_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牲畜图片表';


-- ----------------------------
-- Table structure for t_livestock_position_instance
-- 牲畜实时位置表
-- 需要分表, 采用余数方式： var remainder = ranchId%200 
-- 分表规则：t_livestock_position_instance_[0,1,2,...,197,198,199]
-- 200张表，按1000w的牲畜总量计算，每张表平均存5W条记录
-- 
-- 清除原则：1 牲畜与设备解绑定后立刻清除
-- -----------------------------
DROP TABLE IF EXISTS `t_livestock_position_instance`;
CREATE TABLE `t_livestock_position_instance` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号(必填项)',
  `device_type` int(2) DEFAULT NULL COMMENT '设备类型，1：头羊网关, 2：小羊耳标，3：牛 4：马',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `livestock_type` int(2) DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡, 6：鹿',
  `variety` int(4) DEFAULT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡, 601:梅花鹿',
  `longtitude_gps` double DEFAULT NULL COMMENT 'GPS经度',
  `longtitude_baidu` double DEFAULT NULL COMMENT '百度经度',
  `longtitude_google` double DEFAULT NULL COMMENT 'google经度',
  `latitude_gps` double DEFAULT NULL COMMENT 'GPS纬度',
  `latitude_baidu` double DEFAULT NULL COMMENT '百度纬度',
  `latitude_google` double DEFAULT NULL COMMENT 'goole纬度',
  `address` varchar(256) DEFAULT NULL COMMENT '定位地址',
  `alarm_type` int(2) NOT NULL DEFAULT 0 COMMENT '报警类型；0: 非报警 1：报警',
  `report_time` datetime DEFAULT NULL COMMENT '数据上报时间,  yyyy-MM-dd hh:mm:ss',
  `record_time` datetime DEFAULT NULL COMMENT '记录变化时间,  yyyy-MM-dd hh:mm:ss',
  `device_no_header` varchar(64) DEFAULT NULL COMMENT '头羊设备编号',
  `lamb_device_no` varchar(1024) DEFAULT NULL COMMENT '小羊设备编号，逗号分隔',
  `lamb_num` int(11) DEFAULT NULL COMMENT '上报的小羊数量',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`device_no`),
  KEY `key_position_ins_device_no` (`device_no`),
  KEY `key_position_ins_livestock_id` (`ranch_id`),
  KEY `key_position_ins_ranch_id` (`ranch_id`),
  KEY `key_position_ins_qi_id` (`qi_id`),
  KEY `key_position_ins_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牲畜实时位置表';

-- ----------------------------
-- Table structure for t_people_position_instance
-- 人员实时位置表
-- -----------------------------
DROP TABLE IF EXISTS `t_people_position_instance`;
CREATE TABLE `t_people_position_instance` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号(必填项)',
  `device_type` varchar(32) DEFAULT NULL COMMENT '设备类型，2A,2C...',
  `user_id` bigint(20) DEFAULT NULL COMMENT '人员id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '人员名称',
  `user_type` varchar(32) DEFAULT NULL COMMENT '人员类型; 技术员，羊倌，饲养员...',
  `longtitude_gps` double DEFAULT NULL COMMENT 'GPS经度',
  `longtitude_baidu` double DEFAULT NULL COMMENT '百度经度',
  `longtitude_google` double DEFAULT NULL COMMENT 'google经度',
  `latitude_gps` double DEFAULT NULL COMMENT 'GPS纬度',
  `latitude_baidu` double DEFAULT NULL COMMENT '百度纬度',
  `latitude_google` double DEFAULT NULL COMMENT 'goole纬度',
  `address` varchar(256) DEFAULT NULL COMMENT '定位地址',
  `alarm_type` int(2) NOT NULL DEFAULT 0 COMMENT '报警类型；0: 非报警 1：报警',
  `time_zone` int(2) DEFAULT NULL DEFAULT 0 COMMENT '时区',
  `report_time` datetime DEFAULT NULL COMMENT '数据上报时间,  yyyy-MM-dd hh:mm:ss',
  `record_time` datetime DEFAULT NULL COMMENT '记录变化时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  PRIMARY KEY (`device_no`),
  KEY `key_position_ins_device_no` (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '人员实时位置表';

-- ----------------------------
-- Table structure for t_people_position_history
-- 人员位置历史表
-- -----------------------------
DROP TABLE IF EXISTS `t_people_position_history`;
CREATE TABLE `t_people_position_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_no` varchar(32) NOT NULL COMMENT '设备编号(必填项)',
  `device_type` varchar(32) DEFAULT NULL COMMENT '设备类型，2A,2C...',
  `user_id` bigint(20) DEFAULT NULL COMMENT '人员id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '人员名称',
  `user_type` varchar(32) DEFAULT NULL COMMENT '人员类型; 技术员，羊倌，饲养员...',
  `longtitude_gps` double DEFAULT NULL COMMENT 'GPS经度',
  `longtitude_baidu` double DEFAULT NULL COMMENT '百度经度',
  `longtitude_google` double DEFAULT NULL COMMENT 'google经度',
  `latitude_gps` double DEFAULT NULL COMMENT 'GPS纬度',
  `latitude_baidu` double DEFAULT NULL COMMENT '百度纬度',
  `latitude_google` double DEFAULT NULL COMMENT 'goole纬度',
  `address` varchar(256) DEFAULT NULL COMMENT '定位地址',
  `alarm_type` int(2) NOT NULL DEFAULT 0 COMMENT '报警类型；0: 非报警 1：报警',
  `time_zone` int(2) DEFAULT NULL DEFAULT 0 COMMENT '时区',
  `report_time` datetime DEFAULT NULL COMMENT '数据更新时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '人员位置历史表';

-- ----------------------------
-- Table structure for t_animal_rail_info
-- 电子围栏
-- -----------------------------
DROP TABLE IF EXISTS `t_animal_rail_info`;
CREATE TABLE `t_animal_rail_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '管理员名称',
  `rail_name` varchar(50) DEFAULT NULL COMMENT '区域名称',
  `rail_type` int(2) DEFAULT NULL COMMENT '区域类型',
  `rail_points` varchar(255) DEFAULT NULL COMMENT '区域坐标点集合',
  `message` varchar(32) DEFAULT NULL COMMENT '推送信息',
  `is_push` int(2) NOT NULL DEFAULT 0 COMMENT '是否推送消息 0否 1是',
  `map_level` int(2) DEFAULT NULL COMMENT '地图级别',
  `map_type` int(2) DEFAULT NULL COMMENT '地图类型',
  `ranch_id` int(8) NOT NULL DEFAULT 0 COMMENT '牧场id,(必填项)',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '电子围栏';

-- ----------------------------
-- Table structure for t_device
-- 设备表
-- 需要分表, 采用余数方式： var cutFlag = 设备编号末2位 
-- 分表规则：t_device_[00,01,02,...,97,98,99]
-- 200张表，按1000w的牲畜总量计算，每张表平均存10W调记录
--
-- 清除原则：bind_status不为1的设备记录可以在牧场设备管理列表中删除
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号,(必填项)',
  `device_type` int(4) DEFAULT NULL COMMENT '设备类型(根据设备编号获取)；1：头羊网关，2：小羊耳标，3：牛，4：马',
  `battery` float(4,2) NOT NULL DEFAULT '0.00' COMMENT '电量V',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡',
  `variety` int(4) DEFAULT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡, 601:梅花鹿',
  `bind_status` int(2) NOT NULL DEFAULT 0 COMMENT '绑定状态; 0: 默认状态，1：绑定，2：解绑定',
  `bind_num` int(4) DEFAULT 0 COMMENT '绑定次数',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `last_bind_time` datetime DEFAULT NULL COMMENT '上一次绑定时间,  yyyy-MM-dd hh:mm:ss',
  `last_data_time` datetime DEFAULT NULL COMMENT '上一次数据上报时间,  yyyy-MM-dd hh:mm:ss',
  `last_record_time` datetime DEFAULT NULL COMMENT '上一次记录更新时间,  yyyy-MM-dd hh:mm:ss',
  `need_config_server` int(1) NOT NULL DEFAULT 1 COMMENT '是否需要更新上报服务器的地址; 0: 不需要, 1: 需要',
  `need_config_livestock_param` int(1) NOT NULL DEFAULT 1 COMMENT '是否需要更新大牲畜的上报参数; 0: 不需要, 1: 需要',
  `need_config_goat_param` int(1) NOT NULL DEFAULT 1 COMMENT '是否需要更新头羊的上报参数; 0: 不需要, 1: 需要',
  `config_livestock_param` varchar(1024) DEFAULT NULL COMMENT '自定义大牲畜设备配置, 16进制串',
  `config_goat_param` varchar(1024) DEFAULT NULL COMMENT '自定义头羊设备配置, 16进制串',
  `last_livestock_id` bigint(20) DEFAULT NULL COMMENT '上一个牲畜id(用于打印已宰杀牲畜的二维码)',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`device_no`),
  KEY `key_device_last_data_time` (`last_data_time`),
  KEY `key_device_ranch_id` (`ranch_id`),
  KEY `key_device_qi_id` (`qi_id`),
  KEY `key_device_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '设备表';


-- ----------------------------
-- Table structure for t_people_device
-- 人员设备表
-- ----------------------------
DROP TABLE IF EXISTS `t_people_device`;
CREATE TABLE `t_people_device` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号,(必填项)',
  `device_type` varchar(32) DEFAULT NULL COMMENT '设备类型，2A，2C ...',
  `battery` float(4,2) NOT NULL DEFAULT '0.00' COMMENT '电量V',
  `user_id` bigint(20) DEFAULT NULL COMMENT '人员id',
  `bind_status` int(2) NOT NULL DEFAULT 1 COMMENT '绑定状态; 0: 绑定，1：未绑定',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `bind_time` datetime DEFAULT NULL COMMENT '绑定时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  PRIMARY KEY (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '人员设备表';

-- ----------------------------
-- Table structure for t_pasture_device
-- 牧场设备表
-- ----------------------------
DROP TABLE IF EXISTS `t_pasture_device`;
CREATE TABLE `t_pasture_device` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `device_name` varchar(32) DEFAULT NULL COMMENT '设备名称',
  `device_function` varchar(32) DEFAULT NULL COMMENT '设备功能',
  `device_count` int(8) DEFAULT 0 COMMENT '设备数量',
  `device_use_time` varchar(32) DEFAULT NULL COMMENT '设备使用时间',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牧场设备表';

-- ----------------------------
-- Table structure for t_user_info
-- 牧场人员表
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(32) DEFAULT NULL COMMENT '人员名称',
  `user_type` varchar(32) DEFAULT NULL COMMENT '人员类型',
  `sex` int(1) DEFAULT '1' COMMENT '性别; 0:女， 1：男',
  `age` int(2) DEFAULT NULL COMMENT '年龄',
  `id_card` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `cellphone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `device_no` varchar(30) DEFAULT NULL COMMENT '人员设备编号',
  `device_type` varchar(20) DEFAULT NULL COMMENT '人员设备类型',
  `work_address` varchar(256) DEFAULT NULL COMMENT '工作地址',
  `home_address` varchar(256) DEFAULT NULL COMMENT '住址',
  `is_receive_alarm` int(2) DEFAULT NULL COMMENT '是否接收报警',
  `link_name` varchar(32) DEFAULT NULL COMMENT '联系人名称',
  `link_cellphone` varchar(11) DEFAULT NULL COMMENT '联系人电话',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牧场人员表';

-- ----------------------------
-- Table structure for t_ranch_device
-- 牧场设备表-按牧场维度进行拆分的设备表
-- 需要分表, 采用余数方式： var remainder = ranchId/200 
-- 分表规则：t_ranch_device_[0,1,2,...,197,198,199]
-- 200张表，按1000w的牲畜总量计算，每张表平均存5W条记录
--
-- 清除原则：bind_status不为1的设备记录可以在牧场设备管理列表中删除, 同时删除t_device表中的对应设备
-- ----------------------------
DROP TABLE IF EXISTS `t_ranch_device`;
CREATE TABLE `t_ranch_device` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号,(必填项)',
  `device_type` int(4) DEFAULT NULL COMMENT '设备类型(根据设备编号获取)；1：头羊网关，2：小羊耳标，3：大牲畜',
  `battery` float(4,2) NOT NULL DEFAULT '0.00' COMMENT '电量V',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡',
  `variety` int(4) DEFAULT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡, 601:梅花鹿',
  `bind_status` int(2) NOT NULL DEFAULT 0 COMMENT '绑定状态; 0: 默认状态，1：绑定，2：解绑定',
  `bind_num` int(4) NOT NULL DEFAULT 0 COMMENT '绑定次数',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `last_bind_time` datetime DEFAULT NULL COMMENT '上一次绑定时间,  yyyy-MM-dd hh:mm:ss',
  `last_data_time` datetime DEFAULT NULL COMMENT '上一次数据上报时间,  yyyy-MM-dd hh:mm:ss',
  `last_record_time` datetime DEFAULT NULL COMMENT '上一次记录更新时间,  yyyy-MM-dd hh:mm:ss',
  `last_livestock_id` bigint(20) DEFAULT NULL COMMENT '上一个牲畜id(用于打印已宰杀牲畜的二维码)',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`device_no`),
  KEY `key_ranch_device_last_data_time` (`last_data_time`),
  KEY `key_ranch_device_ranch_id` (`ranch_id`),
  KEY `key_ranch_device_qi_id` (`qi_id`),
  KEY `key_ranch_device_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牧场设备表';



-- ----------------------------
-- Table structure for t_ranch_stat_info
-- 牧场热点数据：设备数，绑定设备的牛、羊数
-- ----------------------------
DROP TABLE IF EXISTS `t_ranch_stat_info`;
CREATE TABLE `t_ranch_stat_info` (
  `ranch_id` int(8) NOT NULL COMMENT '牧场ID',
  `ranch_name` varchar(64)  COMMENT '牧场名称',
  `num_device_1` int(8) NOT NULL  DEFAULT '0' COMMENT '设备数-头羊网关', 
  `num_device_2` int(8) NOT NULL  DEFAULT '0' COMMENT '设备数-小羊耳标', 
  `num_device_3` int(8) NOT NULL  DEFAULT '0' COMMENT '设备数-牛', 
  `total_num_livestock_1` int(8) NOT NULL DEFAULT '0' COMMENT '绑定设备的头羊的个数',
  `total_num_livestock_2` int(8) NOT NULL DEFAULT '0' COMMENT '绑定设备的小羊的个数',
  `total_num_livestock_3` int(8) NOT NULL DEFAULT '0' COMMENT '绑定设备的牛的个数',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`ranch_id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='牧场热点数据';


-- ----------------------------
-- Table structure for t_ranch_stat_variety
-- 牧场热点数据：各个品种的牲畜数量统计
-- ----------------------------
DROP TABLE IF EXISTS `t_ranch_stat_variety`;
CREATE TABLE `t_ranch_stat_variety` (
  `ranch_id` int(8) NOT NULL COMMENT '牧场ID',
  `livestock_type` int(2) NOT NULL DEFAULT '1' COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡, 6:鹿',
  `variety` int(4) NOT NULL DEFAULT '100' COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡, 601:梅花鹿',
  `num` int(8) NOT NULL  DEFAULT '0' COMMENT '牲畜数量', 
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`ranch_id`, `livestock_type`, `variety`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='牧场品种统计数据';

-- ----------------------------
-- Table structure for t_county_info
-- 旗表
-- ----------------------------
DROP TABLE IF EXISTS `t_county_info`;
CREATE TABLE `t_county_info` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(32) DEFAULT NULL COMMENT '简称',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旗';

-- ----------------------------
-- Table structure for t_towns_info
-- 苏木表
-- ----------------------------
DROP TABLE IF EXISTS `t_towns_info`;
CREATE TABLE `t_towns_info` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(32) DEFAULT NULL COMMENT '简称',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `img_url` varchar(2048) DEFAULT NULL COMMENT '苏木图片路径',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='苏木';

-- ----------------------------
-- Table structure for t_kill_factory_info
-- 屠宰场表
-- ----------------------------
DROP TABLE IF EXISTS `t_kill_factory_info`;
CREATE TABLE `t_kill_factory_info` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `address` varchar(256) DEFAULT NULL COMMENT '地址',
  `name` varchar(128) DEFAULT NULL COMMENT '名称',
  `short_name` varchar(32) DEFAULT NULL COMMENT '简称',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='屠宰场';


-- ----------------------------
-- Table structure for t_ranch
-- 牧场表, 牧场主登录时处理经纬度，从设备中获取，如果没有使用默认的。
-- ----------------------------
DROP TABLE IF EXISTS `t_ranch`;
CREATE TABLE `t_ranch` (
  `ranch_id` int(8) NOT NULL AUTO_INCREMENT COMMENT '牧场ID',
  `name` varchar(32) NOT NULL COMMENT '牧场名称',
  `area_address` varchar(256) DEFAULT NULL  COMMENT '市/区地址',
  `address` varchar(256) DEFAULT NULL  COMMENT '牧场地址',
  `rancher_account` varchar(32) DEFAULT NULL COMMENT '牧场主账号',
  `rancher_name` varchar(32) DEFAULT NULL COMMENT '牧场主名字',
  `rancher_phone` varchar(16) DEFAULT NULL COMMENT '牧场主手机号',
  `acreage` double DEFAULT NULL COMMENT '面积',
  `introduce_animal_count` int(11) DEFAULT '0' COMMENT '牲畜数量描述',
  `introduce_animal_type` varchar(512) DEFAULT NULL COMMENT '牲畜种类描述',
  `introduce_meadow` varchar(512) DEFAULT NULL COMMENT '草场种类描述',
  `introduce_river` varchar(512) DEFAULT NULL COMMENT '是否有河流',
  `is_fence_Close` varchar(512) DEFAULT NULL COMMENT '围栏关闭状态',
  `introduce_native_product` varchar(512) DEFAULT NULL COMMENT '特产描述',
  `img_url` varchar(2048) DEFAULT NULL COMMENT '牧场介绍图片链接,多个图片用,号分隔',
  `ranch_img_url` varchar(2048) DEFAULT NULL COMMENT '牧场介绍生活照,多个图片用,号分隔',
  `video_url` varchar(2048) DEFAULT NULL COMMENT '牧场介绍视频链接,多个视频用,号分隔',
  `introduce` text DEFAULT NULL COMMENT '牧场介绍',
  `longtitude_gps` double DEFAULT NULL COMMENT 'GPS经度',
  `longtitude_baidu` double DEFAULT NULL COMMENT '百度经度',
  `longtitude_google` double DEFAULT NULL COMMENT 'google经度',
  `lantitude_gps` double DEFAULT NULL COMMENT 'GPS纬度',
  `lantitude_baidu` double DEFAULT NULL COMMENT '百度纬度',
  `lantitude_google` double DEFAULT NULL COMMENT 'google纬度',
  `num_sort` int(4) DEFAULT '9999' COMMENT '排序', 
  `status` int(2) DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '记录更新时间,  yyyy-MM-dd hh:mm:ss',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`ranch_id`),
  KEY `key_ranch_qi_id` (`qi_id`),
  KEY `key_ranch_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='牧场表';

-- ----------------------------
-- Table structure for t_livestock_photographic_video
-- 认领牲畜拍照录像表
-- ----------------------------
DROP TABLE IF EXISTS `t_livestock_photographic_video`;
CREATE TABLE `t_livestock_photographic_video` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `img_url` varchar(256) DEFAULT NULL COMMENT '牲畜照片(没有图片时返回一个默认图片)',
  `video_url` varchar(256) DEFAULT NULL COMMENT '牲畜录像(没有视频时返回一个默认视频)',
  `member_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `cellphone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `is_photographic` int(1) NOT NULL DEFAULT 0 COMMENT '请求拍照状态: 0: 请求中，1：已完成', 
  `is_video` int(1) NOT NULL DEFAULT 0 COMMENT '请求拍照状态: 0: 请求中，1：已完成',
  `businessType` int(1) NOT NULL DEFAULT 0 COMMENT '请求业务类型: 1: 拍照请求，2：录像请求，3：其它业务待定', 
  `create_time` datetime DEFAULT NULL COMMENT '发布时间,  yyyy-MM-dd hh:mm:ss',
  `claim_time` datetime DEFAULT NULL COMMENT '认领时间,  yyyy-MM-dd hh:mm:ss',
  `birth_time` datetime DEFAULT NULL COMMENT '录入时间,  yyyy-MM-dd hh:mm:ss',
  `photographic_time` datetime DEFAULT NULL COMMENT '请求拍照时间,  yyyy-MM-dd hh:mm:ss',
  `video_time` datetime DEFAULT NULL COMMENT '请求录像时间,  yyyy-MM-dd hh:mm:ss',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认领牲畜拍照录像表';

-- ----------------------------
-- Table structure for t_livestock_claim
-- 认领牲畜表，Livestock
-- ----------------------------
DROP TABLE IF EXISTS `t_livestock_claim`;
CREATE TABLE `t_livestock_claim` (
  `device_no` varchar(32) NOT NULL COMMENT '设备编号',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `weight` float(4,1) DEFAULT NULL COMMENT '体重，以Kg为单位',
  `price` float(4,1) DEFAULT NULL COMMENT '金额，以元为单位',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡',
  `variety` int(4) DEFAULT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡 ...',
  `livestock_name` varchar(32) DEFAULT NULL COMMENT '牲畜昵称; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡 ...',
  `health` int(1) NOT NULL DEFAULT 3 COMMENT '健康情况; 1:优, 2:良, 3：好 ...',
  `color` int(2) NOT NULL DEFAULT 2 COMMENT '颜色; 1:黑白, 2:白色, 3:花色, 4:红棕色, 5:黄白花, 6:淡红白花',
  `livestock_details` varchar(256) DEFAULT NULL COMMENT '牲畜详情',
  `life_time` int(2) DEFAULT NULL COMMENT '牲畜寿命',
  `characteristics` varchar(128) DEFAULT NULL COMMENT '牲畜特点',
  `img_url` varchar(256) DEFAULT NULL COMMENT '牲畜照片(没有图片时返回一个默认图片)',
  `livestock_img_url` varchar(256) DEFAULT NULL COMMENT '牲畜生活照片(没有图片时返回一个默认图片)',  
  `video_url` varchar(256) DEFAULT NULL COMMENT '牲畜录像(没有视频时返回一个默认视频)',
  `member_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `cellphone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `is_claimed` int(1) NOT NULL DEFAULT 0 COMMENT '是否被认领: 0: 否，1：是', 
  `is_photographic` int(1) NOT NULL DEFAULT 0 COMMENT '请求拍照状态: 0: 请求中，1：已完成', 
  `businessType` int(1) NOT NULL DEFAULT 0 COMMENT '请求业务类型: 1: 拍照请求，2：录像请求，3：其它业务待定', 
  `is_video` int(1) NOT NULL DEFAULT 0 COMMENT '请求拍照状态: 0: 请求中，1：已完成',
  `create_time` datetime DEFAULT NULL COMMENT '牲畜登记时间,  yyyy-MM-dd hh:mm:ss',
  `claim_time` datetime DEFAULT NULL COMMENT '认领时间,  yyyy-MM-dd hh:mm:ss',
  `finish_time` datetime DEFAULT NULL COMMENT '认领结束,  yyyy-MM-dd hh:mm:ss',
  `birth_time` datetime DEFAULT NULL COMMENT '发布时间,  yyyy-MM-dd hh:mm:ss',
  `photographic_time` datetime DEFAULT NULL COMMENT '请求拍照时间,  yyyy-MM-dd hh:mm:ss',
  `video_time` datetime DEFAULT NULL COMMENT '请求录像时间,  yyyy-MM-dd hh:mm:ss',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`device_no`),
  KEY `key_livestock_claim_ranch_id` (`ranch_id`),
  KEY `key_livestock_claim_device_no` (`device_no`),
  KEY `key_livestock_claim_livestock_id` (`livestock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认领牲畜表';

-- ----------------------------
-- Table structure for t_alarm_cache
-- 人员报警表
-- ----------------------------
DROP TABLE IF EXISTS `t_alarm_cache`;
CREATE TABLE `t_alarm_cache` (
	`device_no` varchar(32) NOT NULL COMMENT '报警设备编号',
  `alarm_location` varchar(255) DEFAULT NULL COMMENT '报警位置',
  `alarm_time` datetime DEFAULT NULL COMMENT '报警时间,  yyyy-MM-dd hh:mm:ss',
  `alarm_type` int(2) DEFAULT NULL COMMENT '报警类型；1：普通，2：SOS',
  `cellphone` varchar(11) DEFAULT NULL COMMENT '报警人手机号',
  `longtitude_gps` double DEFAULT NULL COMMENT 'GPS经度',
  `longtitude_baidu` double DEFAULT NULL COMMENT '百度经度',
  `longtitude_google` double DEFAULT NULL COMMENT 'google经度',
  `lantitude_gps` double DEFAULT NULL COMMENT 'GPS纬度',
  `lantitude_baidu` double DEFAULT NULL COMMENT '百度纬度',
  `lantitude_google` double DEFAULT NULL COMMENT 'google纬度',
  `notice_people_name` varchar(512) DEFAULT NULL COMMENT '报警通知人名称',
  `notice_user_id` varchar(512) DEFAULT NULL COMMENT '报警通知人编号',
  `alarm_user_name` varchar(30) DEFAULT NULL COMMENT '报警人名称',
  `alarm_user_type` varchar(30) DEFAULT NULL COMMENT '报警人类型',
  `time_zone` double DEFAULT NULL COMMENT '报警时区',
  `alarm_user_id` varchar(50) DEFAULT NULL COMMENT '报警人编号',
  PRIMARY KEY (`device_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for search_livestock_ranch
-- 搜索表对应牧场和认领牲畜表查询, search
-- ----------------------------
DROP TABLE IF EXISTS `search_livestock_ranch`;
CREATE TABLE `search_livestock_ranch` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `device_no` varchar(32) DEFAULT NULL COMMENT '设备编号',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `ranch_name` varchar(32) NOT NULL COMMENT '牧场名称',
  `weight` float(4,1) DEFAULT NULL COMMENT '体重，以Kg为单位',
  `price` float(4,1) DEFAULT NULL COMMENT '金额，以元为单位',
  `livestock_type` int(2) DEFAULT NULL COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡',
  `livestock_type_name` varchar(32) DEFAULT NULL COMMENT '牲畜类型名',
  `variety` int(4) DEFAULT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡 ...',
  `variety_name` varchar(32) DEFAULT NULL COMMENT '牲畜品种名称',
  `livestock_name` varchar(32) DEFAULT NULL COMMENT '牲畜昵称',
  `health` int(1) DEFAULT NULL COMMENT '健康情况; 1:优, 2:良, 3：好 ...',
  `health_name` varchar(32) DEFAULT NULL COMMENT '健康情况名称',
  `color` int(2) DEFAULT NULL COMMENT '颜色; 1:黑白, 2:白色, 3:花色, 4:红棕色, 5:黄白花, 6:淡红白花',
  `color_name` varchar(32) DEFAULT NULL COMMENT '颜色名称',
  `livestock_details` varchar(256) DEFAULT NULL COMMENT '牲畜详情',
  `life_time` int(2) DEFAULT NULL COMMENT '牲畜寿命',
  `characteristics` varchar(128) DEFAULT NULL COMMENT '牲畜特点',
  `member_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `cellphone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '发布时间,  yyyy-MM-dd hh:mm:ss',
  `claim_time` datetime DEFAULT NULL COMMENT '认领时间,  yyyy-MM-dd hh:mm:ss',
  `finish_time` datetime DEFAULT NULL COMMENT '认领结束,  yyyy-MM-dd hh:mm:ss',
  `birth_time` datetime DEFAULT NULL COMMENT '录入时间,  yyyy-MM-dd hh:mm:ss',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`id`),
  KEY `key_livestock_claim_ranch_id` (`ranch_id`),
  KEY `key_livestock_claim_device_no` (`device_no`),
  KEY `key_livestock_claim_livestock_id` (`livestock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认领牲畜表';


-- ----------------------------
-- Table structure for t_livestock_public_info
-- 认领牲畜公共信息表
-- ----------------------------
DROP TABLE IF EXISTS `t_livestock_public_info`;
CREATE TABLE `t_livestock_public_info` (
  `variety` int(32) NOT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡 ...',
  `characteristics` varchar(128) DEFAULT NULL COMMENT '牲畜特点',
  `price` float(4,1) DEFAULT NULL COMMENT '金额，以元为单位',
  `livestock_details` varchar(256) DEFAULT NULL COMMENT '牲畜详情',
  `life_time` int(2) DEFAULT NULL COMMENT '牲畜寿命',
  `img_url` varchar(256) DEFAULT NULL COMMENT '牲畜照片(没有图片时返回一个默认图片)',
  `livestock_img_url` varchar(256) DEFAULT NULL COMMENT '牲畜生活照片(没有图片时返回一个默认图片)',  
  `video_url` varchar(256) DEFAULT NULL COMMENT '牲畜录像(没有视频时返回一个默认视频)',
  `orderInstructions` varchar(256) DEFAULT NULL COMMENT '认领订单-订单说明',
  PRIMARY KEY (`variety`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认领牲畜公共信息表';
  
-- ----------------------------
-- 第三方支付订单表
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_order`;
CREATE TABLE `t_pay_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `member_id` bigint(20) NOT NULL COMMENT '用户ID',
  `cellphone` varchar(20) NOT NULL COMMENT '客户手机号',
  `device_no` varchar(32) DEFAULT NULL COMMENT '设备编号',
  `order_no` varchar(32) NOT NULL COMMENT '平台订单号',
  `payment_platform` int(1) DEFAULT NULL COMMENT '支付平台1=支付宝 2=微信',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '第三方支付交易号',
  `buyer_id` varchar(32) DEFAULT NULL COMMENT '买家会员号',
  `buyer_email` varchar(32) DEFAULT NULL COMMENT '买家支付号（支付宝或微信帐号）',
  `seller_id` varchar(32) DEFAULT NULL COMMENT '卖家会员号',
  `seller_email` varchar(32) DEFAULT NULL COMMENT '卖家支付号（支付宝或微信帐号）',
  `subject_type` int(1)  DEFAULT NULL COMMENT '交易类型,1=众币；2=充值',
  `subject_id` bigint(20)  DEFAULT NULL COMMENT '交易id',
  `subject_name` varchar(32) DEFAULT NULL COMMENT '交易名称',
  `price` decimal(20,5) DEFAULT '0.00000' COMMENT '交易单价',
  `total_fee` decimal(20,5) DEFAULT '0.00000' COMMENT '交易金额',
  `quantity` decimal(20,5) DEFAULT '0.00000' COMMENT '购买数量',
  `trade_status` int(1) NOT NULL DEFAULT '0' COMMENT '0=订单创建1=交易创建2=交易关闭3=交易成功4=交易结束不可做任何操作',
  `refund_status` int(1) DEFAULT NULL COMMENT '退款状态0=已申请退款1=退款成功2=退款关闭',
  `err_code` varchar(16) DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(64) DEFAULT NULL COMMENT '错误信息',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  `order_create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
  `gmt_create_time` datetime DEFAULT NULL COMMENT '交易创建时间',
  `gmt_payment_time` datetime DEFAULT NULL COMMENT '交易付款时间',
  `gmt_refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方支付订单表';

-- ----------------------------
-- Table structure for t_ranch_photo_common
-- 牧场公共图片
-- ----------------------------
DROP TABLE IF EXISTS `t_ranch_photo_common`;
CREATE TABLE `t_ranch_photo_common` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '纪录id',
  `img_url` varchar(256) NOT NULL COMMENT '图片相对路径,(必填项)',
  `img_type` int(2) DEFAULT '0' COMMENT '图片类型；1，生活照，2：牧场电子档案地图',
  `img_url_time` datetime DEFAULT NULL COMMENT '牲畜照片上传时间,  yyyy-MM-dd hh:mm:ss',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`id`),
  KEY `key_photo_common_ranch_id` (`ranch_id`),
  KEY `key_photo_common_qi_id` (`qi_id`),
  KEY `key_photo_common_sumu_id` (`sumu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '牧场公共图片表';

-- ----------------------------
-- Table structure for t_livestock_photo_claim
-- 认领牲畜图片
-- ----------------------------
DROP TABLE IF EXISTS `t_livestock_photo_claim`;
CREATE TABLE `t_livestock_photo_claim` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '纪录id',
  `img_url` varchar(256) NOT NULL COMMENT '图片相对路径,(必填项)',
  `img_type` int(2) DEFAULT '0' COMMENT '图片类型；1，生活照，',
  `img_url_time` datetime DEFAULT NULL COMMENT '牲畜照片上传时间,  yyyy-MM-dd hh:mm:ss',
  `device_no` varchar(32) DEFAULT NULL COMMENT '设备编号',
  `livestock_id` bigint(20) DEFAULT NULL COMMENT '牲畜id',
  `livestock_type` int(2) NOT NULL DEFAULT 1 COMMENT '牲畜类型; 1:羊, 2:牛, 3:马, 4:猪, 5:鸡',
  `variety` int(4) DEFAULT NULL COMMENT '牲畜品种; 100:乌珠穆沁黑头羊, 101:山羊, 201:西门塔尔牛, 301:蒙古马, 401:草原黑毛猪, 501：草原绿鸟鸡 ...',
  `ranch_id` int(8) NOT NULL COMMENT '牧场id,(必填项)',
  `qi_id` int(8) NOT NULL COMMENT '旗id,(必填项)',
  `sumu_id` int(8) NOT NULL COMMENT '苏木id,(必填项)',
  PRIMARY KEY (`id`),
  KEY `key_photo_claim_ranch_id` (`ranch_id`),
  KEY `key_photo_claim_device_no` (`device_no`),
  KEY `key_photo_claim_livestock_id` (`livestock_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '认领牲畜图片表';

-- ----------------------------
-- Table structure for t_admin_info
-- 管理员表(主要用于旗、苏木、屠宰场、牧场用户的管理)
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_info`;
CREATE TABLE `t_admin_info` (
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(64) DEFAULT NULL COMMENT '登录名称',
  `password` varchar(64) DEFAULT NULL COMMENT '密码(MD5)',
  `people_name` varchar(64) DEFAULT NULL COMMENT '用户名称',
  `people_name_pinyin` varchar(64) DEFAULT NULL COMMENT '用户名称拼音',
  `sex` int(1) DEFAULT '1' COMMENT '性别; 0:女， 1：男',
  `age` int(2) DEFAULT NULL COMMENT '年龄',
  `id_card` varchar(32) DEFAULT NULL COMMENT '身份证号',
  `cellphone` varchar(11) DEFAULT NULL COMMENT '手机号码',
  `device_no` varchar(30) DEFAULT NULL COMMENT '人员设备编号',
  `device_type` varchar(20) DEFAULT NULL COMMENT '人员设备类型',
  `admin_type` int(2) DEFAULT NULL COMMENT '人员类型; 1: 超级管理员，2：旗管理员， 3：苏木管理员 4：屠宰场管理员 5：牧场管理员',
  `duty` varchar(32) DEFAULT NULL COMMENT '职务',
  `orgnize_type` int(20) DEFAULT NULL COMMENT '所属机构类型; 1: 超级管理员，2：旗员， 3：苏木 4：屠宰场 5：牧场',
  `orgnize_id` int(20) DEFAULT NULL COMMENT '所属机构id',
  `work_address` varchar(256) DEFAULT NULL COMMENT '工作地址',
  `home_address` varchar(256) DEFAULT NULL COMMENT '住址',
  `is_receive_alarm` int(2) DEFAULT NULL COMMENT '是否收到报警',
  `operater` varchar(30) DEFAULT NULL COMMENT '操作人名称',
  `operate_ip` varchar(20) DEFAULT NULL COMMENT '操作人ip',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `token` varchar(50) DEFAULT NULL COMMENT '用于app登录',
  `token_md5` varchar(50) DEFAULT NULL COMMENT '用于app登录',
  `link_name` varchar(32) DEFAULT NULL COMMENT '联系人名称',
  `link_cellphone` varchar(11) DEFAULT NULL COMMENT '联系人电话',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukey_admin_username` (`username`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ----------------------------
-- Table structure for t_log_job_progress
-- 任务进度日志表
-- -----------------------------
DROP TABLE IF EXISTS `t_log_job_progress`;
CREATE TABLE `t_log_job_progress` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `job_type` int(2) NOT NULL DEFAULT '0' COMMENT '务任类型;0：其他任务， 1:前台用户触发的任务，2：后台运维人员触发的任务，3 定时任务触发的任务',
  `job_status` int(2) NOT NULL DEFAULT '1' COMMENT '任务状态; 0:其他, 1:执行中, 2:结束(成功), 3:结束(异常), 4:结束(服务重启)',
  `info` varchar(256) DEFAULT '' COMMENT '信息',
  `msg` varchar(2048) DEFAULT '' COMMENT '详情',
  `time_start` datetime DEFAULT NULL COMMENT '开始时间',
  `time_update` datetime DEFAULT NULL COMMENT '更新时间',
  `time_end` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务进度日志表';


-- ----------------------------
-- Table structure for t_log_operation
-- 后台人员操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `t_log_operation`;
CREATE TABLE `t_log_operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人id',
  `operator_ip` varchar(32) DEFAULT NULL COMMENT '操作人IP',
  `operator_name` varchar(32) DEFAULT NULL COMMENT '操作人名称',
  `module_name` varchar(128) DEFAULT NULL COMMENT '模块名称',
  `content` varchar(1024) DEFAULT NULL COMMENT '日志内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台员工操作日志表';


-- ----------------------------
-- Table structure for t_syscode
-- 字典表
-- ----------------------------
DROP TABLE IF EXISTS `t_syscode`;
CREATE TABLE `t_syscode` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `kind` varchar(64) NOT NULL DEFAULT '' COMMENT '类型',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT '名称, (一般放的是英文名称)',
  `data` varchar(1024) NOT NULL DEFAULT '' COMMENT '值, (一般放的是显示名称、不同类型的码也可以是其它的描述信息)',
  `code_order` int(4) DEFAULT '0' COMMENT '序号, (很多时候码的显示是有顺序的)',
  `comment` varchar(1024) DEFAULT NULL COMMENT '描述信息',
  `is_active` int(1) DEFAULT '1' COMMENT '状态，0：禁用，1：启用',
  `is_system` int(1) DEFAULT '0' COMMENT '是否系统码',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `creater_id` bigint(20) DEFAULT NULL,
  `updater_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_kind_name` (`kind`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for t_member
-- 会员表
-- ----------------------------
DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'member_id',
  `login_name` varchar(32) DEFAULT NULL COMMENT '录登名称',
  `password` varchar(32) DEFAULT NULL COMMENT '录登密码',
  `nickname` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '会员名',
  `cell_phone_num` varchar(512) DEFAULT NULL COMMENT '手机号, 加密字段，前台长度限制为16, 唯一性由程序保证',
  `email` varchar(512) DEFAULT NULL COMMENT '邮箱, 加密字段，前台长度限制为32, 唯一性由程序保证',
  `real_name` varchar(512) DEFAULT NULL COMMENT '真实姓名, 加密字段，前台长度限制为16',
  `sex` int(1) DEFAULT NULL  COMMENT '性别。1：女 2：男', 
  `member_type` int(2) NOT NULL DEFAULT '0' COMMENT '会员身份标识; 0：普通会员', 
  `birthday` int(8) DEFAULT '19961212' COMMENT '出生年月日，身份证认证后程序填入 ',
  `id_card` varchar(512) DEFAULT NULL COMMENT '身份证号码, 加密字段，前台长度限制为32',
  `is_authed` int(1)  DEFAULT '0' COMMENT '是否已实名认证  0：否，1：是', 
  `province` varchar(32)  DEFAULT null COMMENT '省', 
  `city` varchar(32)  DEFAULT null COMMENT '市', 
  `country` varchar(32)  DEFAULT null COMMENT '县',
  `address` varchar(512)  DEFAULT null COMMENT '详细地址',  
  `head_img_url` varchar(128) DEFAULT NULL COMMENT '头像; 存放会员上传的头像图片url',
  `qr_code_url` varchar(128) DEFAULT NULL COMMENT '二维码存储路径',  
  `longitude` double(9,6) DEFAULT NULL COMMENT '经度',
  `latitude` double(9,6) DEFAULT NULL COMMENT '纬度',
  `is_staff` int(1)  DEFAULT '0' COMMENT '是否运维人员  0：否，1：是', 
  `num_sort` int(4)  DEFAULT '9999' COMMENT '热推排序  9999：不出现在热榜', 
  `status` int(2) DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用，2：冻结-密码次数超限, 3:冻结-违规， 5：冻结-临时 10：冻结-黑客',
  `inviter_id` bigint(20) DEFAULT NULL COMMENT '邀请人id',
  `checker_id` bigint(20) DEFAULT NULL COMMENT '审核员工id',
  `check_time` datetime DEFAULT NULL COMMENT '审核时间,  yyyy-MM-dd hh:mm:ss',
  `check_content`varchar(256) DEFAULT NULL COMMENT '审核内容',
  `creater_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`member_id`),
  KEY `index_m_numsort` (`num_sort`),
  KEY `index_m_nickname` (`nickname`), 
  KEY `index_m_loginname` (`login_name`)   
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';


-- ----------------------------
-- Table structure for t_member_login_info
-- 记录登录的信息， 登录会员每人一条
-- ----------------------------
DROP TABLE IF EXISTS `t_member_login_info`;
CREATE TABLE `t_member_login_info` (
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `cell_phone_num` varchar(512) DEFAULT NULL COMMENT '手机号, 加密字段，前台长度限制为16, 唯一性由程序保证',
  `password` varchar(32) DEFAULT NULL COMMENT '登录密码',
  `id_token` varchar(64) DEFAULT NULL,
  `id_token_md5` varchar(64) DEFAULT NULL,
  `deviceId` varchar(64) DEFAULT NULL COMMENT '手机设备号',
  `last_login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `reg_ip` varchar(32) NOT NULL COMMENT '注册ip',
  `reg_time` datetime NOT NULL COMMENT '注册时间',
  `last_login_ip` varchar(32) DEFAULT NULL COMMENT '上次登录ip',
  `last_app_version` varchar(20) DEFAULT NULL COMMENT 'app最近版本号',
  `last_login_device_type` int(2) NOT NULL DEFAULT '10' COMMENT '最后登录点  10=苹果手机 20=安卓 30=PC',
  `last_login_device_name` varchar(32) DEFAULT NULL COMMENT '最后登录设备名称',
  `last_login_device_number` varchar(64) DEFAULT NULL COMMENT '最后登录设备唯一号',
  `login_pwd_err_num` int(11) NOT NULL DEFAULT '0' COMMENT '当天登陆密码连续错误输入次数',
  `pay_pwd_err_num` int(11) NOT NULL DEFAULT '0' COMMENT '当天交易密码连续错误输入次数',
  `idcard_verify_num` int(11) NOT NULL DEFAULT '0' COMMENT '当天实名认证次数',
  `login_day_num` int(11) NOT NULL DEFAULT '0' COMMENT '当日登录次数',
  `login_total_num` int(11) NOT NULL DEFAULT '0' COMMENT '总登录次数',
  `ban_deadline` datetime DEFAULT NULL COMMENT '禁令期限（举报限制登录结束时间）',
  `continue_login_count` int(11) DEFAULT '0' COMMENT '连续登录次数',
  `status` int(2) DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用',
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员登录信息表';


-- ----------------------------
-- Table structure for t_member_detail
-- 会员详情表   与t_member表一对多的关系
-- ----------------------------
DROP TABLE IF EXISTS `t_member_detail`;
CREATE TABLE `t_member_detail` (
  `member_id` bigint(20) NOT NULL COMMENT '会员id',
  `height` int(3) DEFAULT NULL COMMENT '身高，以厘米为单位',
  `weight` float(4,1) DEFAULT NULL COMMENT '体重，以公斤为单位',
  `astrology` varchar(8) DEFAULT NULL COMMENT '星座',
  `blood_type` varchar(4) DEFAULT NULL COMMENT '血型',
  `body_size` varchar(64) DEFAULT NULL COMMENT '胸围，腰围，臀围；使用逗号分隔存储',
  `characters` varchar(256) DEFAULT NULL COMMENT '性格特点',
  `signature` varchar(128) DEFAULT NULL COMMENT '个性签名',
  `speciality` varchar(128) DEFAULT NULL COMMENT '特长',
  `qq` varchar(128) DEFAULT NULL COMMENT 'QQ',
  `weixin` varchar(128) DEFAULT NULL COMMENT '微信',
  `weibo` varchar(128) DEFAULT NULL COMMENT '微博',
  `phone_used` varchar(128) DEFAULT NULL COMMENT '常用手机',  
  `brief` varchar(1024) DEFAULT NULL COMMENT '自我介绍', 
  `card_img` varchar(128) DEFAULT NULL COMMENT '身份证正面图片',
  `card_img_back` varchar(128) DEFAULT NULL COMMENT '身份证反面图片',
  `card_status` int(1) DEFAULT '0' COMMENT '实名认证状态：1已通过，2待审核，0未通过',
  `card_reason` varchar(128) DEFAULT NULL COMMENT '认证不通过理由',
  `auth_apply_time` datetime DEFAULT NULL COMMENT '申请认证时间,  yyyy-MM-dd hh:mm:ss',
  `auth_approved_time` datetime DEFAULT NULL COMMENT '认证通过时间,  yyyy-MM-dd hh:mm:ss',
  `bank_card_no` varchar(512) DEFAULT NULL COMMENT '实名认证银行卡号，加密字段，前台长度限制为32',
  `bank_code` varchar(128) DEFAULT NULL COMMENT '实名认证银行代码',
  `status` int(2) DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员详情表';

-- ----------------------------
-- Table structure for t_member_invite_code
-- ----------------------------
DROP TABLE IF EXISTS `t_member_invite_code`;
CREATE TABLE `t_member_invite_code` (
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `code` varchar(10) NOT NULL COMMENT '推广码',
  `status` int(2) DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用',
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `unique_member_invite_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员推广码表';


-- ----------------------------
-- Table structure for t_member_login_third
-- ----------------------------
DROP TABLE IF EXISTS `t_member_login_third`;
CREATE TABLE `t_member_login_third` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uid` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '第三方登录的ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '第三方登录名',
  `type` varchar(10) CHARACTER SET utf8 NOT NULL COMMENT '第三方类型：qq,weibo',
  `gender` varchar(2) CHARACTER SET utf8 DEFAULT NULL COMMENT '性别：男，女',
  `small_img_url` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '小图头像地址(50*50）',
  `big_img_url` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '大图头像地址',
  `access_token` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '第三方登录授权令牌',
  `unionid` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '第三方微信同开发者账号跨应用唯一会员ID',
  `status` int(2) DEFAULT '1' COMMENT '状态，-1：已删除 0：禁用，1：启用',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间,  yyyy-MM-dd hh:mm:ss',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间,  yyyy-MM-dd hh:mm:ss',  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方登录对应表';