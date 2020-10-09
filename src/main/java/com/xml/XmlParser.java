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

                    DataStructureBean beans = new DataStructureBean.BeanBuilder(element.getAttribute(TAG_CLASS))
                            .addScope(element.getAttribute(TAG_SCOPE))
                            .addConstructorArg(element.getAttribute(TAG_CONSTRUCTOR))
                            .addAutowiringMode(element.getAttribute(TAG_AUTOWIRING_MODE))
                            .addLazyInit(Boolean.parseBoolean(element.getAttribute(TAG_LAZY)))
                            .addInitMethod(element.getAttribute(TAG_INIT))
                            .addDestroyMethod(element.getAttribute(TAG_DESTROY))
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
