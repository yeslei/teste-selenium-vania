package automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Página "ACCOUNT CREATED!". */
public class ContaCriadaPage extends BasePage {

	@FindBy(css = "h2[data-qa='account-created']")
	private WebElement titulo;

	@FindBy(css = "a[data-qa='continue-button']")
	private WebElement botaoContinuar;

	public ContaCriadaPage(WebDriver driver) {
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
