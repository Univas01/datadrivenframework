package test.java.seleniumgridexample;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class GridForFirefoxThree {

    public static WebDriver driver;
    public static Properties prop;

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

            String hubRUL = "http://localhost:4444/wd/hub";
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setPlatform(Platform.MAC);
            desiredCapabilities.setBrowserName("firefox");
            desiredCapabilities.acceptInsecureCerts();

        try{
            driver = new RemoteWebDriver(new URL(hubRUL), desiredCapabilities);

        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(prop.getProperty("urlRediff"));

    }

    @Test
    public static void gridForFirefoxTest(){

        driver.findElement(By.xpath("//input[@value='Go']")).click();
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();

        try{
            if(alertText.equalsIgnoreCase("Please enter a valid user name")){
                System.out.println("Validation message is correct");
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
