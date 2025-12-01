define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('ShiftService', ShiftService);

        function ShiftService(httpFactory) {
            return {
                createShift: createShift,
                updateShift: updateShift,
                deleteShift: deleteShift,
                getAllShifts: getAllShifts,
                getShiftById: getShiftById,
                getShiftsByIds: getShiftsByIds,
                getShiftReferences: getShiftReferences,
                createShiftPersons: createShiftPersons,
                personsBasedOnShift: personsBasedOnShift,
                getAllShiftPersons: getAllShiftPersons,
                deleteShiftPerson: deleteShiftPerson

            };

            function deleteShiftPerson(personId, shiftId) {
                var url = 'api/shifts/shiftpersons/' + personId + '/' + shiftId;
                return httpFactory.delete(url);
            }

            function getShiftById(shiftId) {
                var url = 'api/shifts/' + shiftId;
                return httpFactory.get(url);

            }

            function getShiftsByIds(shiftIds) {
                var url = "api/shifts/multiple/[" + shiftIds + ']';
                return httpFactory.get(url);
            }

            function getShiftReferences(objects, property) {
                var shiftIds = [];
                var idMap = new Hashtable();
                angular.forEach(objects, function (object) {
                    var shiftId = object[property];
                    if (idMap.get(shiftId) == null) {
                        shiftIds.push(shiftId);
                        idMap.put(shiftId, object);
                    }
                });
                if (shiftIds.length > 0){
                    getShiftsByIds(shiftIds).then(
                        function(shifts){
                            var map = new Hashtable();
                            angular.forEach(shifts, function(shift){
                                map.put(shift.shiftId, shift)
                            });
                            angular.forEach(objects,function(object){
                               var id = object[property];
                               var shift = map.get(id);
                                if(shift != null){
                                    object[property + "Object"] = shift;
                                }
                            });
                        }
                    )
                }

                    }

                function getAllShiftPersons(shift) {
                    var url = 'api/shifts/shiftpersons/' + shift.shiftId;
                    return httpFactory.get(url);
                }

                function createShiftPersons(persons) {
                    var url = "api/shifts/shiftpersons"
                    return httpFactory.post(url, persons);
                }

                function personsBasedOnShift(criteria, pageable) {
                    var url = "api/shifts/shiftpersons/pageable";
                    url += "?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field);
                    url += "&shift={0}&searchQuery={1}".format(criteria.shiftId, criteria.searchQuery);

                    return httpFactory.get(url);
                }

                function createShift(shift) {
                    var url = "api/shifts/";
                    return httpFactory.post(url, shift);
                }

                function updateShift(shift) {
                    var url = "api/shifts/" + shift.id;
                    return httpFactory.post(url, shift);
                }

                function deleteShift(shiftId) {
                    var url = "api/shifts/" + shiftId;
                    return httpFactory.delete(url);

                }

                function getAllShifts() {
                    var url = "api/shifts";
                    return httpFactory.get(url);
                }


            }
        }

        )
        ;