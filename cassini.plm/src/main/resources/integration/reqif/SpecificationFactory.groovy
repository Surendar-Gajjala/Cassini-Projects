package integration.reqif

import com.cassinisys.plm.integration.reqif.model.*
import groovy.xml.StreamingMarkupBuilder

import java.text.SimpleDateFormat

def createSpecification(elem, specObjectsMap, specTypesMap, attDefsMap, enumValuesMap) {
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    def specification = new Specification()
    specification.setType(specTypesMap[""+elem.'TYPE'.'SPECIFICATION-TYPE-REF'])

    def attsXml = elem.'VALUES'.children()
    def attributeValues = []
    attsXml.each { attXml ->
        def tagName = attXml.name()
        def attributeValue = null

        if(tagName == "ATTRIBUTE-VALUE-STRING") {
            attributeValue = new AttributeValueString()
            ((AttributeValueString)attributeValue).setValue(""+attXml.@'THE-VALUE')
        }
        else if(tagName == "ATTRIBUTE-VALUE-INTEGER") {
            attributeValue = new AttributeValueInteger()
            ((AttributeValueInteger)attributeValue).setValue(Integer.parseInt(""+attXml.@'THE-VALUE'))
        }
        else if(tagName == "ATTRIBUTE-VALUE-REAL") {
            attributeValue = new AttributeValueReal()
            ((AttributeValueReal)attributeValue).setValue(Float.parseFloat(""+attXml.@'THE-VALUE'))
        }
        else if(tagName == "ATTRIBUTE-VALUE-BOOLEAN") {
            attributeValue = new AttributeValueBoolean()
            ((AttributeValueBoolean)attributeValue).setValue(Boolean.parseBoolean(""+attXml.@'THE-VALUE'))
        }
        else if(tagName == "ATTRIBUTE-VALUE-DATE") {
            attributeValue = new AttributeValueDate()
            ((AttributeValueDate)attributeValue).setValue(dateFormat.parse(""+attXml.@'THE-VALUE'))
        }
        else if(tagName == "ATTRIBUTE-VALUE-XHTML") {
            attributeValue = new AttributeValueXhtml()
            def xhtml = "";
            attXml.'THE-VALUE'.children().each { child ->
                xhtml += new StreamingMarkupBuilder().bindNode(child).toString();
            }
            ((AttributeValueXhtml)attributeValue).setValue(xhtml)
        }
        else if(tagName == "ATTRIBUTE-VALUE-ENUMERATION") {
            attributeValue = new AttributeValueEnumeration()

            def enumValues = []
            def valuesXml = attXml.'VALUES'.'ENUM-VALUE-REF'
            valuesXml.each { valueXml ->
                enumValues.add(enumValuesMap[""+valueXml])
            }
            ((AttributeValueEnumeration) attributeValue).setValues(enumValues)
        }

        def identifier = "" + attXml.'DEFINITION'.children()[0]
        attributeValue.setDefinition(attDefsMap[identifier])
        attributeValues.add(attributeValue)
    }

    if(elem.'CHILDREN'.size() > 0) {
        createHierarchy(specification, elem.'CHILDREN', specObjectsMap)
    }

    specification.setIdentifier(""+elem.@'IDENTIFIER')
    specification.setLastChange(dateFormat.parse(""+elem.@'LAST-CHANGE'))
    specification.setLongName(""+elem.@'LONG-NAME')
    specification.setDescription(""+elem.@'DESC')

    specification.setValues(attributeValues)

    return specification
}

def createHierarchy(parent, childrenElem, specObjectsMap) {
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    def hierElems = childrenElem.'SPEC-HIERARCHY'
    def children = []
    hierElems.each { hierElem ->
        def specHierachy = new SpecHierarchy()

        specHierachy.setIdentifier(""+hierElem.@'IDENTIFIER')
        specHierachy.setLastChange(dateFormat.parse(""+hierElem.@'LAST-CHANGE'))
        specHierachy.setLongName(""+hierElem.@'LONG-NAME')
        specHierachy.setDescription(""+hierElem.@'DESC')

        specHierachy.setObject(specObjectsMap[""+hierElem.'OBJECT'.'SPEC-OBJECT-REF'])
        children.add(specHierachy)
        if(hierElem.'CHILDREN'.size() > 0) {
            createHierarchy(specHierachy, hierElem.'CHILDREN', specObjectsMap)
        }
    }
    parent.children = children
}