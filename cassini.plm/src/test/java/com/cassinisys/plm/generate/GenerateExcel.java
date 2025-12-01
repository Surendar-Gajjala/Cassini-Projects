package com.cassinisys.plm.generate;

import com.cassinisys.plm.generate.pojo.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nageshreddy on 25-11-2021.
 */
public class GenerateExcel {

    Integer rows = 25000;
    static final String excelLocation = "C:\\savedexcel\\obj\\";

    @Test
    public void generateSuppliers() {
        String fileNamePath = excelLocation + "suppliers_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Suppliers());
        }

        Object[] objects = GenerateUtils.getColumns("Suppliers");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Suppliers.class);
    }

    @Test
    public void generateSpecifications() {
        String fileNamePath = excelLocation + "specifications";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Specifications());
        }

        Object[] objects = GenerateUtils.getColumns("Specifications");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Specifications.class);
    }

    @Test
    public void generateMfrPart() {
        String fileNamePath = excelLocation + "mfr_part_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();

        for (int i = 0; i < rows; i++) {
            excelObjects.add(new ManufacturePart());
        }

        Object[] objects = GenerateUtils.getColumns("Manufacturer and Manufacturer Parts");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, ManufacturePart.class);
    }

    @Test
    public void generateSubstances() {
        GenerateUtils.substanceId = 1;
        String fileNamePath = excelLocation + "substances_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Substances());
        }
        Object[] objects = GenerateUtils.getColumns("Substances");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Substances.class);
    }

    @Test
    public void generateCustomers() {
        String fileNamePath = excelLocation + "customers_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        int n = 20;
        String name;

        for (int i = 0; i < rows; i++) {
            String number = GenerateUtils.getRandomPhone();
            name = GenerateUtils.generateRandomString(n);
            excelObjects.add(new Customers(name, number, "Address-" + name, name + "@gmail.com", name, name.substring(1, 6),
                    name.substring(7, 15), name.substring(1, 6) + "@gmail.com", number));
        }

        Object[] objs = GenerateUtils.getColumns("Customers");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objs, Customers.class);
    }

    @Test
    public void generateItems() {
        String fileNamePath = excelLocation + "items_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        fileNamePath = fileNamePath.trim();
        List<BaseClass> excelObjects = new ArrayList();
        int n = 20;
        String name;

        for (int i = 0; i < rows; i++) {
            Integer qty = GenerateUtils.getRandomIntegerBetweenRange(1, 8);
            String number = GenerateUtils.getItemNumber(qty);

            name = GenerateUtils.generateRandomString(n);
            String typePath = "Part";
            if (qty == 1) typePath = "Assembly";

            excelObjects.add(new Items(number, typePath, "Part-" + name, name + "-Description", typePath, typePath,
                    "NORMAL", "-", "In Work", "EA", "Make"));
        }

        Object[] objs = GenerateUtils.getColumns("Items");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objs, Items.class);
    }

    @Test
    public void generateBom() {
        String fileNamePath = excelLocation + "bom_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        Integer level = 20;
        fileNamePath = fileNamePath.trim();
        List<BaseClass> excelObjects = new ArrayList();
        int n = 20;
        String name;
        Map<String, String> typesMap = new ConcurrentHashMap<>();
        typesMap.put("C", "Capasitors");
        typesMap.put("R", "Resistor");
        typesMap.put("I", "Inductor");
        typesMap.put("CON", "Connector");
        Map<String, Integer> typesCount = new ConcurrentHashMap<>();
        typesCount.put("C", 1);
        typesCount.put("R", 1);
        typesCount.put("I", 1);
        typesCount.put("CON", 1);
        List<String> types = new ArrayList<>();
        types.add("C");
        types.add("R");
        types.add("I");
        types.add("CON");
        for (int i = 0; i < rows; i++) {
            name = GenerateUtils.generateRandomString(n);

            Integer val = GenerateUtils.getRandomIntegerBetweenRange(1, 8);
            String number = GenerateUtils.getItemNumber(val, i, level);
            String type = GenerateUtils.getType(val, i, level);
            if (excelObjects.size() < level) {
                excelObjects.add(new Bom(GenerateUtils.convertString(i), number, type, name, type.toUpperCase(), type, "1", name + "refDes", name + "notes"));
            } else {
                String qty = GenerateUtils.getRandomIntegerBetweenRangeToString(1, 50);
                String path1 = GenerateUtils.getTypePath(types);
                Integer count = typesCount.get(path1);
                String refDes = path1 + count++;
                for (int j = 1; j < Integer.parseInt(qty); j++) {
                    refDes = refDes + "," + path1 + count++;
                }
                typesCount.put(path1, count);

                excelObjects.add(new Bom(GenerateUtils.getRandomIntegerBetweenRangeToString(1, (level - 1)), number, type, name, type.toUpperCase(), type, qty, name + "refDes", name + "notes"));
            }
        }
        Object[] objects = GenerateUtils.getColumns("BOM");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Bom.class);
    }

    @Test
    public void generatePlants() {
        GenerateUtils.plantId = 1;
        String fileNamePath = excelLocation + "Plants_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Plants());
        }
        Object[] objects = GenerateUtils.getColumns("Plants");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Plants.class);
    }

    @Test
    public void generateAssemblyLines() {
        String fileNamePath = excelLocation + "AssemblyLines_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new AssemblyLine());
        }
        Object[] objects = GenerateUtils.getColumns("Assembly Lines");

        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, AssemblyLine.class);
    }

    @Test
    public void generateWorkCenters() {
        GenerateUtils.plantId = 1;
        GenerateUtils.assyId = 1;
        String fileNamePath = excelLocation + "WorkCenters_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Workcenters());
        }
        Object[] objects = GenerateUtils.getColumns("Work Centers");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Workcenters.class);
    }

    @Test
    public void generateEquipments() {
        GenerateUtils.meterId = 1;
        String fileNamePath = excelLocation + "Equipments_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Equipments());
        }
        Object[] objects = GenerateUtils.getColumns("Equipments");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Equipments.class);
    }

    @Test
    public void generateInstruments() {
        GenerateUtils.meterId = 1;
        String fileNamePath = excelLocation + "Instruments_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Instruments());
        }
        Object[] objects = GenerateUtils.getColumns("Instruments");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Instruments.class);
    }

    @Test
    public void generateMachines() {
        GenerateUtils.wcId = 1;
        GenerateUtils.plantId = 1;
        String fileNamePath = excelLocation + "Machines_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Machines());
        }
        Object[] objects = GenerateUtils.getColumns("Machines");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Machines.class);
    }

    @Test
    public void generateManpowers() {
        String fileNamePath = excelLocation + "Manpowers_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        int n = 20;
        String name;
        for (int i = 0; i < rows; i++) {
            String type = GenerateUtils.generateRandomString(6);
            String manNumber = GenerateUtils.generateSpecNumber("MAN-");
            String typePath = "Manpower/" + GenerateUtils.generateRandomString(6);
            String manName = GenerateUtils.generateRandomString(20);
            String description = GenerateUtils.generateRandomString(20);
            name = GenerateUtils.generateRandomString(n);
            String phone = GenerateUtils.getRandomPhone();

            excelObjects.add(new Manpowers(type, manNumber, typePath, manName, description, "Yes", name,
                    name.substring(1, 6), phone, name + "@gmail.com"));
        }
        Object[] objects = GenerateUtils.getColumns("Manpowers");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Manpowers.class);
    }

    @Test
    public void generateTools() {
        GenerateUtils.plantId = 1;
        String fileNamePath = excelLocation + "Tools_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Tools());
        }
        Object[] objects = GenerateUtils.getColumns("Tools");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Tools.class);

    }

    @Test
    public void generateMaterials() {
        String fileNamePath = excelLocation + "Materials_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Materials());
        }
        Object[] objects = GenerateUtils.getColumns("Materials");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Materials.class);

    }

    @Test
    public void generateShifts() {
        String fileNamePath = excelLocation + "Shifts_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Shifts());
        }
        Object[] objects = GenerateUtils.getColumns("Shifts");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Shifts.class);

    }

    @Test
    public void generateJigFixtures() {
        GenerateUtils.meterId = 1;
        String fileNamePath = excelLocation + "JigFixtures_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new JigFixtures());
        }
        Object[] objects = GenerateUtils.getColumns("Jigs&Fixtures");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, JigFixtures.class);

    }

    @Test
    public void generateOperations() {
        String fileNamePath = excelLocation + "Operations_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Operations());
        }
        Object[] objects = GenerateUtils.getColumns("Operations");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Operations.class);

    }

    @Test
    public void generateAssets() {
        String fileNamePath = excelLocation + "Assets_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Assets());
        }
        Object[] objects = GenerateUtils.getColumns("Assets");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Assets.class);
    }

    @Test
    public void generateMeters() {
        GenerateUtils.meterId = 1;
        rows = rows + 100;
        String fileNamePath = excelLocation + "Meters_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new Meters());
        }
        Object[] objects = GenerateUtils.getColumns("Meters");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, Meters.class);

    }

    @Test
    public void generateSpareParts() {
        String fileNamePath = excelLocation + "SpareParts_";
        fileNamePath = (fileNamePath + rows.toString() + ".xlsx").trim();
        List<BaseClass> excelObjects = new ArrayList();
        for (int i = 0; i < rows; i++) {
            excelObjects.add(new SpareParts());
        }
        Object[] objects = GenerateUtils.getColumns("SpareParts");
        GenerateUtils.writeInExcel(excelObjects, fileNamePath, objects, SpareParts.class);

    }

}
