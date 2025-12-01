package com.cassinisys.platform.service.activitystream;

import com.cassinisys.platform.filtering.ActivityStreamCriteria;
import com.cassinisys.platform.filtering.ActivityStreamPredicateBuilder;
import com.cassinisys.platform.filtering.SessionCriteria;
import com.cassinisys.platform.filtering.SessionPredicateBuilder;
import com.cassinisys.platform.model.activitystream.*;
import com.cassinisys.platform.model.core.QSession;
import com.cassinisys.platform.model.core.Session;
import com.cassinisys.platform.repo.activitystream.ActivityStreamObjectRepository;
import com.cassinisys.platform.repo.activitystream.ActivityStreamRepository;
import com.cassinisys.platform.repo.security.SessionRepository;
import com.cassinisys.platform.service.security.SessionWrapper;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivityStreamService {
    private ActivityStreamObjectRepository activityStreamObjectRepository;
    private ActivityStreamRepository activityStreamRepository;
    private ActivityStreamConverterRegistry activityStreamConverterRegistry;
    private SessionRepository sessionRepository;
    private SessionPredicateBuilder sessionPredicateBuilder;
    @Autowired
    private SessionWrapper sessionWrapper;

    private ActivityStreamPredicateBuilder activityStreamPredicateBuilder;

    public ActivityStreamService(ActivityStreamObjectRepository activityStreamObjectRepository,
                                 ActivityStreamRepository activityStreamRepository, SessionRepository sessionRepository,
                                 ActivityStreamConverterRegistry activityStreamConverterRegistry,
                                 ActivityStreamPredicateBuilder activityStreamPredicateBuilder,
                                 SessionPredicateBuilder sessionPredicateBuilder) {
        this.activityStreamObjectRepository = activityStreamObjectRepository;
        this.activityStreamRepository = activityStreamRepository;
        this.activityStreamConverterRegistry = activityStreamConverterRegistry;
        this.sessionRepository = sessionRepository;
        this.activityStreamPredicateBuilder = activityStreamPredicateBuilder;
        this.sessionPredicateBuilder = sessionPredicateBuilder;
    }

    public ActivityStream create(ActivityStream activityStream) {
        return activityStreamRepository.save(activityStream);
    }

    @Transactional(readOnly = true)
    public Page<ActivityStream> getActivityStream(Pageable page) {
        Page<ActivityStream> results = activityStreamRepository.findAll(page);
        convertActivityStreamToString(results.getContent());

        return results;
    }

    @Transactional(readOnly = true)
    public Page<ActivityStream> getObjectActivityStream(Integer objectId, Pageable page, ActivityStreamCriteria criteria) {
        Predicate predicate = activityStreamPredicateBuilder.build(criteria, QActivityStream.activityStream);
        Page<ActivityStream> results = activityStreamRepository.findAll(predicate, page);
        convertActivityStreamToString(results.getContent());

        return results;
    }

    public Page<ActivityStream> getActivityStreams(Pageable page, ActivityStreamCriteria criteria) {
        Predicate predicate = activityStreamPredicateBuilder.build(criteria, QActivityStream.activityStream);
        Page<ActivityStream> results = activityStreamRepository.findAll(predicate, page);
        convertActivityStreamToString(results.getContent());

        return results;
    }

    private void convertActivityStreamToString(List<ActivityStream> activityStreams) {
        activityStreams.forEach(a -> {
            try {
                String conS = a.getConverter();
                if (conS != null) {
                    ActivityStreamConverter converter = activityStreamConverterRegistry.getConverter(conS);
                    if (converter != null) {
                        a.setSummary(converter.convertToString(a));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional(readOnly = true)
    public ActivityStreamDto getObjectActivityStreamByDate(Integer objectId, Pageable pageable, ActivityStreamCriteria criteria) {
        ActivityStreamDto historyDto = new ActivityStreamDto();

        Map<String, List<ActivityStream>> historyMap = new LinkedHashMap<>();


        Page<ActivityStream> activityStreams = getObjectActivityStream(objectId, pageable, criteria);
        for (ActivityStream history : activityStreams.getContent()) {
            DateFormat df = new SimpleDateFormat(sessionWrapper.getSession().getPreferredShortDateFormat());
            String createdDate = df.format(history.getTimestamp());
            if (history.getSession() != null) {
                history.setSessionObject(sessionRepository.findBySessionId(history.getSession()));
            }
            List<ActivityStream> historyList = historyMap.containsKey(createdDate) ? historyMap.get(createdDate) : new LinkedList<>();
            historyList.add(history);
            historyMap.put(createdDate, historyList);
        }
        historyDto.setFirst(activityStreams.isFirst());
        historyDto.setLast(activityStreams.isLast());
        historyDto.setHistories(historyMap);
        return historyDto;
    }

    @Transactional(readOnly = true)
    public ActivityStreamDto getAllActivityStream(Pageable pageable, ActivityStreamCriteria criteria) {
        ActivityStreamDto historyDto = new ActivityStreamDto();

        Map<String, List<ActivityStream>> historyMap = new LinkedHashMap<>();


        Page<ActivityStream> activityStreams = getActivityStreams(pageable, criteria);
        for (ActivityStream history : activityStreams.getContent()) {

            DateFormat df = new SimpleDateFormat(sessionWrapper.getSession().getPreferredShortDateFormat());

            String createdDate = df.format(history.getTimestamp());
            if (history.getSession() != null) {
                history.setSessionObject(sessionRepository.findBySessionId(history.getSession()));
            }
            List<ActivityStream> historyList = historyMap.containsKey(createdDate) ? historyMap.get(createdDate) : new LinkedList<>();
            historyList.add(history);
            historyMap.put(createdDate, historyList);
        }
        historyDto.setFirst(activityStreams.isFirst());
        historyDto.setLast(activityStreams.isLast());
        historyDto.setHistories(historyMap);
        return historyDto;
    }

    @Transactional(readOnly = true)
    public ActivityStreamSessionsDto getAllActivityStreamSessions(Pageable pageable, SessionCriteria criteria) {

        ActivityStreamSessionsDto streamSessionsDto = new ActivityStreamSessionsDto();
        Predicate predicate = sessionPredicateBuilder.build(criteria, QSession.session);
        Page<Session> sessions = sessionRepository.findAll(predicate, pageable);

        for (Session session : sessions.getContent()) {
            SessionsDto sessionDto = new SessionsDto();
            sessionDto.setSession(session);
            List<ActivityStream> activityStreams = activityStreamRepository.findBySessionOrderByTimestampDesc(session.getSessionId());
            convertActivityStreamToString(activityStreams);
            sessionDto.getActivityStreams().addAll(activityStreams);
            streamSessionsDto.getSessionDtoList().add(sessionDto);
        }
        streamSessionsDto.setFirst(sessions.isFirst());
        streamSessionsDto.setLast(sessions.isLast());

        return streamSessionsDto;
    }

    @Transactional(readOnly = true)
    public List<String> getActivityStreamObjectTypes() {
        List<String> objectTypes = new LinkedList<>();
        List<String> strings = activityStreamObjectRepository.findObjectTypesDistinct();
        strings.forEach(string -> {
            if (objectTypes.indexOf(string) == -1) {
                objectTypes.add(string);
            }
        });
        return objectTypes;
    }

    @Transactional(readOnly = true)
    public List<String> getActivityStreamObjectTypeActionsByIds(List<Integer> objectIds, String objectType) {
        List<String> objectTypes = new LinkedList<>();
        String converter = "";
        if (objectType.equals("ITEM")) {
            converter = "plm.items";
        }

        List<String> actions = activityStreamRepository.findByObjectTypeActionsIds(objectIds, converter);
        actions.forEach(action -> {
            if (objectType.equals("ITEM")) {
                String[] strings = action.split("\\.");
                if (strings.length > 1 && objectTypes.indexOf(strings[2]) == -1) {
                    String actionName = strings[2];
                    if(!actionName.equals("revision") && !actionName.equals("create")){
                        objectTypes.add(actionName);
                    }
                }
            }
        });
        return objectTypes;
    }

    @Transactional(readOnly = true)
    public List<String> getActivityStreamObjectTypeActions(Integer objectId, String objectType) {
        List<String> objectTypes = new LinkedList<>();
        String converter = "";
        if (objectType.equals("PROJECT")) {
            converter = "plm.project";
        } else if (objectType.equals("PROGRAM")) {
            converter = "plm.program";
        } else if (objectType.equals("PLANT")) {
            converter = "plm.mes.plant";
        } else if (objectType.equals("ASSEMBLYLINE")) {
            converter = "plm.mes.assemblyline";
        } else if (objectType.equals("MACHINE")) {
            converter = "plm.mes.machine";
        } else if (objectType.equals("EQUIPMENT")) {
            converter = "plm.mes.equipment";
        } else if (objectType.equals("SHIFT")) {
            converter = "plm.mes.shift";
        } else if (objectType.equals("MATERIAL")) {
            converter = "plm.mes.material";
        } else if (objectType.equals("JIGFIXTURE")) {
            converter = "plm.mes.jigfixture";
        } else if (objectType.equals("TOOL")) {
            converter = "plm.mes.tool";
        } else if (objectType.equals("OPERATION")) {
            converter = "plm.mes.operation";
        } else if (objectType.equals("WORKCENTER")) {
            converter = "plm.mes.workcenter";
        } else if (objectType.equals("INSTRUMENT")) {
            converter = "plm.mes.instrument";
        } else if (objectType.equals("MANPOWER")) {
            converter = "plm.mes.manpower";
        } else if (objectType.equals("MBOM")) {
            converter = "plm.mes.mbom";
        } else if (objectType.equals("BOP")) {
            converter = "plm.mes.bop";
        } else if (objectType.equals("MANUFACTURER")) {
            converter = "plm.oem.manufacturer";
        } else if (objectType.equals("MANUFACTURERPART")) {
            converter = "plm.oem.manufacturerPart";
        } else if (objectType.equals("SUPPLIER")) {
            converter = "plm.oem.supplier";
        } else if (objectType.equals("ASSET")) {
            converter = "plm.mro.mroasset";
        } else if (objectType.equals("METER")) {
            converter = "plm.mro.mrometer";
        } else if (objectType.equals("SPAREPART")) {
            converter = "plm.mro.mrosparepart";
        } else if (objectType.equals("MAINTENANCEPLAN")) {
            converter = "plm.mro.mromaintenanceplan";
        } else if (objectType.equals("WORKORDER")) {
            converter = "plm.mro.mroworkorder";
        } else if (objectType.equals("WORKREQUEST")) {
            converter = "plm.mro.mroworkrequest";
        } else if (objectType.equals("PPAP")) {
            converter = "plm.quality.ppap";
        } else if (objectType.equals("SUPPLIERAUDIT")) {
            converter = "plm.quality.supplierAudit";
        } else if (objectType.equals("QCR")) {
            converter = "plm.quality.qcr";
        } else if (objectType.equals("NCR")) {
            converter = "plm.quality.ncr";
        } else if (objectType.equals("PROBLEMREPORT")) {
            converter = "plm.quality.problemReport";
        } else if (objectType.equals("INSPECTION")) {
            converter = "plm.quality.inspection";
        } else if (objectType.equals("INSPECTIONPLAN")) {
            converter = "plm.quality.plan";
        } else if (objectType.equals("ECR")) {
            converter = "plm.change.ecr";
        } else if (objectType.equals("ECO")) {
            converter = "plm.change.eco";
        } else if (objectType.equals("DCR")) {
            converter = "plm.change.dcr";
        } else if (objectType.equals("DCO")) {
            converter = "plm.change.dco";
        } else if (objectType.equals("MCO")) {
            converter = "plm.change.mco";
        } else if (objectType.equals("VARIANCE")) {
            converter = "plm.change.variance";
        } else if (objectType.equals("PLMNPR")) {
            converter = "plm.npr";
        }


        List<String> actions = activityStreamRepository.findByObjectTypeActions(objectId, converter);
        actions.forEach(action -> {
            if (objectType.equals("PROJECT") || objectType.equals("PROGRAM") || objectType.equals("PLMNPR")) {
                String[] strings = action.split("\\.");
                if (strings.length > 1 && objectTypes.indexOf(strings[2]) == -1) {
                    String actionName = strings[2];
                    if(!actionName.equals("create")){
                        objectTypes.add(actionName);
                    }
                }
            } else if (objectType.equals("PLANT") || objectType.equals("ASSEMBLYLINE") || objectType.equals("MACHINE")
                    || objectType.equals("EQUIPMENT") || objectType.equals("JIGFIXTURE") || objectType.equals("TOOL")
                    || objectType.equals("OPERATION") || objectType.equals("SHIFT") || objectType.equals("MATERIAL")
                    || objectType.equals("WORKCENTER") || objectType.equals("INSTRUMENT") || objectType.equals("MANPOWER")
                    || objectType.equals("MBOM") || objectType.equals("BOP") || objectType.equals("MANUFACTURER")
                    || objectType.equals("MANUFACTURERPART") || objectType.equals("SUPPLIER") || objectType.equals("ASSET")
                    || objectType.equals("METER") || objectType.equals("SPAREPART") || objectType.equals("MAINTENANCEPLAN")
                    || objectType.equals("WORKORDER") || objectType.equals("WORKREQUEST") || objectType.equals("PPAP")
                    || objectType.equals("SUPPLIERAUDIT") || objectType.equals("QCR") || objectType.equals("NCR")
                    || objectType.equals("PROBLEMREPORT") || objectType.equals("INSPECTION") || objectType.equals("INSPECTIONPLAN")
                    || objectType.equals("ECR") || objectType.equals("ECO") || objectType.equals("DCR") || objectType.equals("DCO")
                    || objectType.equals("MCO") || objectType.equals("VARIANCE")) {
                String[] strings = action.split("\\.");
                if (strings.length > 2 && objectTypes.indexOf(strings[3]) == -1) {
                    String actionName = strings[3];
                    if(!actionName.equals("revision") && !actionName.equals("create")){
                        objectTypes.add(actionName);
                    }
                }
            }
        });
        return objectTypes;
    }
}
