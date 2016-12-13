package com.liaozl.utils.xml;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author liaozuliang
 * @date 2016-12-13
 */
public class XmlPojoUtil {

    public static final String pojoToXml(Object pojo) {
        return pojoToXml(pojo, null);
    }

    public static final String pojoToXml(Object pojo, List<Class> pojoClass) {
        if (pojo == null) {
            return null;
        }

        XStream xStream = new XStream();
        if (pojoClass != null && pojoClass.size() > 0) {
            for (Class c : pojoClass) {
                xStream.alias(c.getSimpleName(), c);
            }
        }

        return xStream.toXML(pojo);
    }

    public static final <T> T xmlToPojo(String xmlStr) {
        return xmlToPojo(xmlStr, null);
    }

    public static final <T> T xmlToPojo(String xmlStr, List<Class> pojoClass) {
        if (StringUtils.isBlank(xmlStr)) {
            return null;
        }

        XStream xStream = new XStream();
        if (pojoClass != null && pojoClass.size() > 0) {
            for (Class c : pojoClass) {
                xStream.alias(c.getSimpleName(), c);
            }
        }

        return (T) xStream.fromXML(xmlStr);
    }
}
