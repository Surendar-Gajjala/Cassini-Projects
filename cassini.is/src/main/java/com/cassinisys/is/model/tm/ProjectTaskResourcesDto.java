package com.cassinisys.is.model.tm;

import com.cassinisys.is.model.pm.ISProjectRole;
import com.cassinisys.is.model.procm.ISMachineItem;
import com.cassinisys.is.model.procm.ISManpowerItem;
import com.cassinisys.is.model.procm.ISMaterialItem;
import com.cassinisys.platform.model.common.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CassiniSystems on 30-01-2020.
 */
public class ProjectTaskResourcesDto {

    List<ISManpowerItem> manpowerItemList = new ArrayList<>();
    List<ISMachineItem> machineItems = new ArrayList<>();
    List<ISMaterialItem> materialItems = new ArrayList<>();
    List<ISProjectRole> projectRoles = new ArrayList<>();
    List<Person> persons = new ArrayList<>();

    public List<ISManpowerItem> getManpowerItemList() {
        return manpowerItemList;
    }

    public void setManpowerItemList(List<ISManpowerItem> manpowerItemList) {
        this.manpowerItemList = manpowerItemList;
    }

    public List<ISMachineItem> getMachineItems() {
        return machineItems;
    }

    public void setMachineItems(List<ISMachineItem> machineItems) {
        this.machineItems = machineItems;
    }

    public List<ISMaterialItem> getMaterialItems() {
        return materialItems;
    }

    public void setMaterialItems(List<ISMaterialItem> materialItems) {
        this.materialItems = materialItems;
    }

    public List<ISProjectRole> getProjectRoles() {
        return projectRoles;
    }

    public void setProjectRoles(List<ISProjectRole> projectRoles) {
        this.projectRoles = projectRoles;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
