package datadriven;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ExcelDataDrivenLoop {

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

        System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"/browsers/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(prop.getProperty("url"));
        driver.findElement(By.cssSelector(".language-region-change")).click();
    }

    @Test (enabled = false)
    public void homePageTitleTest(){
        Assert.assertEquals(driver.getTitle(), "United Airlines – Airline Tickets, Travel Deals and Flights");
    }

    @Test
    public void excelDataTest() {
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

        sh1 = workbook.getSheet("sheet1");
        String getCellOne = sh1.getRow(1).getCell(0).getStringCellValue();
        String getCellTwo = sh1.getRow(1).getCell(1).getStringCellValue();
        System.out.println(getCellOne);
        System.out.println(getCellTwo);
    }



    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
