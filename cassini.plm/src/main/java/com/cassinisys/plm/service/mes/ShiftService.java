package com.cassinisys.plm.service.mes;

import com.cassinisys.platform.exceptions.CassiniException;
import com.cassinisys.platform.model.common.Person;
import com.cassinisys.platform.model.core.AutoNumber;
import com.cassinisys.platform.repo.common.PersonRepository;
import com.cassinisys.platform.service.core.AutoNumberService;
import com.cassinisys.platform.service.core.CrudService;
import com.cassinisys.platform.util.JsonUtils;
import com.cassinisys.plm.event.ProjectEvents;
import com.cassinisys.plm.event.ShiftEvents;
import com.cassinisys.plm.filtering.ShiftCriteria;
import com.cassinisys.plm.filtering.ShiftPredicateBuilder;
import com.cassinisys.plm.model.dto.ItemDetailsDto;
import com.cassinisys.plm.model.dto.ShiftPersonListDto;
import com.cassinisys.plm.model.mes.MESShift;
import com.cassinisys.plm.model.mes.MESShiftPerson;
import com.cassinisys.plm.model.mes.QMESShift;
import com.cassinisys.plm.model.plm.PLMObjectType;
import com.cassinisys.plm.model.pm.PLMProject;
import com.cassinisys.plm.model.pm.PLMProjectMember;
import com.cassinisys.plm.repo.mes.MESShiftRepository;
import com.cassinisys.plm.repo.mes.ProductionOrderRepository;
import com.cassinisys.plm.repo.mes.ShiftPersonsRepository;
import com.cassinisys.plm.service.pqm.ObjectFileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cassinisys.plm.model.pqm.dto.ObjectFileDto;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftService implements CrudService<MESShift, Integer> {
    @Autowired
    private MESShiftRepository mesShiftRepo;
    @Autowired
    private ShiftPredicateBuilder shiftPredicateBuilder;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AutoNumberService autoNumberService;
    @Autowired
    private ObjectFileService objectFileService;
    @Autowired
    private ShiftPersonsRepository shiftPersonsRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ProductionOrderRepository productionOrderRepository;


    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesShift,'create')")
    public MESShift create(MESShift mesShift) {
        LocalTime localStartTime = LocalTime.parse(mesShift.getLocalStartTime(),
                DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime localEndTime = LocalTime.parse(mesShift.getLocalEndTime(),
                DateTimeFormatter.ofPattern("HH:mm"));
        mesShift.setStartTime(localStartTime);
        mesShift.setEndTime(localEndTime);
        MESShift existShiftNumber = mesShiftRepo.findByNumber(mesShift.getNumber());
        if (existShiftNumber != null) {
            String message = messageSource.getMessage("shift_number_already_exists", null, "{0} Shift number already exist", LocaleContextHolder.getLocale());
            String result = MessageFormat.format(message + ".", existShiftNumber.getNumber());
            throw new CassiniException(result);
        }
        this.checkShiftTimings(mesShift.getStartTime(), mesShift.getEndTime());
        MESShift existingShift = mesShiftRepo.findByName(mesShift.getName());
        if (existingShift != null) {
            throw new CassiniException(messageSource.getMessage("shift_already_exist", null, "Shift name already exist", LocaleContextHolder.getLocale()));
        } else {
            AutoNumber autoNumber = autoNumberService.getByName("Default Shift Number Source");
            autoNumberService.saveNextNumber(autoNumber.getId(), mesShift.getNumber());
            mesShift = mesShiftRepo.save(mesShift);
        }
        applicationEventPublisher.publishEvent(new ShiftEvents.ShiftCreatedEvent(mesShift));
        return mesShift;

    }

    private void checkShiftTimings(LocalTime startTime, LocalTime endDate) {
        MESShift shift = mesShiftRepo.findBystartTimeAndEndTime(startTime, endDate);
        if (shift != null) {
            throw new CassiniException(messageSource.getMessage("shift_timings_exist", null, "Shift already exist", LocaleContextHolder.getLocale()));
        }

    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#mesShift.id ,'edit')")
    public MESShift update(MESShift mesShift) {
        MESShift oldShift = JsonUtils.cloneEntity(mesShiftRepo.findOne(mesShift.getId()), MESShift.class);
        LocalTime localStartTime = LocalTime.parse(mesShift.getLocalStartTime(),
                DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime localEndTime = LocalTime.parse(mesShift.getLocalEndTime(),
                DateTimeFormatter.ofPattern("HH:mm"));
        mesShift.setStartTime(localStartTime);
        mesShift.setEndTime(localEndTime);
        MESShift shiftExistingTimings = mesShiftRepo.findBystartTimeAndEndTime(mesShift.getStartTime(), mesShift.getEndTime());
        if (shiftExistingTimings != null && !shiftExistingTimings.getId().equals(mesShift.getId())) {
            throw new CassiniException(messageSource.getMessage("shift_timings_exist", null, "Shift already exist", LocaleContextHolder.getLocale()));
        }
        MESShift existingShift = mesShiftRepo.findByName(mesShift.getName());
        if (existingShift != null && !existingShift.getId().equals(mesShift.getId())) {
            throw new CassiniException(messageSource.getMessage("shift_already_exist", null, "Shift name already exist", LocaleContextHolder.getLocale()));
        } else {
            mesShift = mesShiftRepo.save(mesShift);
        }
        applicationEventPublisher.publishEvent(new ShiftEvents.ShiftBasicInfoUpdatedEvent(oldShift, mesShift));
        return mesShift;
    }

    @Override
    @Transactional
    @PreAuthorize("hasPermission(#shiftId,'delete')")
    public void delete(Integer shiftId) {
        mesShiftRepo.delete(shiftId);
    }

    @Override
    @Transactional(readOnly = true)
    @PostAuthorize("hasPermission(returnObject, 'view')")
    public MESShift get(Integer shiftId) {
        MESShift shift = mesShiftRepo.findOne(shiftId);
        shift.setLocalStartTime(shift.getStartTime().toString());
        shift.setLocalEndTime(shift.getEndTime().toString());
        return shift;
    }

    @Override
    @PostFilter("hasPermission(filterObject,'view')")
    public List<MESShift> getAll() {
        return mesShiftRepo.findAll();
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject,'view')")
    public Page<MESShift> getAllShiftsByPageable(Pageable pageable, ShiftCriteria criteria) {
        Predicate predicate = shiftPredicateBuilder.build(criteria, QMESShift.mESShift);
        Page<MESShift> shifts = mesShiftRepo.findAll(predicate, pageable);
        shifts.getContent().forEach(shift -> {

            ObjectFileDto objectFileDto = objectFileService.getObjectFiles(shift.getId(), PLMObjectType.MESOBJECT, false);
            shift.setShiftFiles(objectFileDto.getObjectFiles());
        });
        return shifts;
    }

    @Transactional
    public MESShiftPerson createShiftPerson(Integer id, MESShiftPerson shiftPerson) {
        MESShiftPerson mesShiftPerson = shiftPersonsRepository.save(shiftPerson);
        //applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMemberAddedEvent(project, person));
        return mesShiftPerson;

    }

    @Transactional
    public MESShiftPerson updateShiftPerson(Integer id, MESShiftPerson shiftPerson) {
        MESShiftPerson oldShiftPerson = shiftPersonsRepository.findOne(shiftPerson.getId());
        //applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMemberUpdatedEvent(oldProjectMember, projectMember));
        return shiftPersonsRepository.save(shiftPerson);
    }

    @Transactional
    public List<MESShiftPerson> createShiftPersons(Integer id, List<MESShiftPerson> shiftPersons) {
        List<MESShiftPerson> mesShiftPersons = shiftPersonsRepository.save(shiftPersons);
        //applicationEventPublisher.publishEvent(new ProjectEvents.ProjectMembersAddedEvent(project, persons));
        return mesShiftPersons;

    }

    @Transactional(readOnly = true)
    public Page<MESShiftPerson> getShiftPersons(Integer shiftId, Pageable pageable) {
        if (pageable.getSort() == null) {
            pageable = new PageRequest(pageable.getPageNumber(),
                    pageable.getPageSize(), new Sort(new Sort.Order("id")));
        }
        Page<MESShiftPerson> shiftPersons = shiftPersonsRepository.findByShift(shiftId, pageable);
        return shiftPersons;

    }

    @Transactional
    public void deleteShiftPerson(Integer shift, Integer personId) {
        MESShiftPerson shiftPerson = shiftPersonsRepository.findByShiftAndPerson(shift, personId);
        Integer productionOrders = productionOrderRepository.getProductionOrdersCountByAssignedTo(personId);
        if(productionOrders > 0){
            throw new CassiniException(messageSource.getMessage("shift_person_already_exist_in_production_order", null,
                    "Shift person already exist in productionOrders", LocaleContextHolder.getLocale()));
        }
        shiftPersonsRepository.delete(shiftPerson);
    }

    @Transactional(readOnly = true)
    public ItemDetailsDto getShiftTabCount(Integer id) {
        ItemDetailsDto detailsDto = new ItemDetailsDto();
        detailsDto.setPersons(shiftPersonsRepository.getShiftPersonsCount(id));
        return detailsDto;
    }

    @Transactional(readOnly = true)
    public List<ShiftPersonListDto> getShiftPersonList() {
        List<ShiftPersonListDto> shiftPersonListDtos = new ArrayList<>();
        List<MESShift> shifts = mesShiftRepo.findAll();
        for (MESShift shift: shifts){
            ShiftPersonListDto shiftPersonListDto = new ShiftPersonListDto();
            shiftPersonListDto.setId(shift.getId());
            shiftPersonListDto.setNumber(shift.getNumber());
            shiftPersonListDto.setName(shift.getName());
            shiftPersonListDto.setStartTime(shift.getStartTime());
            shiftPersonListDto.setEndTime(shift.getEndTime());
            List<Integer> shiftPersonIds = shiftPersonsRepository.findByPersonIdsByShift(shift.getId());
            List<Person> persons = personRepository.findByIdIn(shiftPersonIds);
            shiftPersonListDto.setPersons(persons);
            shiftPersonListDtos.add(shiftPersonListDto);
        }
        return shiftPersonListDtos;
    }
}