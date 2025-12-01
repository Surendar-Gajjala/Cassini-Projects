define(
    [
        'app/desktop/modules/main/main.module',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/commonService',
        'app/shared/services/core/inwardService',
        'app/shared/services/core/itemTypeService',
        'app/assets/bower_components/cassini-platform/app/shared/services/common/attributeAttachmentService'
    ],
    function (module) {
        module.controller('SearchBarController', SearchBarController);

        function SearchBarController($scope, $rootScope, $timeout, $interval, $translate, $state, $cookies, $application,
                                     CommonService, InwardService, ItemTypeService, AttributeAttachmentService) {

            var vm = this;

            var parsed = angular.element("<div></div>");

            vm.pageable = {
                page: 0,
                size: 20,
                sort: {
                    field: "modifiedDate",
                    order: "DESC"
                }
            };

            vm.search = {
                name: null,
                description: null,
                searchType: null,
                query: null,
                objectType: null,
                owner: null
            };

            var pagedResults = {
                content: [],
                last: true,
                totalPages: 0,
                totalElements: 0,
                size: vm.pageable.size,
                number: 0,
                sort: null,
                first: true,
                numberOfElements: 0
            };

            vm.searchResults = angular.copy(pagedResults);

            vm.searchText = "";
            vm.filter = 'All';
            vm.active = 0;
            vm.filesDto = null;

            vm.performSearch = performSearch;

            vm.setFilter = setFilter;
            vm.preventClick = preventClick;
            vm.showFiltersMenu = showFiltersMenu;

            vm.nextPage = nextPage;
            vm.previousPage = previousPage;

            vm.criteria = {
                searchQuery: null
            };

            function nextPage() {
                vm.pageable.page++;
                searchInstances();
            }

            function previousPage() {
                vm.pageable.page--;
                searchInstances();
            }

            function preventClick(event) {
                event.stopPropagation();
                event.preventDefault();
            }

            function showFiltersMenu() {
                $('.search-input-container .dropdown-menu').show();
            }

            function searchInstances() {
                InwardService.searchItemInstances(vm.pageable, vm.criteria).then(
                    function (data) {
                        vm.searchResults = data;
                        var attributeDefs = [];
                        var instanceIds = [];

                        angular.forEach(vm.itemInstanceAttributes, function (attribute) {
                            if (attribute.attributeDef.id != null && attribute.attributeDef.id != "" && attribute.attributeDef.id != 0) {
                                attributeDefs.push(attribute.attributeDef.id);
                            }
                        });

                        angular.forEach(vm.searchResults.content, function (searchResult) {
                            instanceIds.push(searchResult.id);
                        });

                        if (attributeDefs.length > 0 && instanceIds.length > 0) {
                            InwardService.getAttributesByItemIdAndAttributeId(instanceIds, attributeDefs).then(
                                function (data) {
                                    vm.selectedObjectAttributes = data;

                                    var map = new Hashtable();
                                    angular.forEach(vm.itemInstanceAttributes, function (att) {
                                        if (att.attributeDef.id != null && att.attributeDef.id != "" && att.attributeDef.id != 0) {
                                            map.put(att.attributeDef.id, att);
                                        }
                                    });

                                    angular.forEach(vm.searchResults.content, function (searchResult) {
                                        var attributes = [];
                                        attributes = vm.selectedObjectAttributes[searchResult.id];

                                        angular.forEach(attributes, function (attribute) {
                                            var selectAtt = map.get(attribute.id.attributeDef);
                                            if (selectAtt != null) {
                                                if (selectAtt.attributeDef.dataType == 'ATTACHMENT' && selectAtt.attributeDef.name == "Certificate") {
                                                    var revisionAttachmentIds = [];
                                                    if (attribute.attachmentValues.length > 0) {
                                                        angular.forEach(attribute.attachmentValues, function (attachmentId) {
                                                            revisionAttachmentIds.push(attachmentId);
                                                        });
                                                        AttributeAttachmentService.getMultipleAttributeAttachments(revisionAttachmentIds).then(
                                                            function (data) {
                                                                vm.revisionAttachments = data;
                                                                searchResult.certificates = vm.revisionAttachments;
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        });
                                    })

                                }
                            )
                        }

                        vm.loading = false;
                    }
                )
            }

            function performSearch() {

                if (vm.searchText == "") {
                    $('#search-results-container').hide();
                    vm.criteria.searchQuery = null;
                }
                if (vm.searchText != "" && vm.searchText != null && vm.searchText != undefined) {
                    vm.loading = true;
                    vm.pageable.page = 0;
                    vm.criteria.searchQuery = vm.searchText;
                    var height = $('.contentpanel').outerHeight();
                    var width = $('.contentpanel').width();
                    $('#search-results-container').height(height / 1.01);
                    $('#search-results-container').width(width / 1.5);
                    $('#search-results-container').show();
                    searchInstances();
                } else {
                    $('#search-results-container').hide();
                    vm.criteria.searchQuery = null;
                }
            }

            function setFilter(filter) {
                vm.filter = filter;
                $('.search-input-container .dropdown-menu').hide();
                performSearch();
            }

            function loadItemInstanceAttributes() {
                vm.itemInstanceAttributes = [];
                ItemTypeService.getAttributesByObjectType("ITEMINSTANCE").then(
                    function (data) {
                        angular.forEach(data, function (attribute) {
                            var att = {
                                id: {
                                    objectId: null,
                                    attributeDef: attribute.id
                                },
                                attributeDef: attribute,
                                value: {
                                    id: {
                                        objectId: null,
                                        attributeDef: attribute.id
                                    },
                                    stringValue: null,
                                    integerValue: null,
                                    doubleValue: null,
                                    booleanValue: null,
                                    dateValue: null,
                                    listValue: null,
                                    newListValue: null,
                                    listValueEditMode: false,
                                    timeValue: null,
                                    timestampValue: null,
                                    refValue: null,
                                    imageValue: null,
                                    attachmentValues: [],
                                    currencyValue: null,
                                    currencyType: null
                                },
                                stringValue: null,
                                integerValue: null,
                                doubleValue: null,
                                booleanValue: null,
                                dateValue: null,
                                listValue: null,
                                newListValue: null,
                                listValueEditMode: false,
                                timeValue: null,
                                timestampValue: null,
                                refValue: null,
                                imageValue: null,
                                attachmentValues: [],
                                currencyValue: null,
                                currencyType: null
                            };
                            vm.itemInstanceAttributes.push(att);
                        });
                    }
                )
            }

            vm.openPropertyAttachment = openPropertyAttachment;
            function openPropertyAttachment(attachment) {
                var url = "{0}//{1}/api/col/attachments/{2}/preview".
                    format(window.location.protocol, window.location.host,
                    attachment.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = attachment.name;
                });
                /*window.open(url);
                $timeout(function () {
                    window.close();
                 }, 2000);*/
                //launchUrl(url);
            }

            vm.downloadGatePass = downloadGatePass;
            function downloadGatePass(gatePass) {
                var url = "{0}//{1}/api/drdo/inwards/gatePass/{2}/{3}/preview".
                    format(window.location.protocol, window.location.host,
                    gatePass.id, gatePass.gatePass.id);
                var newWindow = window.open(url, "_blank");
                newWindow.addEventListener('load', function () {
                    newWindow.document.title = gatePass.gatePass.name;
                });
                /*window.open(url);
                $timeout(function () {
                    window.close();
                 }, 2000);*/
            }

            (function () {
                loadItemInstanceAttributes();
                $(document).click(function () {
                    $('#search-results-container').hide();
                    $('.search-input-container .dropdown-menu').hide();
                });
                $(document).on('keydown', function (evt) {
                    if (evt.keyCode == 27) {
                        $('#search-results-container').hide();
                        $('.search-input-container .dropdown-menu').hide();
                    }
                });

                $timeout(function () {
                    $('#search-results-container').click(function (event) {
                        event.stopPropagation();
                        event.preventDefault();
                    });
                }, 1000);
            })();
        }
    }
);