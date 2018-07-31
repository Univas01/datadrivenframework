package datadriven;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class DateColFromExcel {

    public static WebDriver driver;
    public static Properties prop;
    public Workbook workbook;
    public Sheet sh1;

    @BeforeMethod
    public static void setUp(){
        try{
           prop  = new Properties();
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/resources/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void excelDataTest() throws ParseException {
        try{
            File dataFile = new File(System.getProperty("user.dir")+"/UnitedAirlineData.xlsx");
            FileInputStream inputStream = new FileInputStream(dataFile);
            BufferedInputStream bufferStream = new BufferedInputStream(inputStream);
            workbook = WorkbookFactory.create(bufferStream);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (InvalidFormatException e){
            e.printStackTrace();
        }

        // Create an object for the sheet
        sh1 = workbook.getSheet("sheet1");

        // Count number of rows in excel sheet
        int lastRow = sh1.getLastRowNum();
        System.out.println("Total number of Rows is :"+lastRow);

        // for loop to automate all the test scenarios
        for(int i = 1; i <= lastRow; i++){

            // Cell variable
            int j = 0;

            // Create variables
            String from = sh1.getRow(i).getCell(j).getStringCellValue();
            String to = sh1.getRow(i).getCell(++j).getStringCellValue();
            String tripType = sh1.getRow(i).getCell(++j).getStringCellValue();

            double departDateNumber = (double) sh1.getRow(i).getCell(++j).getNumericCellValue();
            Date departDate = DateUtil.getJavaDate(departDateNumber);
            SimpleDateFormat formatDepartDate = new SimpleDateFormat("dd/MM/yyyy");

            double returnDateNumber = (double) sh1.getRow(i).getCell(++j).getNumericCellValue();
            Date returnDate = DateUtil.getJavaDate(returnDateNumber);
            SimpleDateFormat formatReturnDate = new SimpleDateFormat("dd/MM/yyyy");

            int noOfPassengers = (int)sh1.getRow(i).getCell(++j).getNumericCellValue();
            String flightclass = sh1.getRow(i).getCell(++j).getStringCellValue();

            System.out.println("from : "+ from);
            System.out.println("to : "+ to);
            System.out.println("triptype : "+tripType);
            System.out.println("departDate :"+formatDepartDate.format(departDate));
            System.out.println("returnDate :"+formatReturnDate.format(returnDate));
            System.out.println("noOfPassengers :"+noOfPassengers);
            System.out.println("flightclass : "+flightclass);
        }
    }



    @AfterMethod (enabled = false)
    public void tearDown(){
        driver.quit();
    }
}
