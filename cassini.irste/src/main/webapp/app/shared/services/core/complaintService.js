/**
 * Created by Nageshreddy on 08-11-2018.
 */
define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],

    function (mdoule) {
        mdoule.factory('ComplaintService', ComplaintService);
        var baseURL = "api/irste/complaints";

        function ComplaintService(httpFactory) {
            return {
                getOne: getOne,
                getByComplaintNumber: getByComplaintNumber,
                getByResponder: getByResponder,
                getAllComplaints: getAllComplaints,
                getByEmailAndNumber: getByEmailAndNumber,
                createComplaint: createComplaint,
                getComplaints: getComplaints,
                deleteComplaint: deleteComplaint,
                freeTextSearch: freeTextSearch,
                updateComplaint: updateComplaint,
                updateComplaintStatus: updateComplaintStatus,
                getCompliantHistory: getCompliantHistory,
                getComplaintsByFilter: getComplaintsByFilter,
                exportUtilityReport: exportUtilityReport,
                getReportByDates: getReportByDates
            };

            function getCompliantHistory(complaintId) {
                return httpFactory.get(baseURL + "/history/complaint/" + complaintId);
            }

            function updateComplaintStatus(complaint) {
                return httpFactory.put(baseURL + "/status/" + complaint.id, complaint);
            }

            function getByComplaintNumber(number) {
                return httpFactory.get(baseURL + "/number/" + number);
            }

            function getByResponder(responder, pageable) {
                var url = baseURL + "/responder/" + responder + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getAllComplaints() {
                return httpFactory.get(baseURL + "/all");
            }

            function getComplaints(pageable) {
                var url = baseURL + "/pageable?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function freeTextSearch(pageable, filters) {
                var url = baseURL + "/freeTextSearch?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&location={0}&utility={1}&searchQuery={2}".format(filters.location, filters.utility, filters.searchQuery);
                return httpFactory.get(url);
            }

            function getComplaintsByFilter(pageable, filters) {
                var url = baseURL + "/filters?page={0}&size={1}&sort={2}:{3}".
                        format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&location={0}&utility={1}&searchQuery={2}&responder={3}&assistor={4}&facilitator={5}".format(filters.location,
                    filters.utility, filters.searchQuery, filters.responder, filters.assistor, filters.facilitator);
                return httpFactory.get(url);
            }

            function createComplaint(complaint) {
                return httpFactory.post(baseURL, complaint);
            }

            function updateComplaint(complaint) {
                return httpFactory.put(baseURL + "/" + complaint.id, complaint);
            }

            function getOne(id) {
                return httpFactory.get(baseURL + '/' + id);
            }

            function getByEmailAndNumber(email, number) {
                return httpFactory.get(baseURL + "/" + email + "/" + number);
            }

            function deleteComplaint(id) {
                return httpFactory.delete(baseURL + '/' + id);
            }
            function exportUtilityReport() {
                var url = "api/irste/complaints/" + 'excel' + "/report";
                return httpFactory.get(url);
            }
            function getReportByDates(complaintId) {
                var url = "api/complaints/report/"+complaintId;
                return httpFactory.get(url);
            }
        }

    }
);