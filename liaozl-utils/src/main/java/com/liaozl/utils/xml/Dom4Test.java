package com.liaozl.photo.util.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author liaozuliang
 * @date 2015年9月29日
 */
public class Dom4Test {

	static final Logger log = Logger.getLogger(Dom4Test.class);

	private static final Dom4Test INSTANCE = new Dom4Test();

	// key:businessType_accountRole_houseUseType_housePublishType, value:weightType
	private static final Map<String, String> RELATION_MAP = new HashMap<String, String>();

	// key:weightType, value:List<RedPacketAmountWeight>
	// private static final Map<String, List<RedPacketAmountWeight>> WEIGHT_TYPE_MAP = new HashMap<String,
	// List<RedPacketAmountWeight>>();

	static {
		Element root = null;

		SAXReader reader = new SAXReader();
		try {
			InputStream inputStream = Dom4Test.class.getResourceAsStream("/redPacketAmountWeight.xml");
			root = reader.read(inputStream).getRootElement();
		} catch (Exception e) {
			log.error("解析红包金额权重配置文件出错：", e);
		}

		List<Element> relationList = root.selectNodes("relation");
		if (relationList != null) {
			for (Element relation : relationList) {
				String key = relation.attributeValue("businessType") + "_" + relation.attributeValue("accountRole")
						+ "_" + relation.attributeValue("houseUseType") + "_"
						+ relation.attributeValue("housePublishType");
				String weightType = relation.attributeValue("weightType");

				System.out.println(key + "-" + weightType);

				RELATION_MAP.put(key, weightType);
			}
		}

		System.out.println();

		List<Element> weightList = root.selectNodes("weight");
		if (weightList != null) {
			for (Element weight : weightList) {
				String weightType = weight.attributeValue("type");

				List<Element> itemList = weight.selectNodes("item");
				if (itemList != null) {
					for (Element item : itemList) {
						double itemAmount = Double.parseDouble(item.attributeValue("amount"));
						int itemWeight = Integer.parseInt(item.attributeValue("weight"));
						System.out.println(weightType + "-" + itemAmount + "-" + itemWeight);
					}
				}

				System.out.println();
			}
		}
	}

	private Dom4Test() {

	}

	public static Dom4Test getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) {
		System.out.println(RELATION_MAP.size());
	}
}
