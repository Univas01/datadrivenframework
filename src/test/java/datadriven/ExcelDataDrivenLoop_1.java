package datadriven;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ExcelDataDrivenLoop_1 {

    public static WebDriver driver;
    public static Properties prop;
    public Workbook workbook;
    public Sheet sh2;

    @Test
    public void excelDataTest() {
        try {
            File dataFile = new File(System.getProperty("user.dir") + "/UnitedAirlineData.xlsx");
            FileInputStream inputStream = new FileInputStream(dataFile);
            BufferedInputStream bufferStream = new BufferedInputStream(inputStream);
            workbook = WorkbookFactory.create(bufferStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        // Create an object for the sheet
        sh2 = workbook.getSheet("sheet2");

        // Count number of rows in excel sheet
        int lastRow = sh2.getLastRowNum();
        System.out.println("Total number of Rows is :" + lastRow);

        for(int i = 1; i <= lastRow; i++){
            int j = 0;

            System.out.println("======================================");
            String from = sh2.getRow(i).getCell(j).getStringCellValue();
            String to = sh2.getRow(i).getCell(++j).getStringCellValue();
            String tripType = sh2.getRow(i).getCell(++j).getStringCellValue();
            String departDate = sh2.getRow(i).getCell(++j).getStringCellValue();
            String returnDate = sh2.getRow(i).getCell(++j).getStringCellValue();
            int noOfPassenger = (int) sh2.getRow(i).getCell(++j).getNumericCellValue();
            String flightClass = sh2.getRow(i).getCell(++j).getStringCellValue();

            /*System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/browsers/chromedriver");
            driver = new ChromeDriver();*/

            System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/browsers/geckodriver");
            driver = new FirefoxDriver();

            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.get("https://www.united.com/ual/en/us/");
            driver.findElement(By.cssSelector(".language-region-change")).click();

            System.out.println("from : "+from);
            System.out.println("to : "+to);
            System.out.println("tripType : "+tripType);
            System.out.println("departDate : "+departDate);
            System.out.println("returnDate : "+returnDate);
            System.out.println("noOfPassenger : "+noOfPassenger);
            System.out.println("flightClass : "+flightClass);

            WebElement departFrom_ftf = driver.findElement(By.id("Origin"));
            departFrom_ftf.clear();
            departFrom_ftf.sendKeys(from);

            WebElement destination_ftt = driver.findElement(By.id("Destination"));
            destination_ftt.clear();
            destination_ftt.sendKeys(to);

            WebElement tripType_radioBtn = driver.findElement(By.xpath("//input[@id=\'"+tripType+"\']"));
            tripType_radioBtn.click();

            WebElement departDateField = driver.findElement(By.xpath("//input[@id='DepartDate']"));
            String departDateValue = departDate;
            departDateField.clear();
            selectDateByJS(departDateField,departDateValue);

            WebElement returnDateField = driver.findElement(By.xpath("//input[@id='ReturnDate']"));
            String returnDateValue = returnDate;
            returnDateField.clear();
            selectDateByJS(returnDateField,returnDateValue);

            WebElement noOfPassengerDDM = driver.findElement(By.xpath("//a[@id='travelers-selector']/child::*/child::span[2]"));
            noOfPassengerDDM.click();
            WebElement selectNoOfPass = driver.findElement(By.xpath("//input[@name='NumOfAdults']"));
            selectNoOfPass.clear();
            sendKeys(driver, selectNoOfPass, 10, String.valueOf(noOfPassenger));

            WebElement selectCabinType = driver.findElement(By.id("cabinType"));
            Select selectCabinTypes = new Select(selectCabinType);
            selectCabinTypes.selectByVisibleText(flightClass);

            WebElement submitFlightBookingBtn = driver.findElement(By.xpath("//button[@id='flightBookingSubmit']"));
            clickMethod(driver, 10, submitFlightBookingBtn);

            driver.quit();
        }
    }

    public static void selectDateByJS(WebElement element, String dateValue) {
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].setAttribute('value','"+dateValue+"');", element);
    }

    public static void clickMethod(WebDriver driver, long timeout, WebElement element) {
        WebDriverWait one = new WebDriverWait(driver, timeout);
        one.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void sendKeys(WebDriver driver, WebElement element, int timeout, String value) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(value);
    }

    @AfterMethod (enabled =  false)
    public void tearDown(){
        driver.quit();
    }
}