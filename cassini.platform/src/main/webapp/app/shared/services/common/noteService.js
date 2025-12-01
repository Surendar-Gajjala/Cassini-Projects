define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('NoteService', NoteService);

        function NoteService(httpFactory) {
            return {
                getNotes: getNotes,
                getNote: getNote,
                createNote: createNote,
                deleteNote: deleteNote
            };


            function getNotes(objectType, objectId) {
                var url = "api/col/notes";
                if(objectType != null && objectType != undefined &&
                    objectId != null && objectId != undefined) {
                    url += "?objectType={0}&objectId={1}".
                                format(objectType, objectId);
                }
                return httpFactory.get(url);
            }

            function getNote(noteId) {
                var url = "api/col/notes/" + noteId;
                return httpFactory.get(url);
            }

            function createNote(note) {
                var url = "api/col/notes";
                return httpFactory.post(url, note);
            }

            function deleteNote(noteId) {
                var url = "api/col/notes/" + noteId;
                return httpFactory.delete(url);
            }
        }
    }
);