define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (module) {
        module.factory('LibraryService', LibraryService);

        function LibraryService(httpFactory) {
            return {
                getLibraries: getLibraries,
                getLibraryById: getLibraryById,
                createLibrary: createLibrary,
                updateLibrary: updateLibrary,
                deleteLibrary: deleteLibrary
            };

            function getLibraries() {
                var url = "api/pdm/libraries"
                return httpFactory.get(url);
            }

            function getLibraryById(libraryId) {
                var url = "api/pdm/libraries" + libraryId;
                return httpFactory.get(url);
            }

            function createLibrary(library) {
                var url = "api/pdm/libraries"
                return httpFactory.post(url, library);
            }

            function updateLibrary(library) {
                var url = "api/pdm/libraries"
                return httpFactory.put(url, library);
            }

            function deleteLibrary(libraryId) {
                var url = "api/pdm/libraries" + libraryId;
                return httpFactory.delete(url);
            }
        }
    }
);