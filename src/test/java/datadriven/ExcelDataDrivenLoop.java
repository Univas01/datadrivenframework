package datadriven;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ExcelDataDrivenLoop {

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

            System.out.println("from : "+from);
            System.out.println("to : "+to);
            System.out.println("tripType : "+tripType);
            System.out.println("departDate : "+departDate);
            System.out.println("returnDate : "+returnDate);
            System.out.println("noOfPassenger : "+noOfPassenger);
            System.out.println("flightClass : "+flightClass);

            try{
                prop  = new Properties();
                FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/resources/config.properties");
                prop.load(ip);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            String browserName = prop.getProperty("browser");

            if(browserName.equalsIgnoreCase("chrome")){
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/browsers/chromedriver");
                driver = new ChromeDriver();
            } else if(browserName.equalsIgnoreCase("firefox")){
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/browsers/geckodriver");
                driver = new FirefoxDriver();
            } else if(browserName.equalsIgnoreCase("safari")){
                driver = new SafariDriver();
            }

            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.get(prop.getProperty("url"));

            WebElement btn = driver.findElement(By.cssSelector(".language-region-change"));

            if(btn.isEnabled()){
                btn.click();
            }

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
            selectNoOfPass.sendKeys(Keys.BACK_SPACE);
            sendKeys(driver, selectNoOfPass, 10, String.valueOf(noOfPassenger));
            driver.findElement(By.xpath("//div[@id='travelers-select']/descendant::button")).click();

            WebElement selectCabinType = driver.findElement(By.id("cabinType"));
            Select selectCabinTypes = new Select(selectCabinType);
            selectCabinTypes.selectByVisibleText(flightClass);

            WebElement submitFlightBookingBtn = driver.findElement(By.xpath("//button[@id='flightBookingSubmit']"));
            clickOnJSObject(submitFlightBookingBtn);

            String title = driver.getTitle();
            Assert.assertEquals(title,"Flight Search Results | United Airlines");
            System.out.println(title);

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

    public static void clickOnJSObject(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].click();", element);
    }
}