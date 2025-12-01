<object-workflow object-type="ECR"
                 can-start-workflow="ecrDetailsCount.affectedItems > 0 && ecr.createdBy == loginPersonDetails.person.id"
                 can-finish-workflow="true" object="ecr"
                 permission="hasPermission('change','ecr','edit') && !ecr.isApproved && ecr.statusType != 'REJECTED'"></object-workflow>