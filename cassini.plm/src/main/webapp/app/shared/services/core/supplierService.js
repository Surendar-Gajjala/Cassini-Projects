define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],
    function (mdoule) {
        mdoule.factory('SupplierService', SupplierService);

        function SupplierService(httpFactory) {
            return {
                createSupplier: createSupplier,
                updateSupplier: updateSupplier,
                getSupplier: getSupplier,
                deleteSupplier: deleteSupplier,
                getSuppliers: getSuppliers,
                getAllSuppliers: getAllSuppliers,
                getMultipleSuppliers: getMultipleSuppliers,
                uploadImageAttribute: uploadImageAttribute,
                saveSupplierAttributes: saveSupplierAttributes,
                getSupplierObjectAttributesWithHierarchy: getSupplierObjectAttributesWithHierarchy,
                updateSupplierAttribute: updateSupplierAttribute,
                getSupplierFileCounts: getSupplierFileCounts,
                getSupplierContacts: getSupplierContacts,
                createSupplierContact: createSupplierContact,
                updateSupplierContact: updateSupplierContact,
                deleteSupplierContact: deleteSupplierContact,
                createSupplierParts: createSupplierParts,
                getSupplierParts: getSupplierParts,
                deleteSupplierParts: deleteSupplierParts,
                createSupplierPart: createSupplierPart,
                updateSupplierPart: updateSupplierPart,
                getSupplierContact: getSupplierContact,
                promoteSupplier: promoteSupplier,
                demoteSupplier: demoteSupplier,
                getApprovedSuppliers: getApprovedSuppliers,

                getObjectsCountByType: getObjectsCountByType,
                subscribeSupplier: subscribeSupplier,
                getSupplierPPAPs: getSupplierPPAPs,
                getSupplierAuditsPlans: getSupplierAuditsPlans,
                getSupplierReferences: getSupplierReferences,
                getSupplierActiveContacts: getSupplierActiveContacts
            };

            function createSupplier(supplier) {
                var url = "api/mfr/suppliers";
                return httpFactory.post(url, supplier)
            }


            function updateSupplier(supplier) {
                var url = "api/mfr/suppliers/" + supplier.id;
                return httpFactory.put(url, supplier);
            }

            function getSupplier(id) {
                var url = "api/mfr/suppliers/" + id;
                return httpFactory.get(url)
            }

            function deleteSupplier(supplier) {
                var url = "api/mfr/suppliers/" + supplier;
                return httpFactory.delete(url);
            }


            function getSuppliers() {
                var url = "api/mfr/suppliers";
                return httpFactory.get(url);
            }

            function getApprovedSuppliers() {
                var url = "api/mfr/suppliers/approved";
                return httpFactory.get(url);
            }

            function getAllSuppliers(pageable, filters) {
                var url = "api/mfr/suppliers/all?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                url += "&searchQuery={0}&name={1}&type={2}&audit={3}&city={4}".
                    format(filters.searchQuery, filters.name, filters.type, filters.audit, filters.city);
                return httpFactory.get(url);
            }

            function getMultipleSuppliers(supplierIds) {
                var url = "api/mfr/suppliers/multiple/[" + supplierIds + "]";
                return httpFactory.get(url);
            }

            function getSupplierReferences(objects, property) {
                var supplierIds = [];
                angular.forEach(objects, function (object) {
                    if (object[property] != null && supplierIds.indexOf(object[property]) == -1) {
                        supplierIds.push(object[property]);
                    }
                });

                if (supplierIds.length > 0) {
                    getMultipleSuppliers(supplierIds).then(
                        function (persons) {
                            var map = new Hashtable();
                            angular.forEach(persons, function (supplier) {
                                map.put(supplier.id, supplier);
                            });

                            angular.forEach(objects, function (object) {
                                if (object[property] != null) {
                                    var supplier = map.get(object[property]);
                                    if (supplier != null) {
                                        object[property + "Object"] = supplier;
                                    }
                                }
                            });
                        }
                    );
                }
            }


            function uploadImageAttribute(objectId, attributeId, file) {
                var url = "api/mfr/suppliers/uploadimageattribute/" + objectId + "/" + attributeId;
                return httpFactory.upload(url, file);
            }


            function saveSupplierAttributes(attributes) {
                var url = "api/mfr/suppliers/create/attributes/multiple";
                return httpFactory.post(url, attributes);
            }

            function getAllSuppliersByPage(pageable) {
                var url = "api/mfr/suppliers/pageable?page={0}&size={1}&sort={2}:{3}".
                    format(pageable.page, pageable.size, pageable.sort.field, pageable.sort.order);
                return httpFactory.get(url);
            }

            function getSupplierObjectAttributesWithHierarchy(typeId) {
                var url = "api/plm/suppliertypes/type/" + typeId + "/attributes?hierarchy=true";
                return httpFactory.get(url);
            }

            function updateSupplierAttribute(objectId, attribute) {
                var url = "api/mfr/suppliers/" + objectId + "/attributes";
                return httpFactory.put(url, attribute);
            }

            function getSupplierFileCounts(supplierId) {
                var url = "api/mfr/suppliers/" + supplierId + "/count";
                return httpFactory.get(url);
            }


            function createSupplierContact(supplier) {
                var url = "api/mfr/suppliers/contact";
                return httpFactory.post(url, supplier)
            }


            function updateSupplierContact(contact) {
                var url = "api/mfr/suppliers/contact/" + contact.id;
                return httpFactory.put(url, contact);
            }


            function deleteSupplierContact(contactId) {
                var url = "api/mfr/suppliers/contact/" + contactId;
                return httpFactory.delete(url);
            }

            function getSupplierContacts(supplierId) {
                var url = "api/mfr/suppliers/" + supplierId + "/contacts";
                return httpFactory.get(url);
            }

            function getSupplierActiveContacts(supplierId) {
                var url = "api/mfr/suppliers/" + supplierId + "/contacts/active";
                return httpFactory.get(url);
            }

            function getSupplierContact(contactId) {
                var url = "api/mfr/suppliers/contact/" + contactId;
                return httpFactory.get(url);
            }

            function createSupplierParts(supplierId, mfrParts) {
                var url = "api/mfr/suppliers/create/supplier/mfrparts";
                return httpFactory.post(url, mfrParts);
            }

            function getSupplierParts(supplierId) {
                var url = "api/mfr/suppliers/" + supplierId + "/mfrparts";
                return httpFactory.get(url);
            }

            function createSupplierPart(supplierId, parts) {
                var url = "api/mfr/suppliers/" + supplierId + "/mfrparts";
                return httpFactory.post(url, parts);
            }

            function updateSupplierPart(supplierId, part) {
                var url = "api/mfr/suppliers/" + supplierId + "/mfrparts/" + part.id;
                return httpFactory.put(url, part);
            }

            function deleteSupplierParts(partId) {
                var url = "api/mfr/suppliers/delete/supplierpart/" + partId;
                return httpFactory.delete(url);
            }

            function promoteSupplier(supplierId, supplier) {
                var url = "api/mfr/suppliers/" + supplierId + "/promote";
                return httpFactory.put(url, supplier);
            }

            function demoteSupplier(supplierId, supplier) {
                var url = "api/mfr/suppliers/" + supplierId + "/demote";
                return httpFactory.put(url, supplier);
            }

            function getObjectsCountByType(id) {
                var url = "api/plm/suppliertypes/" + id + "/count";
                return httpFactory.get(url);
            }

            function subscribeSupplier(id) {
                var url = "api/mfr/suppliers/" + id + "/subscribe";
                return httpFactory.post(url);
            }

            function getSupplierPPAPs(id) {
                var url = "api/mfr/suppliers/" + id + "/ppaps";
                return httpFactory.get(url);
            }

            function getSupplierAuditsPlans(supplierId) {
                var url = "api/mfr/suppliers/" + supplierId + "/supplieraudits";
                return httpFactory.get(url);
            }


        }
    }
);

