package test.java.paralleltestwithTestNG;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ParallelFirefox {

    public static WebDriver driver;
    public static Properties prop;

    @BeforeMethod
    public static void setUp() {

        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/resources/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String browserName = prop.getProperty("browserFirefox");

        if (browserName.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/browsers/chromedriver");
            driver = new ChromeDriver();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/browsers/geckodriver");
            driver = new FirefoxDriver();
        } else if (browserName.equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(prop.getProperty("urlRediff"));

    }

    @Test
    public static void firefoxTest(){

        driver.findElement(By.xpath("//input[@value='Go']")).click();
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();

        try{
            if(alertText.equalsIgnoreCase("Please enter a valid user name")){
                System.out.println("Validation message is correct for firefoxTest");
            } else {
                System.out.println("Incorrect validation message");
            }
        } catch (UnhandledAlertException e){
            e.printStackTrace();
        } finally {
            alert.accept();
        }

    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }

}
