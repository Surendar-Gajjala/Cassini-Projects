define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('InstrumentService', InstrumentService);

        function InstrumentService(httpFactory) {
            return {
                createInstrument: createInstrument,
                updateInstrument: updateInstrument,
                getInstrument: getInstrument,
                deleteInstrument: deleteInstrument,
                getAllInstruments: getAllInstruments,
                getMultipleInstruments: getMultipleInstruments,
                uploadImageAttribute: uploadImageAttribute,
                saveInstrumentAttributes: saveInstrumentAttributes,
                uploadImage: uploadImage
            };

            function createInstrument(instrument) {
                var url = "api/mes/instruments";
                return httpFactory.post(url, instrument)
            }


            function updateInstrument(instrument) {
                var url = "api/mes/instruments/" + instrument.id;
                return httpFactory.put(url, instrument);
            }

            function getInstrument(id) {
                var url = "api/mes/instruments/" + id;
                return httpFactory.get(url)
            }

            function deleteInstrument(instrument) {
                var url = "api/mes/instruments/" + instrument;
                return httpFactory.delete(url);
            }

            function getAllInstruments(pageable, filters) {
                var url = "api/mes/instruments/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&searchQuery={3}&workOrder={4}".
                    format(filters.number, filters.type, filters.name, filters.searchQuery,filters.workOrder);
                return httpFactory.get(url);
            }

            function getMultipleInstruments(instrumentIds) {
                var url = "api/mes/instruments/multiple/[" + instrumentIds + "]";
                return httpFactory.get(url);
            }

            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mes/instruments/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }


            function saveInstrumentAttributes(instrumentId, attributes) {
                var url = "api/mes/instruments/" + instrumentId + "/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function uploadImage(instrumentId, file) {
                var url = "api/mes/instruments/" + instrumentId + "/image";
                return httpFactory.upload(url, file);
            }

        }
    }
);