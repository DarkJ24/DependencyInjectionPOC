package com.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XmlParser {

    static final String FILE_PATH = "context.xml";
    static final String TAG_BEAN = "bean";
    static final String TAG_ID = "id";
    static final String TAG_CLASS = "class";
    static final String TAG_SCOPE = "scope";
    static final String TAG_CONSTRUCTOR = "constructorArg";
    static final String TAG_AUTOWIRING_MODE = "autowiringMode";
    static final String TAG_LAZY = "lazyInit";
    static final String TAG_INIT = "initMethod";
    static final String TAG_DESTROY = "destroyMethod";
    static final String TAG_PROPERTY = "property";
    static final String TAG_PROPERTY_NAME = "name";
    static final String TAG_PROPERTY_VALUE = "value";
    static final String TAG_PROPERTY_REF = "ref";


    File inputFile = new File(FILE_PATH);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    Map<String, DataStructureBean> mapBeans = new HashMap<String, DataStructureBean>();

    {
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            doc.getDocumentElement().getNodeName();
            System.out.println(doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName(TAG_BEAN);

            for (int i=0; i<nodeList.getLength(); i++){
                Node node = nodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    System.out.println(element.getAttribute(TAG_CLASS));

                    DataStructureBean beans = new DataStructureBean.BeanBuilder(element.getAttribute(TAG_CLASS))
                            .scope(element.getAttribute(TAG_SCOPE))
                            .constructorArg(element.getAttribute(TAG_CONSTRUCTOR))
                            .autowiringMode(element.getAttribute(TAG_AUTOWIRING_MODE))
                            .lazyInit(Boolean.parseBoolean(element.getAttribute(TAG_LAZY)))
                            .initMethod(element.getAttribute(TAG_INIT))
                            .destroyMethod(element.getAttribute(TAG_DESTROY))
                            .build();

                    if(element.getElementsByTagName(TAG_PROPERTY).getLength() != 0) {

                        for (int j = 0; j < element.getElementsByTagName(TAG_PROPERTY).getLength(); j++) {

                            String tempName = element.getElementsByTagName(TAG_PROPERTY).item(j).getAttributes().
                                    getNamedItem(TAG_PROPERTY_NAME).toString().replaceAll("\"", "").replace("name=","");
                            String tempValue = null;
                            String tempRef = element.getElementsByTagName(TAG_PROPERTY).item(j).getAttributes().
                                    getNamedItem(TAG_PROPERTY_REF).toString().replaceAll("\"", "").replace("ref=","");

                            if (tempValue != null && tempRef != null){
                                beans.addNewProperty(tempName,tempValue,tempRef);
                            } else if (tempValue != null) {
                                beans.addNewProperty(tempName,tempValue,null);
                            } else{
                                beans.addNewProperty(tempName,null,tempRef);
                            }

                        }
                    }
                    mapBeans.put(element.getAttribute(TAG_ID),beans);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCls(String beanId){
        return mapBeans.get(beanId).getCls();
    }
    public String getScope(String beanId){
        return mapBeans.get(beanId).getScope();
    }
    public String getConstructorArg(String beanId){
        return mapBeans.get(beanId).getConstructorArg();
    }
    public String getAutowiringMode(String beanId){
        return mapBeans.get(beanId).getAutowiringMode();
    }
    public boolean getLazyInit(String beanId){
        return mapBeans.get(beanId).isLazyInit();
    }
    public String getInitMethod(String beanId){ return mapBeans.get(beanId).getInitMethod(); }
    public String getDestroyMethod(String beanId){ return mapBeans.get(beanId).getDestroyMethod(); }
    public String getPropertyValue(String beanId, String propertyName){ return mapBeans.get(beanId).getPropertyValue(propertyName); }
    public String getPropertyRef(String beanId, String propertyName){ return mapBeans.get(beanId).getPropertyRef(propertyName); }
}
