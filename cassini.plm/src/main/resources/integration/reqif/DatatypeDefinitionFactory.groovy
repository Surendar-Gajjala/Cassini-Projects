package integration.reqif

import com.cassinisys.plm.integration.reqif.model.*

import java.text.SimpleDateFormat


def createDatatypeDefinition(elem) {
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

    def datatype = null
    def elemName = elem.name()

    if(elemName == "DATATYPE-DEFINITION-STRING") {
        datatype = new DatatypeDefinitionString()
        try {
            datatype.setMaxLength(Integer.parseInt(""+elem.@'MAX-LENGTH'))
        } catch (NumberFormatException e) {
        }
    }
    else if(elemName == "DATATYPE-DEFINITION-INTEGER") {
        datatype = new DatatypeDefinitionInteger()
        try {
            datatype.setMax(Integer.parseInt(""+elem.@'MAX'))
            datatype.setMin(Integer.parseInt(""+elem.@'MIN'))
        } catch (NumberFormatException e) {
        }
    }
    else if(elemName == "DATATYPE-DEFINITION-REAL") {
        datatype = new DatatypeDefinitionReal()
        try {
            datatype.setMax(Float.parseFloat(""+elem.@'MAX'))
            datatype.setMin(Float.parseFloat(""+elem.@'MIN'))
            datatype.setAccuracy(Integer.parseInt(""+elem.@'ACCURACY'))
        } catch (NumberFormatException e) {
        }
    }
    else if(elemName == "DATATYPE-DEFINITION-DATE") {
        datatype = new DatatypeDefinitionDate()
    }
    else if(elemName == "DATATYPE-DEFINITION-BOOLEAN") {
        datatype = new DatatypeDefinitionBoolean()
    }
    else if(elemName == "DATATYPE-DEFINITION-XHTML") {
        datatype = new DatatypeDefinitionXhtml()
    }
    else if(elemName == "DATATYPE-DEFINITION-ENUMERATION") {
        datatype = new DatatypeDefinitionEnumeration()
        def enumvalues = elem.'SPECIFIED-VALUES'.'ENUM-VALUE'
        def values = []
        enumvalues.each { value ->
            EnumValue enumValue = new EnumValue();

            enumValue.setIdentifier(""+value.@'IDENTIFIER')
            enumValue.setLastChange(dateFormat.parse(""+value.@'LAST-CHANGE'))
            enumValue.setLongName(""+value.@'LONG-NAME')
            enumValue.setDescription(""+value.@'DESC')

            EmbeddedValue ev = new EmbeddedValue();
            ev.setKey(Integer.parseInt(""+ value.'PROPERTIES'.'EMBEDDED-VALUE'.@'KEY'))
            ev.setOtherContent(""+ value.'PROPERTIES'.'EMBEDDED-VALUE'.@'OTHER-CONTENT')
            enumValue.setEmbeddedValue(ev)
            values.add(enumValue)
        }
        datatype.specifiedValues = values
    }

    if(datatype != null) {
        datatype.setIdentifier(""+elem.@'IDENTIFIER')
        datatype.setLastChange(dateFormat.parse(""+elem.@'LAST-CHANGE'))
        datatype.setLongName(""+elem.@'LONG-NAME')
        datatype.setDescription(""+elem.@'DESC')
    }

    return datatype;
}