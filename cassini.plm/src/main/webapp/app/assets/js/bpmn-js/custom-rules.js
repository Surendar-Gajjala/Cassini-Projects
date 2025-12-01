'use strict';

window.CustomRulesInit = function() {
    this.addRule('connection.create', 1100, function(context) {
        var source = context.source,
            target = context.target;

        var sourceActivity = source.activity,
            targetActivity = target.activity;

        if(source.type === 'bpmn:StartEvent') {
            if(sourceActivity != undefined && sourceActivity.outgoing !== undefined && sourceActivity.outgoing.length === 1) {return false;}
        }

        if (source.type === 'bpmn:StartEvent' && target.type === 'bpmn:EndEvent') {return false;}

        if(source.type === 'bpmn:Task') {
            if(sourceActivity !== undefined && sourceActivity.type === 'NORMAL' && target.type === 'bpmn:EndEvent') {return false;}
            else if(sourceActivity !== undefined && sourceActivity.type === 'NORMAL' && target.type === 'bpmn:IntermediateThrowEvent') {return false;}
            else if(sourceActivity !== undefined && sourceActivity.type === 'REJECTED' && target.type === 'bpmn:EndEvent') {return false;}
            else if(sourceActivity !== undefined && sourceActivity.type === 'RELEASED' && target.type === 'bpmn:IntermediateThrowEvent') {return false;}
            else if(sourceActivity !== undefined && sourceActivity.type === 'RELEASED' && targetActivity !== undefined && targetActivity.type === 'REJECTED') {return false;}
            else if(sourceActivity !== undefined && sourceActivity.type === 'REJECTED' && targetActivity !== undefined && targetActivity.type === 'RELEASED') {return false;}
            else if(sourceActivity !== undefined && sourceActivity.type === 'REJECTED' && targetActivity !== undefined && targetActivity.type === 'NORMAL') {return false;}
        }

    });

    this.addRule('shape.append', 1200, function(context) {
        var source = context.source,
            target = context.target;

        var sourceBo = source.businessObject,
            targetBo = target.businessObject;

        var sourceActivity = source.activity,
            targetActivity = target.activity;

        if(source.type === 'bpmn:StartEvent') {
            if(sourceBo.outgoing !== undefined &&
                sourceBo.outgoing.length === 1) {
                return false;
            }
        }
        else if(sourceActivity !== null && sourceActivity !== undefined &&
            sourceActivity.type === 'NORMAL' &&
            target.type === 'bpmn:IntermediateThrowEvent') {
            return false;
        }

    });
};