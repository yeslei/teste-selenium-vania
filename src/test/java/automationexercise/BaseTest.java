package automationexercise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class BaseTest {

	/*
	 * O automationexercise.com exibe anúncios de tela cheia que interceptam os
	 * cliques (ElementClickInterceptedException). Os domínios de anúncio são
	 * resolvidos para um endereço inválido para que não carreguem.
	 */
	private static final String BLOQUEIO_DE_ANUNCIOS = "--host-resolver-rules="
			+ "MAP *.doubleclick.net 0.0.0.0, MAP doubleclick.net 0.0.0.0, "
			+ "MAP *.googlesyndication.com 0.0.0.0, MAP *.googleadservices.com 0.0.0.0, "
			+ "MAP adservice.google.com 0.0.0.0, MAP *.adtrafficquality.google 0.0.0.0";

	protected WebDriver driver;

	@BeforeEach
	public void createDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(BLOQUEIO_DE_ANUNCIOS);
		// Com -Dheadless=true o Chrome roda sem abrir janela
		boolean headless = Boolean.getBoolean("headless");
		if (headless) {
			options.addArguments("--headless=new", "--window-size=1920,1080");
		}
		driver = WebDriverManager.chromedriver().capabilities(options).create();
		if (!headless) {
			driver.manage().window().maximize();
		}
	}

	@AfterEach
	public void quitDriver() {
		if (driver != null) {
			driver.quit();
		}
	}
}
