package integration.reqif

import com.cassinisys.plm.integration.reqif.model.*

import java.text.SimpleDateFormat

def createAttributeDefinition(elem, datatypes) {
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    AttributeDefinition attributeDefinition = null
    def tagName = elem.name()

    if(tagName == "ATTRIBUTE-DEFINITION-STRING") {
        attributeDefinition = new AttributeDefinitionString()
    }
    else if(tagName == "ATTRIBUTE-DEFINITION-INTEGER") {
        attributeDefinition = new AttributeDefinitionInteger()
    }
    else if(tagName == "ATTRIBUTE-DEFINITION-REAL") {
        attributeDefinition = new AttributeDefinitionReal()
    }
    else if(tagName == "ATTRIBUTE-DEFINITION-BOOLEAN") {
        attributeDefinition = new AttributeDefinitionBoolean()
    }
    else if(tagName == "ATTRIBUTE-DEFINITION-DATE") {
        attributeDefinition = new AttributeDefinitionDate()
    }
    else if(tagName == "ATTRIBUTE-DEFINITION-XHTML") {
        attributeDefinition = new AttributeDefinitionXhtml()
    }
    else if(tagName == "ATTRIBUTE-DEFINITION-ENUMERATION") {
        attributeDefinition = new AttributeDefinitionEnumeration()
    }

    def datatypeRef = elem.'TYPE'.children()[0].text()
    def datatype = datatypes[datatypeRef]
    attributeDefinition.setType(datatype)


    if(attributeDefinition != null) {
        attributeDefinition.setIdentifier(""+elem.@'IDENTIFIER')
        attributeDefinition.setLastChange(dateFormat.parse(""+elem.@'LAST-CHANGE'))
        attributeDefinition.setLongName(""+elem.@'LONG-NAME')
        attributeDefinition.setDescription(""+elem.@'DESC')
    }

    return attributeDefinition

}