define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('ShiftService', ShiftService);

        function ShiftService(httpFactory) {
            return {
                createShift: createShift,
                updateShift: updateShift,
                getShift: getShift,
                deleteShift: deleteShift,
                getShifts: getShifts,
                getAllShifts: getAllShifts,
                getMultipleShifts: getMultipleShifts,
                createShiftPerson: createShiftPerson,
                updateShiftPerson: updateShiftPerson,
                createShiftMultiplePersons: createShiftMultiplePersons,
                getShiftPersons: getShiftPersons,
                deleteShiftPerson: deleteShiftPerson,
                getShiftObjectCounts: getShiftObjectCounts,
                getPOShiftPersons: getPOShiftPersons
            };

            function createShift(plant) {
                var url = "api/mes/shifts";
                return httpFactory.post(url, plant)
            }


            function updateShift(plant) {
                var url = "api/mes/shifts/" + plant.id;
                return httpFactory.put(url, plant);
            }

            function getShift(id) {
                var url = "api/mes/shifts/" + id;
                return httpFactory.get(url)
            }

            function deleteShift(plant) {
                var url = "api/mes/shifts/" + plant;
                return httpFactory.delete(url);
            }


            function getShifts() {
                var url = "api/mes/shifts";
                return httpFactory.get(url);
            }

            function getAllShifts(pageable, filters) {
                var url = "api/mes/shifts/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}".
                    format(filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleShifts(plantIds) {
                var url = "api/mes/shifts/multiple/[" + plantIds + "]";
                return httpFactory.get(url);
            }

            function createShiftPerson(shiftId, shiftPerson) {
                var url = "api/mes/shifts/" + shiftId + "/person";
                return httpFactory.post(url, shiftPerson);
            }

            function updateShiftPerson(shiftId, shiftPerson) {
                var url = "api/mes/shifts/" + shiftId + "/person/" + shiftPerson.id;
                return httpFactory.put(url, shiftPerson);
            }

            function createShiftMultiplePersons(shiftId, shiftPersons) {
                var url = "api/mes/shifts/" + shiftId + "/person/multiple";
                return httpFactory.post(url, shiftPersons);
            }

            function getShiftPersons(shiftId, pageable) {
                var url = "api/mes/shifts/" + shiftId + "/persons?page={0}&size={1}".
                        format(pageable.page, pageable.size);
                return httpFactory.get(url);
            }

            function deleteShiftPerson(shiftId, personId) {
                var url = "api/mes/shifts/" + shiftId + "/person/" + personId;
                return httpFactory.delete(url);
            }

            function getShiftObjectCounts(shiftId){
                var url = "api/mes/shifts/" + shiftId + "/counts";
                return httpFactory.get(url)
            }

            function getPOShiftPersons(){
                var url = "api/mes/shifts/persons";
                return httpFactory.get(url)
            }

        }
    }
);