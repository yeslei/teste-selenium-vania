package automationexercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import automationexercise.pages.HomePage;
import automationexercise.pages.LoginPage;

/**
 * Test Case 3: Login User with incorrect email and password.
 *
 * CLASSES DE EQUIVALÊNCIA
 * E-mail:
 *   CE1 (válida)   formato válido, não cadastrado  -> site responde "Your email or password is incorrect!"
 *   CE7 (válida)   formato válido, cadastrado      -> site responde com erro se a senha estiver errada
 *   CE2 (inválida) vazio                           -> navegador bloqueia (campo required)
 *   CE3 (inválida) sem "@"                         -> navegador bloqueia (type=email)
 *   CE4 (inválida) sem domínio após "@"            -> navegador bloqueia (type=email)
 *   CE8 (inválida) sem parte local antes do "@"    -> navegador bloqueia (type=email)
 * Senha:
 *   CE5 (válida)   1 ou mais caracteres, incorreta -> site responde com erro
 *   CE6 (inválida) vazia                           -> navegador bloqueia (campo required)
 *
 * ANÁLISE DE VALOR LIMITE
 * Cada limite é testado no valor limite (aceito) e logo abaixo dele (rejeitado):
 *
 *   Campo                      | Limite             | No limite (aceito) | Abaixo (rejeitado)
 *   Senha (tamanho)            | mínimo 1 caractere | "x"   (1)          | ""      (0)
 *   E-mail (parte local)       | mínimo 1 caractere | "a@b" (1)          | "@exemplo.com" (0)
 *   E-mail (domínio)           | mínimo 1 caractere | "a@b" (1)          | "aluno@" (0)
 *
 * Não há limite máximo nos campos de login (nem maxlength no HTML, nem regra no site).
 * Por isso as entradas de 100 caracteres de senha e 64 caracteres de parte local
 * (máximo da RFC 5321) são "valores grandes", não limites: verificam que o site trata
 * entradas longas como qualquer outra credencial incorreta.
 *
 * Senha "vizinha" da correta (a senha certa sem o último caractere): fica a 1 caractere
 * da fronteira entre credencial correta e incorreta na classe CE7.
 *
 * As contas cadastradas (CE7) são criadas e excluídas pela API do site (ContaApi).
 */
public class LoginIncorretoTest extends BaseTest {

	private static final String SENHA_100_CARACTERES = repetir('a', 100);
	private static final String EMAIL_PARTE_LOCAL_64_CARACTERES = repetir('a', 64) + "@exemplo.com";

	private static String repetir(char caractere, int vezes) {
		return new String(new char[vezes]).replace('\0', caractere);
	}

	private static String emailUnico() {
		return "aluno" + System.nanoTime() + "@exemplo.com";
	}

	private LoginPage abrirTelaDeLogin() {
		// 1-2. Launch browser e navegar para a url
		HomePage home = new HomePage(driver).abrir();
		// 3. Verify that home page is visible successfully
		assertTrue(home.estaVisivel(), "Home page deveria estar visível");
		// 4. Click on 'Signup / Login' button
		LoginPage login = home.clicarSignupLogin();
		// 5. Verify 'Login to your account' is visible
		assertTrue(login.tituloLoginEstaVisivel(), "'Login to your account' deveria estar visível");
		return login;
	}

	private LoginPage tentarLogin(String email, String senha) {
		LoginPage login = abrirTelaDeLogin();
		// 6. Enter incorrect email address and password
		login.preencherEmailLogin(email).preencherSenhaLogin(senha);
		// 7. Click 'login' button
		return login.clicarLogin();
	}

	private void verificarMensagemDeErro(String email, String senha) {
		LoginPage login = tentarLogin(email, senha);
		// 8. Verify error 'Your email or password is incorrect!' is visible
		assertTrue(login.erroLoginEstaVisivel(), "Mensagem de erro deveria estar visível");
		assertEquals("Your email or password is incorrect!", login.textoErroLogin());
	}

	private void verificarBloqueioDoNavegador(String email, String senha) {
		LoginPage login = tentarLogin(email, senha);
		// 8. A requisição nem chega ao site: o navegador barra o formulário
		String validacao = login.validacaoEmailLogin() + login.validacaoSenhaLogin();
		assertFalse(validacao.isEmpty(), "Navegador deveria exibir mensagem de validação");
		assertTrue(login.tituloLoginEstaVisivel(), "Deveria continuar na tela de login");
		assertFalse(login.erroLoginPresente(), "Formulário inválido não deveria ser enviado ao site");
	}

	// ---------- Classes válidas: o site responde "Your email or password is incorrect!" ----------

	@Test
	@DisplayName("CE1+CE5: e-mail válido não cadastrado e senha incorreta")
	public void emailNaoCadastradoESenhaIncorreta() {
		verificarMensagemDeErro("aluno.inexistente@exemplo.com", "SenhaErrada123");
	}

	@Test
	@DisplayName("CE7+CE5: e-mail cadastrado e senha incorreta")
	public void emailCadastradoESenhaIncorreta() {
		String email = emailUnico();
		String senhaCorreta = "Senha@123";
		ContaApi.criar(email, senhaCorreta);
		try {
			verificarMensagemDeErro(email, "OutraSenha456");
		} finally {
			ContaApi.excluir(email, senhaCorreta);
		}
	}

	@Test
	@DisplayName("CE7 vizinho da fronteira: senha correta sem o último caractere")
	public void emailCadastradoESenhaQuaseCorreta() {
		String email = emailUnico();
		String senhaCorreta = "Senha@123";
		ContaApi.criar(email, senhaCorreta);
		try {
			verificarMensagemDeErro(email, senhaCorreta.substring(0, senhaCorreta.length() - 1));
		} finally {
			ContaApi.excluir(email, senhaCorreta);
		}
	}

	@Test
	@DisplayName("No limite: e-mail mínimo (a@b) e senha com 1 caractere")
	public void limiteMinimoDeEmailESenha() {
		verificarMensagemDeErro("a@b", "x");
	}

	@Test
	@DisplayName("Valor grande: senha com 100 caracteres")
	public void senhaLonga() {
		verificarMensagemDeErro("aluno.inexistente@exemplo.com", SENHA_100_CARACTERES);
	}

	@Test
	@DisplayName("Valor grande: e-mail com parte local de 64 caracteres (máximo da RFC 5321)")
	public void emailComParteLocalLonga() {
		verificarMensagemDeErro(EMAIL_PARTE_LOCAL_64_CARACTERES, "SenhaErrada123");
	}

	// ---------- Classes inválidas: o navegador bloqueia o envio ----------

	@Test
	@DisplayName("CE6 / abaixo do limite da senha: senha vazia (0 caracteres)")
	public void senhaVazia() {
		verificarBloqueioDoNavegador("aluno@exemplo.com", "");
	}

	@Test
	@DisplayName("CE8 / abaixo do limite da parte local: e-mail sem nada antes do @ (@exemplo.com)")
	public void emailSemParteLocal() {
		verificarBloqueioDoNavegador("@exemplo.com", "SenhaErrada123");
	}

	@Test
	@DisplayName("CE4 / abaixo do limite do domínio: e-mail sem nada depois do @ (aluno@)")
	public void emailSemDominio() {
		verificarBloqueioDoNavegador("aluno@", "SenhaErrada123");
	}

	@Test
	@DisplayName("CE2: e-mail vazio")
	public void emailVazio() {
		verificarBloqueioDoNavegador("", "SenhaErrada123");
	}

	@Test
	@DisplayName("CE3: e-mail sem @")
	public void emailSemArroba() {
		verificarBloqueioDoNavegador("aluno.exemplo.com", "SenhaErrada123");
	}
}
