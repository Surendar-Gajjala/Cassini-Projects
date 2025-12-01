/*Define custom buttons for the mobile UI ribbon
Highest elements are displayed on the first level and need no icon ID
Use the lang.loc file in the custom/ directory to translate the langIds
Include svg icons in the custom/ directory to include new svgs or use existing svg (iconId = file name)
Run the merge-custom-svg.js before deploying to the server to include custom icons

Structure information:

iconId: Used to map to the svg file, not defined for first level buttons
langId: Used to map to the translation
id: Internal usage
children(optional): Children are displayed in a new row when the button is clicked
method(optional): A method can be defined that will be executed on button click. The client parameter represents the VSWebClient and a state object represents the button state (e.g. state.active).
interactionClass(optional): An interaction class for this button. Interaction classes are used to group button behaviour.
interaction(optional): A special instance of a interaction class.
paneId(optional): A pane that should be opened on button click. The id isn't of the type 'wv-' but instead the real template name is used.

Examples for interactionClass/interaction:

There are 4 pre defined interaction classes on which a button can be registered.

        Interaction: The buttons defined in this class will be highlighted accordingly to the interaction statemachine and the panes and pane badges for the interaction are managed automatically.
        Select: The buttons defined in this class will be highlighted accordingly to selection mode currently used. The selection mode is automatically managed.
        LMB: The buttons defined in this class will be highlighted accordingly to the current Left Mouse Button mode. The left mouse button mode is automatically managed.
        RenderMode: The buttons defined in this class will be highlighted accordingly to the current render modes. The render mode handling is managed automatically.

Each interaction represents an instance of the higher interaction class, giving some examples:
        Interaction: Flythrough, Measurement_PointCoordinate... The interaction is toggled on a button click.
        Select: Select, Ghost... The selection mode is set on button click.
        LMB: RectZoom, Rotate... The left mouse button function is set on button click.
        RenderMode: Illustration, Wires... The render mode is toggled on button click.

PaneId examples:

VisibilityTestPane, QuickComparePane, ExactComparePane, ProjectedAreaPane, WallThicknessPane, MirrorPane, FilterPane,
DistortionPane, NeighbourhoodSearchPane, TextSearchPane, NeutralAxisPane

The interaction panes are not listed because it is advised to use the automatic management via interactionClass/interaction to avoid potential failures.


*/

var _CustomRibbon = [
    {
        iconId: '',
        langId: 'customInteraction',
        id: 'rCustomLevel1',
        children: [
            {
                iconId: '1_entformungsschraegen_analyse',
                    langId: 'ID_RIBBON_DRAFT_ANGLE',
                    id: 'rDraftAngle',
                    interactionClass: 'Interaction',
                    interaction: 'DraftAngle',
                    method: function (client)
                    {

                }
            },
            {
                iconId: '3_pan',
                langId: 'ID_RIBBON_PAN',
                id: 'rPan',
                interactionClass: 'LMB',
                interaction: 'Pan',
                method: function (client)
                {
                }
            },
            {
                iconId: '2_drahtmodell',
                langId: 'ID_RIBBON_WIREFRAME',
                id: 'rWireFrame',
                interactionClass: 'RenderMode',
                interaction: 'Wires',
                method: function (client)
                {
                }
            },
            {
                iconId: '5_ghost',
                langId: 'ID_RIBBON_GHOST',
                id: 'rGhostMode',
                interactionClass: 'Select',
                interaction: 'Ghost',
                method: function (client)
                {
                }
            }
        ]
    }, {
        iconId: '',
        langId: 'customMisc',
        id: 'rCustomLevel2',
        children: [
            {
                iconId: 'distortion',
                    langId: 'ID_RIBBON_DISTORT',
                    id: 'rDistortion',
                    paneId: 'DistortionPane',
                    method: function (client)
                    {

                    }
            },
            {
                iconId: '1_raster_darstellen',
                langId: 'ID_RIBBON_SHOW_GRID',
                id: 'rGrid',
                method: function (client, state)
                {
                    state.active = !state.active;
                    let xml = '<Settings><Scene><Visualization><ShowGrid>' + state.active + '</ShowGrid></Visualization></Scene></Settings>';
                    client.command.executeXML('<Call Method="SetSceneSettings">' + xml + '</Call>');
                }
            }
        ]
    }
]