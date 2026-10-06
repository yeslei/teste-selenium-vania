package automationexercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automationexercise.pages.CadastroPage;
import automationexercise.pages.ContaCriadaPage;
import automationexercise.pages.ContaExcluidaPage;
import automationexercise.pages.HomePage;
import automationexercise.pages.LoginPage;

/**
 * Test Case 1: Register User.
 *
 * CLASSES DE EQUIVALÊNCIA
 *   CE1 (válida)   nome com 1+ caracteres, e-mail novo em formato válido e senha com 1+ caracteres
 *                  -> conta criada
 *   CE2 (inválida) nome vazio                       -> navegador bloqueia (campo required)
 *   CE3 (inválida) e-mail sem "@"                   -> navegador bloqueia (type=email)
 *   CE4 (inválida) e-mail já cadastrado             -> site responde "Email Address already exist!"
 *   CE5 (inválida) e-mail sem parte local ou domínio -> navegador bloqueia (type=email)
 *   CE6 (inválida) senha vazia                      -> navegador bloqueia (campo required)
 *
 * ANÁLISE DE VALOR LIMITE
 * Cada limite é testado no valor limite (aceito) e logo abaixo dele (rejeitado):
 *
 *   Campo                  | Limite             | No limite (aceito) | Abaixo (rejeitado)
 *   Nome (tamanho)         | mínimo 1 caractere | "A"   (1)          | ""             (0)
 *   Senha (tamanho)        | mínimo 1 caractere | "1"   (1)          | ""             (0)
 *   E-mail (parte local)   | mínimo 1 caractere | "a@b" (1)*         | "@exemplo.com" (0)
 *   E-mail (domínio)       | mínimo 1 caractere | "a@b" (1)*         | "aluno@"       (0)
 *
 *   * o menor e-mail aceito é exercitado no login (LoginIncorretoTest); aqui o cadastro
 *     usa e-mails únicos gerados para que cada conta seja nova.
 *
 * Data de nascimento: os combos do site oferecem dia 1..31, mês January..December e
 * ano 1900..2021. São testados os dois extremos de cada faixa (1/January/1900 e
 * 31/December/2021) e um valor nominal (15/June/1990). Os valores fora da faixa
 * (dia 0 ou 32, ano 1899 ou 2022) não podem ser escolhidos pela interface, porque o
 * combo não oferece essas opções; por isso não há caso "fora do limite" para a data.
 *
 * Nome com 49 caracteres e senha com 30 caracteres são "valores grandes": o site não
 * define tamanho máximo para esses campos.
 */
public class RegistrarUsuarioTest extends BaseTest {

	private static String emailUnico() {
		return "aluno" + System.nanoTime() + "@exemplo.com";
	}

	private LoginPage abrirTelaDeSignup() {
		// 1-2. Launch browser e navegar para a url
		HomePage home = new HomePage(driver).abrir();
		// 3. Verify that home page is visible successfully
		assertTrue(home.estaVisivel(), "Home page deveria estar visível");
		// 4. Click on 'Signup / Login' button
		LoginPage login = home.clicarSignupLogin();
		// 5. Verify 'New User Signup!' is visible
		assertTrue(login.tituloSignupEstaVisivel(), "'New User Signup!' deveria estar visível");
		return login;
	}

	/** Passos 6 a 12: faz o signup e preenche a tela "ENTER ACCOUNT INFORMATION". */
	private CadastroPage preencherCadastro(LoginPage login, String nome, String email, String senha,
			String titulo, String dia, String mes, String ano) {
		// 6. Enter name and email address
		login.preencherNomeSignup(nome).preencherEmailSignup(email);
		// 7. Click 'Signup' button
		CadastroPage cadastro = login.clicarSignup();

		// 8. Verify that 'ENTER ACCOUNT INFORMATION' is visible
		assertTrue(cadastro.tituloEstaVisivel(), "'ENTER ACCOUNT INFORMATION' deveria estar visível");
		assertEquals("ENTER ACCOUNT INFORMATION", cadastro.textoTitulo().toUpperCase());
		assertEquals(nome, cadastro.nomePreenchido());
		assertEquals(email, cadastro.emailPreenchido());

		// 9. Fill details: Title, Name, Email, Password, Date of birth
		//    (o Email já vem do signup e o site bloqueia o campo; por isso só é conferido acima)
		cadastro.selecionarTitulo(titulo)
				.preencherNome(nome)
				.preencherSenha(senha)
				.selecionarDataNascimento(dia, mes, ano);
		assertEquals(nome, cadastro.nomePreenchido());
		// 10. Select checkbox 'Sign up for our newsletter!'
		cadastro.marcarNewsletter();
		assertTrue(cadastro.newsletterEstaMarcada(), "'Sign up for our newsletter!' deveria estar marcado");
		// 11. Select checkbox 'Receive special offers from our partners!'
		cadastro.marcarOfertasParceiros();
		assertTrue(cadastro.ofertasParceirosEstaMarcada(),
				"'Receive special offers from our partners!' deveria estar marcado");
		// 12. Fill details: First name, Last name, Company, Address, Address2, Country, State, City, Zipcode, Mobile Number
		cadastro.preencherPrimeiroNome("Aluno")
				.preencherSobrenome("Teste")
				.preencherEmpresa("UFF")
				.preencherEndereco("Av. Gal. Milton Tavares de Souza, s/n")
				.preencherEndereco2("Campus da Praia Vermelha")
				.selecionarPais("Canada")
				.preencherEstado("Ontario")
				.preencherCidade("Toronto")
				.preencherCep("24210346")
				.preencherCelular("21999999999");
		return cadastro;
	}

	/** Passos 13 a 16: cria a conta e retorna a home já logada. */
	private HomePage concluirCadastro(CadastroPage cadastro, String nome) {
		// 13. Click 'Create Account button'
		ContaCriadaPage contaCriada = cadastro.clicarCriarConta();

		// 14. Verify that 'ACCOUNT CREATED!' is visible
		assertTrue(contaCriada.tituloEstaVisivel(), "'ACCOUNT CREATED!' deveria estar visível");
		assertEquals("ACCOUNT CREATED!", contaCriada.textoTitulo().toUpperCase());
		// 15. Click 'Continue' button
		HomePage home = contaCriada.clicarContinuar();

		// 16. Verify that 'Logged in as username' is visible
		assertTrue(home.logadoComoEstaVisivel(), "'Logged in as' deveria estar visível");
		assertEquals("Logged in as " + nome, home.textoLogadoComo().trim());
		return home;
	}

	/** Passos 17 e 18: exclui a conta logada. */
	private void excluirConta(HomePage home) {
		// 17. Click 'Delete Account' button
		ContaExcluidaPage contaExcluida = home.clicarDeleteAccount();
		// 18. Verify that 'ACCOUNT DELETED!' is visible and click 'Continue' button
		assertTrue(contaExcluida.tituloEstaVisivel(), "'ACCOUNT DELETED!' deveria estar visível");
		assertEquals("ACCOUNT DELETED!", contaExcluida.textoTitulo().toUpperCase());
		contaExcluida.clicarContinuar();
	}

	private void verificarCadastroCompleto(String nome, String senha, String titulo,
			String dia, String mes, String ano) {
		CadastroPage cadastro = preencherCadastro(abrirTelaDeSignup(), nome, emailUnico(), senha,
				titulo, dia, mes, ano);
		HomePage home = concluirCadastro(cadastro, nome);
		excluirConta(home);
	}

	private void verificarBloqueioDoSignup(String nome, String email) {
		LoginPage login = abrirTelaDeSignup();

		// 6. Enter name and email address
		login.preencherNomeSignup(nome).preencherEmailSignup(email);
		// 7. Click 'Signup' button
		login = login.clicarSignupSemSucesso();

		// 8. 'ENTER ACCOUNT INFORMATION' NÃO deve aparecer: o navegador barra o formulário
		String validacao = login.validacaoNomeSignup() + login.validacaoEmailSignup();
		assertFalse(validacao.isEmpty(), "Navegador deveria exibir mensagem de validação");
		assertTrue(login.tituloSignupEstaVisivel(), "Deveria continuar na tela de signup");
	}

	// ---------- Classe válida: a conta é criada (fluxo completo, passos 1 a 18) ----------

	@Test
	@DisplayName("No limite inferior: nome e senha com 1 caractere, data 1/January/1900")
	public void limiteInferior() {
		verificarCadastroCompleto("A", "1", "Mr", "1", "January", "1900");
	}

	@Test
	@DisplayName("CE1 valor nominal: data 15/June/1990")
	public void valorNominal() {
		verificarCadastroCompleto("Maria Silva", "Senha@123", "Mrs", "15", "June", "1990");
	}

	@Test
	@DisplayName("No limite superior da data: 31/December/2021 (nome e senha com valores grandes)")
	public void limiteSuperior() {
		verificarCadastroCompleto("Nome Muito Longo Para Testar O Limite Superior Ok",
				"SenhaMuitoLongaComTrintaChars!", "Mr", "31", "December", "2021");
	}

	// ---------- Classes inválidas: o cadastro não é concluído ----------

	@Test
	@DisplayName("CE2 / abaixo do limite do nome: nome vazio (0 caracteres)")
	public void nomeVazio() {
		verificarBloqueioDoSignup("", "aluno@exemplo.com");
	}

	@Test
	@DisplayName("CE6 / abaixo do limite da senha: senha vazia (0 caracteres)")
	public void senhaVazia() {
		String nome = "Aluno Teste";
		CadastroPage cadastro = preencherCadastro(abrirTelaDeSignup(), nome, emailUnico(), "",
				"Mr", "15", "June", "1990");

		// 13. Click 'Create Account button'
		cadastro = cadastro.clicarCriarContaSemSucesso();

		// 14. 'ACCOUNT CREATED!' NÃO deve aparecer: o navegador barra a senha vazia
		assertFalse(cadastro.validacaoSenha().isEmpty(), "Navegador deveria exibir mensagem de validação");
		assertTrue(cadastro.tituloEstaVisivel(), "Deveria continuar em 'ENTER ACCOUNT INFORMATION'");
	}

	@Test
	@DisplayName("CE5 / abaixo do limite da parte local: e-mail sem nada antes do @ (@exemplo.com)")
	public void emailSemParteLocal() {
		verificarBloqueioDoSignup("Aluno Teste", "@exemplo.com");
	}

	@Test
	@DisplayName("CE5 / abaixo do limite do domínio: e-mail sem nada depois do @ (aluno@)")
	public void emailSemDominio() {
		verificarBloqueioDoSignup("Aluno Teste", "aluno@");
	}

	@Test
	@DisplayName("CE3: e-mail sem @")
	public void emailSemArroba() {
		verificarBloqueioDoSignup("Aluno Teste", "aluno.exemplo.com");
	}

	@Test
	@DisplayName("CE4: e-mail já cadastrado")
	public void emailJaCadastrado() {
		String email = emailUnico();
		String senha = "Senha@123";

		// Pré-condição: conta já cadastrada com o e-mail (criada pela API do site)
		ContaApi.criar(email, senha);
		try {
			LoginPage login = abrirTelaDeSignup();
			// 6. Enter name and email address (e-mail já cadastrado)
			login.preencherNomeSignup("Outro Aluno").preencherEmailSignup(email);
			// 7. Click 'Signup' button
			login = login.clicarSignupSemSucesso();
			// 8. O cadastro não prossegue: o site exibe 'Email Address already exist!'
			assertTrue(login.erroEmailExistenteEstaVisivel(), "'Email Address already exist!' deveria estar visível");
		} finally {
			ContaApi.excluir(email, senha);
		}
	}
}
