/*  Widget Types:
        Button = 1
        ButtonLet = 2
        Dropdown = 3
        Input = 4
*/
var UserConfiguration = {
    langId: 'User',
    groups: [{
        langId: 'Demo Button',
        widgets: [
            { type: 1, langId: 'ID_RIBBON_DEACTIVATE_ALL', iconId: 'deactivate_all' }
        ],
        groupClick: function (context, id, group)
        {
            var client = context.client;
            var ribbon = context.ribbon;
            let xml = '<Call Method="ActivateInteraction"><Interaction>Deactivate_AllInteractions</Interaction></Call>'
            client.command.executeXMLAsync(xml)
        }
    },
    {
        langId: 'Demo Buttonlet',
        widgets: [
            { type: 2, langId: 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL', iconId: '1_1_schnittebene_aus_punkt', ribbonClass: ['Interaction'] },
            { type: 2, langId: 'ID_RIBBON_DRAFT_ANGLE', iconId: '1_entformungsschraegen_analyse', ribbonClass: ['Interaction'] }
        ],
        groupClick: function (context, id, group)
        {
            var client = context.client
            let ribbon = context.ribbon
            let clickedButton = group.getButtonById(id);
            let mode
            if (clickedButton.langId === 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL')
            {
                mode = 'Section_FromPointNormal'
            }
            else
            {
                mode = 'DraftAngle'
            }
            clickedButton.event = mode
            context.menu.handleActivateInteraction(client, mode)
        }
    },
    {
        langId: 'Demo dropdownbutton',
        widgets: [
            {
                type: 3, langId: 'ID_RIBBON_MEASUREMENT', iconId: '2_bemassung', ribbonClass: ['Interaction'],
                dropdownItems: [
                    { langId: 'ID_RIBBON_LENGTH', iconId: '1_laenge' },
                    { langId: 'ID_RIBBON_RADIUS', iconId: '1_1_radius' },
                    { langId: 'ID_RIBBON_DISTANCE_BETWEEN_TWO_POINTS', iconId: '1_1_punkt_zu_punkt' }
                ],
                dropdownClick: function (context, item, button)
                {
                    var client = context.client
                    let ribbon = context.ribbon
                    let mode
                    switch (item.langId)
                    {
                        case 'ID_RIBBON_CUTTING_PLANE_FROM_POINT_NORMAL':
                            mode = 'Measurement_EdgeLength'
                            break
                        case 'ID_RIBBON_RADIUS':
                            mode = 'Measurement_CircleRadius'
                            break
                        case 'ID_RIBBON_DISTANCE_BETWEEN_TWO_POINTS':
                            mode = 'Measurement_DistancePointToPoint'
                            break
                    }
                    item.event = mode
                    context.menu.handleActivateInteraction(client, mode)
                }
            }]
    },
    {
        langId: 'Attribute filter',
        widgets: [
            { type: 1, langId: 'Open dialog', iconId: '1_filter' }
        ],
        groupClick: function (context, id, group)
        {
            var client = context.client;
            var ribbon = context.ribbon;
            let modal = document.createElement('div');
            modal.style.position = 'absolute';
            modal.style.top = '10%';
            modal.style.left = '10%';
            modal.style.zIndex = '10000';
            modal.style.height = '300px';
            modal.style.width = '300px';
            modal.style.backgroundColor = 'white';
            modal.style.border = '1px solid black';
            modal.style.padding = '20px';

            let nameTitle = document.createElement('div');
            nameTitle.textContent = 'Attribute name';
            nameTitle.style.padding = '10px';
            nameTitle.style.fontSize = '16px';
            nameTitle.style.fontWeight = '600';
            modal.appendChild(nameTitle);

            let nameInput = document.createElement('input');
            nameInput.id = 'Custom_attribute_filter_name';
            nameInput.style.margin = '10px';
            modal.appendChild(nameInput);

            let valueTitle = document.createElement('div');
            valueTitle.textContent = 'Attribute value';
            valueTitle.style.padding = '10px';
            valueTitle.style.fontSize = '16px';
            valueTitle.style.fontWeight = '600';
            modal.appendChild(valueTitle);

            let valueInput = document.createElement('input');
            valueInput.id = 'Custom_attribute_filter_value';
            valueInput.style.margin = '10px';
            modal.appendChild(valueInput);

            let submitButton = document.createElement('button');
            $(submitButton).off('click').click(function ()
            {
                let xml = '<Call Method="SearchNodes">';
                xml += '<SearchString>"' + nameInput.value + '"="' + valueInput.value + '"</SearchString>';
                xml += '<SelectionMode>Select</SelectionMode>';
                xml += '</Call>';
                xml += '<Call Method="ModifySelection">';
                xml += '<SelectionModifier>Isolate</SelectionModifier>';
                xml += '</Call>';
                client.command.executeXML(xml);
                $(modal).remove();
            });
            submitButton.textContent = 'Filter and isolate';
            submitButton.style.margin = '10px';
            submitButton.style.display = 'block';
            modal.appendChild(submitButton);

            document.body.appendChild(modal);

        }
    },

    // Demo for exporting of temporary files 3D-PDF and SVG on server side.
    // Open / download exported files on client after successull export

    //!!You have to modify the full path on server side below to the work directory
    //check write access permitted for WebViewer server process

    {
        langId: 'Export',
        widgets: [
            { type: 1, langId: '3D-PDF', iconId: '1_pdf' },
            { type: 1, langId: 'SVG', iconId: '1_svg' },
            { type: 1, langId: 'Save', iconId: '4_speichern' }
        ],
        groupClick: function (context, id, group)
        {
            var client = context.client;
            var ribbon = context.ribbon;
            let clickedButton = group.getButtonById(id);
            let settings = '<Call Method="SetSettings"><Settings>'
            let xml = '';
            let filename = generateUUID();
            switch (clickedButton.langId)
            {
                case '3D-PDF':
                    //Set 3D PDF export settings as nedded
                    settings += '<Export3D><PDFPRC>';
                    settings += '<CompressTesselation>true</CompressTesselation><CompressBREP>true</CompressBREP>';
                    settings += '<BrepCompressionLevel>High</BrepCompressionLevel>false<RemoveAttributes>false</RemoveAttributes>';
                    settings += '<RemoveBREP>true</RemoveBREP><PMICrossHighlight>true</PMICrossHighlight>';
                    settings += '<PageHeight>595</PageHeight><PageWidth>842</PageWidth>';
                    settings += '<RenderMode>SolidOutline</RenderMode><Lighting>CADOptimized</Lighting>';
                    settings += '<ColorBackground>#FFD0D0D0</ColorBackground><UseTemplate>true</UseTemplate>';
                    settings += '<TemplateXML>ViewCarousel+PartList.xml</TemplateXML></PDFPRC></Export3D></Settings></Call>';
                    //Export call
                    xml = '<Call Method="Export3D">';
                    xml += '<ExportFormat3D>PDFPRC</ExportFormat3D>';
                    //Configure the full path on server side within WebViewer www directory to download file to client after export
                    xml += '<FileName>D:\\WebViewer\\www\\work\\' + filename + '.pdf</FileName>';
                    xml += '</Call>';
                    client.once('Export3D', function (response)
                    {
                        if (response.Error === 'SUCCESS')
                        {
                            window.open('work/' + filename + '.pdf', '_blank');
                        }
                    });
                    client.command.executeXML(settings);
                    client.command.executeXML(xml);
                    break;
                case 'SVG':
                    //Set SVG export settings as nedded
                    settings += '<Export2D><SVG><UseModelScale>false</UseModelScale></SVG></Export2D></Settings></Call>';
                    xml = '<Call Method="Export2D">';
                    xml += '<ExportFormat2D>SVG</ExportFormat2D>';
                    //Configure the full path on server side within WebViewer www directory to download file to client after export
                    xml += '<FileName>D:\\WebViewer\\www\\work\\' + filename + '.svg</FileName>';
                    xml += '</Call>';
                    client.once('Export2D', function (response)
                    {
                        if (response.Error === 'SUCCESS')
                        {
                            window.open('work/' + filename + '.svg', '_blank');
                        }
                    });
                    client.command.executeXML(settings);
                    client.command.executeXML(xml);
                    break;
                case 'Save':
                    let modal = document.createElement('div');
                    modal.style.position = 'absolute';
                    modal.style.top = '10%';
                    modal.style.left = '10%';
                    modal.style.zIndex = '10000';
                    modal.style.height = '300px';
                    modal.style.width = '300px';
                    modal.style.backgroundColor = 'white';
                    modal.style.border = '1px solid black';
                    modal.style.padding = '20px';

                    let nameTitle = document.createElement('div');
                    nameTitle.textContent = 'File name';
                    nameTitle.style.padding = '10px';
                    nameTitle.style.fontSize = '16px';
                    nameTitle.style.fontWeight = '600';
                    modal.appendChild(nameTitle);

                    let nameInput = document.createElement('input');
                    nameInput.id = 'Custom_attribute_filter_name';
                    nameInput.style.margin = '10px';
                    modal.appendChild(nameInput);

                    let submitButton = document.createElement('button');
                    $(submitButton).off('click').click(function ()
                    {
                        var xml = '<Call Method="SaveAs"><FileName>';
                        xml += 'D:\\Development\\ViewStation\\Products\\ViewStationWeb\\models\\3DVS_Save_Test\\' + nameInput.value + '.3dvs';
                        xml += '</FileName></Call>';
                        client.command.executeXML(xml);
                        $(modal).remove();
                    });
                    submitButton.textContent = 'Save';
                    submitButton.style.margin = '10px';
                    submitButton.style.display = 'block';
                    modal.appendChild(submitButton);

                    document.body.appendChild(modal);
                    break;
            }
        }
    }]
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