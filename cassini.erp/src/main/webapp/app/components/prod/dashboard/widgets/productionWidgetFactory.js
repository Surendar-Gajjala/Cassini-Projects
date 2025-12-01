define(['app/app.modules', 'app/shared/factories/httpFactory'],
    function ($app) {
        $app.factory('productionWidgetFactory',
            [
                '$http', '$q', 'httpFactory',

                function ($http, $q, httpFactory) {
                    return {

                        getMachines: function (isPagable, pagable) {
                            var url = "api/production/machine";
                            if (isPagable) {
                                url = "api/production/machine/pagable?page={0}&size={1}&sort={2}:{3}".
                                    format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                            }
                            return httpFactory.get(url);
                        },

                        createMachine: function (machine) {
                            var url = "api/production/machine";
                            return httpFactory.post(url, machine);
                        },
                        updateMachine: function (machine) {
                            var url = "api/production/machine/" + machine.id;
                            return httpFactory.put(url, machine);
                        },
                        deleteMachine: function (machine) {
                            var url = "api/production/machine/" + machine.id;
                            return httpFactory.delete(url);
                        },

                        getProcessSteps: function (isPagable, pagable) {
                            var url = "api/production/processstep";
                            if (isPagable) {
                                url = "api/production/processstep/pagable?page={0}&size={1}&sort={2}:{3}".
                                    format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                            }
                            return httpFactory.get(url);
                        },
                        createProcessStep: function (processstep) {
                            var url = "api/production/processstep";
                            return httpFactory.post(url, processstep);
                        },
                        updateProcessStep: function (processstep) {
                            var url = "api/production/processstep/" + processstep.id;
                            return httpFactory.put(url, processstep);
                        },
                        deleteProcessStep: function (processstep) {
                            var url = "api/production/processstep/" + processstep.id;
                            return httpFactory.delete(url);
                        },

                        getProcesses: function (isPagable, pagable) {
                            var url = "api/production/process";
                            if (isPagable) {
                                url = "api/production/process/pagable?page={0}&size={1}&sort={2}:{3}".
                                    format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                            }
                            return httpFactory.get(url);
                        },
                        createProcess: function (process) {
                            var url = "api/production/process";
                            return httpFactory.post(url, process);
                        },
                        updateProcess: function (process) {
                            var url = "api/production/process/" + process.id;
                            return httpFactory.put(url, process);
                        },
                        deleteProcess: function (process) {
                            var url = "api/production/process/" + process.id;
                            return httpFactory.delete(url);
                        },

                        getWorkCenters: function (isPagable, pagable) {
                            var url = "api/production/workcenter";
                            if (isPagable) {
                                url = "api/production/workcenter/pagable?page={0}&size={1}&sort={2}:{3}".
                                    format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                            }
                            return httpFactory.get(url);
                        },
                        createWorkCenter: function (workcenter) {
                            var url = "api/production/workcenter";
                            return httpFactory.post(url, workcenter);
                        },
                        updateWorkCenter: function (workcenter) {
                            var url = "api/production/workcenter/" + workcenter.id;
                            return httpFactory.put(url, workcenter);
                        },
                        deleteWorkCenter: function (workcenter) {
                            var url = "api/production/workcenter/" + workcenter.id;
                            return httpFactory.delete(url);
                        },

                        getProcessDropdownData: function () {
                            var urlArr = [];
                            urlArr.push("api/production/workcenter");
                            urlArr.push("api/production/process");

                            return httpFactory.all(urlArr);
                        },

                        getWorkShiftEmps: function (isPagable, pagable) {
                            var url = "api/production/workshiftemployee";
                            if (isPagable) {
                                url = "api/production/workshiftemployee/pagable?page={0}&size={1}&sort={2}:{3}".
                                    format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                            }
                            return httpFactory.get(url);
                        },
                        getWorkShifts: function () {
                            var url = "api/production/workshift";
                            return httpFactory.get(url);
                        },
                        getWorkShiftEmpsByShiftId: function (shiftId) {
                            var url = "api/production/workshiftemployee/" + shiftId;

                            return httpFactory.get(url);
                        },


                        /*   getWorkShiftEmpsByShiftId: function(shiftId, pagable) {
                         var url = "api/production/workshiftemployee/"+shiftId+"/empWorkShifts?page={0}&size={1}&sort={2}:{3}".
                         format(pagable.page-1, pagable.size, pagable.sort.field, pagable.sort.order);

                         return httpFactory.get(url);
                         },
                         */       createWorkShiftEmp: function (workshiftemployee) {
                            var url = "api/production/workshiftemployee";
                            return httpFactory.post(url, workshiftemployee);
                        },
                        updateWorkShiftEmp: function (workshiftemployee) {
                            var url = "api/production/workshiftemployee/" + workshiftemployee.rowId;
                            return httpFactory.put(url, workshiftemployee);
                        },
                        deleteWorkShiftEmp: function (workshiftemployee) {
                            var url = "api/production/workshiftemployee/" + workshiftemployee.rowId;
                            return httpFactory.delete(url);
                        },
                        getBoms: function () {
                            var url = "api/production/boms";
                            return httpFactory.get(url);
                        },

                        getBomTypes: function () {
                              var url = "api/production/boms/enums";
                              return httpFactory.get(url);
                          },
                       getBoms: function (isPagable, pagable) {
                        var url = "api/production/boms";
                        if (isPagable) {
                            url = "api/production/boms/pagable?page={0}&size={1}&sort={2}:{3}".
                                format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                        }
                        return httpFactory.get(url);
                       },
                       createBom: function (bom) {
                        var url = "api/production/boms";
                        return httpFactory.post(url, bom);
                       },
                       updateBom: function (bom) {
                        var url = "api/production/boms/" + bom.bomId;
                        return httpFactory.put(url, bom);
                       },
                        deleteBom: function (bom) {
                        var url = "api/production/boms/" + bom.bomId;
                        return httpFactory.delete(url);
                       },

                        getBomItems: function (bomId) {
                            var url = "api/production/bomitems/bom/"+bomId;
                            return httpFactory.get(url);
                        },

                        getBomItemTypes: function () {
                            var url = "api/production/bomitems/enums";
                            return httpFactory.get(url);
                        },
                        getBomItemsPagable: function (isPagable, pagable) {
                            var url = "api/production/bomitems";
                            if (isPagable) {
                                url = "api/production/bomitems/pagable?page={0}&size={1}&sort={2}:{3}".
                                    format(pagable.page - 1, pagable.size, pagable.sort.field, pagable.sort.order);
                            }
                            return httpFactory.get(url);
                        },
                        createBomItem: function (bomItem) {
                            var url = "api/production/bomitems";
                            return httpFactory.post(url, bomItem);
                        },
                        updateBomItem: function (bomItem) {
                            var url = "api/production/bomitems/" + bomItem.rowId;
                            return httpFactory.put(url, bomItem);
                        },
                        deleteBomItem: function (bomItem) {
                            var url = "api/production/bomitems/" + bomItem.rowId;
                            return httpFactory.delete(url);
                        }

                    }
                }
            ]
        );
    }
);