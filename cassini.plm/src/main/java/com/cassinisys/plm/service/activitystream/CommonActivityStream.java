package com.cassinisys.plm.service.activitystream;

import com.cassinisys.platform.events.CommonEvents;
import com.cassinisys.platform.model.activitystream.ActivityStream;
import com.cassinisys.platform.model.col.Comment;
import com.cassinisys.platform.model.core.Currency;
import com.cassinisys.platform.model.core.DataType;
import com.cassinisys.platform.model.core.ObjectAttribute;
import com.cassinisys.platform.model.core.ObjectTypeAttribute;
import com.cassinisys.platform.repo.core.CurrencyRepository;
import com.cassinisys.platform.service.activitystream.ActivityStreamConverter;
import com.cassinisys.platform.service.activitystream.BaseActivityStream;
import com.cassinisys.plm.event.InspectionPlanEvents;
import com.cassinisys.plm.event.ItemEvents;
import com.cassinisys.plm.model.plm.PLMItemRevision;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pqm.PQMInspectionPlanRevision;
import com.cassinisys.plm.repo.plm.ItemRevisionRepository;
import com.cassinisys.plm.repo.pqm.InspectionPlanRevisionRepository;
import com.cassinisys.plm.service.activitystream.dto.ASAttributeChangeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CommonActivityStream extends BaseActivityStream implements ActivityStreamConverter {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ItemRevisionRepository itemRevisionRepository;
    @Autowired
    private InspectionPlanRevisionRepository inspectionPlanRevisionRepository;
    @Autowired
    private CurrencyRepository currencyRepository;


    @Async
    @EventListener
    public void commentAdded(CommonEvents.CommentAdded event) {
        Comment comment = event.getComment();

        Enum objectType = comment.getObjectType();
        Integer objectId = comment.getObjectId();
        if (objectType != null && objectType.equals(PLMObjectType.ITEMREVISION)) {
            PLMItemRevision itemRevision = itemRevisionRepository.findOne(objectId);
            applicationEventPublisher.publishEvent(new ItemEvents.ItemCommentAddedEvent(itemRevision, comment));
        } else if (objectType != null && objectType.equals(PLMObjectType.INSPECTIONPLANREVISION)) {
            PQMInspectionPlanRevision inspectionPlanRevision = inspectionPlanRevisionRepository.findOne(objectId);
            applicationEventPublisher.publishEvent(new InspectionPlanEvents.PlanCommentAddedEvent(inspectionPlanRevision, comment));
        }
    }

    @Override
    public String getConverterKey() {
        return "platform.common";
    }

    @Override
    public String convertToString(ActivityStream as) {
        String convertedString = "";

        return convertedString;
    }

    public List<ASAttributeChangeDTO> getAttributeUpdateJsonData(ObjectAttribute oldAttribute, ObjectAttribute newAttribute, ObjectTypeAttribute attDef) {
        List<ASAttributeChangeDTO> changes = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        String oldValue = "";
        if (oldAttribute != null) {
            oldValue = oldAttribute.getValueAsString();
        }
        String newValue = newAttribute.getValueAsString();

        if (attDef.getDataType() == DataType.DATE) {
            if (oldAttribute != null) {
                Date oldDate = oldAttribute.getDateValue();
                Date newDate = newAttribute.getDateValue();
                if (oldDate.compareTo(newDate) != 0) {
                    String oldDateValue = dateFormat.format(oldDate);
                    String newDateValue = dateFormat.format(newDate);
                    changes.add(new ASAttributeChangeDTO(attDef.getName(), oldDateValue, newDateValue));
                }
            } else {
                Date newDate = newAttribute.getDateValue();
                String oldDateValue = "";
                String newDateValue = dateFormat.format(newDate);
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldDateValue, newDateValue));
            }
        } else if (attDef.getDataType() == DataType.TIMESTAMP) {
            if (oldAttribute != null) {
                Date oldDate = oldAttribute.getTimestampValue();
                Date newDate = newAttribute.getTimestampValue();
                if (oldDate.compareTo(newDate) != 0) {
                    String oldDateValue = timeStampFormat.format(oldDate);
                    String newDateValue = timeStampFormat.format(newDate);
                    changes.add(new ASAttributeChangeDTO(attDef.getName(), oldDateValue, newDateValue));
                }
            } else {
                Date newDate = newAttribute.getTimestampValue();
                String oldDateValue = "";
                String newDateValue = timeStampFormat.format(newDate);
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldDateValue, newDateValue));
            }
        } else if (attDef.getDataType() == DataType.TIME) {
            if (oldAttribute != null) {
                Date oldDate = oldAttribute.getTimeValue();
                Date newDate = newAttribute.getTimeValue();
                if (oldDate.compareTo(newDate) != 0) {
                    String oldDateValue = timeFormat.format(oldDate);
                    String newDateValue = timeFormat.format(newDate);
                    changes.add(new ASAttributeChangeDTO(attDef.getName(), oldDateValue, newDateValue));
                }
            } else {
                Date newDate = newAttribute.getTimeValue();
                String oldDateValue = "";
                String newDateValue = timeFormat.format(newDate);
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldDateValue, newDateValue));
            }
        } else if (attDef.getDataType() == DataType.LIST) {
            if (oldAttribute != null) {
                if (oldAttribute.getMListValue().length > 0) {
                    changes.add(new ASAttributeChangeDTO("MULTIPLELIST", null, null));
                } else {
                    String oldListValue = oldAttribute.getListValue();
                    String newListValue = newAttribute.getListValue();
                    if (!oldListValue.equals(newListValue)) {
                        changes.add(new ASAttributeChangeDTO(attDef.getName(), oldListValue, newListValue));
                    }
                }
            } else {
                if (newAttribute.getMListValue().length > 0) {
                    changes.add(new ASAttributeChangeDTO("MULTIPLELIST", null, null));
                } else {
                    String oldListValue = "";
                    String newListValue = newAttribute.getListValue();
                    if (!oldListValue.equals(newListValue)) {
                        changes.add(new ASAttributeChangeDTO(attDef.getName(), oldListValue, newListValue));
                    }
                }
            }

        } else if (attDef.getDataType() == DataType.LONGTEXT) {
            String oldListValue = "";
            if (oldAttribute != null) {
                oldListValue = oldAttribute.getLongTextValue();
            }
            String newListValue = newAttribute.getLongTextValue();
            if (!oldListValue.equals(newListValue)) {
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldListValue, newListValue));
            }
        } else if (attDef.getDataType() == DataType.RICHTEXT) {
            String oldListValue = "";
            if (oldAttribute != null) {
                oldListValue = oldAttribute.getRichTextValue();
            }
            String newListValue = newAttribute.getRichTextValue();
            if (!oldListValue.equals(newListValue)) {
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldListValue, newListValue));
            }
        } else if (attDef.getDataType() == DataType.HYPERLINK) {
            String oldListValue = "";
            if (oldAttribute != null) {
                oldListValue = oldAttribute.getHyperLinkValue();
            }
            String newListValue = newAttribute.getHyperLinkValue();
            if (!oldListValue.equals(newListValue)) {
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldListValue, newListValue));
            }
        } else if (attDef.getDataType() == DataType.CURRENCY) {
            String oldListValue = "";
            if (oldAttribute != null) {
                oldListValue = oldAttribute.getCurrencyValue().toString();
                Currency currency = currencyRepository.findOne(oldAttribute.getCurrencyType());
                oldListValue = currency.getSymbol() + oldListValue;
            }
            String newListValue = newAttribute.getCurrencyValue().toString();
            Currency currency = currencyRepository.findOne(newAttribute.getCurrencyType());
            newListValue = currency.getSymbol() + newListValue;
            if (!oldListValue.equals(newListValue)) {
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldListValue, newListValue));
            }
        } else if (attDef.getDataType() == DataType.OBJECT) {
            changes.add(new ASAttributeChangeDTO(attDef.getDataType().toString(), null, null));
        } else if (attDef.getDataType() == DataType.IMAGE) {
            changes.add(new ASAttributeChangeDTO(attDef.getDataType().toString(), null, null));
        } else if (attDef.getDataType() == DataType.ATTACHMENT) {
            changes.add(new ASAttributeChangeDTO(attDef.getDataType().toString(), null, null));
        } else {
            if (!newValue.equals(oldValue)) {
                changes.add(new ASAttributeChangeDTO(attDef.getName(), oldValue, newValue));
            }
        }
        return changes;
    }

}
