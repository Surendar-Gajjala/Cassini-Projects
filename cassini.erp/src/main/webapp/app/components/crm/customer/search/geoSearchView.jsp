<div>
    <div class="blue-gradient" style="border: 1px solid rgb(238, 238, 238);margin-bottom: 5px;padding: 10px">
        <div class="row form-group" style="margin-left: auto; margin-right: auto; margin-bottom: 0">
            <div class="col-sm-4">
                <div class="rdio rdio-success">
                    <input name="radio" id="radioBox" value="1" checked="checked" type="radio" ng-click="setMode('box')">
                    <label for="radioBox" style="color: #FFF">Box Search</label>
                </div>
                <div class="rdio rdio-success">
                    <input name="radio" id="radioRadius" value="2" type="radio" ng-click="setMode('radius')">
                    <label for="radioRadius" style="color: #FFF">Radius Search</label>
                </div>
            </div>
            <div class="col-sm-8" style="padding-top: 8px;" ng-if="mode == 'radius'">
                <div class="row">
                    <div class="col-sm-10" style="text-align: right;height: 100%;padding-top: 8px; font-size: 16px">
                        <label class="control-label mr5" style="color: #FFF">Click on the map and enter radius (in km):</label>
                    </div>
                    <div class="col-sm-2"><input class="form-control" type="number" ng-model="circle.radius" min="0"/></div>
                </div>
            </div>
        </div>
    </div>
    <div>
        <ui-gmap-google-map
                center="map.center"
                zoom="map.zoom"
                dragging="map.dragging"
                bounds="map.bounds"
                events="map.events"
                options="map.options"
                pan="true"
                control="map.control">

            <ui-gmap-circle center="clickMarker" stroke="circle.stroke" fill="circle.fill" radius="circle.radiusInMeters"
                            visible="circle.visible" geodesic="true" editable="true" draggable="false" clickable="false"
                            control="circle.control" events="circleEvents">
            </ui-gmap-circle>

            <ui-gmap-markers models="map.markers" coords="'self'" click="markerClicked"
                             doCluster="map.clusterMarkers" clusterOptions="map.clusterOptions">
            </ui-gmap-markers>

        </ui-gmap-google-map>
    </div>
</div>