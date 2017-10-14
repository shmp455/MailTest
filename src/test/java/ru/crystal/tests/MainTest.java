package ru.crystal.tests;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.crystal.pages.LoginPage;
import ru.crystal.pages.MailBoxPage;
import ru.crystal.pages.LetterPage;
import ru.crystal.driver.WebDriverManager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MainTest {
    public static WebDriver driver;
    public static LoginPage loginPage;
    public static MailBoxPage mailBoxPage;
    public static LetterPage letterPage;
    private static Properties props = new Properties();

    @BeforeClass
    public static void Setup() {
        try {
            driver = WebDriverManager.GetLocalChromeDriver( WebDriverManager.Drivers.ChromeDriver_linux32 );
            loginPage = new LoginPage(driver);
            mailBoxPage = new MailBoxPage(driver);
            letterPage = new LetterPage(driver);

            props.load( new FileInputStream( "./src/test/resources/login.properties" ) );
            String login = String.valueOf( props.getProperty( "login" ) );
            String pass = String.valueOf( props.getProperty( "pass" ) );
            String url = String.valueOf( props.getProperty( "url" ) );
            driver.get( url );
            loginPage.Login( login, pass );

            mailBoxPage.ClickInbox();
            WebElement first = driver.findElement(By.xpath("//a[@href='https://e.mail.ru/message/15079969930000000338/']"));
            first.click();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestSubject() {
        String subject = "";
        try {
            props.loadFromXML( new FileInputStream( "./src/test/resources/test.xml" ) );
            subject = String.valueOf( props.getProperty( "subject" ) );
        }catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals( subject, letterPage.GetSubject() );
    }

    @Test
    public void TestFrom() {
        String from = "";
        try {
            props.loadFromXML( new FileInputStream( "./src/test/resources/test.xml" ) );
            from = String.valueOf( props.getProperty( "from" ) );
        }catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals( from, letterPage.GetFrom() );
    }

    @Test
    public void TestContent() {
        String content = "";
        try {
            props.loadFromXML( new FileInputStream( "./src/test/resources/test.xml" ) );
            content = String.valueOf( props.getProperty( "content" ) );
        }catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals( content, letterPage.GetLetterBody() );
    }

    @AfterClass
    public static void Exit() {
        mailBoxPage.Logout();
        driver.quit();
    }
}
