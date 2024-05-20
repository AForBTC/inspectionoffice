/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : inspectionoffice

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 20/05/2024 16:12:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contrast
-- ----------------------------
DROP TABLE IF EXISTS `contrast`;
CREATE TABLE `contrast`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contrast_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `fatherfile_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `zip_url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `fatherfile_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `create_timestamp` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 435 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contrast
-- ----------------------------
INSERT INTO `contrast` VALUES (431, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.zip', '2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '2024-05-17 15:45:10');
INSERT INTO `contrast` VALUES (432, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.zip', '2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '2024-05-17 16:13:23');
INSERT INTO `contrast` VALUES (433, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.zip', '2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '2024-05-17 16:17:49');
INSERT INTO `contrast` VALUES (434, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.zip', '2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '2024-05-17 16:32:38');
INSERT INTO `contrast` VALUES (435, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '/300db671-8d57-48d8-b38e-c2b00a0dfa73/2.政企领域嵌入式廉洁风险信息库8.0.zip', '2.政企领域嵌入式廉洁风险信息库8.0.xlsx', '2024-05-17 16:32:56');
INSERT INTO `contrast` VALUES (442, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:15:17');
INSERT INTO `contrast` VALUES (443, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:16:40');
INSERT INTO `contrast` VALUES (445, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:21:16');
INSERT INTO `contrast` VALUES (446, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:21:36');
INSERT INTO `contrast` VALUES (447, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:22:14');
INSERT INTO `contrast` VALUES (448, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:22:20');
INSERT INTO `contrast` VALUES (449, '柳城公司政企市场经营嵌入式防控猎施清单.Excel', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:22:50');
INSERT INTO `contrast` VALUES (452, '20230829 工会认证考试题库省级.xls对比', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:46:01');
INSERT INTO `contrast` VALUES (455, '20230829 工会认证考试题库省级.xls对比', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 15:47:56');
INSERT INTO `contrast` VALUES (456, '20230829 工会认证考试题库省级.xls对比', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 16:11:59');
INSERT INTO `contrast` VALUES (457, '20230829 工会认证考试题库省级.xls对比', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.xls', '/ab5dd02d-b92b-45bf-905a-787fe327e5b5/20230829 工会认证考试题库省级.zip', '20230829 工会认证考试题库省级.xls', '2024-05-20 16:12:08');

-- ----------------------------
-- Table structure for number
-- ----------------------------
DROP TABLE IF EXISTS `number`;
CREATE TABLE `number`  (
  `child_total` bigint NULL DEFAULT NULL,
  `riskpoint_total` bigint NULL DEFAULT NULL,
  `add_total` bigint NULL DEFAULT NULL,
  `del_total` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of number
-- ----------------------------
INSERT INTO `number` VALUES (0, 0, 0, 0);

-- ----------------------------
-- Table structure for result
-- ----------------------------
DROP TABLE IF EXISTS `result`;
CREATE TABLE `result`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contrast_id` bigint NOT NULL,
  `childfile_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `childfile_url` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `riskpoint_total` int NULL DEFAULT NULL,
  `resultfile_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `resultfile_url` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `resultfile_html` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL,
  `resultfileHtml_url` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 481 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of result
-- ----------------------------
INSERT INTO `result` VALUES (443, 384, '1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/025b737a-17ea-42d8-818e-1b8321e6ecc7/1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/025b737a-17ea-42d8-818e-1b8321e6ecc7/对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/025b737a-17ea-42d8-818e-1b8321e6ecc7/对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (444, 384, '1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/b6c31482-b596-4539-aaa9-0029b0123121/1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 33, '对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/b6c31482-b596-4539-aaa9-0029b0123121/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/b6c31482-b596-4539-aaa9-0029b0123121/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (445, 385, '1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/277b6121-c8bb-4df8-854f-25c400fb8fed/1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/277b6121-c8bb-4df8-854f-25c400fb8fed/对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/277b6121-c8bb-4df8-854f-25c400fb8fed/对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (446, 385, '1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/2cde0b02-b457-441a-9c25-87a32ff46aba/1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 33, '对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/2cde0b02-b457-441a-9c25-87a32ff46aba/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/2cde0b02-b457-441a-9c25-87a32ff46aba/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (461, 408, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (462, 412, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (463, 413, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/83e93518-cbe6-4e2b-b564-8ec8b6e31499/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/83e93518-cbe6-4e2b-b564-8ec8b6e31499/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/83e93518-cbe6-4e2b-b564-8ec8b6e31499/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (464, 414, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/5515e828-2eb9-4b70-b02f-f996415fa89f/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/5515e828-2eb9-4b70-b02f-f996415fa89f/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/5515e828-2eb9-4b70-b02f-f996415fa89f/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (465, 415, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/c422c92d-3aeb-4459-a0ae-7d187b5eb62f/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/c422c92d-3aeb-4459-a0ae-7d187b5eb62f/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/c422c92d-3aeb-4459-a0ae-7d187b5eb62f/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (466, 416, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/62ea16ef-ccf4-41ef-96a3-7a2ed97b39bd/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/62ea16ef-ccf4-41ef-96a3-7a2ed97b39bd/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/62ea16ef-ccf4-41ef-96a3-7a2ed97b39bd/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (467, 417, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/e92101c4-a43e-4c27-9f97-781341983a4c/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/e92101c4-a43e-4c27-9f97-781341983a4c/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/e92101c4-a43e-4c27-9f97-781341983a4c/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (468, 418, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/3798c486-901a-40a7-ac16-f95eeb74367c/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/3798c486-901a-40a7-ac16-f95eeb74367c/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/3798c486-901a-40a7-ac16-f95eeb74367c/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (469, 419, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/3798c486-901a-40a7-ac16-f95eeb74367c/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/3798c486-901a-40a7-ac16-f95eeb74367c/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/3798c486-901a-40a7-ac16-f95eeb74367c/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (470, 420, '聊城.xlsx', '/21a693f9-373f-498a-a05b-d506511191bc/聊城.xlsx', 50, '对比结果聊城.xlsx', '/21a693f9-373f-498a-a05b-d506511191bc/对比结果聊城.xlsx', NULL, '/21a693f9-373f-498a-a05b-d506511191bc/对比结果聊城.txt');
INSERT INTO `result` VALUES (471, 421, '聊城.xlsx', '/21a693f9-373f-498a-a05b-d506511191bc/聊城.xlsx', 50, '对比结果聊城.xlsx', '/21a693f9-373f-498a-a05b-d506511191bc/对比结果聊城.xlsx', NULL, '/21a693f9-373f-498a-a05b-d506511191bc/对比结果聊城.txt');
INSERT INTO `result` VALUES (472, 423, '聊城.xlsx', '/0006be85-0b2d-44cf-b685-c558cb5bcdf3/聊城.xlsx', 50, '对比结果聊城.xlsx', '/0006be85-0b2d-44cf-b685-c558cb5bcdf3/对比结果聊城.xlsx', NULL, '/0006be85-0b2d-44cf-b685-c558cb5bcdf3/对比结果聊城.txt');
INSERT INTO `result` VALUES (474, 425, '聊城.xlsx', '/0006be85-0b2d-44cf-b685-c558cb5bcdf3/聊城.xlsx', 50, '对比结果聊城.xlsx', '/0006be85-0b2d-44cf-b685-c558cb5bcdf3/对比结果聊城.xlsx', NULL, '/0006be85-0b2d-44cf-b685-c558cb5bcdf3/对比结果聊城.txt');
INSERT INTO `result` VALUES (476, 427, '聊城.xlsx', '/5e2d9b27-83de-4e97-b1dd-20bbe8fefc4f/聊城.xlsx', 50, '对比结果聊城.xlsx', '/5e2d9b27-83de-4e97-b1dd-20bbe8fefc4f/对比结果聊城.xlsx', NULL, '/5e2d9b27-83de-4e97-b1dd-20bbe8fefc4f/对比结果聊城.txt');
INSERT INTO `result` VALUES (477, 431, '聊城.xlsx', '/14a15859-a418-4d78-8397-d69406519147/聊城.xlsx', 50, '对比结果聊城.xlsx', '/14a15859-a418-4d78-8397-d69406519147/对比结果聊城.xlsx', NULL, '/14a15859-a418-4d78-8397-d69406519147/对比结果聊城.txt');
INSERT INTO `result` VALUES (478, 432, '烟台.xlsx', '/0da806be-8d42-45d7-b8a5-18127f901d71/烟台.xlsx', 50, '对比结果烟台.xlsx', '/0da806be-8d42-45d7-b8a5-18127f901d71/对比结果烟台.xlsx', NULL, '/0da806be-8d42-45d7-b8a5-18127f901d71/对比结果烟台.txt');
INSERT INTO `result` VALUES (479, 433, '1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/32196d6e-59f7-418a-8372-0baf64b1ca01/1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/32196d6e-59f7-418a-8372-0baf64b1ca01/对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/32196d6e-59f7-418a-8372-0baf64b1ca01/对比结果1.菏泽分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (480, 434, '1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司_copy.xlsx', '/b41c0e91-c325-43e0-b97a-40756afd8894/1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司_copy.xlsx', 4, '对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司_copy.xlsx', '/b41c0e91-c325-43e0-b97a-40756afd8894/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司_copy.xlsx', NULL, '/b41c0e91-c325-43e0-b97a-40756afd8894/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司_copy.txt');
INSERT INTO `result` VALUES (481, 435, '1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/6b33efe7-31a4-42e8-8199-9e839829efc2/1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 33, '对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/6b33efe7-31a4-42e8-8199-9e839829efc2/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/6b33efe7-31a4-42e8-8199-9e839829efc2/对比结果1.济宁分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (482, 442, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (483, 443, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (484, 445, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (485, 446, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (486, 447, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (487, 448, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (488, 449, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (489, 452, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (490, 455, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (491, 456, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');
INSERT INTO `result` VALUES (492, 457, '聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', 50, '对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.xlsx', NULL, '/91492c9c-300d-4f94-8e8e-786befe2e1e6/对比结果聊城分公司-政企市场经营嵌入式防控措施清单-市公司.txt');

SET FOREIGN_KEY_CHECKS = 1;
