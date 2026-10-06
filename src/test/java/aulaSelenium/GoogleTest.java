package aulaSelenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.github.bonigarcia.wdm.WebDriverManager;

@Disabled("Exemplo da aula: o Google bloqueia buscas automatizadas e não carrega a página de resultados")
public class GoogleTest {

	protected WebDriver driver;

	@BeforeAll
	public static void configuraDriver() {
		//WebDriverManager.chromedriver().setup();
	}

    @BeforeEach
    public void createDriver() {
		//driver = new ChromeDriver();
    	driver = WebDriverManager.chromedriver().create();
        driver.get("https://www.google.com.br");
    }

    @Test
    public void testaGoogle() throws InterruptedException {
    	WebElement caixaBusca = driver.findElement(By.name("q"));
    	caixaBusca.sendKeys("alunos");
    	caixaBusca.sendKeys(Keys.ENTER);
    	System.out.println(driver.getTitle());
    	assertTrue(driver.getTitle().contains("Google"));

    }

    @AfterEach
    public void quitDriver() {
       driver.quit();
    }


}
