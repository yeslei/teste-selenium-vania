package automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Página "ACCOUNT DELETED!". */
public class ContaExcluidaPage extends BasePage {

	@FindBy(css = "h2[data-qa='account-deleted']")
	private WebElement titulo;

	@FindBy(css = "a[data-qa='continue-button']")
	private WebElement botaoContinuar;

	public ContaExcluidaPage(WebDriver driver) {
		super(driver);
	}

	public boolean tituloEstaVisivel() {
		return estaVisivel(titulo);
	}

	public String textoTitulo() {
		return titulo.getText();
	}

	public HomePage clicarContinuar() {
		clicar(botaoContinuar);
		return new HomePage(driver);
	}
}
