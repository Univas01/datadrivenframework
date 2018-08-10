package test.java.seleniumgrid;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GridForFirefox {

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
    }



}
