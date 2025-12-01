package com.cassinisys.platform.security;

import com.cassinisys.platform.model.dto.ClassDto;
import com.cassinisys.platform.model.dto.EnumDto;
import com.cassinisys.platform.model.dto.PropertiesDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.scanners.Scanner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

@Component
 public class PropertiesLoader {

    public String getAllProperties(ServletContext servletContext) throws Exception {
        Map<String, Collection> json = new HashMap<>();
        json.put("classes", getEntityProperties(servletContext));
        json.put("enums", getEnumProperties(servletContext));
        ObjectMapper Obj = new ObjectMapper();
        return Obj.writeValueAsString(json);
    }

    public List<ClassDto> getEntityProperties(ServletContext servletContext) throws Exception {
        List<ClassDto> entityList = new ArrayList<>();
        entityList.addAll(getEntityValues("com.cassinisys.platform.model", servletContext));
        entityList.addAll(getEntityValues("com.cassinisys.plm.model", servletContext));
        return entityList;
    }

    public List<EnumDto> getEnumProperties(ServletContext servletContext) throws Exception {
        List<EnumDto> enumList = new ArrayList<>();
        enumList.addAll(getEnumValues("com.cassinisys.platform.model", servletContext));
        enumList.addAll(getEnumValues("com.cassinisys.plm.model", servletContext));
        return enumList;
    }

    public List<ClassDto> getEntityValues(String path, ServletContext servletContext) throws Exception {
        Reflections reflections = new Reflections(path);
        Set<Scanner> scanners = reflections.getConfiguration().getScanners();
        List<ClassDto> classDtos = new ArrayList<>();
        for(Scanner s: scanners){
            Collection<String> values = s.getStore().get("javax.persistence.Entity");
            for(String v: values){
                ClassDto classDto = new ClassDto();
                List<PropertiesDto> propertiesDtos = new ArrayList<>();
                Field[] declaredFields =  FieldUtils.getAllFields(Class.forName(v));
                classDto.setClassName(Class.forName(v).getSimpleName());
                for(Field f:declaredFields){
                    String type = null;
                    String name = null;
                    String objectType = null;
                    PropertiesDto propertiesDto = new PropertiesDto();
                    Annotation[] annotations = f.getDeclaredAnnotations();
                    for(Annotation a: annotations){
                        String simpelName = f.getType().getSimpleName();
                        if(a.annotationType().getSimpleName().equals("JoinColumn") || a.annotationType().getSimpleName().equals("Column")
                                || a.annotationType().getSimpleName().equals("Type")){
                            if(a.annotationType().getSimpleName().equals("Type")){
                                type = "Enum";
                                name = f.getName();
                                objectType = f.getType().getSimpleName();
                                if(!objectType.equals("Enum")) {
                                    propertiesDto.setName(name);
                                    propertiesDto.setObjectType(objectType);
                                    propertiesDto.setType(type);
                                    propertiesDtos.add(propertiesDto);
                                }
                                break;
                            } else if(simpelName.equalsIgnoreCase("String") || simpelName.equalsIgnoreCase("Integer") || simpelName.equalsIgnoreCase("boolean")
                                    || simpelName.equalsIgnoreCase("Date") || simpelName.contains("[]") || simpelName.equalsIgnoreCase("double")){
                                type = f.getType().getSimpleName();
                                name = f.getName();
                                if(checkIfPersonType(name,servletContext) != null)
                                    propertiesDto.setType(checkIfPersonType(name, servletContext));
                                else propertiesDto.setType(type);
                                propertiesDto.setName(name);
                                propertiesDto.setObjectType(objectType);
                                propertiesDtos.add(propertiesDto);
                                break;
                            }  else {
                                type = "Object";
                                name = f.getName();
                                objectType = f.getType().getSimpleName();
                                if(!objectType.equals("Enum")) {
                                    propertiesDto.setName(name);
                                    propertiesDto.setObjectType(objectType);
                                    propertiesDto.setType(type);
                                    propertiesDtos.add(propertiesDto);
                                }
                                break;
                            }
                        }
                    }
                }
                classDto.setProperties(propertiesDtos);
                classDtos.add(classDto);
            }
        }
        return classDtos;
    }

    public List<EnumDto> getEnumValues(String path, ServletContext servletContext) throws Exception {
        Reflections reflections = new Reflections(path, new SubTypesScanner(false));
        Set<org.reflections.scanners.Scanner> scanners = reflections.getConfiguration().getScanners();
        List<EnumDto> enumDtos = new ArrayList<>();
        for(Scanner s:scanners){
            Collection<String> values = s.getStore().asMap().get("java.lang.Enum");
            for (String v: values){
                EnumDto enumDto = new EnumDto();
                List<String> enums1 = new ArrayList<>();
                enumDto.setObjectType(Class.forName(v).getSimpleName());
                Object[] enums = Class.forName(v).getEnumConstants();
                for(Object e: enums){
                    enums1.add(e.toString());
                }
                enumDto.setValues(enums1);
                enumDtos.add(enumDto);
            }
        }
        return enumDtos;
    }

    public String checkIfPersonType(String name, ServletContext servletContext) throws IOException {
        File resource = new ClassPathResource("propertyTypes.json").getFile();
        if(resource.exists()){
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(resource, Map.class);
            return map.get(name) != null?map.get(name).toString():null;
        }
        return null;
    }


}
