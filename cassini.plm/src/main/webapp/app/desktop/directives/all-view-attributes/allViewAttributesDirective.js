define(
    [
        'app/desktop/modules/pdm/pdm.module',
        'app/shared/services/core/dcrService',
        'app/shared/services/core/ecrService',
        'app/shared/services/core/ecoService',
        'app/shared/services/core/itemService',
        'app/shared/services/core/dcoService',
        'app/shared/services/core/qcrService',
        'app/shared/services/core/problemReportService'
    ],
    function (module) {
        module.directive('allViewAttributes', AllViewAttributesDirective);

        function AllViewAttributesDirective($rootScope, $state, $timeout, $window, $translate, ECRService, DCRService, ECOService, ProblemReportService, QcrService) {

            return {
                templateUrl: 'app/desktop/directives/all-view-attributes/allViewAttributesDirective.jsp',
                restrict: 'E',
                scope: {
                    object: "=",
                    objectAttribute: "="
                },

                link: function ($scope, element, attrs) {
                    $scope.showRichText = showRichText;
                    $scope.showHyperLinkValue = showHyperLinkValue;
                    function showRichText(attributeName, objectAttribute, object) {
                        $rootScope.showRichTextSidePanel(attributeName, objectAttribute, object);
                    }

                    function showHyperLinkValue(value) {
                        $window.open(value);
                    }

                    $scope.showThumbnailImage = showThumbnailImage;
                    function showThumbnailImage(item) {
                        var modal = document.getElementById('item-thumbnail' + item.id + "" + item.id);
                        modal.style.display = "block";

                        var span = document.getElementById("thumbnail-close" + item.id + "" + item.id);
                        $("#thumbnail-image" + item.id + "" + item.id).width($('#thumbnail-view' + item.id + "" + item.id).outerWidth());
                        $("#thumbnail-image" + item.id + "" + item.id).height($('#thumbnail-view' + item.id + "" + item.id).outerHeight());

                        span.onclick = function () {
                            modal.style.display = "none";
                        }
                        $scope.$evalAsync();
                    }

                    $scope.openAttachment = openAttachment;
                    function openAttachment(attachment) {
                        var url = "{0}//{1}/api/col/attachments/{2}/download".
                            format(window.location.protocol, window.location.host,
                            attachment.id);
                        $rootScope.downloadFileFromIframe(url);
                    }

                    $scope.showAttributeDetails = showAttributeDetails;
                    function showAttributeDetails(attribute) {
                        if (attribute.objectType == 'ITEM') {
                            $state.go('app.items.details', {itemId: attribute.latestRevision});
                        } else if (attribute.objectType == 'ITEMREVISION') {
                            $state.go('app.items.details', {itemId: attribute.id});
                        } else if (attribute.objectType == 'CHANGE') {
                            if (attribute.changeType == 'ECO') {
                                $state.go('app.changes.eco.details', {ecoId: attribute.id});
                            } else if (attribute.changeType == 'DCO') {
                                $state.go('app.changes.dco.details', {dcoId: attribute.id});
                            } else if (attribute.changeType == 'ECR') {
                                $state.go('app.changes.ecr.details', {ecrId: attribute.id});
                            } else if (attribute.changeType == 'DCR') {
                                $state.go('app.changes.dcr.details', {dcrId: attribute.id});
                            } else if (attribute.changeType == 'DEVIATION' || attribute.changeType == 'WAIVER') {
                                $state.go('app.changes.variance.details', {varianceId: attribute.id});
                            }
                        } else if (attribute.objectType == 'OEMPARTMCO' || attribute.objectType == 'ITEMMCO') {
                            $state.go('app.changes.mco.details', {mcoId: attribute.id});
                        } else if (attribute.objectType == 'PLMWORKFLOWDEFINITION') {
                            $state.go('app.workflow.editor', {mode: 'edit', workflow: attribute.id});
                        } else if (attribute.objectType == 'MANUFACTURER') {
                            $state.go('app.mfr.details', {manufacturerId: attribute.id});
                        } else if (attribute.objectType == 'MANUFACTURERPART') {
                            $state.go('app.mfr.mfrparts.details', {
                                mfrId: attribute.manufacturer,
                                manufacturePartId: attribute.id
                            });
                        } else if (attribute.objectType == 'PROJECT') {
                            $state.go('app.pm.project.details', {projectId: attribute.id});
                        }

                        else if (attribute.objectType == 'REQUIREMENT') {
                            $state.go('app.req.requirements.details', {requirementId: attribute.latestVersion});
                        } else if (attribute.objectType == 'REQUIREMENTDOCUMENT') {
                            $state.go('app.req.document.details', {
                                reqId: attribute.latestRevision,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'PLANT') {
                            $state.go('app.mes.masterData.plant.details', {
                                plantId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'ASSEMBLYLINE') {
                            $state.go('app.mes.masterData.assemblyline.details', {
                                assemblyLineId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'WORKCENTER') {
                            $state.go('app.mes.masterData.workcenter.details', {
                                workcenterId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'MACHINE') {
                            $state.go('app.mes.masterData.machine.details', {
                                machineId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'EQUIPMENT') {
                            $state.go('app.mes.masterData.equipment.details', {
                                equipmentId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'INSTRUMENT') {
                            $state.go('app.mes.masterData.instrument.details', {
                                instrumentId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'TOOL') {
                            $state.go('app.mes.masterData.tool.details', {
                                toolId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'JIGFIXTURE') {
                            $state.go('app.mes.jigsAndFixture.details', {
                                jigsAndFixtureId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'MATERIAL') {
                            $state.go('app.mes.masterData.material.details', {
                                materialId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'MANPOWER') {
                            $state.go('app.mes.masterData.manpower.details', {
                                manpowerId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'OPERATION') {
                            $state.go('app.mes.masterData.operation.details', {
                                operationId: attribute.id,
                                tab: 'details.basic'
                            });
                        } else if (attribute.objectType == 'PRODUCTIONORDER') {
                            $state.go('app.mes.productionOrder.details', {
                                productionOrderId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.objectType == 'MROASSET') {
                            $state.go('app.mro.asset.details', {
                                assetId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.objectType == 'MROMETER') {
                            $state.go('app.mro.meter.details', {
                                meterId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.objectType == 'MROSPAREPART') {
                            $state.go('app.mro.sparePart.details', {
                                sparePartId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.objectType == 'MROWORKREQUEST') {
                            $state.go('app.mro.workOrder.details', {
                                productionOrderId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.objectType == 'MROWORKORDER') {
                            $state.go('app.mro.workRequest.details', {
                                workRequestId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                        else if (attribute.objectType == 'CUSTOMOBJECT') {
                            $state.go('app.customobjects.details', {
                                customId: attribute.id,
                                tab: 'details.basic'
                            });
                        }
                    }

                    (function () {
                        $scope.$on('app.all.view.attributes', function (event, data) {

                        });
                    })();
                }
            }

        }
    }
);