/*  Widget Types:
        Button = 1
        ButtonLet = 2
        Dropdown = 3
        Input = 4
*/
var UserConfiguration = {
    langId: 'Custom Tab',
    groups: [{
    // Demo for exporting of temporary files to STEP on server side.

    //!!You have to modify the full path to the work directory on server side below 
    //check write access permitted for WebViewer server process

    
		langId: 'Export',
		widgets: [
			{ type: 1, langId: 'STEP', iconId: '4_speichern' }
		],
		groupClick: function (context, id, group)
		{
			var client = context.client;
			var ribbon = context.ribbon;
			let clickedButton = group.getButtonById(id);
			let settings = '<Call Method="SetSettings"><Settings>'
			let xml = '';
            let filepath = client.lastFileOpened;
			let filename = filepath.split('\\')[filepath.split('\\').length - 1];
						
			//Set STEP export settings as needed
			settings += '<Export3D><General>';
			settings += '<WriteMode>BrepAndTesselation</WriteMode>';
			settings += '<ExportHiddenObjects>true</ExportHiddenObjects></General>';					
			settings += '<STEP><SaveAnalyticsToNurbs>true</SaveAnalyticsToNurbs>';
			settings += '<SaveFacetedToWireframe>false</SaveFacetedToWireframe>';
			settings += '<UseShortNames>false</UseShortNames>';
			settings += '<Format>AP214</Format>';                    
			settings += '</STEP></Export3D></Settings></Call>';
			//Export call
			xml = '<Call Method="Export3D">';
			xml += '<ExportFormat3D>STEP</ExportFormat3D>';
			//Configure the full path on server side within WebViewer www directory to download file to client after export
			xml += '<FileName>C:\\Full\\path\\with\\double\\slashes\\' + filename + '.stp</FileName>';
			xml += '</Call>';
			client.command.executeXML(settings);
			client.command.executeXML(xml);
		}
	}]
};