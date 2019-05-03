package fr.utbm.gl52.droneSimulator.model;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.io.*;
import java.util.Properties;

public class SpecieManager {

    public static Animal getSpecie(String specie) {
        Properties prop = new Properties();
        InputStream input = null;
        Animal animal = null;

        try {
            input = new FileInputStream("properties/animal/"+specie+".properties");
            prop.load(input);
            animal = new Animal(prop);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return animal;
    }

    public static void saveSpecie(Animal a) {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream("properties/animal/"+a.getSpecie()+".properties");

            prop.setProperty("specie", a.getSpecie());
            prop.setProperty("size", String.valueOf(a.getSize()));
            prop.setProperty("speed", String.valueOf(a.getSpeed()));
            prop.setProperty("visibleDistance", String.valueOf(a.getVisibleDistance()));
            prop.setProperty("visibleAngle", String.valueOf(a.getVisibleAngle()));
            prop.setProperty("carnivorous", String.valueOf(a.isCarnivorous()));
            prop.setProperty("herbivorous", String.valueOf(a.isHerbivorous()));
            prop.setProperty("insectivorous", String.valueOf(a.isInsectivorous()));
            prop.setProperty("terrestrial", String.valueOf(a.isTerrestrial()));
            prop.setProperty("aquatic", String.valueOf(a.isAquatic()));
            prop.setProperty("insect", String.valueOf(a.isInsect()));
            prop.setProperty("fertilityRate", String.valueOf(a.getFertilityRate()));

            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveAllSpecies() {
        Properties prop = new Properties();
        OutputStream output = null;

        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("espece.xls"));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }

            for(int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    try {
                        output = new FileOutputStream("properties/animal/"+row.getCell(0)+".properties");

                        prop.setProperty("specie", new DataFormatter().formatCellValue((row.getCell(0))));
                        prop.setProperty("size", new DataFormatter().formatCellValue((row.getCell(1))));
                        prop.setProperty("speed", new DataFormatter().formatCellValue((row.getCell(2))));
                        prop.setProperty("visibleDistance", new DataFormatter().formatCellValue((row.getCell(3))));
//                        System.out.println(new DataFormatter().formatCellValue((row.getCell(4))));
//                        System.out.println(Integer.parseInt(new DataFormatter().formatCellValue((row.getCell(4)))));
//                        System.out.println(Drone.degreeToRadian(Integer.parseInt(new DataFormatter().formatCellValue((row.getCell(4))))));
                        prop.setProperty("visibleAngle", String.valueOf(Animal.degreeToRadian(Integer.parseInt(new DataFormatter().formatCellValue((row.getCell(4)))))));
                        prop.setProperty("carnivorous", new DataFormatter().formatCellValue((row.getCell(5))));
                        prop.setProperty("herbivorous", new DataFormatter().formatCellValue((row.getCell(6))));
                        prop.setProperty("insectivorous", new DataFormatter().formatCellValue((row.getCell(7))));
                        prop.setProperty("terrestrial", new DataFormatter().formatCellValue((row.getCell(8))));
                        prop.setProperty("aquatic", new DataFormatter().formatCellValue((row.getCell(9))));
                        prop.setProperty("insect", new DataFormatter().formatCellValue((row.getCell(10))));
                        prop.setProperty("fertilityRate", new DataFormatter().formatCellValue((row.getCell(11))));

                        prop.store(output, null);
                    } catch (IOException io) {
                        io.printStackTrace();
                    } finally {
                        if (output != null) {
                            try {
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

}
