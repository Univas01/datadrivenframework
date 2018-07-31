package test.java.datadriven;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DBDataDrivenLoop {

    private final static String DBURL = "jdbc:postgresql://localhost:5432/company";
    private final static String USERNAME = "seleniumguru";
    private final static String PASSWORD = "Computer1";
    public static WebDriver driver;
    public static Properties prop;
    public ResultSet resultSet;

    @Test
    public void dBDataTest() {

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM FLIGHT_DETAILS;");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/resources/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (resultSet.next()) {
                String from = resultSet.getString("origin");
                String to = resultSet.getString("destination");
                String tripType = resultSet.getString("triptype");
                String departDate = resultSet.getString("departuredate");
                String returnDate = resultSet.getString("returndate");
                String noOfPassenger = resultSet.getString("passengers");
                String flightClass = resultSet.getString("flightclass");

                System.out.println("=====================");
                System.out.println(from);
                System.out.println(to);
                System.out.println(tripType);
                System.out.println(departDate);
                System.out.println(returnDate);
                System.out.println(noOfPassenger);
                System.out.println(flightClass);

                String browserName = prop.getProperty("browser");

                if (browserName.equalsIgnoreCase("chrome")) {
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/browsers/chromedriver");
                    driver = new ChromeDriver();
                } else if (browserName.equalsIgnoreCase("firefox")) {
                    System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/browsers/geckodriver");
                    driver = new FirefoxDriver();
                } else if (browserName.equalsIgnoreCase("safari")) {
                    driver = new SafariDriver();
                }

                driver.manage().window().maximize();
                driver.manage().deleteAllCookies();
                driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
                driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                driver.get(prop.getProperty("url"));

                WebElement confi = driver.findElement(By.cssSelector(".language-region-change"));
                confi.click();

                WebElement departFrom_ftf = driver.findElement(By.id("Origin"));
                departFrom_ftf.clear();
                departFrom_ftf.sendKeys(from);

                WebElement destination_ftt = driver.findElement(By.id("Destination"));
                destination_ftt.clear();
                destination_ftt.sendKeys(to);

                WebElement tripType_radioBtn = driver.findElement(By.xpath("//input[@id=\'" + tripType + "\']"));
                tripType_radioBtn.click();

                WebElement departDateField = driver.findElement(By.xpath("//input[@id='DepartDate']"));
                String departDateValue = departDate;
                departDateField.clear();
                selectDateByJS(departDateField, departDateValue);

                WebElement returnDateField = driver.findElement(By.xpath("//input[@id='ReturnDate']"));
                String returnDateValue = returnDate;
                returnDateField.clear();
                selectDateByJS(returnDateField, returnDateValue);

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
                Assert.assertEquals(title, "Flight Search Results | United Airlines");
                System.out.println(title);

                driver.quit();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void selectDateByJS(WebElement element, String dateValue) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('value','" + dateValue + "');", element);
    }

    public static void clickMethod(WebDriver driver, long timeout, WebElement element) {
        WebDriverWait one = new WebDriverWait(driver, timeout);
        one.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void sendKeys(WebDriver driver, WebElement element, int timeout, String value) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(value);
    }

    public static void clickOnJSObject(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}