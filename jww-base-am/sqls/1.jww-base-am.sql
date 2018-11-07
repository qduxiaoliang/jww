/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : jww

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2017-12-31 23:10:01
*/

DROP DATABASE IF EXISTS `jww-2.0.0`;
CREATE DATABASE IF NOT EXISTS `jww-2.0.0` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `jww-2.0.0`;
-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id_` bigint(20) NOT NULL COMMENT '主键ID',
  `parent_id` bigint(20) NOT NULL COMMENT '父ID',
  `unit_id` bigint(20) DEFAULT NULL COMMENT '隶属单位ID',
  `dept_name` varchar(16) NOT NULL COMMENT '部门名称',
  `sort_no` tinyint(1) DEFAULT NULL COMMENT '排序',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL,
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用(1:启用/0:禁用)',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是/0:否)',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '-1', null, '吉祥物流', '1', null, '2018-07-01 00:00:00', '1', '2018-07-01 00:00:00', null, '1', '0');

-- ----------------------------
-- Table structure for sys_dic
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic`;
CREATE TABLE `sys_dic` (
  `id_` bigint(20) NOT NULL COMMENT '主键ID',
  `type_` varchar(32) NOT NULL COMMENT '类型',
  `type_name` varchar(32) NOT NULL COMMENT '字典类型名称',
  `code_` varchar(32) NOT NULL DEFAULT '' COMMENT '数据值',
  `code_name` varchar(32) NOT NULL DEFAULT '' COMMENT '数据值名称',
  `sort_no` tinyint(1) NOT NULL DEFAULT '0' COMMENT '排序（升序）',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父id',
  `is_editable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可编辑（0:不可编辑/1:可编辑）',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用(1:启用/0:禁用)',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是/0:否)',
  PRIMARY KEY (`id_`),
  KEY `normal_code` (`type_`,`code_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典表';

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id_` bigint(20) NOT NULL COMMENT '编号',
  `service_name` varchar(32) NOT NULL COMMENT '服务ID',
  `username_` varchar(16) NOT NULL COMMENT '用户名',
  `full_name` varchar(16) DEFAULT NULL COMMENT '姓名',
  `title_` varchar(64) NOT NULL DEFAULT '' COMMENT '日志标题',
  `type_` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作类型(1:新增/2:查询/3:修改/4:删除/5:导入/6:导出)',
  `user_agent` varchar(512) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) NOT NULL COMMENT '请求URI',
  `http_method` varchar(10) NOT NULL COMMENT 'HTTP方式',
  `params_` text COMMENT '操作提交的数据',
  `result_` tinyint(1) NOT NULL DEFAULT '1' COMMENT '操作结果(0:失败/1:成功)',
  `request_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '请求时间',
  `time_` int(11) NOT NULL COMMENT '执行时间',
  `ip_` varchar(32) NOT NULL COMMENT '操作IP地址',
  `ip_detail` varchar(64) DEFAULT NULL COMMENT 'ip运营商',
  `exception_` text CHARACTER SET utf8 COMMENT '异常信息',
  `request_token` varchar(255) DEFAULT NULL COMMENT '请求token',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) NOT NULL COMMENT '创建者',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id_`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_type` (`type_`),
  KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
  `id_` bigint(20) NOT NULL COMMENT '主键ID',
  `resource_name` varchar(16) NOT NULL COMMENT '资源名称',
  `resource_type` tinyint(1) NOT NULL COMMENT '资源类型(0:按钮/1:目录/2:菜单)',
  `permission_` varchar(32) DEFAULT NULL COMMENT '资源权限标识',
  `path_` varchar(128) DEFAULT NULL COMMENT '前端URL',
  `request_url` varchar(128) DEFAULT NULL COMMENT '请求链接',
  `method_` varchar(32) DEFAULT NULL COMMENT '请求方法',
  `parent_id` bigint(20) NOT NULL COMMENT '父资源ID',
  `icon_` varchar(32) DEFAULT NULL COMMENT '图标',
  `component_` varchar(64) DEFAULT NULL COMMENT 'VUE页面',
  `sys_type` varchar(32) DEFAULT '' COMMENT '系统类型',
  `sort_no` tinyint(1) DEFAULT '1' COMMENT '排序值',
  `resource_level` tinyint(1) DEFAULT '1' COMMENT '资源等级',
  `is_show` tinyint(1) DEFAULT NULL COMMENT '是否显示(0:不显示/1:显示)',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用(1:启用/0:禁用)',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是/0:否）',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源权限表';

TRUNCATE TABLE `sys_resource`;
-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('1', '系统管理', '1', NULL, '/system', NULL, NULL, '-1', 'icon_system', 'layout', '运营平台', '1', '1', NULL, NULL, '2017-11-07 20:56:00', '1', '2018-08-16 07:51:38', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('2', '基础管理', '1', NULL, '/basic', NULL, NULL, '-1', 'icon_basic', 'basic', '', '2', '1', NULL, NULL, '2018-08-05 17:37:29', '1', '2018-08-16 08:05:43', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('3', '企业管理', '1', NULL, '/enterprise', NULL, NULL, '-1', 'icon_enterprise', 'enterprise', NULL, '3', '1', NULL, NULL, '2018-08-05 17:37:29', '1', '2018-08-16 08:07:37', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('4', '资源管理', '1', NULL, '/resource', NULL, NULL, '-1', 'icon_resource', 'resource', NULL, '4', '1', NULL, NULL, '2018-08-05 17:37:29', '1', '2018-08-16 08:11:43', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('5', '行业知识', '1', NULL, '/industry', NULL, NULL, '-1', 'icon_industry', 'industry', NULL, '5', '1', NULL, NULL, '2018-08-05 17:37:29', '1', '2018-08-16 08:13:40', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('6', '信息管理', '1', NULL, '/information', NULL, NULL, '-1', 'icon_information', 'information', NULL, '6', '1', NULL, NULL, '2018-08-05 17:37:29', '1', '2018-08-16 08:14:17', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('8', '系统监控', '1', NULL, NULL, NULL, NULL, '-1', 'icon-iconbmgl', NULL, '运营平台', '2', '1', NULL, NULL, '2018-01-22 12:30:41', '1', '2018-08-05 20:09:06', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('101', '用户管理', '2', NULL, 'user_manage', '', NULL, '1', '', 'userManage', '运营平台', '2', '1', NULL, NULL, '2017-11-02 22:24:37', '1', '2018-08-16 08:05:04', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('102', '菜单管理', '2', NULL, 'menu_config', '', NULL, '1', '', 'menuConfig', '运营平台', '3', '1', NULL, NULL, '2017-11-08 09:57:27', '1', '2018-08-16 08:03:06', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('103', '角色管理', '2', NULL, 'role_manage', NULL, NULL, '1', '', 'roleManage', '运营平台', '4', '1', NULL, NULL, '2017-11-08 10:13:37', '1', '2018-08-16 08:04:26', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('104', '日志管理', '2', NULL, 'log', NULL, NULL, '1', 'icon-rizhiguanli', 'views/admin/log/index', '运营平台', '5', '1', NULL, NULL, '2017-11-20 14:06:22', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('105', '字典管理', '2', NULL, 'data_dictionary', NULL, NULL, '1', '', 'dataDictionary', '运营平台', '6', '1', NULL, NULL, '2017-11-29 11:30:52', '1', '2018-08-16 08:04:01', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('106', '部门管理', '2', NULL, 'dept', NULL, NULL, '1', 'icon-iconbmgl', 'views/admin/dept/index', '运营平台', '7', '1', NULL, NULL, '2018-01-20 13:17:19', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('107', '客户端管理', '2', '', 'client', '', '', '1', 'icon-bangzhushouji', 'views/admin/client/index', '运营平台', '9', '1', NULL, NULL, '2018-01-20 13:17:19', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('108', '路由管理', '2', NULL, 'route', NULL, NULL, '1', 'icon-luyou', 'views/admin/route/index', '运营平台', '8', '1', NULL, NULL, '2018-05-15 21:44:51', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('201', '机场管理', '2', NULL, 'airport_info', NULL, NULL, '2', NULL, 'airportInfo', '', '1', '1', NULL, NULL, '2018-08-05 20:24:53', '1', '2018-08-16 08:06:18', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('202', '航空公司管理', '2', '', 'air_company', '', '', '2', '', 'airCompany', '', '2', '1', NULL, NULL, '2018-08-05 20:24:53', '1', '2018-08-16 08:06:56', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('203', '航班时刻管理', '2', '', 'flight_time', '', '', '2', '', 'flightTime', '', '3', '1', NULL, NULL, '2018-08-05 20:24:53', '1', '2018-08-16 08:07:13', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('301', '企业认证', '2', '', 'enterprise_Certification', '', '', '3', '', 'enterpriseCertification', '', '1', '1', NULL, NULL, '2018-08-05 20:24:53', '1', '2018-08-16 08:08:04', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('302', '企业角色管理', '2', '', 'enterprise_role_manage', '', '', '3', '', 'enterpriseRole', '', '2', '1', NULL, NULL, '2018-08-05 20:24:53', '1', '2018-08-16 08:08:18', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('303', '企业用户管理', '2', '', 'enterprise_user_manage', '', '', '3', '', 'enterpriseUser', '', '3', '1', NULL, NULL, '2018-08-05 20:24:53', '1', '2018-08-16 08:08:35', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('401', '上门揽货', '2', '', 'store_pickup', '', '', '4', '', 'storePickup', '', '1', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:12:23', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('402', '航班运输', '2', '', 'flight_transport', '', '', '4', '', 'flightTransport', '', '2', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:12:55', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('403', '机场提货', '2', '', 'airport_delivery', '', '', '4', '', 'airportDelivery', '', '3', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:13:08', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('404', '送货上门', '2', '', 'home_delivery', '', '', '4', '', 'homeDelivery', '', '4', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:13:21', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('501', '知识发布', '2', '', 'knowledge_release', '', '', '5', '', 'knowledgeRelease', '', '1', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:13:53', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('502', '知识管理', '2', '', 'knowledge_manage', '', '', '5', '', 'knowledgeManage', '', '2', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:14:03', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('601', '新闻管理', '2', '', 'news_manage', '', '', '6', '', 'newsManage', '', '1', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:14:30', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('602', '招聘管理', '2', '', 'recruit_manage', '', '', '6', '', 'recruitManage', '', '2', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:14:45', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('603', '广告管理', '2', '', 'ad_manage', '', '', '6', '', 'adManage', '', '3', '1', NULL, '', '2018-08-05 20:24:53', '1', '2018-08-16 08:15:14', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('801', '服务监控', '2', NULL, 'http://127.0.0.1:8751', NULL, NULL, '8', 'icon-jiankong', NULL, '运营平台', '9', '1', NULL, NULL, '2018-01-23 10:53:33', '1', '2018-08-05 23:39:31', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('802', 'zipkin监控', '2', NULL, 'http://139.224.200.249:5002', NULL, NULL, '8', 'icon-jiankong', NULL, '运营平台', '11', '1', NULL, NULL, '2018-01-23 10:55:18', '1', '2018-08-05 23:39:33', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('803', 'pinpoint监控', '2', NULL, 'https://pinpoint.pig4cloud.com', NULL, NULL, '8', 'icon-xiazaihuancun', NULL, '运营平台', '10', '1', NULL, NULL, '2018-01-25 11:08:52', '1', '2018-08-05 23:39:41', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('804', '缓存状态', '2', NULL, 'http://139.224.200.249:8585', NULL, NULL, '8', 'icon-ecs-status', NULL, '运营平台', '12', '1', NULL, NULL, '2018-01-23 10:56:11', '1', '2018-08-05 23:39:43', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('805', 'ELK状态', '2', NULL, 'http://139.224.200.249:5601', NULL, NULL, '8', 'icon-ecs-status', NULL, '运营平台', '13', '1', NULL, NULL, '2018-01-23 10:55:47', '1', '2018-08-05 23:39:45', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('806', '接口文档', '2', NULL, 'http://127.0.0.1:9999/swagger-ui.html', NULL, NULL, '8', 'icon-wendangdocument72', NULL, '运营平台', '14', '1', NULL, NULL, '2018-01-23 10:56:43', '1', '2018-08-05 23:39:47', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('807', '任务监控', '2', NULL, 'http://139.224.200.249:8899', NULL, NULL, '8', 'icon-jiankong', NULL, '运营平台', '15', '1', NULL, NULL, '2018-01-23 10:55:18', '1', '2018-08-05 23:39:49', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1011', '用户查看', '0', NULL, NULL, '/admin/user/**', 'GET', '101', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2017-11-07 20:58:05', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1012', '用户新增', '0', 'sys_user_add', NULL, '/admin/user/*', 'POST', '101', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2017-11-08 09:52:09', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1013', '用户修改', '0', 'sys_user_mod', NULL, '/admin/user/**', 'PUT', '101', NULL, NULL, '运营平台', '3', '1', NULL, NULL, '2017-11-08 09:52:48', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1014', '用户删除', '0', 'sys_user_del', NULL, '/admin/user/*', 'DELETE', '101', NULL, NULL, '运营平台', '4', '1', NULL, NULL, '2017-11-08 09:54:01', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1015', '用户导出', '0', 'sys_user_export', NULL, '/admin/user/*', 'GET', '101', NULL, NULL, '运营平台', '5', '1', NULL, NULL, '2017-11-08 09:54:01', '1', NULL, NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1016', '重置密码', '0', 'sys_user_reset', NULL, '/admin/user/*', 'PUT', '101', NULL, NULL, '运营平台', '6', '1', NULL, NULL, '2017-11-08 09:54:01', '1', '2018-08-05 20:13:21', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1021', '菜单查看', '0', NULL, NULL, '/admin/menu/**', 'GET', '102', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2017-11-08 09:57:56', '1', '2018-08-05 23:40:16', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1022', '菜单新增', '0', 'sys_menu_add', NULL, '/admin/menu/*', 'POST', '102', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2017-11-08 10:15:53', '1', '2018-08-05 23:40:20', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1023', '菜单修改', '0', 'sys_menu_mod', NULL, '/admin/menu/*', 'PUT', '102', NULL, NULL, '运营平台', '3', '1', NULL, NULL, '2017-11-08 10:16:23', '1', '2018-08-05 23:40:23', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1024', '菜单删除', '0', 'sys_menu_del', NULL, '/admin/menu/*', 'DELETE', '102', NULL, NULL, '运营平台', '4', '1', NULL, NULL, '2017-11-08 10:16:43', '1', '2018-08-05 23:40:25', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1031', '角色查看', '0', NULL, NULL, '/admin/role/**', 'GET', '103', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2017-11-08 10:14:01', '1', '2018-08-05 20:15:39', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1032', '角色新增', '0', 'sys_role_add', NULL, '/admin/role/*', 'POST', '103', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2017-11-08 10:14:18', '1', '2018-08-05 20:15:44', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1033', '角色修改', '0', 'sys_role_mod', NULL, '/admin/role/*', 'PUT', '103', NULL, NULL, '运营平台', '3', '1', NULL, NULL, '2017-11-08 10:14:41', '1', '2018-08-05 20:15:46', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1034', '角色删除', '0', 'sys_role_del', NULL, '/admin/role/*', 'DELETE', '103', NULL, NULL, '运营平台', '4', '1', NULL, NULL, '2017-11-08 10:14:59', '1', '2018-08-05 20:15:49', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1035', '分配权限', '0', 'sys_role_perm', 'sys_authorization', '/admin/role/*', 'PUT', '1', NULL, 'sysAuthorization', '运营平台', '5', '1', '1', NULL, '2018-04-20 07:22:55', '1', '2018-08-16 22:59:19', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1041', '日志查看', '0', NULL, NULL, '/admin/log/**', 'GET', '104', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2017-11-20 14:07:25', '1', '2018-08-05 20:15:14', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1042', '日志删除', '0', 'sys_log_del', NULL, '/admin/log/*', 'DELETE', '104', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2017-11-20 20:37:37', '1', '2018-08-05 20:15:15', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1051', '字典查看', '0', NULL, NULL, '/admin/dict/**', 'GET', '105', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2017-11-19 22:04:24', '1', '2018-08-05 20:16:53', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1052', '字典新增', '0', 'sys_dict_add', NULL, '/admin/dict/**', 'POST', '105', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2018-05-11 22:34:55', '1', '2018-08-05 20:17:23', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1053', '字典修改', '0', 'sys_dict_mod', NULL, '/admin/dict/**', 'PUT', '105', NULL, NULL, '运营平台', '3', '1', NULL, NULL, '2018-05-11 22:36:03', '1', '2018-08-05 20:17:30', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1054', '字典删除', '0', 'sys_dict_del', NULL, '/admin/dict/**', 'DELETE', '105', NULL, NULL, '运营平台', '4', '1', NULL, NULL, '2017-11-29 11:30:11', '1', '2018-08-05 20:17:34', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1061', '部门查看', '0', '', NULL, '/admin/dept/**', 'GET', '106', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2018-01-20 13:17:19', '1', '2018-08-05 20:18:51', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1062', '部门新增', '0', 'sys_dept_add', NULL, '/admin/dept/**', 'POST', '106', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2018-01-20 14:56:16', '1', '2018-08-05 20:18:52', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1063', '部门修改', '0', 'sys_dept_mod', NULL, '/admin/dept/**', 'PUT', '106', NULL, NULL, '运营平台', '3', '1', NULL, NULL, '2018-01-20 14:56:59', '1', '2018-08-05 20:18:52', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1064', '部门删除', '0', 'sys_dept_del', NULL, '/admin/dept/**', 'DELETE', '106', NULL, NULL, '运营平台', '4', '1', NULL, NULL, '2018-01-20 14:57:28', '1', '2018-08-05 20:18:54', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1071', '客户端查看', '0', NULL, NULL, '/admin/client/**', 'GET', '107', NULL, NULL, '运营平台', '1', '1', NULL, NULL, '2018-05-15 21:39:57', '1', '2018-08-05 20:21:45', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1072', '客户端新增', '0', 'sys_client_add', NULL, '/admin/client/**', 'POST', '107', NULL, NULL, '运营平台', '2', '1', NULL, NULL, '2018-05-15 21:35:18', '1', '2018-08-05 20:21:46', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1073', '客户端修改', '0', 'sys_client_mod', NULL, '/admin/client/**', 'PUT', '107', NULL, NULL, '运营平台', '3', '1', NULL, NULL, '2018-05-15 21:37:06', '1', '2018-08-05 20:21:46', NULL, '1', '0');
INSERT INTO `sys_resource` VALUES ('1074', '客户端删除', '0', 'sys_client_del', NULL, '/admin/client/**', 'DELETE', '107', NULL, NULL, '运营平台', '4', '1', NULL, NULL, '2018-05-15 21:39:16', '1', '2018-08-05 20:21:47', NULL, '1', '0');

-- ----------------------------
-- Table structure for sys_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `param_key` varchar(32) NOT NULL COMMENT '参数键名',
  `param_value` varchar(32) NOT NULL COMMENT '参数键值',
  `catalog_id` bigint(20) DEFAULT NULL COMMENT '参数分类ID',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用(1:启用/0:禁用)',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是/0:否)',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='全局参数表';

-- ----------------------------
-- Records of sys_param
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `role_name` varchar(16) NOT NULL COMMENT '角色名称',
  `role_code` varchar(32) NOT NULL COMMENT '角色编码',
  `role_desc` varchar(64) DEFAULT NULL COMMENT '角色描述',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用（1:启用/0:禁用）',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除（1:是/0:否）',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '管理员', 'ROLE_ADMIN', '管理员', null, '2017-10-29 15:45:51', '1', '2018-04-22 11:40:29', null, '1', '0');

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource` (
  `id_` bigint(20) NOT NULL COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `resource_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id_`),
  UNIQUE KEY `role_id_resource_id` (`role_id`,`resource_id`) USING BTREE COMMENT '唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色资源关系表';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES ('1','1','1', NULL, '2018-08-16 22:21:12', '1', NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('2','1','1035', NULL, '2018-08-16 22:21:12', '1', NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('3','1','103', NULL, '2018-08-16 22:21:12', '1', NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('4','1','6012', NULL, '2018-08-16 22:21:12', '1', NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('5','1','6013', NULL, '2018-08-16 22:21:12', '1', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id_` bigint(20) NOT NULL COMMENT '主键',
  `username_` varchar(16) NOT NULL COMMENT '用户名',
  `password_` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `salt_` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `user_type` tinyint(1) NOT NULL COMMENT '用户类型(1:管理员/2:内部用户/3:外部用户)',
  `full_name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `name_pinyin` varchar(32) DEFAULT NULL COMMENT '姓名拼音',
  `nick_name` varchar(16) DEFAULT NULL COMMENT '昵称',
  `sex_` tinyint(1) NOT NULL DEFAULT '0' COMMENT '性别(0:未知/1:男/2:女)',
  `phone_` varchar(11) NOT NULL COMMENT '手机号码',
  `tel_` varchar(15) DEFAULT NULL COMMENT '联系电话',
  `avatar_` varchar(255) DEFAULT NULL COMMENT '头像',
  `email_` varchar(32) NOT NULL COMMENT '邮箱',
  `card_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '证件类型(1:身份证)',
  `id_card` varchar(18) DEFAULT NULL COMMENT '证件号码',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `position_` varchar(16) DEFAULT NULL COMMENT '职位',
  `address_` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `staff_no` varchar(16) DEFAULT NULL COMMENT '内部员工工号',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
  `is_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用(1:启用/0:禁用)',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是/0:否)',
  PRIMARY KEY (`id_`),
  UNIQUE KEY `unique_email` (`email_`),
  UNIQUE KEY `unique_username` (`username_`) USING BTREE,
  UNIQUE KEY `unique_phone` (`phone_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$vg5QNHhCknAqevx9vM2s5esllJEzF/pa8VZXtFYHhhOhUcCw/GWyS', null, '1', '管理员', null, null, '0', '17034642111', null, null, 'admin@juneyaoair.com', '1', null, null, null, null, null, null, '2018-04-20 07:15:18', '1', '2018-05-11 17:12:00', null, '1', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id_` bigint(20) NOT NULL COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除(1:是/0:否)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关系表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1', null, '0', '2018-08-10 16:12:22', '1', null, null);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id_` bigint(20) NOT NULL COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `remark_` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与部门对应关系';

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES ('1', '1', '1', null, '2018-07-01 00:00:00', '1', null, null);