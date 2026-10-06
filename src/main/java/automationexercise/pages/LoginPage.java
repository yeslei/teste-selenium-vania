package automationexercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Página "Signup / Login": formulário de login e de novo cadastro. */
public class LoginPage extends BasePage {

	// --- Login ---
	@FindBy(xpath = "//h2[text()='Login to your account']")
	private WebElement tituloLogin;

	@FindBy(css = "input[data-qa='login-email']")
	private WebElement campoEmailLogin;

	@FindBy(css = "input[data-qa='login-password']")
	private WebElement campoSenhaLogin;

	@FindBy(css = "button[data-qa='login-button']")
	private WebElement botaoLogin;

	@FindBy(xpath = "//p[text()='Your email or password is incorrect!']")
	private WebElement erroLogin;

	// --- Signup ---
	@FindBy(xpath = "//h2[text()='New User Signup!']")
	private WebElement tituloSignup;

	@FindBy(css = "input[data-qa='signup-name']")
	private WebElement campoNomeSignup;

	@FindBy(css = "input[data-qa='signup-email']")
	private WebElement campoEmailSignup;

	@FindBy(css = "button[data-qa='signup-button']")
	private WebElement botaoSignup;

	@FindBy(xpath = "//p[text()='Email Address already exist!']")
	private WebElement erroEmailExistente;

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	// --- Login ---
	public boolean tituloLoginEstaVisivel() {
		return estaVisivel(tituloLogin);
	}

	public LoginPage preencherEmailLogin(String email) {
		digitar(campoEmailLogin, email);
		return this;
	}

	public LoginPage preencherSenhaLogin(String senha) {
		digitar(campoSenhaLogin, senha);
		return this;
	}

	public LoginPage clicarLogin() {
		clicar(botaoLogin);
		return new LoginPage(driver);
	}

	public HomePage clicarLoginComSucesso() {
		clicar(botaoLogin);
		return new HomePage(driver);
	}

	public boolean erroLoginEstaVisivel() {
		return estaVisivel(erroLogin);
	}

	/** Verificação imediata (sem espera) de que a mensagem de erro existe na página. */
	public boolean erroLoginPresente() {
		return !driver.findElements(By.xpath("//p[text()='Your email or password is incorrect!']")).isEmpty();
	}

	public String textoErroLogin() {
		return erroLogin.getText();
	}

	/** Mensagem de validação HTML5 do navegador (vazia se o campo é válido). */
	public String validacaoEmailLogin() {
		return campoEmailLogin.getDomProperty("validationMessage");
	}

	public String validacaoSenhaLogin() {
		return campoSenhaLogin.getDomProperty("validationMessage");
	}

	// --- Signup ---
	public boolean tituloSignupEstaVisivel() {
		return estaVisivel(tituloSignup);
	}

	public LoginPage preencherNomeSignup(String nome) {
		digitar(campoNomeSignup, nome);
		return this;
	}

	public LoginPage preencherEmailSignup(String email) {
		digitar(campoEmailSignup, email);
		return this;
	}

	public CadastroPage clicarSignup() {
		clicar(botaoSignup);
		return new CadastroPage(driver);
	}

	public LoginPage clicarSignupSemSucesso() {
		clicar(botaoSignup);
		return new LoginPage(driver);
	}

	public boolean erroEmailExistenteEstaVisivel() {
		return estaVisivel(erroEmailExistente);
	}

	public String validacaoNomeSignup() {
		return campoNomeSignup.getDomProperty("validationMessage");
	}

	public String validacaoEmailSignup() {
		return campoEmailSignup.getDomProperty("validationMessage");
	}
}
