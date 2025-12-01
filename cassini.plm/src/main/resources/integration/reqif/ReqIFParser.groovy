package integration.reqif

import com.cassinisys.plm.integration.reqif.model.*

import java.text.SimpleDateFormat

GroovyShell shell = new GroovyShell()
def datatypeDefinitionFactory = shell.parse(new File(_scriptDir, 'DatatypeDefinitionFactory.groovy'))
def attributeDefinitionFactory = shell.parse(new File(_scriptDir, 'AttributeDefinitionFactory.groovy'))
def specObjectFactory = shell.parse(new File(_scriptDir, 'SpecObjectFactory.groovy'))
def specificationFactory = shell.parse(new File(_scriptDir, 'SpecificationFactory.groovy'))

def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
def router = new XmlSlurper().parse(_reqIfInputStream)
def header = router.'THE-HEADER'.'REQ-IF-HEADER'
def content = router.'CORE-CONTENT'.'REQ-IF-CONTENT'
def toolExtenions = router.'TOOL-EXTENSIONS'


def reqIF = new ReqIF()

/* Build the header */
ReqIFHeader reqIFHeader = new ReqIFHeader()
reqIFHeader.setIdentifier(header.@'IDENTIFIER'.text())
reqIFHeader.setComment(header.'COMMENT'.text())
reqIFHeader.setCreationDate(dateFormat.parse(header.'CREATION-TIME'.text()))
reqIFHeader.setTitle(header.'TITLE'.text())
reqIFHeader.setReqIFToolId(header.'REQ-IF-TOOL-ID'.text())
reqIFHeader.setReqIFVersion(header.'REQ-IF-VERSION'.text())
reqIFHeader.setSourceToolId(header.'SOURCE-TOOL-ID'.text())
reqIF.setHeader(reqIFHeader)


/* Build the content */

def reqIFContent = new ReqIFContent()

/* Build datatype definitions */
def datatypesXml = content.'DATATYPES'
def datatypesMap = [:]
def datatypeDefinitions = []
def enumValuesMap = [:]

datatypesXml.children().each { child ->
    def datatype = datatypeDefinitionFactory.createDatatypeDefinition(child)
    datatypesMap[datatype.identifier] = datatype
    datatypeDefinitions.add(datatype)

    if(datatype instanceof DatatypeDefinitionEnumeration) {
        def values = ((DatatypeDefinitionEnumeration)datatype).specifiedValues
        values.each { value ->
            enumValuesMap[value.identifier] = value
        }
    }
}

reqIFContent.setDatatypeDefinitions(datatypeDefinitions)

/* Build spec object types */
def specTypesXml = content.'SPEC-TYPES'.children()
def specTypes = []
def specTypesMap = [:]
def attDefsMap = [:]

specTypesXml.each { type ->
    SpecType specType = null
    if(type.name() == "SPEC-OBJECT-TYPE") {
        specType = new SpecObjectType()
    }
    else if(type.name() == "SPECIFICATION-TYPE") {
        specType = new SpecificationType()
    }

    specType.setIdentifier(""+type.@'IDENTIFIER')
    specType.setLastChange(dateFormat.parse(""+type.@'LAST-CHANGE'))
    specType.setLongName(""+type.@'LONG-NAME')
    specType.setDescription(""+type.@'DESC')

    def specAttributesXml = type.'SPEC-ATTRIBUTES'
    def specAtts = []
    specAttributesXml.children().each{ attXml ->
        def attributeDefinition = attributeDefinitionFactory.createAttributeDefinition(attXml, datatypesMap)
        if(attributeDefinition != null) {
            specAtts.add(attributeDefinition)
            attDefsMap[attributeDefinition.identifier] = attributeDefinition
        }
    }

    specType.setSpecAttributes(specAtts)
    specTypes.add(specType)
    specTypesMap[specType.identifier] = specType
}

reqIFContent.setSpecTypes(specTypes)


/* Build spec objects */
def specObjectsXml = content.'SPEC-OBJECTS'.'SPEC-OBJECT'
def specObjects = []
def specObjectsMap = [:]
specObjectsXml.each{ specObjectXml ->
    def specObject = specObjectFactory.createSpecObject(specObjectXml, specTypesMap, attDefsMap, enumValuesMap)
    specObjectsMap[specObject.identifier] = specObject
    specObjects.add(specObject)
}

reqIFContent.setSpecObjects(specObjects)

/* Build specification objects */
def specificationsXml = content.'SPECIFICATIONS'.'SPECIFICATION'
def specifications = []
specificationsXml.each { specificationXml ->
    def specification = specificationFactory.createSpecification(specificationXml, specObjectsMap, specTypesMap, attDefsMap, enumValuesMap)
    specifications.add(specification)
}
reqIFContent.setSpecifications(specifications)

reqIF.setContent(reqIFContent)

return reqIF

