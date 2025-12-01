package integration.reqif

import com.cassinisys.plm.integration.reqif.model.*
import com.cassinisys.plm.service.integration.ReqIFService
import groovy.xml.StreamingMarkupBuilder

import java.text.SimpleDateFormat

def createSpecObject(elem, specTypesMap, attDefsMap, enumValuesMap) {
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    def specObject = new SpecObject()

    specObject.setType(specTypesMap[""+elem.'TYPE'.'SPEC-OBJECT-TYPE-REF'])

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
            ((AttributeValueDate)attributeValue).setValue(ReqIFService.fromStringToDate(""+attXml.@'THE-VALUE'))
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

    specObject.setIdentifier(""+elem.@'IDENTIFIER')
    specObject.setLastChange(dateFormat.parse(""+elem.@'LAST-CHANGE'))
    specObject.setLongName(""+elem.@'LONG-NAME')
    specObject.setDescription(""+elem.@'DESC')

    specObject.setValues(attributeValues)

    return specObject

}