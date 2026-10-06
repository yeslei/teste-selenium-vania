package automationexercise.pages;

import java.time.Duration;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base dos Page Objects: inicializa os elementos @FindBy no construtor
 * (boa prática dos slides) e oferece esperas explícitas.
 */
public abstract class BasePage {

	protected final WebDriver driver;
	protected final WebDriverWait wait;

	protected BasePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	/**
	 * Centraliza o elemento na tela antes de clicar. Se outro elemento interceptar
	 * o clique (a página ainda se ajustando durante o carregamento), tenta de novo
	 * até o tempo limite da espera.
	 */
	protected void clicar(WebElement elemento) {
		wait.ignoring(ElementClickInterceptedException.class).until(d -> {
			((JavascriptExecutor) d).executeScript(
					"arguments[0].scrollIntoView({block: 'center'});", elemento);
			WebElement clicavel = ExpectedConditions.elementToBeClickable(elemento).apply(d);
			if (clicavel == null) {
				return false;
			}
			clicavel.click();
			return true;
		});
	}

	protected void digitar(WebElement elemento, String texto) {
		elemento.clear();
		elemento.sendKeys(texto);
	}

	protected boolean estaVisivel(WebElement elemento) {
		try {
			return wait.until(ExpectedConditions.visibilityOf(elemento)).isDisplayed();
		} catch (RuntimeException e) {
			return false;
		}
	}
}
