package automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

	public static final String URL = "https://automationexercise.com";

	@FindBy(css = "img[alt='Website for automation practice']")
	private WebElement logo;

	@FindBy(css = "a[href='/login']")
	private WebElement linkSignupLogin;

	@FindBy(xpath = "//a[contains(., 'Logged in as')]")
	private WebElement logadoComo;

	@FindBy(css = "a[href='/logout']")
	private WebElement linkLogout;

	@FindBy(css = "a[href='/delete_account']")
	private WebElement linkDeleteAccount;

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public HomePage abrir() {
		driver.get(URL);
		return this;
	}

	public boolean estaVisivel() {
		return estaVisivel(logo) && driver.getTitle().equals("Automation Exercise");
	}

	public LoginPage clicarSignupLogin() {
		clicar(linkSignupLogin);
		return new LoginPage(driver);
	}

	public boolean logadoComoEstaVisivel() {
		return estaVisivel(logadoComo);
	}

	public String textoLogadoComo() {
		return logadoComo.getText();
	}

	public LoginPage clicarLogout() {
		clicar(linkLogout);
		return new LoginPage(driver);
	}

	public ContaExcluidaPage clicarDeleteAccount() {
		clicar(linkDeleteAccount);
		return new ContaExcluidaPage(driver);
	}
}
