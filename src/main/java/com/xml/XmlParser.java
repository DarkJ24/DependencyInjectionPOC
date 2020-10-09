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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.darkj24.ioc.models.Constants.*;

public class XmlParser {

    private String filePath = "context.xml";
    private Map<String, DataStructureBean> mapBeans = new HashMap<String, DataStructureBean>();
    private File inputFile;

    public XmlParser(String filePath) {
        this.filePath = filePath;
        this.inputFile = new File(this.filePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

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
                    //Create bean
                    DataStructureBean bean = new DataStructureBean.BeanBuilder(element.getAttribute(TAG_CLASS))
                            .addScope(element.getAttribute(TAG_SCOPE))
                            .addAutowiringMode(element.getAttribute(TAG_AUTOWIRING_MODE))
                            .addLazyInit(Boolean.parseBoolean(element.getAttribute(TAG_LAZY)))
                            .addInitMethod(element.getAttribute(TAG_INIT))
                            .addDestroyMethod(element.getAttribute(TAG_DESTROY))
                            .build();
                    //Add constructor arguments
                    addArguments(element, bean, TAG_CONSTRUCTOR);
                    //Add properties
                    addArguments(element, bean, TAG_PROPERTY);
                    //Add bean to map
                    mapBeans.put(element.getAttribute(TAG_ID),bean);
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

    public void addArguments(Element element, DataStructureBean bean, String id){
        if(element.getElementsByTagName(id).getLength() != 0) {

            for (int j = 0; j < element.getElementsByTagName(id).getLength(); j++) {
                String tempKey;
                String tempValue = null;
                String tempRef = null;
                if(id.equals(TAG_PROPERTY)) {
                    tempKey = element.getElementsByTagName(id).item(j).getAttributes().
                            getNamedItem(TAG_PROPERTY_NAME).toString().replaceAll("\"", "").replace("name=", "");
                }else{
                    tempKey = element.getElementsByTagName(id).item(j).getAttributes().
                            getNamedItem(TAG_CONSTRUCTOR_INDEX).toString().replaceAll("\"", "").replace("index=", "");
                }
                if(element.getElementsByTagName(id).item(j).getAttributes().getNamedItem(TAG_VALUE) != null) {
                    tempValue = element.getElementsByTagName(id).item(j).getAttributes().
                            getNamedItem(TAG_VALUE).toString().replaceAll("\"", "").replace("value=", "");
                }
                if(element.getElementsByTagName(id).item(j).getAttributes().getNamedItem(TAG_REF) !=null) {
                    tempRef = element.getElementsByTagName(id).item(j).getAttributes().
                            getNamedItem(TAG_REF).toString().replaceAll("\"", "").replace("ref=", "");
                }

                if(id.equals(TAG_PROPERTY)) {
                    if (tempValue != null && tempRef != null) {
                        bean.addNewProperty(tempKey, tempValue, tempRef);
                    } else if (tempValue != null) {
                        bean.addNewProperty(tempKey, tempValue, null);
                    } else {
                        bean.addNewProperty(tempKey, null, tempRef);
                    }
                } else {
                    if (tempValue != null && tempRef != null) {
                        bean.addNewConstructorIndex(tempKey, tempValue, tempRef);
                    } else if (tempValue != null) {
                        bean.addNewConstructorIndex(tempKey, tempValue, null);
                    } else {
                        bean.addNewConstructorIndex(tempKey, null, tempRef);
                    }
                }
            }
        }

    }

    public ArrayList<String> getAllCls(){
        ArrayList<String> classes = new ArrayList<String>();
        Iterator<Map.Entry<String,DataStructureBean>> iterator = mapBeans.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String,DataStructureBean> set = (Map.Entry<String,DataStructureBean>) iterator.next();
            classes.add(set.getValue().getCls());
        }
        return classes;
    }

    public ArrayList<String> getAllBeanID(){
        ArrayList<String> beanIds = new ArrayList<String>();
        Iterator<Map.Entry<String,DataStructureBean>> iterator = mapBeans.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String,DataStructureBean> set = (Map.Entry<String,DataStructureBean>) iterator.next();
            beanIds.add(set.getKey());
        }
        return beanIds;
    }

    public ArrayList<String> getAllPropertiesFromBeanId(String beanId){
        ArrayList<String> properties = new ArrayList<String>();
        Iterator<Map.Entry<String,DataStructureProperties>> iterator =
                mapBeans.get(beanId).getProperties().entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String,DataStructureProperties> set = (Map.Entry<String,DataStructureProperties>) iterator.next();
            properties.add(set.getKey());
        }
        return properties;
    }

    public String getCls(String beanId){
        return mapBeans.get(beanId).getCls();
    }
    public String getScope(String beanId){
        return mapBeans.get(beanId).getScope();
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
