package automationexercise;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Usa a API pública do automationexercise.com (https://automationexercise.com/api_list)
 * para criar e excluir contas usadas como pré-condição dos testes, sem depender da interface.
 */
public final class ContaApi {

	private static final String API = "https://automationexercise.com/api/";

	private ContaApi() {
	}

	public static void criar(String email, String senha) {
		Map<String, String> dados = new LinkedHashMap<>();
		dados.put("name", "Aluno Cadastrado");
		dados.put("email", email);
		dados.put("password", senha);
		dados.put("title", "Mr");
		dados.put("birth_date", "10");
		dados.put("birth_month", "3");
		dados.put("birth_year", "2000");
		dados.put("firstname", "Aluno");
		dados.put("lastname", "Teste");
		dados.put("company", "UFF");
		dados.put("address1", "Av. Gal. Milton Tavares de Souza, s/n");
		dados.put("address2", "Campus da Praia Vermelha");
		dados.put("country", "Canada");
		dados.put("zipcode", "24210346");
		dados.put("state", "Ontario");
		dados.put("city", "Toronto");
		dados.put("mobile_number", "21999999999");
		exigirSucesso(enviar("POST", "createAccount", dados), "\"responseCode\": 201");
	}

	public static void excluir(String email, String senha) {
		Map<String, String> dados = new LinkedHashMap<>();
		dados.put("email", email);
		dados.put("password", senha);
		exigirSucesso(enviar("DELETE", "deleteAccount", dados), "\"responseCode\": 200");
	}

	private static void exigirSucesso(String resposta, String esperado) {
		if (!resposta.contains(esperado)) {
			throw new IllegalStateException("Falha na API do automationexercise.com: " + resposta);
		}
	}

	private static String enviar(String metodo, String recurso, Map<String, String> dados) {
		try {
			HttpURLConnection conexao = (HttpURLConnection) new URL(API + recurso).openConnection();
			conexao.setRequestMethod(metodo);
			conexao.setDoOutput(true);
			conexao.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			try (OutputStream saida = conexao.getOutputStream()) {
				saida.write(formulario(dados).getBytes(StandardCharsets.UTF_8));
			}
			try (InputStream entrada = conexao.getInputStream()) {
				ByteArrayOutputStream resposta = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int lidos;
				while ((lidos = entrada.read(buffer)) != -1) {
					resposta.write(buffer, 0, lidos);
				}
				return new String(resposta.toByteArray(), StandardCharsets.UTF_8);
			} finally {
				conexao.disconnect();
			}
		} catch (Exception e) {
			throw new IllegalStateException("Erro ao chamar a API " + recurso, e);
		}
	}

	private static String formulario(Map<String, String> dados) throws UnsupportedEncodingException {
		StringBuilder corpo = new StringBuilder();
		for (Map.Entry<String, String> campo : dados.entrySet()) {
			if (corpo.length() > 0) {
				corpo.append('&');
			}
			corpo.append(URLEncoder.encode(campo.getKey(), "UTF-8"))
					.append('=')
					.append(URLEncoder.encode(campo.getValue(), "UTF-8"));
		}
		return corpo.toString();
	}
}
