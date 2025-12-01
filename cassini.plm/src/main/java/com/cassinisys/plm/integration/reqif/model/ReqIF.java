package com.cassinisys.plm.integration.reqif.model;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ReqIF {
    private ReqIFHeader header;
    private ReqIFContent content;
    private ReqIFToolExtension toolExtension;

    public ReqIFHeader getHeader() {
        return header;
    }

    public void setHeader(ReqIFHeader header) {
        this.header = header;
    }

    public ReqIFContent getContent() {
        return content;
    }

    public void setContent(ReqIFContent content) {
        this.content = content;
    }

    public ReqIFToolExtension getToolExtension() {
        return toolExtension;
    }

    public void setToolExtension(ReqIFToolExtension toolExtension) {
        this.toolExtension = toolExtension;
    }

    public Element toXml() {
        Element elem = new Element("REQ-IF", "http://www.omg.org/spec/ReqIF/20110401/reqif.xsd");
        Namespace xsiNs = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        elem.addNamespaceDeclaration(xsiNs);
        elem.addNamespaceDeclaration(Namespace.getNamespace("reqif", "http://www.omg.org/spec/ReqIF/20110401/reqif.xsd"));
        elem.addNamespaceDeclaration(Namespace.getNamespace("reqif-xhtml", "http://www.w3.org/1999/xhtml"));
        elem.addNamespaceDeclaration(Namespace.getNamespace("xhtml", "http://www.w3.org/1999/xhtml"));
        elem.addContent(header.toXml());
        elem.addContent(content.toXml());
        return elem;
    }

    public String toXmlString() {
        XMLOutputter xml = new XMLOutputter();
        String declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xml.setFormat(Format.getPrettyFormat());
        String reqIfXml = xml.outputString(toXml());
        reqIfXml = reqIfXml.replaceAll(" xmlns=\"\"", "");
        return declaration + reqIfXml;
    }
}
