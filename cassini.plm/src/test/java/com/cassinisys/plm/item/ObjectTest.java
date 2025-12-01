/*


import com.cassini.plugin.salesforce.dto.ClassDto;
import com.cassini.plugin.salesforce.dto.PropertiesDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ObjectTest {

    @Test
    public void testGetObject() throws Exception {
       */
/* CassiniObject o = objectRepository.findByIdAndObjectType(119, ObjectType.valueOf("CHANGE"));
        System.out.println(o);*//*

        Reflections reflections = new Reflections("com.cassinisys.platform.model");
        Set<Scanner> scanners = reflections.getConfiguration().getScanners();
        for(Scanner s: scanners){
            Collection<String> values = s.getStore().get("javax.persistence.Entity");
            List<ClassDto> classDtos = new ArrayList<>();
            for(String v: values){
                ClassDto classDto = new ClassDto();
                List<PropertiesDto> propertiesDtos = new ArrayList<>();
                Field[] declaredFields = Class.forName(v).getDeclaredFields();
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
                                propertiesDto.setName(name);
                                propertiesDto.setObjectType(objectType);
                                propertiesDto.setType(type);
                                propertiesDtos.add(propertiesDto);
                                break;
                            } else if(simpelName.equalsIgnoreCase("String") || simpelName.equalsIgnoreCase("Integer") || simpelName.equalsIgnoreCase("boolean")
                                    || simpelName.equalsIgnoreCase("Date") || simpelName.contains("[]") || simpelName.equalsIgnoreCase("double")){
                                type = f.getType().getSimpleName();
                                name = f.getName();
                                propertiesDto.setName(name);
                                propertiesDto.setObjectType(objectType);
                                propertiesDto.setType(type);
                                propertiesDtos.add(propertiesDto);
                                break;
                            }  else {
                                type = "Object";
                                name = f.getName();
                                objectType = f.getType().getSimpleName();
                                propertiesDto.setName(name);
                                propertiesDto.setObjectType(objectType);
                                propertiesDto.setType(type);
                                propertiesDtos.add(propertiesDto);
                                break;
                            }
                        }
                    }
                }
                classDto.setProperties(propertiesDtos);
                classDtos.add(classDto);
            }
            ObjectMapper Obj = new ObjectMapper();
            System.out.println(Obj.writeValueAsString(classDtos));
        }
    }

    @Test
    public void testObject() throws Exception {
       */
/* CassiniObject o = objectRepository.findByIdAndObjectType(119, ObjectType.valueOf("CHANGE"));
        System.out.println(o);*//*

        Reflections reflections = new Reflections("com.cassinisys.plm.model", new SubTypesScanner(false));
        Set<Scanner> scanners = reflections.getConfiguration().getScanners();
        for(Scanner s:scanners){
            Collection<String> values = s.getStore().asMap().get("java.io.Serializable");
            for (String v: values){
                if(!(v.contains("dto") || v.contains("Dto")))
                    System.out.println(v);
            }
        }
    }

}
*/
