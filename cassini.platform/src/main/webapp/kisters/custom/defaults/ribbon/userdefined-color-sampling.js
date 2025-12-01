var UserConfiguration = {
    langId: 'Color Sampling',
    groups: [
        {
            langId: 'Set Color',
            widgets: [
                { type: 1, langId: 'Reset Colors', iconId: '1_letzte_aktionen' },
                { type: 1, langId: 'By Status', iconId: '10_auswahl_durch_farbe' },
                { type: 1, langId: 'By Stock', iconId: '1_BOM'}
            ],
            groupClick: function (context, id, group)
            {
                var client = context.client;
                let clickedButton = group.getButtonById(id);
                switch (clickedButton.langId)
                {
                    case 'Reset Colors':
                        if (stockChanged)
                        {
                            let uncoloredStruct = JSON.parse(JSON.stringify(struct));
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor0"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor5"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor20"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor30"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor40"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor50"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor60"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor70"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor80"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor90"/g, '');
                            uncoloredStruct = uncoloredStruct.replace(/Color = "stockColor100"/g, '');
                            let xml = '<Call Method="UpdateScene"><VSXML>' + uncoloredStruct + '</VSXML></Call>';
                            client.command.executeXML(xml);
                            stockChanged = false;
                        }
                        else
                        {
                            client.command.executeXML('<Call Method="PauseRendering"></Call>');
                            client.command.executeXML('<Call Method="SetSelectedNodes"><NodeId>0</NodeId></Call>')
                            client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>ResetColors</SelectionModifier></Call>');
                            client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>');
                            client.command.executeXML('<Call Method="ResumeRendering"></Call>');
                        }
                        break;
                    case 'By Status':
                        client.command.executeXML('<Call Method="PauseRendering"></Call>');
                        //Get by stock 0-20: white
                        let selectSatus1Xml = '<Call Method="SearchNodes"><SearchString>"Status"="1"</SearchString><SelectionMode>Select</SelectionMode><IncludeHiddenGeometries>false</IncludeHiddenGeometries></Call>';
                        client.command.executeXML(selectSatus1Xml);
                        let color1Xml = '<Event Type="NodePropertiesChanged"><General><Color>#ff0000</Color></General></Event>';
                        client.command.executeXML(color1Xml);
                        let selectSatus2Xml = '<Call Method="SearchNodes"><SearchString>"Status"="2"</SearchString><SelectionMode>Select</SelectionMode><IncludeHiddenGeometries>false</IncludeHiddenGeometries></Call>';
                        client.command.executeXML(selectSatus2Xml);
                        let color2Xml = '<Event Type="NodePropertiesChanged"><General><Color>#0000ff</Color></General></Event>';
                        client.command.executeXML(color2Xml);
                        let selectSatus3Xml = '<Call Method="SearchNodes"><SearchString>"Status"="3"</SearchString><SelectionMode>Select</SelectionMode><IncludeHiddenGeometries>false</IncludeHiddenGeometries></Call>';
                        client.command.executeXML(selectSatus3Xml);
                        let color3Xml = '<Event Type="NodePropertiesChanged"><General><Color>#00ff00</Color></General></Event>';
                        client.command.executeXML(color3Xml);
                        client.command.executeXML('<Call Method="ModifySelection"><SelectionModifier>Deselect</SelectionModifier></Call>');
                        client.command.executeXML('<Call Method="ResumeRendering"></Call>');
                        break;
                    case 'By Stock':
                        stockChanged = true;
                        let colorMap = {
                            0: 'ff0000', // 0 - 20 red
                            5: 'ff0000',
                            20: 'ff0000',
                            30: '#ff6600', // 30 - 50 orange
                            40: '#ff6600',
                            50: '#ff6600',
                            60: '#ffff00', // 60 - 80 yellow
                            70: '#ffff00',
                            80: '#ffff00',
                            90: '#00ff00', // 90 - 100 green
                            100: '#00ff00'
                        };
                        let colorStruct = JSON.parse(JSON.stringify(struct));
                        colorStruct = colorStruct.replace(/stockColor0/g, colorMap['0']);
                        colorStruct = colorStruct.replace(/stockColor5/g, colorMap['5']);
                        colorStruct = colorStruct.replace(/stockColor20/g, colorMap['20']);
                        colorStruct = colorStruct.replace(/stockColor30/g, colorMap['30']);
                        colorStruct = colorStruct.replace(/stockColor40/g, colorMap['40']);
                        colorStruct = colorStruct.replace(/stockColor50/g, colorMap['50']);
                        colorStruct = colorStruct.replace(/stockColor60/g, colorMap['60']);
                        colorStruct = colorStruct.replace(/stockColor70/g, colorMap['70']);
                        colorStruct = colorStruct.replace(/stockColor80/g, colorMap['80']);
                        colorStruct = colorStruct.replace(/stockColor90/g, colorMap['90']);
                        colorStruct = colorStruct.replace(/stockColor100/g, colorMap['100']);
                        let xml = '<Call Method="UpdateScene"><VSXML>' + colorStruct + '</VSXML></Call>';
                        client.command.executeXML(xml);
                        break;
                }
                
            }
        }
	]
};

function generateUUID()
{
    var d = new Date().getTime();
    if (typeof performance !== 'undefined' && typeof performance.now === 'function')
    {
        d += performance.now();
    }
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c)
    {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
}

var stockChanged = false;

var struct = `<Node
Id = "237"
Name = "_a_l005"
Type = "1">
<Node
    Id = "238"
    Name = "a_L005_ASM"
    Type = "2">
    <Attribute key = "PartNumber" value = "a_L005_ASM"/>
    <Attribute key = "Revision" value = "2"/>
    <Attribute key = "Definition" value = "L005_ASM"/>
    <Attribute key = "Source" value = "made"/>
    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
    <Node
        Id = "239"
        Name = "L005_1300(L005_1300)"
        Type = "2">
        <Attribute key = "InstanceName" value = "L005_1300"/>
        <Attribute key = "PartNumber" value = "L005_1300"/>
        <Attribute key = "Source" value = "Unknown"/>
        <Node
            Id = "240"
            Name = "L005_1300_HST(L005_1300_HST)"
            Type = "2">
            Color = "stockColor80"
            <Attribute key = "Status" value = "1"/>
            <Attribute key = "Stock" value = "80"/>
            <Attribute key = "InstanceName" value = "L005_1300_HST"/>
            <Attribute key = "PartNumber" value = "L005_1300_HST"/>
            <Attribute key = "Source" value = "Unknown"/>
            <Node
                Id = "241"
                Name = "L005_1300_HST_ASM(78)"
                Type = "2"
                >
                <Attribute key = "InstanceName" value = "78"/>
                <Attribute key = "DescriptionInst" value = "L005_1300_HST"/>
                <Attribute key = "PartNumber" value = "L005_1300_HST_ASM"/>
                <Attribute key = "Revision" value = "1"/>
                <Attribute key = "Definition" value = "L005_1300_HST_ASM"/>
                <Attribute key = "Source" value = "made"/>
                <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                <Node
                    Id = "242"
                    Name = "1399754(17)"
                    FileName = "1399754.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 600 122.576 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "SAP-ID" value="456-TR-487991"/>
                    <Attribute key = "Last-Modified-Date" value="2014-03-16 8:30"/>
                    <Attribute key = "Last edited by" value="George Clooney"/>
                    <Attribute key = "Last released by" value="Bill Gates"/>
                    <Attribute key = "HotSpot" value="C:\protected\samples\by_topic\techPub\PowerPainter\Power-Painter Pflegeanleitung.pdf"/>
                    <Attribute key = "InstanceName" value = "17"/>
                    <Attribute key = "DescriptionInst" value = "1399754"/>
                    <Attribute key = "PartNumber" value = "1399754"/>
                    <Attribute key = "Revision" value = "3"/>
                    <Attribute key = "Definition" value = "1399754"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "245"
                    Name = "1396052(18)"
                    FileName = "1396052.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor40"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 276.6 197.576 0 1">
                    <Attribute key = "Status" value = "3"/>
                    <Attribute key = "Stock" value = "40"/>
                    <Attribute key = "InstanceName" value = "18"/>
                    <Attribute key = "DescriptionInst" value = "1396052"/>
                    <Attribute key = "PartNumber" value = "1396052"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396052"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "248"
                    Name = "1396052(19)"
                    FileName = "1396052.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 276.6 122.576 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "19"/>
                    <Attribute key = "DescriptionInst" value = "1396052"/>
                    <Attribute key = "PartNumber" value = "1396052"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396052"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "251"
                    Name = "1396052(20)"
                    FileName = "1396052.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 276.6 122.576 75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "20"/>
                    <Attribute key = "DescriptionInst" value = "1396052"/>
                    <Attribute key = "PartNumber" value = "1396052"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396052"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "254"
                    Name = "1394432_ASM(23)"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 -1 0 0 0 0 -1 0 0 200.6 165.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "23"/>
                    <Attribute key = "DescriptionInst" value = "1394432"/>
                    <Attribute key = "PartNumber" value = "1394432_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1394432_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "255"
                        Name = "1394431(21)"
                        FileName = "1394431.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "21"/>
                        <Attribute key = "DescriptionInst" value = "1394431"/>
                        <Attribute key = "PartNumber" value = "1394431"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1394431"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "258"
                        Name = "1394430(22)"
                        FileName = "1394430.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor50"
                        Transformation = "0 -1 0 0 0 0 -1 0 1 0 0 0 0 -68 58.7 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "50"/>
                        <Attribute key = "InstanceName" value = "22"/>
                        <Attribute key = "DescriptionInst" value = "1394430"/>
                        <Attribute key = "PartNumber" value = "1394430"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1394430"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "261"
                    Name = "1396057(24)"
                    FileName = "1396057.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 580 236.876 0 1">
                    <Attribute key = "Status" value = "2"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "24"/>
                    <Attribute key = "DescriptionInst" value = "1396057"/>
                    <Attribute key = "PartNumber" value = "1396057"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396057"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "264"
                    Name = "292687(25)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 572 236.876 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "25"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "267"
                    Name = "292584(26)"
                    FileName = "292584.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 570 236.876 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "26"/>
                    <Attribute key = "DescriptionInst" value = "292584"/>
                    <Attribute key = "PartNumber" value = "292584"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292584"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "270"
                    Name = "1396057(27)"
                    FileName = "1396057.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 580 8.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "27"/>
                    <Attribute key = "DescriptionInst" value = "1396057"/>
                    <Attribute key = "PartNumber" value = "1396057"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396057"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "273"
                    Name = "292687(28)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 572 8.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "28"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "276"
                    Name = "292584(29)"
                    FileName = "292584.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 570 8.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "29"/>
                    <Attribute key = "DescriptionInst" value = "292584"/>
                    <Attribute key = "PartNumber" value = "292584"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292584"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "279"
                    Name = "1397761(30)"
                    FileName = "1397761.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 -1 0 0 0 0 0 1 0 200.6 149.576 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "30"/>
                    <Attribute key = "DescriptionInst" value = "1397761"/>
                    <Attribute key = "PartNumber" value = "1397761"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1397761"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "282"
                    Name = "1378198(31)"
                    FileName = "1378198.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 200.6 146.376 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "31"/>
                    <Attribute key = "DescriptionInst" value = "1378198"/>
                    <Attribute key = "PartNumber" value = "1378198"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1378198"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "285"
                    Name = "1396051(32)"
                    FileName = "1396051.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 200.6 111.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "32"/>
                    <Attribute key = "DescriptionInst" value = "1396051"/>
                    <Attribute key = "PartNumber" value = "1396051"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396051"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "288"
                    Name = "1394537(33)"
                    FileName = "1394537.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 200.6 146.376 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "33"/>
                    <Attribute key = "DescriptionInst" value = "1394537"/>
                    <Attribute key = "PartNumber" value = "1394537"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1394537"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "291"
                    Name = "1302938(34)"
                    FileName = "1302938.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor0"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 200.6 87.276 0 1">
                    <Attribute key = "Status" value = "3"/>
                    <Attribute key = "Stock" value = "0"/>
                    <Attribute key = "InstanceName" value = "34"/>
                    <Attribute key = "DescriptionInst" value = "1302938"/>
                    <Attribute key = "PartNumber" value = "1302938"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1302938"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "294"
                    Name = "1394436(35)"
                    FileName = "1394436.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 200.6 99.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "35"/>
                    <Attribute key = "DescriptionInst" value = "1394436"/>
                    <Attribute key = "PartNumber" value = "1394436"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1394436"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "297"
                    Name = "292683(36)"
                    FileName = "292683.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 251.85 122.576 75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "36"/>
                    <Attribute key = "DescriptionInst" value = "292683"/>
                    <Attribute key = "PartNumber" value = "292683"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292683"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "300"
                    Name = "292683(37)"
                    FileName = "292683.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 251.85 197.576 0 1">
                    <Attribute key = "Status" value = "2"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "37"/>
                    <Attribute key = "DescriptionInst" value = "292683"/>
                    <Attribute key = "PartNumber" value = "292683"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292683"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "303"
                    Name = "292683(38)"
                    FileName = "292683.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 251.85 122.576 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "38"/>
                    <Attribute key = "DescriptionInst" value = "292683"/>
                    <Attribute key = "PartNumber" value = "292683"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292683"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "306"
                    Name = "246001(39)"
                    FileName = "246001.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 250.6 122.576 75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "39"/>
                    <Attribute key = "DescriptionInst" value = "246001"/>
                    <Attribute key = "PartNumber" value = "246001"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "246001"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "309"
                    Name = "246001(40)"
                    FileName = "246001.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 250.6 197.576 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "40"/>
                    <Attribute key = "DescriptionInst" value = "246001"/>
                    <Attribute key = "PartNumber" value = "246001"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "246001"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "312"
                    Name = "246001(41)"
                    FileName = "246001.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 250.6 122.576 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "41"/>
                    <Attribute key = "DescriptionInst" value = "246001"/>
                    <Attribute key = "PartNumber" value = "246001"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "246001"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "315"
                    Name = "1388861(44)"
                    FileName = "1388861.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 654.5 122.576 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "44"/>
                    <Attribute key = "DescriptionInst" value = "1388861"/>
                    <Attribute key = "PartNumber" value = "1388861"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1388861"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "318"
                    Name = "1388862(45)"
                    FileName = "1388862.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 663 122.576 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "45"/>
                    <Attribute key = "DescriptionInst" value = "1388862"/>
                    <Attribute key = "PartNumber" value = "1388862"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1388862"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "321"
                    Name = "1394439(46)"
                    FileName = "1394439.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 200.6 171.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "46"/>
                    <Attribute key = "DescriptionInst" value = "1394439"/>
                    <Attribute key = "PartNumber" value = "1394439"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1394439"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "324"
                    Name = "292573(47)"
                    FileName = "292573.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 200.6 171.276 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "47"/>
                    <Attribute key = "DescriptionInst" value = "292573"/>
                    <Attribute key = "PartNumber" value = "292573"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292573"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "327"
                    Name = "316828(48)"
                    FileName = "316828.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 200.6 67.876 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "48"/>
                    <Attribute key = "DescriptionInst" value = "316828"/>
                    <Attribute key = "PartNumber" value = "316828"/>
                    <Attribute key = "Revision" value = "11"/>
                    <Attribute key = "Definition" value = "316828"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "330"
                    Name = "296028(67)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 173.075 -138.746 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "67"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "333"
                    Name = "296028(68)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 256.392 -62.3996 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "68"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "336"
                    Name = "296028(69)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 60.1764 -133.816 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "69"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "339"
                    Name = "296028(70)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 -16.1696 -50.4993 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "70"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "342"
                    Name = "296028(71)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 -11.2403 62.3996 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "71"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "345"
                    Name = "296028(72)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 72.0767 138.746 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "72"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "348"
                    Name = "296028(73)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 184.976 133.816 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "73"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "351"
                    Name = "296028(74)"
                    FileName = "296028.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 633.5 261.322 50.4993 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "74"/>
                    <Attribute key = "DescriptionInst" value = "296028"/>
                    <Attribute key = "PartNumber" value = "296028"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "296028"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "354"
                    Name = "320161(75)"
                    FileName = "320161.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 255.9 197.576 0 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "75"/>
                    <Attribute key = "DescriptionInst" value = "320161"/>
                    <Attribute key = "PartNumber" value = "320161"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320161"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "357"
                    Name = "320161(76)"
                    FileName = "320161.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 255.9 122.576 75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "76"/>
                    <Attribute key = "DescriptionInst" value = "320161"/>
                    <Attribute key = "PartNumber" value = "320161"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320161"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "360"
                    Name = "320161(77)"
                    FileName = "320161.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 255.9 122.576 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "77"/>
                    <Attribute key = "DescriptionInst" value = "320161"/>
                    <Attribute key = "PartNumber" value = "320161"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320161"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
            </Node>
        </Node>
        <Node
            Id = "363"
            Name = "L005_1300_SUPPLY(L005_1300_SUPPLY)"
            Type = "2"
            Color = "stockColor80">
            <Attribute key = "Status" value = "1"/>
            <Attribute key = "Stock" value = "80"/>
            <Attribute key = "InstanceName" value = "L005_1300_SUPPLY"/>
            <Attribute key = "PartNumber" value = "L005_1300_SUPPLY"/>
            <Attribute key = "Source" value = "Unknown"/>
            <Node
                Id = "364"
                Name = "L005_1300_SUPPLY_ASM(259)"
                Type = "2"
                Color = "stockColor50"
                >
                <Attribute key = "Status" value = "1"/>
                <Attribute key = "Stock" value = "50"/>
                <Attribute key = "InstanceName" value = "259"/>
                <Attribute key = "DescriptionInst" value = "L005_1300_SUPPLY"/>
                <Attribute key = "PartNumber" value = "L005_1300_SUPPLY_ASM"/>
                <Attribute key = "Revision" value = "1"/>
                <Attribute key = "Definition" value = "L005_1300_SUPPLY_ASM"/>
                <Attribute key = "Source" value = "made"/>
                <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                <Node
                    Id = "365"
                    Name = "1472686_ASM(83)"
                    Type = "2"
                       
                    Transformation = "0.851727 0.523986 0 0 -0.523986 0.851727 0 0 0 0 1 0 326.6 122.576 -115 1">
                    <Attribute key = "InstanceName" value = "83"/>
                    <Attribute key = "DescriptionInst" value = "1472686"/>
                    <Attribute key = "PartNumber" value = "1472686_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1472686_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "366"
                        Name = "20FLH_FITTING(80)"
                        FileName = "20FLH_FITTING.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "80"/>
                        <Attribute key = "DescriptionInst" value = "20FLH_FITTING"/>
                        <Attribute key = "PartNumber" value = "20FLH_FITTING"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "20FLH_FITTING"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "369"
                        Name = "1472686_TUBE_SOLID(81)"
                        FileName = "1472686_TUBE_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor5"
                        >
                        <Attribute key = "Status" value = "2"/>
                        <Attribute key = "Stock" value = "5"/>
                        <Attribute key = "InstanceName" value = "81"/>
                        <Attribute key = "DescriptionInst" value = "1472686_TUBE_SOLID"/>
                        <Attribute key = "PartNumber" value = "1472686_TUBE_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1472686_TUBE_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "372"
                        Name = "16_37DEG_MALE_BRAZEFTG(82)"
                        FileName = "16_37DEG_MALE_BRAZEFTG.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.344277 -0.938868 0 0 0 0 1 0 -0.938868 -0.344277 0 0 240.255 -169.393 -125 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "82"/>
                        <Attribute key = "DescriptionInst" value = "16_37DEG_MALE_BRAZEFTG"/>
                        <Attribute key = "PartNumber" value = "16_37DEG_MALE_BRAZEFTG"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16_37DEG_MALE_BRAZEFTG"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "375"
                    Name = "1399751(84)"
                    FileName = "1399751.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 326.6 122.576 -116.272 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "84"/>
                    <Attribute key = "DescriptionInst" value = "1399751"/>
                    <Attribute key = "PartNumber" value = "1399751"/>
                    <Attribute key = "Revision" value = "5"/>
                    <Attribute key = "Definition" value = "1399751"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "378"
                    Name = "1472687_RIGHT_ARR_ASM(90)"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.703951 0.0345778 -0.709406 0 0.451683 -0.792609 0.409577 0 -0.54812 -0.608749 -0.573576 0 104.04 -93.6783 -37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "90"/>
                    <Attribute key = "DescriptionInst" value = "1472687_RIGHT_ARR"/>
                    <Attribute key = "PartNumber" value = "1472687_RIGHT_ARR_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1472687_RIGHT_ARR_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "379"
                        Name = "16GS_16FLH60(85)"
                        FileName = "16GS_16FLH60.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor50"
                        Transformation = "0 1 0 0 1 0 0 0 0 0 -1 0 0 0 0 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "50"/>
                        <Attribute key = "InstanceName" value = "85"/>
                        <Attribute key = "DescriptionInst" value = "16GS_16FLH60"/>
                        <Attribute key = "PartNumber" value = "16GS_16FLH60"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS_16FLH60"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "382"
                        Name = "16GS-2F(86)"
                        FileName = "16GS-2F.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 95.856 41.148 0 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "86"/>
                        <Attribute key = "DescriptionInst" value = "16GS-2F"/>
                        <Attribute key = "PartNumber" value = "16GS-2F"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS-2F"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "385"
                        Name = "1472687_RIGHT_SOLID(87)"
                        FileName = "1472687_RIGHT_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "87"/>
                        <Attribute key = "DescriptionInst" value = "1472687_RIGHT_SOLID"/>
                        <Attribute key = "PartNumber" value = "1472687_RIGHT_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1472687_RIGHT_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "388"
                        Name = "16GS-16FMX(88)"
                        FileName = "16GS-16FMX.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.531318 -0.845488 0.053399 0 -0.709406 0.409577 -0.573576 0 0.463081 -0.342633 -0.81741 0 497.664 5.14023 -258.799 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "88"/>
                        <Attribute key = "DescriptionInst" value = "16GS-16FMX"/>
                        <Attribute key = "PartNumber" value = "16GS-16FMX"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS-16FMX"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "391"
                        Name = "16GS-2F(89)"
                        FileName = "16GS-2F.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.870076 -2.26763e-014 0.492917 0 0.16889 0.939469 -0.298117 0 -0.463081 0.342633 0.81741 0 488.634 11.8216 -242.859 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "89"/>
                        <Attribute key = "DescriptionInst" value = "16GS-2F"/>
                        <Attribute key = "PartNumber" value = "16GS-2F"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS-2F"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "394"
                    Name = "1472687_LEFT_ARR_ASM(96)"
                    Type = "2"
                    Color = "stockColor0"
                    Transformation = "0.703951 0.0345782 0.709406 0 0.451683 -0.792609 -0.409576 0 0.54812 0.608748 -0.573577 0 104.04 -93.6783 37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "0"/>
                    <Attribute key = "InstanceName" value = "96"/>
                    <Attribute key = "DescriptionInst" value = "1472687_LEFT_ARR"/>
                    <Attribute key = "PartNumber" value = "1472687_LEFT_ARR_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1472687_LEFT_ARR_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "395"
                        Name = "16GS_16FLH60(91)"
                        FileName = "16GS_16FLH60.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 1 0 0 1 0 0 0 0 0 -1 0 0 0 0 1">
                        <Attribute key = "Status" value = "2"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "91"/>
                        <Attribute key = "DescriptionInst" value = "16GS_16FLH60"/>
                        <Attribute key = "PartNumber" value = "16GS_16FLH60"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS_16FLH60"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "398"
                        Name = "16GS-2F(92)"
                        FileName = "16GS-2F.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 95.856 41.148 0 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "92"/>
                        <Attribute key = "DescriptionInst" value = "16GS-2F"/>
                        <Attribute key = "PartNumber" value = "16GS-2F"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS-2F"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "401"
                        Name = "1472687_LEFT_SOLID(93)"
                        FileName = "1472687_LEFT_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor50"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "50"/>
                        <Attribute key = "InstanceName" value = "93"/>
                        <Attribute key = "DescriptionInst" value = "1472687_LEFT_SOLID"/>
                        <Attribute key = "PartNumber" value = "1472687_LEFT_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1472687_LEFT_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "404"
                        Name = "16GS-16FMX(94)"
                        FileName = "16GS-16FMX.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.531318 -0.845488 -0.0534 0 0.709406 -0.409576 -0.573577 0 0.463081 -0.342634 0.817409 0 497.661 5.1428 258.793 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "94"/>
                        <Attribute key = "DescriptionInst" value = "16GS-16FMX"/>
                        <Attribute key = "PartNumber" value = "16GS-16FMX"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS-16FMX"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "407"
                        Name = "16GS-2F(95)"
                        FileName = "16GS-2F.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor50"
                        Transformation = "-0.870076 -4.41314e-015 0.492918 0 0.16889 0.939469 0.298118 0 -0.463081 0.342634 -0.817409 0 488.631 11.8242 242.853 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "50"/>
                        <Attribute key = "InstanceName" value = "95"/>
                        <Attribute key = "DescriptionInst" value = "16GS-2F"/>
                        <Attribute key = "PartNumber" value = "16GS-2F"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16GS-2F"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "410"
                    Name = "1472685_ASM(100)"
                    Type = "2"
                       
                    Transformation = "0.851727 0.523986 0 0 0.523986 -0.851727 0 0 0 0 -1 0 326.6 122.576 115 1">
                    <Attribute key = "InstanceName" value = "100"/>
                    <Attribute key = "DescriptionInst" value = "1472685"/>
                    <Attribute key = "PartNumber" value = "1472685_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1472685_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "411"
                        Name = "20FLH_FITTING(97)"
                        FileName = "20FLH_FITTING.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "97"/>
                        <Attribute key = "DescriptionInst" value = "20FLH_FITTING"/>
                        <Attribute key = "PartNumber" value = "20FLH_FITTING"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "20FLH_FITTING"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "414"
                        Name = "1472685_TUBE_SOLID(98)"
                        FileName = "1472685_TUBE_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor50"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "50"/>
                        <Attribute key = "InstanceName" value = "98"/>
                        <Attribute key = "DescriptionInst" value = "1472685_TUBE_SOLID"/>
                        <Attribute key = "PartNumber" value = "1472685_TUBE_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1472685_TUBE_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "417"
                        Name = "16_37DEG_MALE_BRAZEFTG(99)"
                        FileName = "16_37DEG_MALE_BRAZEFTG.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.344277 -0.938868 0 0 0 0 1 0 -0.938868 0.344277 0 0 240.248 169.395 -125 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "99"/>
                        <Attribute key = "DescriptionInst" value = "16_37DEG_MALE_BRAZEFTG"/>
                        <Attribute key = "PartNumber" value = "16_37DEG_MALE_BRAZEFTG"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "16_37DEG_MALE_BRAZEFTG"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "420"
                    Name = "1399752(101)"
                    FileName = "1399752.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0.669131 0.743145 -3.70577e-021 0 -3.70607e-007 3.33697e-007 1 0 0.743145 -0.669131 4.98702e-007 0 104.941 -94.4896 -37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "101"/>
                    <Attribute key = "DescriptionInst" value = "1399752"/>
                    <Attribute key = "PartNumber" value = "1399752"/>
                    <Attribute key = "Revision" value = "5"/>
                    <Attribute key = "Definition" value = "1399752"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "423"
                    Name = "1399752(102)"
                    FileName = "1399752.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor0"
                    Transformation = "0.669131 0.743145 6.61744e-024 0 8.53903e-008 -7.68858e-008 1 0 0.743145 -0.669131 -1.14904e-007 0 104.941 -94.4896 37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "0"/>
                    <Attribute key = "InstanceName" value = "102"/>
                    <Attribute key = "DescriptionInst" value = "1399752"/>
                    <Attribute key = "PartNumber" value = "1399752"/>
                    <Attribute key = "Revision" value = "5"/>
                    <Attribute key = "Definition" value = "1399752"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "426"
                    Name = "222536(103)"
                    FileName = "222536.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 0 0 1 0 -1 0 0 0 326.6 122.576 115 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "103"/>
                    <Attribute key = "DescriptionInst" value = "222536"/>
                    <Attribute key = "PartNumber" value = "222536"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "222536"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "429"
                    Name = "222536(104)"
                    FileName = "222536.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 326.6 122.576 115 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "104"/>
                    <Attribute key = "DescriptionInst" value = "222536"/>
                    <Attribute key = "PartNumber" value = "222536"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "222536"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "432"
                    Name = "222536(105)"
                    FileName = "222536.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 -1 0 -1 0 0 0 326.6 122.576 -115 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "105"/>
                    <Attribute key = "DescriptionInst" value = "222536"/>
                    <Attribute key = "PartNumber" value = "222536"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "222536"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "435"
                    Name = "222536(106)"
                    FileName = "222536.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 0 0 -1 0 1 0 0 0 326.6 122.576 -115 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "106"/>
                    <Attribute key = "DescriptionInst" value = "222536"/>
                    <Attribute key = "PartNumber" value = "222536"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "222536"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "438"
                    Name = "15158(107)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 342.577 155.913 -116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "107"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "441"
                    Name = "15158(108)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 342.577 89.2385 -116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "108"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "444"
                    Name = "15158(109)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 310.623 155.913 -116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "109"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "447"
                    Name = "15158(110)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 310.623 89.2385 -116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "110"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "450"
                    Name = "15158(111)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 310.623 155.913 116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "111"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "453"
                    Name = "15158(112)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 342.577 155.913 116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "112"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "456"
                    Name = "15158(113)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 310.623 89.2385 116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "113"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "459"
                    Name = "15158(114)"
                    FileName = "15158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 342.577 89.2385 116.524 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "114"/>
                    <Attribute key = "DescriptionInst" value = "15158"/>
                    <Attribute key = "PartNumber" value = "15158"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "462"
                    Name = "221569(115)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 320.148 161.413 -153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "115"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "465"
                    Name = "221569(116)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 320.148 94.7378 -153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "116"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "468"
                    Name = "221569(117)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 352.102 161.413 -153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "117"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "471"
                    Name = "221569(118)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 352.102 94.7378 -153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "118"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "474"
                    Name = "221569(119)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 301.098 161.413 153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "119"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "477"
                    Name = "221569(120)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 301.098 94.7378 153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "120"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "480"
                    Name = "221569(121)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 333.052 161.413 153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "121"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "483"
                    Name = "221569(122)"
                    FileName = "221569.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 333.052 94.7378 153.557 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "122"/>
                    <Attribute key = "DescriptionInst" value = "221569"/>
                    <Attribute key = "PartNumber" value = "221569"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "221569"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "486"
                    Name = "213617(123)"
                    FileName = "213617.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0.669131 0.743145 0 0 0.743145 -0.669131 0 0 0 0 -1 0 104.04 -93.6783 37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "123"/>
                    <Attribute key = "DescriptionInst" value = "213617"/>
                    <Attribute key = "PartNumber" value = "213617"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "213617"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "489"
                    Name = "213617(124)"
                    FileName = "213617.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "-0.669131 -0.743145 0 0 0.743145 -0.669131 0 0 0 0 1 0 104.04 -93.6783 37.5 1">
                    <Attribute key = "Status" value = "3"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "124"/>
                    <Attribute key = "DescriptionInst" value = "213617"/>
                    <Attribute key = "PartNumber" value = "213617"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "213617"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "492"
                    Name = "213617(125)"
                    FileName = "213617.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0.669131 0.743145 0 0 0.743145 -0.669131 0 0 0 0 -1 0 104.04 -93.6783 -37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "125"/>
                    <Attribute key = "DescriptionInst" value = "213617"/>
                    <Attribute key = "PartNumber" value = "213617"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "213617"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "495"
                    Name = "213617(126)"
                    FileName = "213617.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-0.669131 -0.743145 0 0 0.743145 -0.669131 0 0 0 0 1 0 104.04 -93.6783 -37.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "126"/>
                    <Attribute key = "DescriptionInst" value = "213617"/>
                    <Attribute key = "PartNumber" value = "213617"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "213617"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "498"
                    Name = "15157(127)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 122.028 -71.4232 -23.5554 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "127"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "501"
                    Name = "15157(128)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 83.7873 -113.894 -23.5554 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "128"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "504"
                    Name = "15157(129)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 122.028 -71.4232 -51.4446 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "129"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "507"
                    Name = "15157(130)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 83.7873 -113.894 -51.4446 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "130"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "510"
                    Name = "15157(131)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 122.028 -71.4232 51.4446 1">
                    <Attribute key = "Status" value = "4"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "131"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "513"
                    Name = "15157(132)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 83.7873 -113.894 51.4446 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "132"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "516"
                    Name = "15157(133)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 122.028 -71.4232 23.5554 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "133"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "519"
                    Name = "15157(134)"
                    FileName = "15157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.669131 0.743145 0 0 0 0 1 0 0.743145 -0.669131 0 0 83.7873 -113.894 23.5554 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "134"/>
                    <Attribute key = "DescriptionInst" value = "15157"/>
                    <Attribute key = "PartNumber" value = "15157"/>
                    <Attribute key = "Revision" value = "16"/>
                    <Attribute key = "Definition" value = "15157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "522"
                    Name = "131136(135)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 143.332 -101.286 56.0273 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "135"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "525"
                    Name = "131136(136)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 105.091 -143.757 56.0273 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "136"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "528"
                    Name = "131136(137)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 143.332 -101.286 28.1381 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "137"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "531"
                    Name = "131136(138)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 105.091 -143.757 28.1381 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "138"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "534"
                    Name = "131136(139)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 143.332 -101.286 -18.9727 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "139"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "537"
                    Name = "131136(140)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 105.091 -143.757 -18.9727 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "140"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "540"
                    Name = "131136(141)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 143.332 -101.286 -46.8619 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "141"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "543"
                    Name = "131136(142)"
                    FileName = "131136.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.669131 -0.743145 0 0 0 0 1 0 -0.743145 0.669131 0 0 105.091 -143.757 -46.8619 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "142"/>
                    <Attribute key = "DescriptionInst" value = "131136"/>
                    <Attribute key = "PartNumber" value = "131136"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "131136"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "546"
                    Name = "1474602(143)"
                    FileName = "1474602.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0.871897 -0.489689 0 -1 0 0 0 0 0.489689 0.871897 0 662 312.591 -93.51 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "143"/>
                    <Attribute key = "DescriptionInst" value = "1474602"/>
                    <Attribute key = "PartNumber" value = "1474602"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1474602"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "549"
                    Name = "1474603(144)"
                    FileName = "1474603.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0.871947 0.4896 0 1 0 0 0 0 0.4896 -0.871947 0 667 312.59 93.5088 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "144"/>
                    <Attribute key = "DescriptionInst" value = "1474603"/>
                    <Attribute key = "PartNumber" value = "1474603"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1474603"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "552"
                    Name = "222513(145)"
                    FileName = "222513.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.906308 0.422618 0 0 -0.422618 -0.906308 0 0 0 0 1 0 320.6 36.576 -146.942 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "145"/>
                    <Attribute key = "DescriptionInst" value = "222513"/>
                    <Attribute key = "PartNumber" value = "222513"/>
                    <Attribute key = "Revision" value = "19"/>
                    <Attribute key = "Definition" value = "222513"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "555"
                    Name = "1473858(146)"
                    FileName = "1473858.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.785184 0.619263 0 0 0 0 -1 0 -0.619263 -0.785184 0 0 605.187 149.567 -215.464 1">
                    <Attribute key = "Status" value = "4"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "146"/>
                    <Attribute key = "DescriptionInst" value = "1473858"/>
                    <Attribute key = "PartNumber" value = "1473858"/>
                    <Attribute key = "Revision" value = "60"/>
                    <Attribute key = "Definition" value = "1473858"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "558"
                    Name = "1473858(147)"
                    FileName = "1473858.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.785184 0.619263 0 0 0 0 1 0 0.619263 0.785184 0 0 605.261 149.499 215.464 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "147"/>
                    <Attribute key = "DescriptionInst" value = "1473858"/>
                    <Attribute key = "PartNumber" value = "1473858"/>
                    <Attribute key = "Revision" value = "60"/>
                    <Attribute key = "Definition" value = "1473858"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "561"
                    Name = "320160(148)"
                    FileName = "320160.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 605.261 149.499 215.464 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "148"/>
                    <Attribute key = "DescriptionInst" value = "320160"/>
                    <Attribute key = "PartNumber" value = "320160"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320160"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "564"
                    Name = "320160(149)"
                    FileName = "320160.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 605.261 149.499 220.188 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "149"/>
                    <Attribute key = "DescriptionInst" value = "320160"/>
                    <Attribute key = "PartNumber" value = "320160"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320160"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "567"
                    Name = "292515(151)"
                    FileName = "292515.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 605.261 149.499 225.188 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "151"/>
                    <Attribute key = "DescriptionInst" value = "292515"/>
                    <Attribute key = "PartNumber" value = "292515"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292515"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "570"
                    Name = "320160(152)"
                    FileName = "320160.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 605.261 149.499 207.964 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "152"/>
                    <Attribute key = "DescriptionInst" value = "320160"/>
                    <Attribute key = "PartNumber" value = "320160"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320160"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "573"
                    Name = "136692(153)"
                    FileName = "136692.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 605.261 149.499 196.764 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "153"/>
                    <Attribute key = "DescriptionInst" value = "136692"/>
                    <Attribute key = "PartNumber" value = "136692"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "136692"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "576"
                    Name = "320160(154)"
                    FileName = "320160.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 -0.000783533 0 0 0.000783533 1 0 1 0 0 0 605.6 149.815 -212.652 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "154"/>
                    <Attribute key = "DescriptionInst" value = "320160"/>
                    <Attribute key = "PartNumber" value = "320160"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320160"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "579"
                    Name = "320160(155)"
                    FileName = "320160.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 605.187 149.567 -220.188 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "155"/>
                    <Attribute key = "DescriptionInst" value = "320160"/>
                    <Attribute key = "PartNumber" value = "320160"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320160"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "582"
                    Name = "292515(157)"
                    FileName = "292515.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 605.187 149.567 -225.188 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "157"/>
                    <Attribute key = "DescriptionInst" value = "292515"/>
                    <Attribute key = "PartNumber" value = "292515"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292515"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "585"
                    Name = "320160(158)"
                    FileName = "320160.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 605.187 149.567 -207.464 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "158"/>
                    <Attribute key = "DescriptionInst" value = "320160"/>
                    <Attribute key = "PartNumber" value = "320160"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320160"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "588"
                    Name = "136692(159)"
                    FileName = "136692.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 605.187 149.567 -196.264 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "159"/>
                    <Attribute key = "DescriptionInst" value = "136692"/>
                    <Attribute key = "PartNumber" value = "136692"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "136692"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "591"
                    Name = "1388963(160)"
                    FileName = "1388963.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 1045.82 -100 -355.21 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "160"/>
                    <Attribute key = "DescriptionInst" value = "1388963"/>
                    <Attribute key = "PartNumber" value = "1388963"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1388963"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "594"
                    Name = "183635(161)"
                    FileName = "183635.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 1045.82 -100 -334.128 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "161"/>
                    <Attribute key = "DescriptionInst" value = "183635"/>
                    <Attribute key = "PartNumber" value = "183635"/>
                    <Attribute key = "Revision" value = "10"/>
                    <Attribute key = "Definition" value = "183635"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "597"
                    Name = "1388959_ASM(163)"
                    Type = "2"
                    Color = "stockColor80"
                    >
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "163"/>
                    <Attribute key = "DescriptionInst" value = "1388959"/>
                    <Attribute key = "PartNumber" value = "1388959_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1388959_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "598"
                        Name = "1388959SOLID(162)"
                        FileName = "1388959SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor50"
                        >
                        <Attribute key = "Status" value = "2"/>
                        <Attribute key = "Stock" value = "50"/>
                        <Attribute key = "InstanceName" value = "162"/>
                        <Attribute key = "DescriptionInst" value = "1388959SOLID"/>
                        <Attribute key = "PartNumber" value = "1388959SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1388959SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "601"
                    Name = "369804_51(164)"
                    FileName = "369804_51.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 1045.82 -100 -323.889 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "164"/>
                    <Attribute key = "DescriptionInst" value = "369804_51"/>
                    <Attribute key = "PartNumber" value = "369804_51"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "369804_51"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "604"
                    Name = "369804_51(165)"
                    FileName = "369804_51.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 1045.82 -100 -300.315 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "165"/>
                    <Attribute key = "DescriptionInst" value = "369804_51"/>
                    <Attribute key = "PartNumber" value = "369804_51"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "369804_51"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "607"
                    Name = "1399789(166)"
                    FileName = "1399789.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 667.85 -119.6 -284 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "166"/>
                    <Attribute key = "DescriptionInst" value = "1399789"/>
                    <Attribute key = "PartNumber" value = "1399789"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1399789"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "610"
                    Name = "313221(167)"
                    FileName = "313221.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 679.85 -56.6 -279 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "167"/>
                    <Attribute key = "DescriptionInst" value = "313221"/>
                    <Attribute key = "PartNumber" value = "313221"/>
                    <Attribute key = "Revision" value = "60"/>
                    <Attribute key = "Definition" value = "313221"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "613"
                    Name = "320158(168)"
                    FileName = "320158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 679.85 -56.6 -275.952 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "168"/>
                    <Attribute key = "DescriptionInst" value = "320158"/>
                    <Attribute key = "PartNumber" value = "320158"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "616"
                    Name = "292681(169)"
                    FileName = "292681.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 679.85 -56.6 -272.652 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "169"/>
                    <Attribute key = "DescriptionInst" value = "292681"/>
                    <Attribute key = "PartNumber" value = "292681"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292681"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "619"
                    Name = "176640(170)"
                    FileName = "176640.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 679.85 -56.6 -271.652 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "170"/>
                    <Attribute key = "DescriptionInst" value = "176640"/>
                    <Attribute key = "PartNumber" value = "176640"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "176640"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "622"
                    Name = "239503(171)"
                    FileName = "239503.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 679.85 -56.6 -290.8 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "171"/>
                    <Attribute key = "DescriptionInst" value = "239503"/>
                    <Attribute key = "PartNumber" value = "239503"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "239503"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "625"
                    Name = "313221(172)"
                    FileName = "313221.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 -1 0 0 1 0 0 1 0 0 0 679.85 -124.6 -332 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "172"/>
                    <Attribute key = "DescriptionInst" value = "313221"/>
                    <Attribute key = "PartNumber" value = "313221"/>
                    <Attribute key = "Revision" value = "60"/>
                    <Attribute key = "Definition" value = "313221"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "628"
                    Name = "320158(173)"
                    FileName = "320158.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 679.85 -121.552 -332 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "173"/>
                    <Attribute key = "DescriptionInst" value = "320158"/>
                    <Attribute key = "PartNumber" value = "320158"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320158"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "631"
                    Name = "292681(174)"
                    FileName = "292681.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 679.85 -118.252 -332 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "174"/>
                    <Attribute key = "DescriptionInst" value = "292681"/>
                    <Attribute key = "PartNumber" value = "292681"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292681"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "634"
                    Name = "132861(175)"
                    FileName = "132861.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 679.85 -117.252 -332 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "175"/>
                    <Attribute key = "DescriptionInst" value = "132861"/>
                    <Attribute key = "PartNumber" value = "132861"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "132861"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "637"
                    Name = "239503(176)"
                    FileName = "239503.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 679.85 -140.448 -332 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "176"/>
                    <Attribute key = "DescriptionInst" value = "239503"/>
                    <Attribute key = "PartNumber" value = "239503"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "239503"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "640"
                    Name = "212786(177)"
                    FileName = "212786.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 -1 0 0 -1 0 0 -1 0 0 0 679.85 -130.6 -332 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "177"/>
                    <Attribute key = "DescriptionInst" value = "212786"/>
                    <Attribute key = "PartNumber" value = "212786"/>
                    <Attribute key = "Revision" value = "60"/>
                    <Attribute key = "Definition" value = "212786"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "643"
                    Name = "1467897(178)"
                    FileName = "1467897.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "-0.642788 0 0.766044 0 0 1 0 0 -0.766044 0 -0.642788 0 415.5 212.576 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "178"/>
                    <Attribute key = "DescriptionInst" value = "1467897"/>
                    <Attribute key = "PartNumber" value = "1467897"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1467897"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "646"
                    Name = "1480496(179)"
                    FileName = "1480496.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 1 0 0 -1 0 0 0 0 0 1 0 413.5 221.976 125 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "179"/>
                    <Attribute key = "DescriptionInst" value = "1480496"/>
                    <Attribute key = "PartNumber" value = "1480496"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1480496"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "650"
                    Name = "1479490(180)"
                    FileName = "1479490.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "-0.766044 0 -0.642788 0 0 1 0 0 0.642788 0 -0.766044 0 415.5 225.536 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "180"/>
                    <Attribute key = "DescriptionInst" value = "1479490"/>
                    <Attribute key = "PartNumber" value = "1479490"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1479490"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "654"
                    Name = "239222(181)"
                    FileName = "239222.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 -1 0 -1 0 0 0 360.6 222.098 -52 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "181"/>
                    <Attribute key = "DescriptionInst" value = "239222"/>
                    <Attribute key = "PartNumber" value = "239222"/>
                    <Attribute key = "Revision" value = "5"/>
                    <Attribute key = "Definition" value = "239222"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "657"
                    Name = "16970(182)"
                    FileName = "16970.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor70"
                    Transformation = "0.866025 0 0.5 0 -0.5 0 0.866025 0 0 -1 0 0 360.6 279.502 -52 1">
                    <Attribute key = "Status" value = "3"/>
                    <Attribute key = "Stock" value = "70"/>
                    <Attribute key = "InstanceName" value = "182"/>
                    <Attribute key = "DescriptionInst" value = "16970"/>
                    <Attribute key = "PartNumber" value = "16970"/>
                    <Attribute key = "Revision" value = "10"/>
                    <Attribute key = "Definition" value = "16970"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "660"
                    Name = "17777(183)"
                    FileName = "17777.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 -0.5 0 0.866025 0 -0.866025 0 -0.5 0 360.6 333.858 -52 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "183"/>
                    <Attribute key = "DescriptionInst" value = "17777"/>
                    <Attribute key = "PartNumber" value = "17777"/>
                    <Attribute key = "Revision" value = "11"/>
                    <Attribute key = "Definition" value = "17777"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "663"
                    Name = "16923(184)"
                    FileName = "16923.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 -0.5 0 0.866025 0 -0.866025 0 -0.5 0 360.6 323.938 -52 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "184"/>
                    <Attribute key = "DescriptionInst" value = "16923"/>
                    <Attribute key = "PartNumber" value = "16923"/>
                    <Attribute key = "Revision" value = "12"/>
                    <Attribute key = "Definition" value = "16923"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "666"
                    Name = "1396056_ASM(188)"
                    Type = "2"
                       
                    Transformation = "0 -1 0 0 -0.5 0 0.866025 0 -0.866025 0 -0.5 0 312.453 279.502 -79.7978 1">
                    <Attribute key = "InstanceName" value = "188"/>
                    <Attribute key = "DescriptionInst" value = "1396056"/>
                    <Attribute key = "PartNumber" value = "1396056_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1396056_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "667"
                        Name = "H8_3_4UNF(185)"
                        FileName = "H8_3_4UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 0 1 0 0 1 0 0 0 0 0 -28.9536 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "185"/>
                        <Attribute key = "DescriptionInst" value = "H8_3_4UNF"/>
                        <Attribute key = "PartNumber" value = "H8_3_4UNF"/>
                        <Attribute key = "Revision" value = "15"/>
                        <Attribute key = "Definition" value = "H8_3_4UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "670"
                        Name = "H8_3_4UNF(186)"
                        FileName = "H8_3_4UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.666126 0.736629 -0.116849 0 -0.204753 0.33126 0.921055 0 0.717184 -0.589614 0.371488 0 59.6836 195.888 124.604 1">
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "186"/>
                        <Attribute key = "DescriptionInst" value = "H8_3_4UNF"/>
                        <Attribute key = "PartNumber" value = "H8_3_4UNF"/>
                        <Attribute key = "Revision" value = "15"/>
                        <Attribute key = "Definition" value = "H8_3_4UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "673"
                        Name = "1396056SOLID(187)"
                        FileName = "1396056SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        >
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "187"/>
                        <Attribute key = "DescriptionInst" value = "1396056SOLID"/>
                        <Attribute key = "PartNumber" value = "1396056SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1396056SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "676"
                    Name = "1397688_ASM(192)"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0.207912 0.978148 1.40337e-008 0 0.413383 -0.0878673 0.906308 0 0.886503 -0.188432 -0.422618 0 77.5232 231.151 -3.44369 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "192"/>
                    <Attribute key = "DescriptionInst" value = "1397688"/>
                    <Attribute key = "PartNumber" value = "1397688_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1397688_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "677"
                        Name = "FS37_90--4-4S(189)"
                        FileName = "FS37_90--4-4S.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        Transformation = "0 0 -1 0 1 0 0 0 0 -1 0 0 0 0 0 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "189"/>
                        <Attribute key = "DescriptionInst" value = "FS37_90--4-4S"/>
                        <Attribute key = "PartNumber" value = "FS37_90--4-4S"/>
                        <Attribute key = "Revision" value = "8"/>
                        <Attribute key = "Definition" value = "FS37_90--4-4S"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "680"
                        Name = "1397688SOLID(190)"
                        FileName = "1397688SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "190"/>
                        <Attribute key = "DescriptionInst" value = "1397688SOLID"/>
                        <Attribute key = "PartNumber" value = "1397688SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1397688SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "683"
                        Name = "FS37_90--4-4S(191)"
                        FileName = "FS37_90--4-4S.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.133643 0.95999 0.246089 0 0.978148 -0.0878673 -0.188432 0 -0.15927 0.265894 -0.950754 0 87.971 218.117 230.129 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "191"/>
                        <Attribute key = "DescriptionInst" value = "FS37_90--4-4S"/>
                        <Attribute key = "PartNumber" value = "FS37_90--4-4S"/>
                        <Attribute key = "Revision" value = "8"/>
                        <Attribute key = "Definition" value = "FS37_90--4-4S"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "686"
                    Name = "17643(193)"
                    FileName = "17643.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 882.82 -110 -386.27 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "193"/>
                    <Attribute key = "DescriptionInst" value = "17643"/>
                    <Attribute key = "PartNumber" value = "17643"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "17643"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "689"
                    Name = "1399467(194)"
                    FileName = "1399467.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 882.82 -110 -310.832 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "194"/>
                    <Attribute key = "DescriptionInst" value = "1399467"/>
                    <Attribute key = "PartNumber" value = "1399467"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1399467"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "692"
                    Name = "14385(195)"
                    FileName = "14385.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 882.82 -128.548 -310.832 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "195"/>
                    <Attribute key = "DescriptionInst" value = "14385"/>
                    <Attribute key = "PartNumber" value = "14385"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "14385"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "695"
                    Name = "222503(196)"
                    FileName = "222503.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0.422618 -0.906308 0 0 0 0 1 0 -0.906308 -0.422618 0 0 84.7035 39.4979 -193.55 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "196"/>
                    <Attribute key = "DescriptionInst" value = "222503"/>
                    <Attribute key = "PartNumber" value = "222503"/>
                    <Attribute key = "Revision" value = "10"/>
                    <Attribute key = "Definition" value = "222503"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "698"
                    Name = "130845_23(197)"
                    FileName = "130845_23.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0.939692 -0.34202 0 0 0 0 1 0 -0.34202 -0.939692 0 0 108.72 63.0576 -193.55 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "197"/>
                    <Attribute key = "DescriptionInst" value = "130845_23"/>
                    <Attribute key = "PartNumber" value = "130845_23"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "130845_23"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "701"
                    Name = "1399468_ASM(199)"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "-0.766044 0 -0.642788 0 0 1 0 0 0.642788 0 -0.766044 0 426.717 252.836 -88.3675 1">
                    <Attribute key = "Status" value = "2"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "199"/>
                    <Attribute key = "DescriptionInst" value = "1399468"/>
                    <Attribute key = "PartNumber" value = "1399468_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1399468_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "702"
                        Name = "1399468SOLID(198)"
                        FileName = "1399468SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "198"/>
                        <Attribute key = "DescriptionInst" value = "1399468SOLID"/>
                        <Attribute key = "PartNumber" value = "1399468SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1399468SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "705"
                    Name = "130845_26(200)"
                    FileName = "130845_26.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 -1 0 0 0 0 -1 0 1 0 0 0 851.309 -110 -310.832 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "200"/>
                    <Attribute key = "DescriptionInst" value = "130845_26"/>
                    <Attribute key = "PartNumber" value = "130845_26"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "130845_26"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "708"
                    Name = "16560(201)"
                    FileName = "16560.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 1486.04 383.25 -301.483 1">
                    <Attribute key = "Status" value = "5"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "201"/>
                    <Attribute key = "DescriptionInst" value = "16560"/>
                    <Attribute key = "PartNumber" value = "16560"/>
                    <Attribute key = "Revision" value = "15"/>
                    <Attribute key = "Definition" value = "16560"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "711"
                    Name = "222383(202)"
                    FileName = "222383.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 0 0 1 0 -1 0 0 0 415.5 14.9108 75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "202"/>
                    <Attribute key = "DescriptionInst" value = "222383"/>
                    <Attribute key = "PartNumber" value = "222383"/>
                    <Attribute key = "Revision" value = "21"/>
                    <Attribute key = "Definition" value = "222383"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "714"
                    Name = "1526806_ASM(206)"
                    Type = "2"
                       
                    Transformation = "0 0 1 0 0 -1 0 0 1 0 0 0 442.768 -36.6424 75 1">
                    <Attribute key = "InstanceName" value = "206"/>
                    <Attribute key = "DescriptionInst" value = "1526806"/>
                    <Attribute key = "PartNumber" value = "1526806_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1526806_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "715"
                        Name = "H10_7_8_14UNF(203)"
                        FileName = "H10_7_8_14UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        Transformation = "0 -1 0 0 -1 0 0 0 0 0 -1 0 0 -32.3 -27.268 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "203"/>
                        <Attribute key = "DescriptionInst" value = "H10_7_8_14UNF"/>
                        <Attribute key = "PartNumber" value = "H10_7_8_14UNF"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "H10_7_8_14UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "718"
                        Name = "H10_7_8UNF(204)"
                        FileName = "H10_7_8UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 -376.483 -389.948 1043.53 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "204"/>
                        <Attribute key = "DescriptionInst" value = "H10_7_8UNF"/>
                        <Attribute key = "PartNumber" value = "H10_7_8UNF"/>
                        <Attribute key = "Revision" value = "15"/>
                        <Attribute key = "Definition" value = "H10_7_8UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "721"
                        Name = "1526806_SOLID(205)"
                        FileName = "1526806_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "205"/>
                        <Attribute key = "DescriptionInst" value = "1526806_SOLID"/>
                        <Attribute key = "PartNumber" value = "1526806_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1526806_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "724"
                    Name = "1399755(207)"
                    FileName = "1399755.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.334546 -0.07111 -0.939693 0 0.919158 -0.195373 0.34202 0 -0.207912 -0.978148 6.70552e-008 0 42.8446 227.44 44.78 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "207"/>
                    <Attribute key = "DescriptionInst" value = "1399755"/>
                    <Attribute key = "PartNumber" value = "1399755"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1399755"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "728"
                    Name = "1547681(208)"
                    FileName = "1547681.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.207912 -0.978148 6.9722e-008 0 0.978148 -0.207912 -4.63221e-021 0 1.4496e-008 6.81984e-008 1 0 51.1195 266.37 44.78 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "208"/>
                    <Attribute key = "DescriptionInst" value = "1547681"/>
                    <Attribute key = "PartNumber" value = "1547681"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1547681"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "731"
                    Name = "1526807_ASM(212)"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0.364363 0.929361 0.0593911 0 -0.334546 0.07111 0.939693 0 0.869091 -0.362258 0.336824 0 43.6935 222.418 118.146 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "212"/>
                    <Attribute key = "DescriptionInst" value = "1526807"/>
                    <Attribute key = "PartNumber" value = "1526807_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1526807_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "732"
                        Name = "H10_7_8UNF(209)"
                        FileName = "H10_7_8UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        Transformation = "0.904207 0.35039 -0.244207 0 -0.222822 0.874818 0.430168 0 0.364363 -0.334546 0.869091 0 727.101 -867.946 1020.67 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "209"/>
                        <Attribute key = "DescriptionInst" value = "H10_7_8UNF"/>
                        <Attribute key = "PartNumber" value = "H10_7_8UNF"/>
                        <Attribute key = "Revision" value = "15"/>
                        <Attribute key = "Definition" value = "H10_7_8UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "735"
                        Name = "H10_7_8_14UNF(210)"
                        FileName = "H10_7_8_14UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 -1 0 0 -1 0 0 0 0 0 -1 0 0 -32.3 -27.268 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "210"/>
                        <Attribute key = "DescriptionInst" value = "H10_7_8_14UNF"/>
                        <Attribute key = "PartNumber" value = "H10_7_8_14UNF"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "H10_7_8_14UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "738"
                        Name = "1526807_SOLID(211)"
                        FileName = "1526807_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "211"/>
                        <Attribute key = "DescriptionInst" value = "1526807_SOLID"/>
                        <Attribute key = "PartNumber" value = "1526807_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1526807_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "741"
                    Name = "1397717_ASM(215)"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-0.207912 -0.978148 0 0 -0.978148 0.207912 0 0 0 0 -1 0 -55.6728 190.753 94.5248 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "215"/>
                    <Attribute key = "DescriptionInst" value = "1397717"/>
                    <Attribute key = "PartNumber" value = "1397717_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1397717_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "742"
                        Name = "H10_7_8_14UNF(213)"
                        FileName = "H10_7_8_14UNF.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "0 -1 0 0 -1 0 0 0 0 0 -1 0 0 -32.3 -27.268 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "213"/>
                        <Attribute key = "DescriptionInst" value = "H10_7_8_14UNF"/>
                        <Attribute key = "PartNumber" value = "H10_7_8_14UNF"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "H10_7_8_14UNF"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "745"
                        Name = "1397717_SOLID(214)"
                        FileName = "1397717_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "214"/>
                        <Attribute key = "DescriptionInst" value = "1397717_SOLID"/>
                        <Attribute key = "PartNumber" value = "1397717_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1397717_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "748"
                    Name = "130845_26(216)"
                    FileName = "130845_26.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.766044 0 0.642788 0 0 1 0 0 -0.642788 0 0.766044 0 435.257 252.836 -98.5459 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "216"/>
                    <Attribute key = "DescriptionInst" value = "130845_26"/>
                    <Attribute key = "PartNumber" value = "130845_26"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "130845_26"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "751"
                    Name = "2026496_ASM(218)"
                    Type = "2"
                       
                    Transformation = "-0.642788 0 0.766044 0 0 1 0 0 -0.766044 0 -0.642788 0 402.133 252.836 -86.2167 1">
                    <Attribute key = "InstanceName" value = "218"/>
                    <Attribute key = "DescriptionInst" value = "2026496"/>
                    <Attribute key = "PartNumber" value = "2026496_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "2026496_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "752"
                        Name = "2026496_SOLID(217)"
                        FileName = "2026496_SOLID.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "217"/>
                        <Attribute key = "DescriptionInst" value = "2026496_SOLID"/>
                        <Attribute key = "PartNumber" value = "2026496_SOLID"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "2026496_SOLID"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "755"
                    Name = "130845_23(219)"
                    FileName = "130845_23.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor100"
                    Transformation = "0.642788 0 -0.766044 0 0 1 0 0 0.766044 0 0.642788 0 391.954 252.836 -94.7574 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "100"/>
                    <Attribute key = "InstanceName" value = "219"/>
                    <Attribute key = "DescriptionInst" value = "130845_23"/>
                    <Attribute key = "PartNumber" value = "130845_23"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "130845_23"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "758"
                    Name = "1399292_ASM(234)"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 828.32 393.9 -584.1 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "234"/>
                    <Attribute key = "DescriptionInst" value = "1399292"/>
                    <Attribute key = "PartNumber" value = "1399292_ASM"/>
                    <Attribute key = "Revision" value = "2"/>
                    <Attribute key = "Definition" value = "1399292_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                    <Node
                        Id = "759"
                        Name = "1397718(220)"
                        FileName = "1397718.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        >
                        <Attribute key = "Status" value = "3"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "220"/>
                        <Attribute key = "DescriptionInst" value = "1397718"/>
                        <Attribute key = "PartNumber" value = "1397718"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1397718"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "762"
                        Name = "1397740_ASM(223)"
                        Type = "2"
                           
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 182 -19.1 77.5 1">
                        <Attribute key = "InstanceName" value = "223"/>
                        <Attribute key = "DescriptionInst" value = "1397740"/>
                        <Attribute key = "PartNumber" value = "1397740_ASM"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1397740_ASM"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Node
                            Id = "763"
                            Name = "1397745(221)"
                            FileName = "1397745.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            >
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "221"/>
                            <Attribute key = "DescriptionInst" value = "1397745"/>
                            <Attribute key = "PartNumber" value = "1397745"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1397745"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "766"
                            Name = "1397741(222)"
                            FileName = "1397741.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor30"
                            Transformation = "0.968737 0 0.248091 0 0 1 0 0 -0.248091 0 0.968737 0 0 -387 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "30"/>
                            <Attribute key = "InstanceName" value = "222"/>
                            <Attribute key = "DescriptionInst" value = "1397741"/>
                            <Attribute key = "PartNumber" value = "1397741"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1397741"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                    </Node>
                    <Node
                        Id = "769"
                        Name = "293778(224)"
                        FileName = "293778.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 250 -21.6 47.5 1">
                        <Attribute key = "Status" value = "3"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "224"/>
                        <Attribute key = "DescriptionInst" value = "293778"/>
                        <Attribute key = "PartNumber" value = "293778"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "293778"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "772"
                        Name = "293778(225)"
                        FileName = "293778.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 250 -21.6 107.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "225"/>
                        <Attribute key = "DescriptionInst" value = "293778"/>
                        <Attribute key = "PartNumber" value = "293778"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "293778"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "775"
                        Name = "293778(226)"
                        FileName = "293778.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 87 -39.6 77.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "226"/>
                        <Attribute key = "DescriptionInst" value = "293778"/>
                        <Attribute key = "PartNumber" value = "293778"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "293778"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "778"
                        Name = "293741(227)"
                        FileName = "293741.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 250 -23.2 47.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "227"/>
                        <Attribute key = "DescriptionInst" value = "293741"/>
                        <Attribute key = "PartNumber" value = "293741"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "293741"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "781"
                        Name = "293741(228)"
                        FileName = "293741.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 250 -23.2 107.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "228"/>
                        <Attribute key = "DescriptionInst" value = "293741"/>
                        <Attribute key = "PartNumber" value = "293741"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "293741"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "784"
                        Name = "293741(229)"
                        FileName = "293741.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 87 -41.2 77.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "229"/>
                        <Attribute key = "DescriptionInst" value = "293741"/>
                        <Attribute key = "PartNumber" value = "293741"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "293741"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "787"
                        Name = "181941(230)"
                        FileName = "181941.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 182 -307.408 77.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "230"/>
                        <Attribute key = "DescriptionInst" value = "181941"/>
                        <Attribute key = "PartNumber" value = "181941"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "181941"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "790"
                        Name = "190785(231)"
                        FileName = "190785.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 182 -308.932 77.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "231"/>
                        <Attribute key = "DescriptionInst" value = "190785"/>
                        <Attribute key = "PartNumber" value = "190785"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "190785"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "793"
                        Name = "190784(232)"
                        FileName = "190784.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 182 -334.1 77.5 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "232"/>
                        <Attribute key = "DescriptionInst" value = "190784"/>
                        <Attribute key = "PartNumber" value = "190784"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "190784"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "796"
                        Name = "55434(233)"
                        FileName = "55434.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor30"
                        Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 58.9685 -36.0953 46 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "30"/>
                        <Attribute key = "InstanceName" value = "233"/>
                        <Attribute key = "DescriptionInst" value = "55434"/>
                        <Attribute key = "PartNumber" value = "55434"/>
                        <Attribute key = "Revision" value = "49"/>
                        <Attribute key = "Definition" value = "55434"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
                <Node
                    Id = "799"
                    Name = "16155(235)"
                    FileName = "16155.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 936.32 418.238 -468.1 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "235"/>
                    <Attribute key = "DescriptionInst" value = "16155"/>
                    <Attribute key = "PartNumber" value = "16155"/>
                    <Attribute key = "Revision" value = "14"/>
                    <Attribute key = "Definition" value = "16155"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "802"
                    Name = "216590(236)"
                    FileName = "216590.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 -1 0 0 0 0 0 1 0 936.32 380.2 -545.1 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "236"/>
                    <Attribute key = "DescriptionInst" value = "216590"/>
                    <Attribute key = "PartNumber" value = "216590"/>
                    <Attribute key = "Revision" value = "9"/>
                    <Attribute key = "Definition" value = "216590"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "805"
                    Name = "1399751(237)"
                    FileName = "1399751.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 326.6 122.576 116.588 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "237"/>
                    <Attribute key = "DescriptionInst" value = "1399751"/>
                    <Attribute key = "PartNumber" value = "1399751"/>
                    <Attribute key = "Revision" value = "5"/>
                    <Attribute key = "Definition" value = "1399751"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "808"
                    Name = "1393879_5(238)"
                    FileName = "1393879_5.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80" 
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 1294.85 -136.6 -284 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "238"/>
                    <Attribute key = "DescriptionInst" value = "1393879_5"/>
                    <Attribute key = "PartNumber" value = "1393879_5"/>
                    <Attribute key = "Revision" value = "6"/>
                    <Attribute key = "Definition" value = "1393879_5"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "811"
                    Name = "212786(239)"
                    FileName = "212786.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "0 0 1 0 0 1 0 0 -1 0 0 0 1294.85 -130.56 -284 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "239"/>
                    <Attribute key = "DescriptionInst" value = "212786"/>
                    <Attribute key = "PartNumber" value = "212786"/>
                    <Attribute key = "Revision" value = "60"/>
                    <Attribute key = "Definition" value = "212786"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "814"
                    Name = "320157(240)"
                    FileName = "320157.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 1294.85 -127.41 -284 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "240"/>
                    <Attribute key = "DescriptionInst" value = "320157"/>
                    <Attribute key = "PartNumber" value = "320157"/>
                    <Attribute key = "Revision" value = "29"/>
                    <Attribute key = "Definition" value = "320157"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "817"
                    Name = "292681(241)"
                    FileName = "292681.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 1294.85 -124.11 -284 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "241"/>
                    <Attribute key = "DescriptionInst" value = "292681"/>
                    <Attribute key = "PartNumber" value = "292681"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292681"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "820"
                    Name = "132861(242)"
                    FileName = "132861.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 1294.85 -123.11 -284 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "242"/>
                    <Attribute key = "DescriptionInst" value = "132861"/>
                    <Attribute key = "PartNumber" value = "132861"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "132861"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "823"
                    Name = "369804_51(243)"
                    FileName = "369804_51.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 -1 0 -0.422618 -0.906308 0 0 -0.906308 0.422618 0 0 355.662 20.2262 -146.942 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "243"/>
                    <Attribute key = "DescriptionInst" value = "369804_51"/>
                    <Attribute key = "PartNumber" value = "369804_51"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "369804_51"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "826"
                    Name = "369804_51(244)"
                    FileName = "369804_51.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0 0 -1 0 -0.422618 -0.906308 0 0 -0.906308 0.422618 0 0 377.028 10.2634 -146.942 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "244"/>
                    <Attribute key = "DescriptionInst" value = "369804_51"/>
                    <Attribute key = "PartNumber" value = "369804_51"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "369804_51"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "829"
                    Name = "130845_23(245)"
                    FileName = "130845_23.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 911.094 418.238 -468.1 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "245"/>
                    <Attribute key = "DescriptionInst" value = "130845_23"/>
                    <Attribute key = "PartNumber" value = "130845_23"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "130845_23"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "832"
                    Name = "1469340(246)"
                    FileName = "1469340.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 936.32 370.87 -468.1 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "246"/>
                    <Attribute key = "DescriptionInst" value = "1469340"/>
                    <Attribute key = "PartNumber" value = "1469340"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1469340"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "835"
                    Name = "397676(247)"
                    FileName = "397676.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "0.956724 -0.267931 -0.113548 0 0.033761 -0.285368 0.957823 0 -0.289033 -0.920205 -0.263973 0 636.413 254.141 -333.923 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "247"/>
                    <Attribute key = "DescriptionInst" value = "397676"/>
                    <Attribute key = "PartNumber" value = "397676"/>
                    <Attribute key = "Revision" value = "38"/>
                    <Attribute key = "Definition" value = "397676"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "838"
                    Name = "397676(248)"
                    FileName = "397676.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 613.049 -122.666 -13.5145 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "248"/>
                    <Attribute key = "DescriptionInst" value = "397676"/>
                    <Attribute key = "PartNumber" value = "397676"/>
                    <Attribute key = "Revision" value = "38"/>
                    <Attribute key = "Definition" value = "397676"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "841"
                    Name = "397676(249)"
                    FileName = "397676.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 606.167 -142.467 -172.969 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "249"/>
                    <Attribute key = "DescriptionInst" value = "397676"/>
                    <Attribute key = "PartNumber" value = "397676"/>
                    <Attribute key = "Revision" value = "38"/>
                    <Attribute key = "Definition" value = "397676"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "844"
                    Name = "17319(250)"
                    FileName = "17319.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "0 1 0 0 -1 0 0 0 0 0 1 0 360.6 295.682 -52 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "250"/>
                    <Attribute key = "DescriptionInst" value = "17319"/>
                    <Attribute key = "PartNumber" value = "17319"/>
                    <Attribute key = "Revision" value = "15"/>
                    <Attribute key = "Definition" value = "17319"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "847"
                    Name = "1360529(251)"
                    FileName = "1360529.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "0.802584 -0.0335539 0.595595 0 0.152384 0.976824 -0.150312 0 -0.576748 0.211397 0.789097 0 598.288 -65.3272 -228.234 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "251"/>
                    <Attribute key = "DescriptionInst" value = "1360529"/>
                    <Attribute key = "PartNumber" value = "1360529"/>
                    <Attribute key = "Revision" value = "38"/>
                    <Attribute key = "Definition" value = "1360529"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "850"
                    Name = "1360529(252)"
                    FileName = "1360529.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.136493 0.042246 0.98974 0 0.0690054 0.996258 -0.0520407 0 -0.988235 0.0754006 0.133067 0 710.54 -86.2946 -289.065 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "252"/>
                    <Attribute key = "DescriptionInst" value = "1360529"/>
                    <Attribute key = "PartNumber" value = "1360529"/>
                    <Attribute key = "Revision" value = "38"/>
                    <Attribute key = "Definition" value = "1360529"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "853"
                    Name = "1482394(253)"
                    FileName = "1482394.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.999841 0.017819 0 0 -0.017819 0.999841 0 0 0 0 1 0 275.333 -9.22185 -325 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "253"/>
                    <Attribute key = "DescriptionInst" value = "1482394"/>
                    <Attribute key = "PartNumber" value = "1482394"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1482394"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "856"
                    Name = "1484104(254)"
                    FileName = "1484104.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 -1 0 0 1 0 0 0 0 0 1 0 415.5 276.536 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "254"/>
                    <Attribute key = "DescriptionInst" value = "1484104"/>
                    <Attribute key = "PartNumber" value = "1484104"/>
                    <Attribute key = "Revision" value = "3"/>
                    <Attribute key = "Definition" value = "1484104"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "860"
                    Name = "1484095(255)"
                    FileName = "1484095.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 415.5 294.436 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "255"/>
                    <Attribute key = "DescriptionInst" value = "1484095"/>
                    <Attribute key = "PartNumber" value = "1484095"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1484095"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "863"
                    Name = "1484099(256)"
                    FileName = "1484099.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 415.5 344.419 -75 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "256"/>
                    <Attribute key = "DescriptionInst" value = "1484099"/>
                    <Attribute key = "PartNumber" value = "1484099"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1484099"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "866"
                    Name = "16578(257)"
                    FileName = "16578.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "0 0.965926 0.258819 0 0 -0.258819 0.965926 0 1 0 0 0 1486.04 495.25 -301.483 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "257"/>
                    <Attribute key = "DescriptionInst" value = "16578"/>
                    <Attribute key = "PartNumber" value = "16578"/>
                    <Attribute key = "Revision" value = "7"/>
                    <Attribute key = "Definition" value = "16578"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "869"
                    Name = "14587(258)"
                    FileName = "14587.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "0 -0.965926 -0.258819 0 -1 0 0 0 0 0.258819 -0.965926 0 1486.04 516.35 -295.83 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "258"/>
                    <Attribute key = "DescriptionInst" value = "14587"/>
                    <Attribute key = "PartNumber" value = "14587"/>
                    <Attribute key = "Revision" value = "12"/>
                    <Attribute key = "Definition" value = "14587"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
            </Node>
        </Node>
    </Node>
    <Node
        Id = "872"
        Name = "L005_1400(L005_1400)"
        Type = "2"
        Color = "stockColor90">
        <Attribute key = "Status" value = "1"/>
        <Attribute key = "Stock" value = "90"/>
        <Attribute key = "InstanceName" value = "L005_1400"/>
        <Attribute key = "PartNumber" value = "L005_1400"/>
        <Attribute key = "Source" value = "Unknown"/>
        <Node
            Id = "873"
            Name = "K005_1400_MOUNT(K005_1400_MOUNT)"
            Type = "2"
            Color = "stockColor90">
            <Attribute key = "Status" value = "1"/>
            <Attribute key = "Stock" value = "90"/>
            <Attribute key = "InstanceName" value = "K005_1400_MOUNT"/>
            <Attribute key = "PartNumber" value = "K005_1400_MOUNT"/>
            <Attribute key = "Source" value = "Unknown"/>
            <Node
                Id = "874"
                Name = "K005_1400_MOUNT_ASM(415)"
                Type = "2"
                Color = "stockColor90"
                >
                <Attribute key = "Status" value = "1"/>
                <Attribute key = "Stock" value = "90"/>
                <Attribute key = "InstanceName" value = "415"/>
                <Attribute key = "DescriptionInst" value = "K005_1400_MOUNT"/>
                <Attribute key = "PartNumber" value = "K005_1400_MOUNT_ASM"/>
                <Attribute key = "Revision" value = "1"/>
                <Attribute key = "Definition" value = "K005_1400_MOUNT_ASM"/>
                <Attribute key = "Source" value = "made"/>
                <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                <Node
                    Id = "875"
                    Name = "292589(378)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 205.49 149.3 385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "378"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "878"
                    Name = "292589(379)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 237.13 91.03 385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "379"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "881"
                    Name = "292589(380)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 252.61 26.55 385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "380"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "884"
                    Name = "292589(381)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 250.87 -39.73 385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "381"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "887"
                    Name = "292589(382)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 205.49 149.3 -385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "382"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "890"
                    Name = "292589(383)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 237.13 91.03 -385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "383"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "893"
                    Name = "292589(384)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 252.61 26.55 -385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "384"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "896"
                    Name = "292589(385)"
                    FileName = "292589.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 250.87 -39.73 -385 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "385"/>
                    <Attribute key = "DescriptionInst" value = "292589"/>
                    <Attribute key = "PartNumber" value = "292589"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292589"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "899"
                    Name = "292687(386)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 250.87 -39.73 -323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "386"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "902"
                    Name = "292687(387)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 252.61 26.55 -323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "387"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "905"
                    Name = "292687(388)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 237.13 91.03 -323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "388"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "908"
                    Name = "292687(389)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 205.49 149.3 -323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "389"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "911"
                    Name = "292687(390)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 205.49 149.3 323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "390"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "914"
                    Name = "292687(391)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 250.87 -39.73 323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "391"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "917"
                    Name = "292687(392)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 252.61 26.55 323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "392"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "920"
                    Name = "292687(393)"
                    FileName = "292687.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 237.13 91.03 323 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "393"/>
                    <Attribute key = "DescriptionInst" value = "292687"/>
                    <Attribute key = "PartNumber" value = "292687"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292687"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "5"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "923"
                    Name = "292620(394)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 250.87 -39.73 303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "394"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "926"
                    Name = "292620(395)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 252.61 26.55 303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "395"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "929"
                    Name = "292620(396)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor90"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 237.13 91.03 303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "396"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "932"
                    Name = "292620(397)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 205.49 149.3 303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "397"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "935"
                    Name = "292620(398)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 250.87 -39.73 -303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "398"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "938"
                    Name = "292620(399)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 252.61 26.55 -303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "399"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "941"
                    Name = "292620(400)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 237.13 91.03 -303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "400"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "944"
                    Name = "292620(401)"
                    FileName = "292620.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 205.49 149.3 -303 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "401"/>
                    <Attribute key = "DescriptionInst" value = "292620"/>
                    <Attribute key = "PartNumber" value = "292620"/>
                    <Attribute key = "Revision" value = "23"/>
                    <Attribute key = "Definition" value = "292620"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "947"
                    Name = "1482394(402)"
                    FileName = "1482394.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0.999841 0.017819 0 0 -0.017819 0.999841 0 0 0 0 1 0 275.333 -9.22185 -325 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "402"/>
                    <Attribute key = "DescriptionInst" value = "1482394"/>
                    <Attribute key = "PartNumber" value = "1482394"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1482394"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Attribute key = "BOM As Maintained" value = "2"/>
                    <Attribute key = "BOM As Designed" value = "1"/>
                </Node>
                <Node
                    Id = "950"
                    Name = "1473084(403)"
                    FileName = "1473084.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor20"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 159.85 197.4 398 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "20"/>
                    <Attribute key = "InstanceName" value = "403"/>
                    <Attribute key = "DescriptionInst" value = "1473084"/>
                    <Attribute key = "PartNumber" value = "1473084"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1473084"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "953"
                    Name = "1473084(404)"
                    FileName = "1473084.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 232.04 -103.31 398 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "404"/>
                    <Attribute key = "DescriptionInst" value = "1473084"/>
                    <Attribute key = "PartNumber" value = "1473084"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1473084"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "956"
                    Name = "1473084(405)"
                    FileName = "1473084.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor30"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 159.85 197.4 -398 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "30"/>
                    <Attribute key = "InstanceName" value = "405"/>
                    <Attribute key = "DescriptionInst" value = "1473084"/>
                    <Attribute key = "PartNumber" value = "1473084"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1473084"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "959"
                    Name = "1473084(406)"
                    FileName = "1473084.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 232.04 -103.31 -398 1">
                    <Attribute key = "Status" value = "5"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "406"/>
                    <Attribute key = "DescriptionInst" value = "1473084"/>
                    <Attribute key = "PartNumber" value = "1473084"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1473084"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "962"
                    Name = "292689(407)"
                    FileName = "292689.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 159.85 197.4 -322.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "407"/>
                    <Attribute key = "DescriptionInst" value = "292689"/>
                    <Attribute key = "PartNumber" value = "292689"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292689"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "965"
                    Name = "292603(408)"
                    FileName = "292603.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 159.85 197.4 -320 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "408"/>
                    <Attribute key = "DescriptionInst" value = "292603"/>
                    <Attribute key = "PartNumber" value = "292603"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292603"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "968"
                    Name = "292689(409)"
                    FileName = "292689.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 232.04 -103.31 -317.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "409"/>
                    <Attribute key = "DescriptionInst" value = "292689"/>
                    <Attribute key = "PartNumber" value = "292689"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292689"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "971"
                    Name = "292603(410)"
                    FileName = "292603.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor50"
                    Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 232.04 -103.31 -315 1">
                    <Attribute key = "Status" value = "5"/>
                    <Attribute key = "Stock" value = "50"/>
                    <Attribute key = "InstanceName" value = "410"/>
                    <Attribute key = "DescriptionInst" value = "292603"/>
                    <Attribute key = "PartNumber" value = "292603"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292603"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "974"
                    Name = "292689(411)"
                    FileName = "292689.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 159.85 197.4 322.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "411"/>
                    <Attribute key = "DescriptionInst" value = "292689"/>
                    <Attribute key = "PartNumber" value = "292689"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292689"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "977"
                    Name = "292689(412)"
                    FileName = "292689.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 232.04 -103.31 322.5 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "412"/>
                    <Attribute key = "DescriptionInst" value = "292689"/>
                    <Attribute key = "PartNumber" value = "292689"/>
                    <Attribute key = "Revision" value = "22"/>
                    <Attribute key = "Definition" value = "292689"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "980"
                    Name = "292603(413)"
                    FileName = "292603.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 159.85 197.4 320 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "413"/>
                    <Attribute key = "DescriptionInst" value = "292603"/>
                    <Attribute key = "PartNumber" value = "292603"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292603"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
                <Node
                    Id = "983"
                    Name = "292603(414)"
                    FileName = "292603.CATPart.3dvs"
                    Type = "2"
                    Color = "stockColor80"
                    Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 232.04 -103.31 320 1">
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "80"/>
                    <Attribute key = "InstanceName" value = "414"/>
                    <Attribute key = "DescriptionInst" value = "292603"/>
                    <Attribute key = "PartNumber" value = "292603"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "292603"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                </Node>
            </Node>
        </Node>
        <Node
            Id = "986"
            Name = "K005_1400_HST(K005_1400_HST)"
            Type = "2"
            Color = "stockColor80">
            <Attribute key = "Status" value = "1"/>
            <Attribute key = "Stock" value = "80"/>
            <Attribute key = "InstanceName" value = "K005_1400_HST"/>
            <Attribute key = "PartNumber" value = "K005_1400_HST"/>
            <Attribute key = "Source" value = "Unknown"/>
            <Node
                Id = "987"
                Name = "K005_1400_HST_ASM(377)"
                Type = "2"
                Color = "stockColor80"
                >
                <Attribute key = "Status" value = "1"/>
                <Attribute key = "Stock" value = "80"/>
                <Attribute key = "InstanceName" value = "377"/>
                <Attribute key = "DescriptionInst" value = "K005_1400_HST"/>
                <Attribute key = "PartNumber" value = "K005_1400_HST_ASM"/>
                <Attribute key = "Revision" value = "1"/>
                <Attribute key = "Definition" value = "K005_1400_HST_ASM"/>
                <Attribute key = "Source" value = "made"/>
                <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                <Node
                    Id = "988"
                    Name = "1394690_ASM(376)"
                    Type = "2"
                    Color = "stockColor90"
                    >
                    <Attribute key = "Status" value = "1"/>
                    <Attribute key = "Stock" value = "90"/>
                    <Attribute key = "InstanceName" value = "376"/>
                    <Attribute key = "DescriptionInst" value = "1394690"/>
                    <Attribute key = "PartNumber" value = "1394690_ASM"/>
                    <Attribute key = "Revision" value = "1"/>
                    <Attribute key = "Definition" value = "1394690_ASM"/>
                    <Attribute key = "Source" value = "made"/>
                    <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    <Node
                        Id = "989"
                        Name = "1529670_ASM(267)"
                        Type = "2"
                           
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 0 0 -270 1">
                        <Attribute key = "InstanceName" value = "267"/>
                        <Attribute key = "DescriptionInst" value = "1529670"/>
                        <Attribute key = "PartNumber" value = "1529670_ASM"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1529670_ASM"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Node
                            Id = "990"
                            Name = "1528410_AF0(266)"
                            FileName = "1528410_AF0.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            >
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "266"/>
                            <Attribute key = "DescriptionInst" value = "1528410"/>
                            <Attribute key = "PartNumber" value = "1528410_AF0"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1528410_AF0"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                    </Node>
                    <Node
                        Id = "993"
                        Name = "1493910_ASM(297)"
                        Type = "2"
                        Color = "stockColor90"
                        >
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "90"/>
                        <Attribute key = "InstanceName" value = "297"/>
                        <Attribute key = "DescriptionInst" value = "1493910"/>
                        <Attribute key = "PartNumber" value = "1493910_ASM"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1493910_ASM"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Node
                            Id = "994"
                            Name = "1493910_176657(268)"
                            FileName = "1493910_176657.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            >
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "268"/>
                            <Attribute key = "DescriptionInst" value = "1493910_176657"/>
                            <Attribute key = "PartNumber" value = "1493910_176657"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1493910_176657"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "997"
                            Name = "8501503(269)"
                            FileName = "8501503.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 0 0 123.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "269"/>
                            <Attribute key = "DescriptionInst" value = "8501503"/>
                            <Attribute key = "PartNumber" value = "8501503"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "8501503"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1000"
                            Name = "8501505(270)"
                            FileName = "8501505.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 0 0 -123.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "270"/>
                            <Attribute key = "DescriptionInst" value = "8501505"/>
                            <Attribute key = "PartNumber" value = "8501505"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "8501505"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1003"
                            Name = "296079(271)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 86.504 76.533 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "271"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1006"
                            Name = "296079(272)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 67.071 94.03 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "272"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1009"
                            Name = "296079(273)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -23.027 113.181 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "273"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1012"
                            Name = "296079(274)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -47.897 105.101 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "274"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1015"
                            Name = "296079(275)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -109.531 36.649 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "275"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1018"
                            Name = "296079(276)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -114.968 11.07 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "276"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1021"
                            Name = "296079(277)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -86.504 -76.533 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "277"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1024"
                            Name = "296079(278)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -67.071 -94.03 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "278"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1027"
                            Name = "296079(279)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor90"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 23.027 -113.181 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "90"/>
                            <Attribute key = "InstanceName" value = "279"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1030"
                            Name = "296079(280)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 47.897 -105.101 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "280"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1033"
                            Name = "296079(281)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 109.531 -36.649 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "281"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1036"
                            Name = "296079(282)"
                            FileName = "296079.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 114.968 -11.07 -161.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "282"/>
                            <Attribute key = "DescriptionInst" value = "296079"/>
                            <Attribute key = "PartNumber" value = "296079"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296079"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1039"
                            Name = "8501502_A(283)"
                            FileName = "8501502_A.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 0 0 115.05 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "283"/>
                            <Attribute key = "DescriptionInst" value = "8501502_A"/>
                            <Attribute key = "PartNumber" value = "8501502_A"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "8501502_A"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1042"
                            Name = "8501502_A(284)"
                            FileName = "8501502_A.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 0 0 -115.05 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "284"/>
                            <Attribute key = "DescriptionInst" value = "8501502_A"/>
                            <Attribute key = "PartNumber" value = "8501502_A"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "8501502_A"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1045"
                            Name = "296083(285)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 114.968 -11.07 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "285"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1048"
                            Name = "296083(286)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 109.531 -36.649 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "286"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1051"
                            Name = "296083(287)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 23.027 -113.181 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "287"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1054"
                            Name = "296083(288)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 47.897 -105.101 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "288"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1057"
                            Name = "296083(289)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 67.071 94.03 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "289"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1060"
                            Name = "296083(290)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 86.504 76.533 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "290"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1063"
                            Name = "296083(291)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 -23.027 113.181 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "291"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1066"
                            Name = "296083(292)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 -47.897 105.101 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "292"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1069"
                            Name = "296083(293)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor100"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 -109.531 36.649 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "100"/>
                            <Attribute key = "InstanceName" value = "293"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1072"
                            Name = "296083(294)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 -114.968 11.07 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "294"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1075"
                            Name = "296083(295)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 -86.504 -76.533 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "295"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1078"
                            Name = "296083(296)"
                            FileName = "296083.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 -67.071 -94.03 191.55 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "296"/>
                            <Attribute key = "DescriptionInst" value = "296083"/>
                            <Attribute key = "PartNumber" value = "296083"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "296083"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                    </Node>
                    <Node
                        Id = "1081"
                        Name = "1536794_ASM(310)"
                        Type = "2"
                           
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 0 0 548.375 1">
                        <Attribute key = "InstanceName" value = "310"/>
                        <Attribute key = "DescriptionInst" value = "1536794"/>
                        <Attribute key = "PartNumber" value = "1536794_ASM"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1536794_ASM"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Node
                            Id = "1082"
                            Name = "1536684(298)"
                            FileName = "1536684.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            >
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "298"/>
                            <Attribute key = "DescriptionInst" value = "1536684"/>
                            <Attribute key = "PartNumber" value = "1536684"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1536684"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1085"
                            Name = "30098(299)"
                            FileName = "30098.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 -56.6 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "299"/>
                            <Attribute key = "DescriptionInst" value = "30098"/>
                            <Attribute key = "PartNumber" value = "30098"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "30098"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1088"
                            Name = "1313897(300)"
                            FileName = "1313897.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 -1 0 0 0 0 1 0 -1 0 0 0 9 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "300"/>
                            <Attribute key = "DescriptionInst" value = "1313897"/>
                            <Attribute key = "PartNumber" value = "1313897"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1313897"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1091"
                            Name = "504314781(301)"
                            FileName = "504314781.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 1.7 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "301"/>
                            <Attribute key = "DescriptionInst" value = "504314781"/>
                            <Attribute key = "PartNumber" value = "504314781"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "504314781"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1095"
                            Name = "504314782(302)"
                            FileName = "504314782.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 53.7 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "302"/>
                            <Attribute key = "DescriptionInst" value = "504314782"/>
                            <Attribute key = "PartNumber" value = "504314782"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "504314782"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1099"
                            Name = "1394618(303)"
                            FileName = "1394618.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 -1 0 0 0 0 1 0 -1 0 0 0 8.604 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "303"/>
                            <Attribute key = "DescriptionInst" value = "1394618"/>
                            <Attribute key = "PartNumber" value = "1394618"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1394618"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1103"
                            Name = "1393342(304)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 24 122.5 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "304"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1106"
                            Name = "1393342(305)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0.5 -0.866025 0 0 0.866025 0.5 0 1 0 0 0 24 61.25 -106.36 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "305"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1109"
                            Name = "1393342(306)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0.5 0.866025 0 0 -0.866025 0.5 0 1 0 0 0 24 61.3 106.1 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "306"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1112"
                            Name = "1393342(307)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0.5 0.866025 0 0 -0.866025 0.5 0 1 0 0 0 24 -61.25 -106.088 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "307"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1115"
                            Name = "1393342(308)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 0.5 -0.866025 0 0 0.866025 0.5 0 1 0 0 0 24 -61.1 106.1 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "308"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1118"
                            Name = "1393342(309)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 24 -122.5 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "309"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                    </Node>
                    <Node
                        Id = "1121"
                        Name = "1536794_ASM(311)"
                        Type = "2"
                           
                        Transformation = "0 0 1 0 1 0 0 0 0 1 0 0 0 0 -548.375 1">
                        <Attribute key = "InstanceName" value = "311"/>
                        <Attribute key = "DescriptionInst" value = "1536794"/>
                        <Attribute key = "PartNumber" value = "1536794_ASM"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1536794_ASM"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Node
                            Id = "1122"
                            Name = "1536684(298)"
                            FileName = "1536684.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            >
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "298"/>
                            <Attribute key = "DescriptionInst" value = "1536684"/>
                            <Attribute key = "PartNumber" value = "1536684"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1536684"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1125"
                            Name = "30098(299)"
                            FileName = "30098.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 -56.6 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "299"/>
                            <Attribute key = "DescriptionInst" value = "30098"/>
                            <Attribute key = "PartNumber" value = "30098"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "30098"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1128"
                            Name = "1313897(300)"
                            FileName = "1313897.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 -1 0 0 0 0 1 0 -1 0 0 0 9 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "300"/>
                            <Attribute key = "DescriptionInst" value = "1313897"/>
                            <Attribute key = "PartNumber" value = "1313897"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1313897"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1131"
                            Name = "504314781(301)"
                            FileName = "504314781.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 1.7 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "301"/>
                            <Attribute key = "DescriptionInst" value = "504314781"/>
                            <Attribute key = "PartNumber" value = "504314781"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "504314781"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1135"
                            Name = "504314782(302)"
                            FileName = "504314782.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 53.7 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "302"/>
                            <Attribute key = "DescriptionInst" value = "504314782"/>
                            <Attribute key = "PartNumber" value = "504314782"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "504314782"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1139"
                            Name = "1394618(303)"
                            FileName = "1394618.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor70"
                            Transformation = "0 -1 0 0 0 0 1 0 -1 0 0 0 8.604 0 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "70"/>
                            <Attribute key = "InstanceName" value = "303"/>
                            <Attribute key = "DescriptionInst" value = "1394618"/>
                            <Attribute key = "PartNumber" value = "1394618"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "1394618"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1143"
                            Name = "1393342(304)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor0"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 24 122.5 0 1">
                            <Attribute key = "Status" value = ""/>
                            <Attribute key = "Stock" value = "0"/>
                            <Attribute key = "InstanceName" value = "304"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1146"
                            Name = "1393342(305)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0.5 -0.866025 0 0 0.866025 0.5 0 1 0 0 0 24 61.25 -106.36 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "305"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1149"
                            Name = "1393342(306)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0.5 0.866025 0 0 -0.866025 0.5 0 1 0 0 0 24 61.3 106.1 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "306"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1152"
                            Name = "1393342(307)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0.5 0.866025 0 0 -0.866025 0.5 0 1 0 0 0 24 -61.25 -106.088 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "307"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1155"
                            Name = "1393342(308)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0.5 -0.866025 0 0 0.866025 0.5 0 1 0 0 0 24 -61.1 106.1 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "308"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1158"
                            Name = "1393342(309)"
                            FileName = "1393342.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 24 -122.5 0 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "309"/>
                            <Attribute key = "DescriptionInst" value = "1393342"/>
                            <Attribute key = "PartNumber" value = "1393342"/>
                            <Attribute key = "Revision" value = "2"/>
                            <Attribute key = "Definition" value = "1393342"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                    </Node>
                    <Node
                        Id = "1161"
                        Name = "30097(312)"
                        FileName = "30097.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 0 0 603.83 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "312"/>
                        <Attribute key = "DescriptionInst" value = "30097"/>
                        <Attribute key = "PartNumber" value = "30097"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "30097"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1165"
                        Name = "1394621(313)"
                        FileName = "1394621.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 0 1 0 0 -1 0 0 0 0 648 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "313"/>
                        <Attribute key = "DescriptionInst" value = "1394621"/>
                        <Attribute key = "PartNumber" value = "1394621"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1394621"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1168"
                        Name = "1394620(314)"
                        FileName = "1394620.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.994522 -0.104528 0 0 0.104528 0.994522 0 0 0 0 1 0 0 0 648 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "314"/>
                        <Attribute key = "DescriptionInst" value = "1394620"/>
                        <Attribute key = "PartNumber" value = "1394620"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1394620"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1171"
                        Name = "1394622(315)"
                        FileName = "1394622.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.994522 -0.104528 0 0 0.104528 0.994522 0 0 0 0 1 0 0 0 661.169 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "315"/>
                        <Attribute key = "DescriptionInst" value = "1394622"/>
                        <Attribute key = "PartNumber" value = "1394622"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1394622"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1174"
                        Name = "30097(316)"
                        FileName = "30097.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 0 0 -603.83 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "316"/>
                        <Attribute key = "DescriptionInst" value = "30097"/>
                        <Attribute key = "PartNumber" value = "30097"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "30097"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1178"
                        Name = "1394621(317)"
                        FileName = "1394621.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 0 -1 0 0 -1 0 0 0 0 -648 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "317"/>
                        <Attribute key = "DescriptionInst" value = "1394621"/>
                        <Attribute key = "PartNumber" value = "1394621"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1394621"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1181"
                        Name = "1394620(318)"
                        FileName = "1394620.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.994522 -0.104528 0 0 -0.104528 0.994522 0 0 0 0 -1 0 0 0 -648 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "318"/>
                        <Attribute key = "DescriptionInst" value = "1394620"/>
                        <Attribute key = "PartNumber" value = "1394620"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1394620"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1184"
                        Name = "1394622(319)"
                        FileName = "1394622.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.587801 0.809005 0 0 0.809005 0.587801 0 0 0 0 -1 0 0 0 -661.169 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "319"/>
                        <Attribute key = "DescriptionInst" value = "1394622"/>
                        <Attribute key = "PartNumber" value = "1394622"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1394622"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1187"
                        Name = "1479757(320)"
                        FileName = "1479757.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        Transformation = "0.866025 -0.5 0 0 0.5 0.866025 0 0 0 0 1 0 0 0 594.375 1">
                        <Attribute key = "Status" value = ""/>
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "320"/>
                        <Attribute key = "DescriptionInst" value = "1479757"/>
                        <Attribute key = "PartNumber" value = "1479757"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1479757"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1190"
                        Name = "1479757(321)"
                        FileName = "1479757.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor100"
                        Transformation = "-0.866025 -0.5 0 0 -0.5 0.866025 0 0 0 0 -1 0 0 0 -594.375 1">
                        <Attribute key = "Status" value = ""/>
                        <Attribute key = "Stock" value = "100"/>
                        <Attribute key = "InstanceName" value = "321"/>
                        <Attribute key = "DescriptionInst" value = "1479757"/>
                        <Attribute key = "PartNumber" value = "1479757"/>
                        <Attribute key = "Revision" value = "2"/>
                        <Attribute key = "Definition" value = "1479757"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1193"
                        Name = "1388836(322)"
                        FileName = "1388836.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 0 0 363.9 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "322"/>
                        <Attribute key = "DescriptionInst" value = "1388836"/>
                        <Attribute key = "PartNumber" value = "1388836"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1388836"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1196"
                        Name = "292687(324)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 -38.82 144.89 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "324"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1199"
                        Name = "292687(325)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 38.82 144.89 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "325"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1202"
                        Name = "292687(326)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 106.07 106.07 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "326"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1205"
                        Name = "292687(327)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 106.07 -106.07 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "327"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1208"
                        Name = "292687(328)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 38.82 -144.89 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "328"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1211"
                        Name = "292687(329)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 -38.82 -144.89 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "329"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1214"
                        Name = "292687(330)"
                        FileName = "292687.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 -106.07 -106.07 235 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "330"/>
                        <Attribute key = "DescriptionInst" value = "292687"/>
                        <Attribute key = "PartNumber" value = "292687"/>
                        <Attribute key = "Revision" value = "22"/>
                        <Attribute key = "Definition" value = "292687"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Attribute key = "BOM As Maintained" value = "5"/>
                        <Attribute key = "BOM As Designed" value = "1"/>
                    </Node>
                    <Node
                        Id = "1217"
                        Name = "292588(331)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 106.07 -106.07 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "331"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1220"
                        Name = "292588(332)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 38.82 -144.89 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "332"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1223"
                        Name = "292588(333)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -38.82 -144.89 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "333"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1226"
                        Name = "292588(334)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -106.07 -106.07 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "334"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1229"
                        Name = "292588(335)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 -38.82 144.89 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "335"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1232"
                        Name = "292588(336)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor20"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 38.82 144.89 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "20"/>
                        <Attribute key = "InstanceName" value = "336"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1235"
                        Name = "292588(337)"
                        FileName = "292588.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 106.07 106.07 237 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "337"/>
                        <Attribute key = "DescriptionInst" value = "292588"/>
                        <Attribute key = "PartNumber" value = "292588"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "292588"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1238"
                        Name = "352220(340)"
                        FileName = "352220.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 -62.699 45.553 -670.975 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "340"/>
                        <Attribute key = "DescriptionInst" value = "352220"/>
                        <Attribute key = "PartNumber" value = "352220"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "352220"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1241"
                        Name = "352220(341)"
                        FileName = "352220.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 62.699 -45.553 -670.975 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "341"/>
                        <Attribute key = "DescriptionInst" value = "352220"/>
                        <Attribute key = "PartNumber" value = "352220"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "352220"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1244"
                        Name = "352220(342)"
                        FileName = "352220.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 -62.699 -45.553 670.975 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "342"/>
                        <Attribute key = "DescriptionInst" value = "352220"/>
                        <Attribute key = "PartNumber" value = "352220"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "352220"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1247"
                        Name = "352220(343)"
                        FileName = "352220.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 0 1 0 0 1 0 0 62.699 45.553 670.975 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "343"/>
                        <Attribute key = "DescriptionInst" value = "352220"/>
                        <Attribute key = "PartNumber" value = "352220"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "352220"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1250"
                        Name = "1388803(344)"
                        FileName = "1388803.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 134 -5 -80 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "344"/>
                        <Attribute key = "DescriptionInst" value = "1388803"/>
                        <Attribute key = "PartNumber" value = "1388803"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1388803"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1253"
                        Name = "136985(349)"
                        FileName = "136985.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 148.2 75 -60 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "349"/>
                        <Attribute key = "DescriptionInst" value = "136985"/>
                        <Attribute key = "PartNumber" value = "136985"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "136985"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1256"
                        Name = "136985(350)"
                        FileName = "136985.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 148.2 10 -60 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "350"/>
                        <Attribute key = "DescriptionInst" value = "136985"/>
                        <Attribute key = "PartNumber" value = "136985"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "136985"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1259"
                        Name = "136985(351)"
                        FileName = "136985.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 148.2 75 60 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "351"/>
                        <Attribute key = "DescriptionInst" value = "136985"/>
                        <Attribute key = "PartNumber" value = "136985"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "136985"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1262"
                        Name = "136985(352)"
                        FileName = "136985.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 148.2 10 60 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "352"/>
                        <Attribute key = "DescriptionInst" value = "136985"/>
                        <Attribute key = "PartNumber" value = "136985"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "136985"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1265"
                        Name = "1399894_ASM(365)"
                        Type = "2"
                           
                        Transformation = "0 0 -1 0 0.207912 0.978148 0 0 0.978148 -0.207912 0 0 0.126163 207.508 81.7664 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "365"/>
                        <Attribute key = "DescriptionInst" value = "1399894"/>
                        <Attribute key = "PartNumber" value = "1399894_ASM"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1399894_ASM"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        <Node
                            Id = "1266"
                            Name = "CC_222524(353)"
                            FileName = "CC_222524.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            >
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "353"/>
                            <Attribute key = "DescriptionInst" value = "CC_222524"/>
                            <Attribute key = "PartNumber" value = "CC_222524"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "CC_222524"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1269"
                            Name = "CC_420097(354)"
                            FileName = "CC_420097.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 60.124 -24.9 68.0212 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "354"/>
                            <Attribute key = "DescriptionInst" value = "CC_420097"/>
                            <Attribute key = "PartNumber" value = "CC_420097"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "CC_420097"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1272"
                            Name = "CC_420097(355)"
                            FileName = "CC_420097.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "1 0 0 0 0 1 0 0 0 0 1 0 22.86 -24.9 68.0212 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "355"/>
                            <Attribute key = "DescriptionInst" value = "CC_420097"/>
                            <Attribute key = "PartNumber" value = "CC_420097"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "CC_420097"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1275"
                            Name = "14594(356)"
                            FileName = "14594.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 12.3194 -19.9 68.0212 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "356"/>
                            <Attribute key = "DescriptionInst" value = "14594"/>
                            <Attribute key = "PartNumber" value = "14594"/>
                            <Attribute key = "Revision" value = "9"/>
                            <Attribute key = "Definition" value = "14594"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1278"
                            Name = "222441(357)"
                            FileName = "222441.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 42.0116 -24.9 118.633 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "357"/>
                            <Attribute key = "DescriptionInst" value = "222441"/>
                            <Attribute key = "PartNumber" value = "222441"/>
                            <Attribute key = "Revision" value = "12"/>
                            <Attribute key = "Definition" value = "222441"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1281"
                            Name = "222441(358)"
                            FileName = "222441.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "-1 0 0 0 0 1 0 0 0 0 -1 0 42.0116 -56.007 118.633 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "358"/>
                            <Attribute key = "DescriptionInst" value = "222441"/>
                            <Attribute key = "PartNumber" value = "222441"/>
                            <Attribute key = "Revision" value = "12"/>
                            <Attribute key = "Definition" value = "222441"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1284"
                            Name = "222443(359)"
                            FileName = "222443.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 -1.1938 -56.007 68.0212 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "359"/>
                            <Attribute key = "DescriptionInst" value = "222443"/>
                            <Attribute key = "PartNumber" value = "222443"/>
                            <Attribute key = "Revision" value = "12"/>
                            <Attribute key = "Definition" value = "222443"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1287"
                            Name = "CP700-1L-B-0-150(360)"
                            FileName = "CP700-1L-B-0-150.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 -1 0 0 0 0 1 0 0 42.0116 -24.9 2.3876 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "360"/>
                            <Attribute key = "DescriptionInst" value = "CP700-1L-B-0-150"/>
                            <Attribute key = "PartNumber" value = "CP700-1L-B-0-150"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "CP700-1L-B-0-150"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1290"
                            Name = "CC_321828_COIL(361)"
                            FileName = "CC_321828_COIL.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 0 -1 0 1 0 0 0 0 -1 0 0 89.46 -34.0106 37.641 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "361"/>
                            <Attribute key = "DescriptionInst" value = "CC_321828_COIL"/>
                            <Attribute key = "PartNumber" value = "CC_321828_COIL"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "CC_321828_COIL"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1293"
                            Name = "222442(362)"
                            FileName = "222442.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 -8.2546 -19.9 68.0212 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "362"/>
                            <Attribute key = "DescriptionInst" value = "222442"/>
                            <Attribute key = "PartNumber" value = "222442"/>
                            <Attribute key = "Revision" value = "12"/>
                            <Attribute key = "Definition" value = "222442"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1296"
                            Name = "COSMETIC_PART(363)"
                            FileName = "COSMETIC_PART.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor80"
                            Transformation = "-1 0 0 0 0 -1 0 0 0 0 1 0 12.3114 -56.007 35.0266 1">
                            <Attribute key = "Status" value = "2"/>
                            <Attribute key = "Stock" value = "80"/>
                            <Attribute key = "InstanceName" value = "363"/>
                            <Attribute key = "DescriptionInst" value = "COSMETIC_PART"/>
                            <Attribute key = "PartNumber" value = "COSMETIC_PART"/>
                            <Attribute key = "Revision" value = "1"/>
                            <Attribute key = "Definition" value = "COSMETIC_PART"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                        <Node
                            Id = "1299"
                            Name = "222442(364)"
                            FileName = "222442.CATPart.3dvs"
                            Type = "2"
                            Color = "stockColor60"
                            Transformation = "0 1 0 0 0 0 1 0 1 0 0 0 -20.1879 -56.007 35.0266 1">
                            <Attribute key = "Status" value = "1"/>
                            <Attribute key = "Stock" value = "60"/>
                            <Attribute key = "InstanceName" value = "364"/>
                            <Attribute key = "DescriptionInst" value = "222442"/>
                            <Attribute key = "PartNumber" value = "222442"/>
                            <Attribute key = "Revision" value = "12"/>
                            <Attribute key = "Definition" value = "222442"/>
                            <Attribute key = "Source" value = "made"/>
                            <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                        </Node>
                    </Node>
                    <Node
                        Id = "1302"
                        Name = "201752(366)"
                        FileName = "201752.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.169854 0.0361035 0.984808 0 -0.963287 0.204753 -0.173648 0 -0.207912 -0.978148 0 0 101.244 208.243 41.2534 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "366"/>
                        <Attribute key = "DescriptionInst" value = "201752"/>
                        <Attribute key = "PartNumber" value = "201752"/>
                        <Attribute key = "Revision" value = "12"/>
                        <Attribute key = "Definition" value = "201752"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1305"
                        Name = "293781(367)"
                        FileName = "293781.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.978148 0.207912 0 0 0 0 1 0 0.207912 0.978148 0 0 47.0968 197.524 64.75 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "367"/>
                        <Attribute key = "DescriptionInst" value = "293781"/>
                        <Attribute key = "PartNumber" value = "293781"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "293781"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1308"
                        Name = "296037(368)"
                        FileName = "296037.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.207912 0.978148 0 0 -0.978148 0.207912 0 0 0 0 1 0 47.6166 199.97 64.75 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "368"/>
                        <Attribute key = "DescriptionInst" value = "296037"/>
                        <Attribute key = "PartNumber" value = "296037"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "296037"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1311"
                        Name = "16538(369)"
                        FileName = "16538.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.207912 0.978148 0 0 -0.978148 0.207912 0 0 0 0 1 0 38.3071 206.092 11.764 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "369"/>
                        <Attribute key = "DescriptionInst" value = "16538"/>
                        <Attribute key = "PartNumber" value = "16538"/>
                        <Attribute key = "Revision" value = "21"/>
                        <Attribute key = "Definition" value = "16538"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1314"
                        Name = "293781(370)"
                        FileName = "293781.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.978148 0.207912 0 0 0 0 1 0 0.207912 0.978148 0 0 86.2227 189.208 15.75 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "370"/>
                        <Attribute key = "DescriptionInst" value = "293781"/>
                        <Attribute key = "PartNumber" value = "293781"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "293781"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1317"
                        Name = "293781(371)"
                        FileName = "293781.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "-0.978148 0.207912 0 0 0 0 1 0 0.207912 0.978148 0 0 13.8398 204.593 15.75 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "371"/>
                        <Attribute key = "DescriptionInst" value = "293781"/>
                        <Attribute key = "PartNumber" value = "293781"/>
                        <Attribute key = "Revision" value = "10"/>
                        <Attribute key = "Definition" value = "293781"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1320"
                        Name = "296037(372)"
                        FileName = "296037.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.207912 0.978148 0 0 -0.978148 0.207912 0 0 0 0 1 0 86.7425 191.653 15.75 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "372"/>
                        <Attribute key = "DescriptionInst" value = "296037"/>
                        <Attribute key = "PartNumber" value = "296037"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "296037"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1323"
                        Name = "296037(373)"
                        FileName = "296037.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "0.207912 0.978148 0 0 -0.978148 0.207912 0 0 0 0 1 0 14.3596 207.039 15.75 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "373"/>
                        <Attribute key = "DescriptionInst" value = "296037"/>
                        <Attribute key = "PartNumber" value = "296037"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "296037"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1326"
                        Name = "1466922(374)"
                        FileName = "1466922.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor80"
                        Transformation = "1 0 0 0 0 0 -1 0 0 1 0 0 0 0 384.55 1">
                        <Attribute key = "Status" value = "1"/>
                        <Attribute key = "Stock" value = "80"/>
                        <Attribute key = "InstanceName" value = "374"/>
                        <Attribute key = "DescriptionInst" value = "1466922"/>
                        <Attribute key = "PartNumber" value = "1466922"/>
                        <Attribute key = "Revision" value = "1"/>
                        <Attribute key = "Definition" value = "1466922"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                    <Node
                        Id = "1329"
                        Name = "222413(375)"
                        FileName = "222413.CATPart.3dvs"
                        Type = "2"
                        Color = "stockColor40"
                        Transformation = "0.978148 -0.207912 0 0 -0.207912 -0.978148 0 0 0 0 -1 0 12.5891 179.403 121.543 1">
                        <Attribute key = "Stock" value = "40"/>
                        <Attribute key = "InstanceName" value = "375"/>
                        <Attribute key = "DescriptionInst" value = "222413"/>
                        <Attribute key = "PartNumber" value = "222413"/>
                        <Attribute key = "Revision" value = "7"/>
                        <Attribute key = "Definition" value = "222413"/>
                        <Attribute key = "Source" value = "made"/>
                        <Attribute key = "DescriptionRef" value = "NOT SPECIFIED"/>
                    </Node>
                </Node>
            </Node>
        </Node>
    </Node>
</Node>
</Node>`;