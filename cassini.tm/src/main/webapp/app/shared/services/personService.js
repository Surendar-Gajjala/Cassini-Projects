define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('PersonService', PersonService);

        function PersonService(httpFactory) {
            return {
                createEmergencyContact: createEmergencyContact,
                updateEmergencyContact: updateEmergencyContact,
                createPersonOtherInfo : createPersonOtherInfo,
                updatePersonOtherInfo : updatePersonOtherInfo,
                getEmergencyContactByPersonId:getEmergencyContactByPersonId,
                getPersonOtherInfoByPersonId:getPersonOtherInfoByPersonId,
                getAllEmergencyContact : getAllEmergencyContact,
                getAllPersonOtherInfo : getAllPersonOtherInfo,
                getPersonsByRole : getPersonsByRole,
                getRoleByPerson : getRoleByPerson,
                getTasksByPersons: getTasksByPersons
            };


            function getTasksByPersons(personId) {
                var url = "api/projects/null/tasks/allTasks/" + personId;
                return httpFactory.get(url);
            }

            function getRoleByPerson(personId){
                var url = "api/personOtherInfo/" + personId +"/role";
                return httpFactory.get(url);
            }

            function updateEmergencyContact(emrContact){
                var url = "api/emergencyContact/" + emrContact.id ;
                return httpFactory.put(url,emrContact);
            }

            function updatePersonOtherInfo(otherInfo){
                var url = "api/personOtherInfo/" + otherInfo.id ;
                return httpFactory.put(url,otherInfo);
            }

            function getPersonsByRole(personrole){
                var url = "api/personOtherInfo/role/" + personrole ;
                return httpFactory.get(url);
            }


            function createEmergencyContact(emrcntct) {
                var url = "api/emergencyContact";
                return httpFactory.post(url, emrcntct);
            }

            function getAllEmergencyContact() {
                var url = "api/emergencyContact";
                return httpFactory.get(url);
            }

            function getEmergencyContactByPersonId(personId) {
                var url = "api/emergencyContact/"+ personId;
                return httpFactory.get(url);
            }


            function createPersonOtherInfo(otherInfo) {
                var url = "api/personOtherInfo";
                return httpFactory.post(url, otherInfo);
            }

            function getAllPersonOtherInfo() {
                var url = "api/personOtherInfo";
                return httpFactory.get(url);
            }

            function getPersonOtherInfoByPersonId(personId) {
                var url = "api/personOtherInfo/"+ personId;
                return httpFactory.get(url);
            }

        }
    }
);
