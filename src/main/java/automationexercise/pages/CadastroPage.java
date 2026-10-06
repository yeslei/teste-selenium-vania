package automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/** Página "ENTER ACCOUNT INFORMATION" exibida após o Signup. */
public class CadastroPage extends BasePage {

	@FindBy(xpath = "//b[text()='Enter Account Information']")
	private WebElement titulo;

	@FindBy(id = "id_gender1")
	private WebElement tituloMr;

	@FindBy(id = "id_gender2")
	private WebElement tituloMrs;

	@FindBy(css = "input[data-qa='name']")
	private WebElement campoNome;

	@FindBy(css = "input[data-qa='email']")
	private WebElement campoEmail;

	@FindBy(css = "input[data-qa='password']")
	private WebElement campoSenha;

	@FindBy(css = "select[data-qa='days']")
	private WebElement comboDia;

	@FindBy(css = "select[data-qa='months']")
	private WebElement comboMes;

	@FindBy(css = "select[data-qa='years']")
	private WebElement comboAno;

	@FindBy(id = "newsletter")
	private WebElement checkNewsletter;

	@FindBy(id = "optin")
	private WebElement checkOfertas;

	@FindBy(css = "input[data-qa='first_name']")
	private WebElement campoPrimeiroNome;

	@FindBy(css = "input[data-qa='last_name']")
	private WebElement campoSobrenome;

	@FindBy(css = "input[data-qa='company']")
	private WebElement campoEmpresa;

	@FindBy(css = "input[data-qa='address']")
	private WebElement campoEndereco;

	@FindBy(css = "input[data-qa='address2']")
	private WebElement campoEndereco2;

	@FindBy(css = "select[data-qa='country']")
	private WebElement comboPais;

	@FindBy(css = "input[data-qa='state']")
	private WebElement campoEstado;

	@FindBy(css = "input[data-qa='city']")
	private WebElement campoCidade;

	@FindBy(css = "input[data-qa='zipcode']")
	private WebElement campoCep;

	@FindBy(css = "input[data-qa='mobile_number']")
	private WebElement campoCelular;

	@FindBy(css = "button[data-qa='create-account']")
	private WebElement botaoCriarConta;

	public CadastroPage(WebDriver driver) {
		super(driver);
	}

	public boolean tituloEstaVisivel() {
		return estaVisivel(titulo);
	}

	public String textoTitulo() {
		return titulo.getText();
	}

	public CadastroPage selecionarTitulo(String titulo) {
		clicar("Mrs".equals(titulo) ? tituloMrs : tituloMr);
		return this;
	}

	public String nomePreenchido() {
		return campoNome.getDomProperty("value");
	}

	public CadastroPage preencherNome(String nome) {
		digitar(campoNome, nome);
		return this;
	}

	public String emailPreenchido() {
		return campoEmail.getDomProperty("value");
	}

	public CadastroPage preencherSenha(String senha) {
		digitar(campoSenha, senha);
		return this;
	}

	public CadastroPage selecionarDataNascimento(String dia, String mes, String ano) {
		new Select(comboDia).selectByValue(dia);
		new Select(comboMes).selectByVisibleText(mes);
		new Select(comboAno).selectByValue(ano);
		return this;
	}

	public CadastroPage marcarNewsletter() {
		if (!checkNewsletter.isSelected()) {
			clicar(checkNewsletter);
		}
		return this;
	}

	public CadastroPage marcarOfertasParceiros() {
		if (!checkOfertas.isSelected()) {
			clicar(checkOfertas);
		}
		return this;
	}

	public boolean newsletterEstaMarcada() {
		return checkNewsletter.isSelected();
	}

	public boolean ofertasParceirosEstaMarcada() {
		return checkOfertas.isSelected();
	}

	public CadastroPage preencherPrimeiroNome(String valor) {
		digitar(campoPrimeiroNome, valor);
		return this;
	}

	public CadastroPage preencherSobrenome(String valor) {
		digitar(campoSobrenome, valor);
		return this;
	}

	public CadastroPage preencherEmpresa(String valor) {
		digitar(campoEmpresa, valor);
		return this;
	}

	public CadastroPage preencherEndereco(String valor) {
		digitar(campoEndereco, valor);
		return this;
	}

	public CadastroPage preencherEndereco2(String valor) {
		digitar(campoEndereco2, valor);
		return this;
	}

	public CadastroPage selecionarPais(String pais) {
		new Select(comboPais).selectByVisibleText(pais);
		return this;
	}

	public CadastroPage preencherEstado(String valor) {
		digitar(campoEstado, valor);
		return this;
	}

	public CadastroPage preencherCidade(String valor) {
		digitar(campoCidade, valor);
		return this;
	}

	public CadastroPage preencherCep(String valor) {
		digitar(campoCep, valor);
		return this;
	}

	public CadastroPage preencherCelular(String valor) {
		digitar(campoCelular, valor);
		return this;
	}

	public ContaCriadaPage clicarCriarConta() {
		clicar(botaoCriarConta);
		return new ContaCriadaPage(driver);
	}

	public CadastroPage clicarCriarContaSemSucesso() {
		clicar(botaoCriarConta);
		return new CadastroPage(driver);
	}

	/** Mensagem de validação HTML5 do navegador (vazia se o campo é válido). */
	public String validacaoSenha() {
		return campoSenha.getDomProperty("validationMessage");
	}
}
