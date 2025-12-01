define([
        'app/desktop/modules/home/home.module',
        'split-pane',
        'app/assets/bower_components/cassini-platform/app/desktop/directives/commonDirectives',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/tripService'
    ],
    function (module) {
        module.controller('HomeController', HomeController);

        function HomeController($scope, $rootScope, $sce, $state, $timeout, $cookies, $interval,
                                uiGmapGoogleMapApi, uiGmapIsReady, TripService) {
            $rootScope.viewInfo.icon = "fa fa-home";
            $rootScope.viewInfo.title = "Home";

            var vm = this;

            $scope.map = {
                center: {
                    latitude: 17.436653333333332,
                    longitude: 78.513435
                },
                zoom: 8
            };

            vm.slider = {
                minValue: 0,
                options: {
                    floor: 0,
                    ceil: 100,
                    showTicksValues: 1,
                    onChange: sliderValueChanged
                }
            };
            vm.trips = [];
            vm.selectedTrip = null;

            vm.showTripDetails = showTripDetails;
            vm.playVideos = playVideos;
            vm.stopVideos = stopVideos;
            vm.playSpeed = playSpeed;
            vm.showFullscreen = showFullscreen;
            vm.playForward = playForward;
            vm.playBackward = playBackward;

            vm.playing = false;
            vm.playingForward = null;
            vm.currentSpeed = 0;
            vm.toggleDocumentMarkers = toggleDocumentMarkers;
            vm.showDocuments = false;

            $rootScope.showTrips = showTrips;
            $rootScope.tripsVisible = false;

            var timerPromise = null;
            var allVideosFullScreen = false;
            var singleVideoFullScreen = false;
            var fullScreenCamera = 0;
            var playbackRate = 1.0;
            var intervalRewind;
            var googleMap = null;
            var mapsApi = null;
            var routeCoordinates = [];
            var mapDistanceLocations = new Hashtable();
            var trainMarker = null;
            var trainroute = null;
            var gpsPoints = [];
            var trainSpeeds = [];
            var infoWindow = null;
            var docsWindow = null;
            var docMarkers = [];
            var prevDocMarker = null;
            var sections = [];
            var mapLocationToSection = new Hashtable();
            var prevSection = null;
            var docMarkerInfoWindowsMap = new Hashtable();
            var sectionStations = new Hashtable();

            var section = {
                from: null,
                to: null,
                route: null
            };


            function showFullscreen() {
                if(allVideosFullScreen || singleVideoFullScreen) {
                    toggleFullscreen();
                }
                else {
                    var vidPanel = $('#videoPanel');
                    vidPanel.addClass('full-screen-video');
                    allVideosFullScreen = true;

                    $('#controls-toolbar').addClass('full-screen-toolbar');
                    $('#distance-bar').addClass('full-screen-distance-bar');
                    $('#footer').hide();

                }
            }


            function playSpeed(speed) {
                playbackRate = speed;
                for(var i=1; i<=4; i++) {
                    var cam = document.getElementById("camera" + i);
                    cam.playbackRate = playbackRate;
                }

                playVideos();
            }


            function sliderValueChanged(sliderId, modelValue, highValue, pointerType) {
                var percent = modelValue/vm.slider.options.ceil;

                for(var i=1; i<=4; i++) {
                    var cam = document.getElementById("camera" + i);
                    cam.currentTime = cam.duration * percent;
                }

                panToTrainLocation();
            }


            function showTrips() {
                if($rootScope.tripsVisible == false) {
                    var pos = $("#viewContent").position();
                    var height = $("#viewContent").height();
                    $('#tripsList').height(height + 20);
                    $('#tripsList').css({top: pos.top});
                    $('#tripsList').show();

                    $rootScope.tripsVisible = true;
                }
                else {
                    $('#tripsList').hide();
                    $rootScope.tripsVisible = false;
                }
            }


            function playVideos() {
                if(vm.playingForward == null) {
                    vm.playingForward = true;
                }
                for(var i=1; i<=4; i++) {
                    var cam = document.getElementById("camera" + i);

                    if(!vm.playingForward) {
                        cam.playbackRate = -cam.playbackRate;
                    }
                    else {
                        cam.play();
                    }
                }

                vm.playing = true;
            }

            function stopVideos() {
                for(var i=1; i<=4; i++) {
                    var cam = document.getElementById("camera" + i);
                    cam.pause();
                }

                vm.playing = false;
            }

            function playForward() {
                vm.playingForward = true;
                playVideos();
            }

            function playBackward() {
                vm.playingForward = false;
                playVideos();
                //rewind();
            }

            function loadTrips() {
                TripService.getTrips().then(
                    function(data) {
                        vm.trips = data;

                        if(vm.trips.length > 0) {
                            vm.selectedTrip = vm.trips[0];
                            loadRouteCoordinates(vm.selectedTrip.id);

                            vm.slider.maxValue = vm.selectedTrip.distance;
                            vm.slider.options.ceil = vm.selectedTrip.distance;
                        }
                    }
                )
            }

            function showTripDetails(trip) {
                vm.selectedTrip.active = false;
                vm.selectedTrip = trip;
                vm.selectedTrip.active = true;

                vm.slider.minValue = 0;
                vm.slider.maxValue = vm.selectedTrip.distance;
                vm.slider.options.ceil = vm.selectedTrip.distance;

                $('.videoSrc').attr('src', trip.videoUrl);
                $('video').load();

                $('#tripsList').hide();
                $rootScope.tripsVisible = false;

                $interval.cancel(timerPromise);
                initTimer();

                loadRouteCoordinates(vm.selectedTrip.id);
            }


            function adjustPanelHeight() {
                var height = $("#viewContent").height();
                $("#videoPanel").height(height-160);
                $("#mapPanel").height(height-160);
                $(".angular-google-map-container").height(height-170);


                if(googleMap != null) {
                    var bounds = new google.maps.LatLngBounds();
                    angular.forEach(routeCoordinates, function (coord) {
                        bounds.extend(coord);
                    });

                    googleMap.map.fitBounds(bounds);
                }
            }


            function positionTripsContainer() {
                var height = $("#viewContent").height();
                var pos = $("#viewContent").position();

                var cont = $("#tripsList");
                cont.css({top: pos.top, left: pos.left});
                cont.height(height+20);
            }

            function panToTrainLocation() {
                var cam = document.getElementById('camera1');
                var currentTime = cam.currentTime;
                var duration = cam.duration;
                var factor = currentTime/duration;
                vm.slider.minValue = vm.slider.options.ceil*factor;

                var index = Math.ceil(routeCoordinates.length*factor)-1;
                if(index < 0) {
                    index = 0;
                }

                var latlng = null;

                if(index <= routeCoordinates.length) {
                    latlng = routeCoordinates[index];
                    googleMap.map.panTo(latlng);
                }
            }

            function showNextStationMarker() {
                var currentTrainPosition = trainMarker.getPosition();
                if(currentTrainPosition != null && currentTrainPosition != undefined) {
                    var trainIndex = getLocationIndex(currentTrainPosition);
                    var docMarker = getNextDocumentMarker(trainIndex);
                    if(docMarker != null) {
                        var docMarkerIndex = getLocationIndex(docMarker.getPosition());

                        var locations = [];
                        for(var i = trainIndex; i<docMarkerIndex; i++) {
                            locations.push(routeCoordinates[i]);
                        }

                        var km = (google.maps.geometry.spherical.computeLength(locations)/1000);
                        km  = Math.round(km);
                        if(km <= 2) {
                            docMarker.setVisible(true);
                            if(prevDocMarker != null && docMarker != prevDocMarker) {
                                prevDocMarker.setVisible(false);

                            }
                            prevDocMarker = docMarker;
                        }
                    }
                }
            }

            function getLocationIndex(latlng) {
                var index = -1;
                for(var i=0; i<routeCoordinates.length; i++) {
                    var loc = routeCoordinates[i];
                    if(loc.lat() == latlng.lat() && loc.lng() == latlng.lng()) {
                        index = i;
                        break;
                    }
                }

                return index;
            }

            function getNextDocumentMarker(trainIndex) {
                var docMarker = null;
                for(var i=0; i<docMarkers.length; i++) {
                    var marker = docMarkers[i];
                    var docIndex = getLocationIndex(marker.getPosition());
                    if(docIndex > trainIndex) {
                        docMarker = marker;
                        break;
                    }
                }

                return docMarker;
            }

            function initTimer() {
                timerPromise = $interval(function() {
                    var camera = fullScreenCamera != 0 ? fullScreenCamera : 1;

                    if(camera != null) {
                        var cam = document.getElementById('camera' + camera);
                        var currentTime = cam.currentTime;
                        var duration = cam.duration;
                        var factor = currentTime/duration;
                        vm.slider.minValue = vm.slider.options.ceil*factor;

                        var index = Math.ceil(routeCoordinates.length*factor)-1;
                        if(index < 0) {
                            index = 0;
                        }

                        if(trainMarker != null) {
                            var latlng = null;

                            if(index <= routeCoordinates.length) {
                                latlng = routeCoordinates[index];
                                trainMarker.setPosition(latlng);
                                if(vm.playing) {
                                    googleMap.map.panTo(latlng);
                                }
                            }
                            else {
                                latlng = routeCoordinates[routeCoordinates.length-1];
                                trainMarker.setPosition();
                                if(vm.playing) {
                                    googleMap.map.panTo(latlng);
                                }
                            }

                            if(infoWindow != null) {
                                var speed = null;
                                if(index != 0 && trainSpeeds[index] != undefined) {
                                    speed = trainSpeeds[index];
                                }
                                else {
                                    speed = 0;
                                }

                                vm.currentSpeed = speed;

                                var msg = "Speed: {0} Kmph".format(speed);
                                infoWindow.setContent(msg);
                            }

                            showNextStationMarker();
                            showCurrentTrainSection();
                        }
                    }

                }, 500);
            }

            function initEscapeKeyEvent() {
                $(document).on('keyup',function(evt) {
                    if (evt.keyCode == 27) {
                        toggleFullscreen();

                        $('#tripsList').hide();
                        $rootScope.tripsVisible = false;
                    }
                });
            }

            function toggleFullscreen() {
                if(!singleVideoFullScreen) {
                    var vidPanel = $('#videoPanel');
                    vidPanel.removeClass('full-screen-video');
                    $('#controls-toolbar').removeClass('full-screen-toolbar');
                    $('#distance-bar').removeClass('full-screen-distance-bar');
                    $('#footer').show();
                    allVideosFullScreen = false;
                }
                else {
                    $('video').removeClass('full-screen-video');
                    $('#camera-label' + fullScreenCamera).removeClass('full-screen-camera-label');
                    if(!allVideosFullScreen) {
                        $('#controls-toolbar').removeClass('full-screen-toolbar');
                        $('#distance-bar').removeClass('full-screen-distance-bar');
                        $('#footer').show();
                    }

                    toggleOtherVideos(false);

                    fullScreenCamera = 0;
                    singleVideoFullScreen = false;
                }
            }

            function initDoubleClickEvent() {
                $('video').on('dblclick', function() {
                    $( this ).addClass('full-screen-video');

                    var id = $(this).attr('id');
                    id = id.charAt(id.length - 1);
                    fullScreenCamera = id;
                    $('#camera-label' + fullScreenCamera).addClass('full-screen-camera-label');
                    $('#controls-toolbar').addClass('full-screen-toolbar');
                    $('#distance-bar').addClass('full-screen-distance-bar');
                    $('#footer').hide();

                    singleVideoFullScreen = true;

                    toggleOtherVideos(true)
                });
            }

            function toggleOtherVideos(flag) {
                var currentCam = document.getElementById('camera' + fullScreenCamera);
                var currentTime = currentCam.currentTime;

                if(flag && fullScreenCamera != 0) {
                    for(var i=1; i<=4; i++) {
                        if(i != fullScreenCamera) {
                            var cam = document.getElementById('camera' + i);
                            if(vm.playing) {
                                cam.pause();
                            }
                        }
                    }
                }
                else {
                    for(var i=1; i<=4; i++) {
                        if(i != fullScreenCamera) {
                            var cam = document.getElementById('camera' + i);
                            if(vm.playing) {
                                cam.currentTime = currentTime;
                                cam.play();
                            }
                        }
                    }
                }

            }

            function initSpacebarKeyEvent() {
                $(document).on('keyup',function(evt) {
                    if (evt.keyCode == 32 && (allVideosFullScreen || singleVideoFullScreen)) {
                        if(vm.playing) {
                            stopVideos();
                        }
                        else {
                            playVideos();
                        }
                    }
                });
            }


            function rewind() {
                $interval.cancel(intervalRewind);
                var startSystemTime = new Date().getTime();
                var cam = document.getElementById('camera1');
                var startVideoTime = cam.currentTime;
                cam.pause();

                intervalRewind = $interval(function () {
                    for(var i=1; i<=4; i++) {
                        var cam = document.getElementById("camera" + i);

                        if (cam.currentTime == 0) {
                            $interval.cancel(intervalRewind);
                            cam.pause();
                        } else {
                            var elapsed = new Date().getTime() - startSystemTime;
                            cam.currentTime = Math.max(startVideoTime - elapsed * cam.playbackRate / 1000.0, 0);
                        }
                    }
                }, 10);
            }


            function loadRouteCoordinates(tripId) {
                TripService.loadJsonFile('app/assets/videos/trip1.gps').then(
                    function(data) {
                        gpsPoints = data;

                        angular.forEach(gpsPoints, function(point) {
                            var glatlng = new google.maps.LatLng(point.latitude, point.longitude);
                            routeCoordinates.push(glatlng);
                        });

                        console.log("# of points: " + gpsPoints.length);

                        addDocumentMarkers();
                        initTimer();
                    }
                )
            }


            function closestPointOnRoute(latlng) {
                var pnt     = latlng;
                var distArr = [];
                var dist    = google.maps.geometry.spherical.computeDistanceBetween;


                for (var index in routeCoordinates) {
                    distArr.push([routeCoordinates[index], dist(pnt, routeCoordinates[index])]);
                }

                if(distArr.length > 0) {
                    return distArr.sort(function (a, b) {
                        return a[1] - b[1];
                    })[0][0];
                }
                else {
                    return null;
                }
            }

            function showVideoAtMapLocation(latlng) {
                var index = -1;
                for(var i=0; i<routeCoordinates.length; i++) {
                    var ll = routeCoordinates[i];
                    if(ll.lat() == latlng.lat() &&
                        ll.lng() == latlng.lng()) {
                        index = i;
                    }
                }

                if(index != -1) {
                    var percent = index/routeCoordinates.length;

                    for(var i=1; i<=4; i++) {
                        var cam = document.getElementById("camera" + i);
                        cam.currentTime = cam.duration * percent;
                    }
                }
            }

            function addDocumentMarkers() {
                TripService.getDocuments().then(
                    function(docs) {
                        angular.forEach(docs, function(doc) {
                            var latlng = new google.maps.LatLng(doc.latitude, doc.longitude);
                            var closest = closestPointOnRoute(latlng);
                            var docMarker = new google.maps.Marker({
                                icon:"app/assets/images/blue_circle_dot.png",
                                position: closest,
                                map: googleMap.map
                            });
                            var markerImage = new google.maps.MarkerImage(
                                "app/assets/images/blue_circle_dot.png",
                                null, /* size is determined at runtime */
                                null, /* origin is 0,0 */
                                null, /* anchor is bottom center of the scaled image */
                                new google.maps.Size(12, 12));
                            docMarker.setIcon(markerImage);
                            docMarker.setVisible(false);
                            docMarker.addListener('click', function() {
                                showDocuments(doc, docMarker);
                            });
                            docMarkers.push(docMarker);

                            var docInfoWindow = new google.maps.InfoWindow({
                                content: doc.name
                            });
                            docMarkerInfoWindowsMap.put(docMarker, docInfoWindow);
                        });

                        addSections();
                        $timeout(function() {
                            $(window).trigger('resize');
                        });
                    }
                );
            }

            function showCurrentTrainSection() {
                var trainLoc = trainMarker.getPosition();
                if(trainLoc != null && trainLoc != undefined) {
                    var index = getLocationIndex(trainLoc);
                    var loc = routeCoordinates[index];
                    var section = mapLocationToSection.get(loc);
                    if(section != null) {
                        section.setVisible(true);

                        if(prevSection != null && prevSection != section) {
                            prevSection.setVisible(false);
                            var stations = sectionStations.get(prevSection);
                            if(stations != null) {
                                var prevStation = stations[0];
                                var nextStation = stations[1];
                                prevStation.setVisible(false);
                                nextStation.setVisible(false);
                            }
                        }

                        var stations = sectionStations.get(section);
                        if(stations != null) {
                            var prevStation = stations[0];
                            var nextStation = stations[1];
                            prevStation.setVisible(true);
                            nextStation.setVisible(true);

                            /*
                            var prevStationInfoWindow = docMarkerInfoWindowsMap.get(prevStation);
                            var nextStationInfoWindow = docMarkerInfoWindowsMap.get(nextStation);

                            if(prevStationInfoWindow != null &&
                                nextStationInfoWindow != null) {
                                prevStationInfoWindow.open(googleMap.map, prevStation);
                                nextStationInfoWindow.open(googleMap.map, nextStation);
                            }
                            */
                        }
                        prevSection = section;
                    }
                }
            }

            function addSections() {
                var prevMarker = null;
                for(var i = 0; i<docMarkers.length; i++) {
                    var currentMarker = docMarkers[i];

                    if(prevMarker != null) {
                        var prevLatLng = prevMarker.getPosition();
                        var curLatLng = currentMarker.getPosition();

                        var prevIndex = getLocationIndex(prevLatLng);
                        var curIndex = getLocationIndex(curLatLng);

                        var locations = getLocationsFromAndTo(prevIndex, curIndex);
                        var section = new google.maps.Polyline({
                            path:locations,
                            strokeColor:"green",
                            strokeOpacity:1,
                            strokeWeight:6,
                            zIndex: 88888
                        });

                        google.maps.event.addListener(section, 'click', function(e) {
                            var closest = closestPointOnRoute(e.latLng);
                            showVideoAtMapLocation(closest);
                        });

                        section.setVisible(false);
                        section.setMap(googleMap.map);
                        sections.push(section);


                        angular.forEach(locations, function(loc) {
                            mapLocationToSection.put(loc, section);
                        });

                        sectionStations.put(section, [prevMarker, currentMarker]);
                    }

                    prevMarker = currentMarker;
                }
            }


            function getLocationsFromAndTo(from, to) {
                var locations = [];
                for(var i = from; i <= to; i++) {
                    locations.push(routeCoordinates[i]);
                }

                return locations;
            }

            function toggleDocumentMarkers(){
                angular.forEach(docMarkers, function(docMarker){
                    docMarker.setVisible(vm.showDocuments);
                });
            }


            function showDocuments(doc, marker) {
                var content = "<div id='docsWindow'>";
                content += "<h5>{0}</h5>".format(doc.name);
                angular.forEach(doc.documents, function(item) {
                    content += "<i class='fa fa-arrow-circle-o-right mr5'></i>";
                    content += "<a href='app/assets/videos/docs/{0}' target='_blank'>{1}</a><br>".format(item.fileName, item.name);
                });
                content += "</div>";

                docsWindow.setContent(content);
                docsWindow.open(googleMap.map, marker);
            }


            function updateMap() {
                var routCheckInterval = $interval(function () {
                    if(googleMap != null && routeCoordinates.length > 0) {
                        trainroute = new google.maps.Polyline({
                            path:routeCoordinates,
                            strokeColor:"red",
                            strokeOpacity:1,
                            strokeWeight:5,
                            zIndex: 88887
                        });
                        //trainroute.setVisible(false);
                        trainroute.setMap(googleMap.map);

                        google.maps.event.addListener(trainroute, 'click', function(e) {
                            var closest = closestPointOnRoute(e.latLng);
                            showVideoAtMapLocation(closest);
                        });

                        $interval.cancel(routCheckInterval);

                        var startMarker = new google.maps.Marker({
                            icon:"http://maps.google.com/mapfiles/ms/micons/green-dot.png",
                            position: routeCoordinates[0],
                            map: googleMap.map
                        });

                        var endMarker = new google.maps.Marker({
                            icon:"http://maps.google.com/mapfiles/ms/micons/red-dot.png",
                            position: routeCoordinates[routeCoordinates.length-1],
                            map: googleMap.map
                        });

                        trainMarker = new google.maps.Marker({
                            icon:"app/assets/images/train.png",
                            position: routeCoordinates[0],
                            map: googleMap.map
                        });


                        infoWindow = new google.maps.InfoWindow({
                            content: "",
                            closeBoxURL: ""
                        });


                        //infoWindow.open(googleMap.map, trainMarker);

                        docsWindow = new google.maps.InfoWindow({
                            content: ""
                        });

                        google.maps.event.addListener(docsWindow, 'domready', function() {
                            var iwOuter = $('#docsWindow').parent().parent().parent();
                            $(iwOuter).addClass("docs-window");
                            $(iwOuter).parent().css({'z-index': 999});
                        });

                        google.maps.event.trigger(googleMap.map, 'resize');

                        var prevCoord = null;
                        var totalDist = 0;
                        var index = 0;
                        angular.forEach(routeCoordinates, function(coord) {
                            if(prevCoord != null) {
                                var dist = google.maps.geometry.spherical.computeDistanceBetween(prevCoord, coord)/1000;

                                totalDist = totalDist + dist;

                                var km = Math.round(totalDist);
                                if(mapDistanceLocations.get(km) == null) {
                                    mapDistanceLocations.put(km, coord);
                                }

                                var dur = gpsPoints[index].timestamp - gpsPoints[index-1].timestamp;
                                var speed = Math.ceil(dist/(dur * 0.000000278));
                                trainSpeeds[index] = speed;
                            }

                            prevCoord = coord;
                            index++;
                        });

                        var km = (google.maps.geometry.spherical.computeLength(routeCoordinates)/1000);
                        km  = Math.round(km);
                        vm.slider.options.ceil = km;

                        var arr = [];
                        for(var i=0; i<km; i++) {
                            if(i % 5 == 0)
                                arr.push(i);
                        }

                        if(km % 5 != 0) {
                            arr.splice(arr.length-1, 1);
                            arr.push(km);
                        }

                        vm.slider.options.ticksArray = arr;
                    }
                }, 100);
            }


            $scope.$on('$viewContentLoaded', function () {
                uiGmapGoogleMapApi.then(function(api) {
                    mapsApi = api;
                });

                uiGmapIsReady.promise().then(function (maps) {
                    googleMap = maps[0];

                    $timeout(function () {
                        $application.homeLoaded = true;

                        $(window).resize(function() {
                            adjustPanelHeight();
                            positionTripsContainer();
                        });
                        $scope.$broadcast('reCalcViewDimensions');

                        initEscapeKeyEvent();
                        initSpacebarKeyEvent();
                        initDoubleClickEvent();

                        $("#preloader").hide();
                        $("#appview").show();

                        positionTripsContainer();

                        adjustPanelHeight();
                        updateMap();
                        loadTrips();

                    }, 1000);

                });
            });
        }
    }
);