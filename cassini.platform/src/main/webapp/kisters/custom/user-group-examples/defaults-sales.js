
//Native loading settings

//Show native loading modal
var _NativeLoadingModal = false;

//Hide the switch to decide between 2D or 3D prefered loading
var _Hide2DSwitch = false;


//Slider step setting presets
var _IncludeHiddenNativeLoading = '<Settings><Import3D><General><TessellationLOD>ExtraHigh</TessellationLOD><ReadMode>PreferGeometry</ReadMode></General><Filter>'
        + '<ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>true</ReadWireframes><ReadPMI>true</ReadPMI>'
        + '<ReadAttributes>true</ReadAttributes><ReadHiddenObjects>true</ReadHiddenObjects><ReadViews>true</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>false</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>true</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><PDF><ForceRead3D>false</ForceRead3D></PDF></Import3D></Settings>';

var _CompleteNativeLoading = '<Settings><Import3D><General><ReadMode>PreferGeometry</ReadMode><TessellationLOD>High</TessellationLOD></General><Filter>'
        + '<ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>true</ReadWireframes><ReadPMI>true</ReadPMI>'
        + '<ReadAttributes>true</ReadAttributes><ReadHiddenObjects>false</ReadHiddenObjects><ReadViews>true</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>false</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>false</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><PDF><ForceRead3D>false</ForceRead3D></PDF></Import3D></Settings>';

var _ModerateNativeLoading = '<Settings><Import3D><General><ReadMode>PreferTessellation</ReadMode><TessellationLOD>Medium</TessellationLOD></General>'
        + '<Filter><ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>true</ReadWireframes><ReadPMI>true</ReadPMI>'
        + '<ReadAttributes>true</ReadAttributes><ReadHiddenObjects>false</ReadHiddenObjects><ReadViews>true</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>false</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>false</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><PDF><ForceRead3D>true</ForceRead3D></PDF></Import3D></Settings>';

var _FastNativeLoading = '<Settings><Import3D><General><ReadMode>PreferTessellation</ReadMode><TessellationLOD>Low</TessellationLOD></General>'
        + '<Filter><ReadSolids>true</ReadSolids><ReadSurfaces>true</ReadSurfaces><ReadWireframes>false</ReadWireframes><ReadPMI>false</ReadPMI>'
        + '<ReadAttributes>false</ReadAttributes><ReadHiddenObjects>false</ReadHiddenObjects><ReadViews>false</ReadViews><Extended>'
        + '<LoadDefaultConfiguration>true</LoadDefaultConfiguration><ReadStructureOnly>false</ReadStructureOnly></Extended></Filter><Miscellaneous>'
        + '<WireGeneration><DeleteOldWiresOnLoad>false</DeleteOldWiresOnLoad><GenerateWiresOnLoad>false</GenerateWiresOnLoad></WireGeneration>'
        + '</Miscellaneous><PDF><ForceRead3D>true</ForceRead3D></PDF></Import3D></Settings>';
/*----------------------------------------------------------------------------------------------------------------------------------------------*/