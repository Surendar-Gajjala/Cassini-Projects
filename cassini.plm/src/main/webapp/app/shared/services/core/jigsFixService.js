define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('JigsFixtureService', JigsFixtureService);

        function JigsFixtureService(httpFactory) {
            return {
                createJigsFix: createJigsFix,
                updateJigsFix: updateJigsFix,
                getJigsFix: getJigsFix,
                deleteJigsFix: deleteJigsFix,
                getAllJigsFixs: getAllJigsFixs,
                getMultipleJigsFixs: getMultipleJigsFixs,
                uploadImage: uploadImage
            };

            function createJigsFix(jigsfix) {
                var url = "api/mes/jigsfixs";
                return httpFactory.post(url, jigsfix)
            }


            function updateJigsFix(jigsfix) {
                var url = "api/mes/jigsfixs/" + jigsfix.id;
                return httpFactory.put(url, jigsfix);
            }

            function getJigsFix(id) {
                var url = "api/mes/jigsfixs/" + id;
                return httpFactory.get(url)
            }

            function deleteJigsFix(jigsfix) {
                var url = "api/mes/jigsfixs/" + jigsfix;
                return httpFactory.delete(url);
            }

            function getAllJigsFixs(pageable, filters) {
                var url = "api/mes/jigsfixs/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&number={0}&type={1}&name={2}&description={3}&jigType={4}&searchQuery={5}".
                    format(filters.number, filters.type, filters.name, filters.description, filters.jigType, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getMultipleJigsFixs(jigsfixIds) {
                var url = "api/mes/jigsfixs/multiple/[" + jigsfixIds + "]";
                return httpFactory.get(url);
            }

            function uploadImage(jigsfixId, file) {
                var url = "api/mes/jigsfixs/" + jigsfixId + "/image";
                return httpFactory.upload(url, file);
            }
        }
    }
);