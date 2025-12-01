package com.cassinisys.plm.integration.reqif.model;

import com.cassinisys.plm.service.integration.ReqIF2Cassini;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributeValueXhtml extends AttributeValue {
    private static List<ImageObject> imageObjects = new ArrayList<>();
    private String value;
    private String originalValue;
    private boolean isSimplified;

    public AttributeValueXhtml(AttributeDefinition def) {
        super(def);
    }

    public static List<ImageObject> getImageObjects() {
        return imageObjects;
    }

    public static void clearImageObjects() {
        imageObjects.clear();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.value = normalizeValue();
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public boolean isSimplified() {
        return isSimplified;
    }

    public void setSimplified(boolean simplified) {
        isSimplified = simplified;
    }

    public Element toXml() {
        Element elem = createXmlElement("ATTRIBUTE-VALUE-XHTML");
        Element valueElem = new Element("THE-VALUE");
        try {
            Element valueNode = getValueAsElement();
            Namespace ns = Namespace.getNamespace("reqif-xhtml", "http://www.w3.org/1999/xhtml");
            setNamespace(ns, valueNode);
            valueNode.detach();
            List<Content> contents = valueNode.getContent();
            List<Content> newContent = new ArrayList<>();
            for (Content c : contents) {
                newContent.add(c.clone());
            }
            valueElem.setContent(newContent);
            elem.addContent(valueElem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elem;
    }

    public Element getValueAsElement() {
        Element valueNode = null;
        try {
            String xml = "<valuenode " +
                    "xmlns=\"http://www.omg.org/spec/ReqIF/20110401/reqif.xsd\" " +
                    "xmlns:reqif=\"http://www.omg.org/spec/ReqIF/20110401/reqif.xsd\" " +
                    "xmlns:reqif-xhtml=\"http://www.w3.org/1999/xhtml\" " +
                    "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" " +
                    "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                    this.value +
                    "</valuenode>";
            org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(xml);
            org.w3c.dom.Document doc = new W3CDom().fromJsoup(jsoupDoc);
            DOMBuilder domb = new DOMBuilder();
            Document jdomDoc = domb.build(doc);
            XPathFactory xFactory = XPathFactory.instance();
            XPathExpression<Element> expr = xFactory.compile("/html/body", Filters.element());
            List<Element> nodes = expr.evaluate(jdomDoc);
            if (nodes.size() > 0) {
                Element vNode = (Element) nodes.get(0).getContent().get(0);
                valueNode = vNode.detach();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueNode;
    }

    private void setNamespace(Namespace ns, Element element) {
        List<Element> children = element.getChildren();
        for (Element c : children) {
            checkForImages(c, ns);
            c.setNamespace(ns);
            setNamespace(ns, c);
        }
    }

    private void checkForImages(Element element, Namespace ns) {
        String tag = element.getName();
        if (tag.equalsIgnoreCase("img")) {
            ImageObject imageObject = new ImageObject();
            String dataFile = element.getAttributeValue("data-filename");
            String imageData = element.getAttributeValue("src");
            imageObject.setDataFileName(dataFile);
            imageObject.setImageData(imageData);
            String w = "100";
            String h = "100";
            String s = element.getAttributeValue("width");
            if (s != null && !s.isEmpty()) {
                if (s.endsWith("px")) {
                    s = s.replaceAll("px", "");
                }
                w = s;
            }
            s = element.getAttributeValue("height");
            if (s != null && !s.isEmpty()) {
                if (s.endsWith("px")) {
                    s = s.replaceAll("px", "");
                }
                h = s;
            }
            String style = element.getAttributeValue("style");
            Element parent = element.getParentElement();
            Element o = new Element("object");
            o.setNamespace(ns);
            o.setAttribute("width", "100");
            o.setAttribute("height", "100");
            o.setAttribute("type", "application/rtf");
            imageObject.setOleFileName(imageObject.getIdentifier() + ".ole");
            o.setAttribute("data", Specification.specName + "/" + imageObject.getOleFileName());
            Element i = new Element("object");
            i.setNamespace(ns);
            if (style != null && !style.isEmpty()) {
                i.setAttribute("style", style);
            }
            i.setAttribute("type", "image/" + FilenameUtils.getExtension(dataFile));
            imageObject.setImageFileName(imageObject.getIdentifier() + "." + FilenameUtils.getExtension(dataFile));
            i.setAttribute("data", Specification.specName + "/" + imageObject.getImageFileName());
            o.addContent(i);
            parent.setContent(parent.getChildren().indexOf(element), o);
            imageObjects.add(imageObject);
        }
    }

    private String normalizeValue() {
        String normalizedValue = "";
        Element valueNode = getValueAsElement();
        stripNamespaces(valueNode);
        XMLOutputter outp = new XMLOutputter();
        List<Content> children = valueNode.getContent();
        for (Content child : children) {
            if (child instanceof Element) {
                normalizedValue += outp.outputString((Element) child);
            } else if (child instanceof Text) {
                normalizedValue += ((Text) child).getText();
            }
        }
        return normalizedValue;
    }

    private void stripNamespaces(Element element) {
        List<Element> children = element.getChildren();
        for (Element child : children) {
            child.setNamespace(Namespace.NO_NAMESPACE);
            stripNamespaces(child);
        }
    }

    public List<String> getFileNames() {
        List<String> fileNames = new ArrayList<>();
        Element valueNode = getValueAsElement();
        stripNamespaces(valueNode);
        List<Element> children = valueNode.getChildren();
        for (Element child : children) {
            if (child.getName().toLowerCase().equalsIgnoreCase("object")) {
                fileNames.add(child.getAttributeValue("data"));
            }
        }
        return fileNames;
    }

    private void recursivelyUpdateImages(Element elem, Map<String, ReqIF2Cassini.ReqIFArchiveFile> archiveFilesMap) {
        List<Content> contents = elem.getContent();
        for (Content content : contents) {
            if (content instanceof Element) {
                Element child = (Element) content;
                String ename = child.getName();
                if (ename.equalsIgnoreCase("object")) {
                    String type = child.getAttributeValue("type");
                    if (type.equalsIgnoreCase("application/rtf")) {
                        List<Element> subchildren = child.getChildren();
                        if (subchildren.size() == 1) {
                            Element subchild = subchildren.get(0);
                            String fname = subchild.getAttributeValue("data");
                            if (fname != null && !fname.isEmpty()) {
                                ReqIF2Cassini.ReqIFArchiveFile file = archiveFilesMap.get(fname);
                                if (file != null) {
                                    String base64 = Base64.encodeBase64String(file.getBytes());
                                    if (base64 != null && !base64.isEmpty()) {
                                        Element e = new Element("img");
                                        String w = "100", h = "100";
                                        String s = child.getAttributeValue("width");
                                        if (s != null) {
                                            w = s;
                                        }
                                        s = child.getAttributeValue("height");
                                        if (s != null) {
                                            h = s;
                                        }
                                        String t = subchild.getAttributeValue("type");
                                        String style = subchild.getAttributeValue("style");
                                        if (style != null && !style.isEmpty()) {
                                            e.setAttribute("style", style);
                                        } else {
                                            e.setAttribute("width", w);
                                            e.setAttribute("height", h);
                                        }
                                        e.setAttribute("src", "data:" + t + ";base64, " + base64);
                                        int index = elem.indexOf(child);
                                        elem.setContent(index, e);
                                    } else {
                                        //System.out.println("WARNING: Image base64 data is empty");
                                    }
                                } else {
                                    //System.out.println("WARNING: Image file not found in the archive - " + fname);
                                }
                            } else {
                                //System.out.println("WARNING: Image file name is empty");
                            }
                        }
                    }
                } else {
                    recursivelyUpdateImages(child, archiveFilesMap);
                }
            }
        }
    }

    public void updateImages(Map<String, ReqIF2Cassini.ReqIFArchiveFile> archiveFilesMap) {
        Element valueNode = getValueAsElement();
        stripNamespaces(valueNode);
        recursivelyUpdateImages(valueNode, archiveFilesMap);
        XMLOutputter outp = new XMLOutputter();
        List<Content> children = valueNode.getContent();
        String normalizedValue = "";
        for (Content child : children) {
            if (child instanceof Element) {
                normalizedValue += outp.outputString((Element) child);
            } else if (child instanceof Text) {
                normalizedValue += ((Text) child).getText();
            }
        }
        this.value = normalizedValue;
    }

}
