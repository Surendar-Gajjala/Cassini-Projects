let _PanePresets = {
    "wv-measurement":
        [
            {
                "name": "Red Text [Default]",
                "isDefault": true,
                "settings": "<Settings><Scene><Tools><Dimensioning><Markup><Text><Color>#f20f07</Color></Text></Markup></Dimensioning></Tools></Scene></Settings>"
            },
            {
                "name": "Blue Text",
                "isDefault": false,
                "settings": "<Settings><Scene><Tools><Dimensioning><Markup><Text><Color>#1322c6</Color></Text></Markup></Dimensioning></Tools></Scene></Settings>"
            },
            {
                "name": "Green Text",
                "isDefault": false,
                "settings": "<Settings><Scene><Tools><Dimensioning><Markup><Text><Color>#04ce1f</Color></Text></Markup></Dimensioning></Tools></Scene></Settings>"
            }
        ]
        ,
    "wv-markup":
        [
            {
                "name": "Box [Default]",
                "isDefault": true,
                "settings": "<Settings><Scene><Tools><Markup><Text><UseAttribute>true</UseAttribute><Text>Note</Text><FontName>Verdana</FontName><FontSize>12</FontSize><Color>#FF000000</Color></Text><Display><Zoomable>false</Zoomable><ScreenAligned>true</ScreenAligned><AlwaysOnTop>true</AlwaysOnTop><Alignment>None</Alignment><FrameVisible>true</FrameVisible><FrameColor>#00950000</FrameColor><FrameType>Rectangle</FrameType><BackgroundVisible>true</BackgroundVisible><BackgroundColor>#FFFFFFFF</BackgroundColor></Display><Anchor><Color>#00950000</Color><ConnectionType>Direct</ConnectionType><EndType>Arrow</EndType><Knockout>true</Knockout></Anchor></Markup></Tools></Scene></Settings>"
            },
            {
                "name": "Black Text only",
                "isDefault": false,
                "settings": "<Settings><Scene><Tools><Markup><Text><UseAttribute>true</UseAttribute><Text>Note</Text><FontName>Verdana</FontName><FontSize>14</FontSize><Color>#FF000000</Color></Text><Display><Zoomable>false</Zoomable><ScreenAligned>true</ScreenAligned><AlwaysOnTop>true</AlwaysOnTop><Alignment>None</Alignment><FrameVisible>false</FrameVisible><FrameColor>#00000000</FrameColor><FrameType>Rectangle</FrameType><BackgroundVisible>false</BackgroundVisible><BackgroundColor>#FFFFFFFF</BackgroundColor></Display><Anchor><Color>#00000000</Color><ConnectionType>Direct</ConnectionType><EndType>Sphere</EndType><Knockout>true</Knockout></Anchor></Markup></Tools></Scene></Settings>"
            }
        ]
        ,
    "wv-callout":
        [
            {
                "name": "Circle Black [Default]",
                "isDefault": true,
                "settings": "<Settings><Scene><Tools><Callout><AutoIncrementId>true</AutoIncrementId><MultiAnchorGroups>false</MultiAnchorGroups><SelectAssociatedNodes>false</SelectAssociatedNodes><DistanceBetweenMarkups>0.2</DistanceBetweenMarkups><PixelDistanceToBorder>10</PixelDistanceToBorder><FactorCircleRadiusAroundGeometry>1.25</FactorCircleRadiusAroundGeometry><AllowOverlappingOfLines>true</AllowOverlappingOfLines><UseGlobalFixedCircle>true</UseGlobalFixedCircle><Alignment>NearToSolid</Alignment></Callout><Markup><Text><UseAttribute>true</UseAttribute><Text>Note</Text><FontName>Verdana</FontName><FontSize>14</FontSize><Color>#FF000000</Color></Text><Display><Zoomable>false</Zoomable><ScreenAligned>true</ScreenAligned><AlwaysOnTop>true</AlwaysOnTop><FrameVisible>true</FrameVisible><FrameColor>#00000000</FrameColor><FrameType>Circle</FrameType><BackgroundVisible>true</BackgroundVisible><BackgroundColor>#FFFFFFFF</BackgroundColor></Display><Anchor><Color>#00000000</Color><ConnectionType>Direct</ConnectionType><EndType>Sphere</EndType><Knockout>true</Knockout></Anchor></Markup></Tools></Scene></Settings>"
            },
            {
                "name": "Number only",
                "isDefault": false,
                "settings": "<Settings><Scene><Tools><Callout><AutoIncrementId>true</AutoIncrementId><MultiAnchorGroups>false</MultiAnchorGroups><SelectAssociatedNodes>false</SelectAssociatedNodes><DistanceBetweenMarkups>0.2</DistanceBetweenMarkups><PixelDistanceToBorder>10</PixelDistanceToBorder><FactorCircleRadiusAroundGeometry>1.25</FactorCircleRadiusAroundGeometry><AllowOverlappingOfLines>true</AllowOverlappingOfLines><UseGlobalFixedCircle>true</UseGlobalFixedCircle><Alignment>NearToSolid</Alignment></Callout><Markup><Text><UseAttribute>true</UseAttribute><Text>Note</Text><FontName>Verdana</FontName><FontSize>14</FontSize><Color>#FF000000</Color></Text><Display><Zoomable>false</Zoomable><ScreenAligned>true</ScreenAligned><AlwaysOnTop>true</AlwaysOnTop><FrameVisible>false</FrameVisible><FrameColor>#00000000</FrameColor><FrameType>Circle</FrameType><BackgroundVisible>false</BackgroundVisible><BackgroundColor>#FFFFFFFF</BackgroundColor></Display><Anchor><Color>#00000000</Color><ConnectionType>Direct</ConnectionType><EndType>Sphere</EndType><Knockout>true</Knockout></Anchor></Markup></Tools></Scene></Settings>"
            },
            {
                "name": "Rectangle Curve Blue",
                "isDefault": false,
                "settings": "<Settings><Scene><Tools><Callout><AutoIncrementId>true</AutoIncrementId><MultiAnchorGroups>false</MultiAnchorGroups><SelectAssociatedNodes>false</SelectAssociatedNodes><DistanceBetweenMarkups>0.2</DistanceBetweenMarkups><PixelDistanceToBorder>10</PixelDistanceToBorder><FactorCircleRadiusAroundGeometry>1.25</FactorCircleRadiusAroundGeometry><AllowOverlappingOfLines>true</AllowOverlappingOfLines><UseGlobalFixedCircle>true</UseGlobalFixedCircle><Alignment>NearToSolid</Alignment></Callout><Markup><Text><UseAttribute>true</UseAttribute><Text>Note</Text><FontName>Verdana</FontName><FontSize>14</FontSize><Color>#00000080</Color></Text><Display><Zoomable>false</Zoomable><ScreenAligned>true</ScreenAligned><AlwaysOnTop>true</AlwaysOnTop><FrameVisible>true</FrameVisible><FrameColor>#00000080</FrameColor><FrameType>Rectangle</FrameType><BackgroundVisible>false</BackgroundVisible><BackgroundColor>#FFFFFFFF</BackgroundColor></Display><Anchor><Color>#00000080</Color><ConnectionType>Curve</ConnectionType><EndType>Cross</EndType><Knockout>true</Knockout></Anchor></Markup></Tools></Scene></Settings>"
            },
            {
                "name": "Underline",
                "isDefault": false,
                "settings": "<Settings><Scene><Tools><Callout><AutoIncrementId>true</AutoIncrementId><MultiAnchorGroups>false</MultiAnchorGroups><SelectAssociatedNodes>false</SelectAssociatedNodes><DistanceBetweenMarkups>0.2</DistanceBetweenMarkups><PixelDistanceToBorder>10</PixelDistanceToBorder><FactorCircleRadiusAroundGeometry>1.25</FactorCircleRadiusAroundGeometry><AllowOverlappingOfLines>true</AllowOverlappingOfLines><UseGlobalFixedCircle>true</UseGlobalFixedCircle><Alignment>NearToSolid</Alignment></Callout><Markup><Text><UseAttribute>true</UseAttribute><Text>Note</Text><FontName>Verdana</FontName><FontSize>14</FontSize><Color>#FF000000</Color></Text><Display><Zoomable>false</Zoomable><ScreenAligned>true</ScreenAligned><AlwaysOnTop>true</AlwaysOnTop><FrameVisible>true</FrameVisible><FrameColor>#00000000</FrameColor><FrameType>Underline</FrameType><BackgroundVisible>false</BackgroundVisible><BackgroundColor>#FFFFFFFF</BackgroundColor></Display><Anchor><Color>#00000000</Color><ConnectionType>Rectangle</ConnectionType><EndType>Sphere</EndType><Knockout>false</Knockout></Anchor></Markup></Tools></Scene></Settings>"
            }
        ]
};