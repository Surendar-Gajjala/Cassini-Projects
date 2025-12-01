package com.cassinisys.plm.filtering;

import com.cassinisys.platform.filtering.Criteria;

/**
 * Created by Suresh CassiniPLM on 27-10-2020.
 */
public class PlantCriteria extends Criteria {
    Integer plantType;
    String name;
    String number;
    String description;
    String searchQuery;
    String address;
    String city;
    String country;
    String postalCode;
    String phoneNumber;
    String mobileNumber;
    String faxAddress;
    String email;
    Integer plantPerson;
    String notes;

    public Integer getPlantType() {
        return plantType;
    }

    public void setPlantType(Integer plantType) {
        this.plantType = plantType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFaxAddress() {
        return faxAddress;
    }

    public void setFaxAddress(String faxAddress) {
        this.faxAddress = faxAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPlantPerson() {
        return plantPerson;
    }

    public void setPlantPerson(Integer plantPerson) {
        this.plantPerson = plantPerson;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}