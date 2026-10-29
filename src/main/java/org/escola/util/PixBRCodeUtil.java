package org.escola.util;

import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Locale;

// Gera o payload estatico do Pix (BR Code / "copia e cola"), padrao EMV QRCPS-MPM
// usado pelo Banco Central. Nao depende de nenhuma API externa -- e so montar
// os campos TLV (id+tamanho+valor) e calcular o CRC16 no final.
public class PixBRCodeUtil {

	private PixBRCodeUtil() {
	}

	public static String gerarBRCode(String chavePix, String nomeRecebedor, String cidadeRecebedor, double valor) {
		String nome = normalizar(nomeRecebedor, 25);
		String cidade = normalizar(cidadeRecebedor, 15);

		String merchantAccountInfo = campo("00", "br.gov.bcb.pix") + campo("01", chavePix.trim());
		String valorFormatado = String.format(Locale.US, "%.2f", valor);

		StringBuilder payload = new StringBuilder();
		payload.append(campo("00", "01")); // Payload Format Indicator
		payload.append(campo("26", merchantAccountInfo)); // Merchant Account Information - Pix
		payload.append(campo("52", "0000")); // Merchant Category Code
		payload.append(campo("53", "986")); // Transaction Currency (BRL)
		payload.append(campo("54", valorFormatado)); // Transaction Amount (valor travado)
		payload.append(campo("58", "BR")); // Country Code
		payload.append(campo("59", nome.isEmpty() ? "NAO INFORMADO" : nome)); // Merchant Name
		payload.append(campo("60", cidade.isEmpty() ? "NAO INFORMADO" : cidade)); // Merchant City
		payload.append(campo("62", campo("05", "***"))); // Additional Data Field - txid generico
		payload.append("6304"); // abre o campo do CRC (id 63, tamanho 04), valor vem a seguir

		String crc = crc16(payload.toString());
		return payload.toString() + crc;
	}

	private static String campo(String id, String valor) {
		int tamanho = valor.getBytes(StandardCharsets.UTF_8).length;
		return id + String.format("%02d", tamanho) + valor;
	}

	private static String normalizar(String texto, int tamanhoMaximo) {
		if (texto == null) {
			return "";
		}
		String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
		String limpo = semAcento.replaceAll("[^a-zA-Z0-9 ]", "").toUpperCase(Locale.US).trim();
		if (limpo.length() > tamanhoMaximo) {
			limpo = limpo.substring(0, tamanhoMaximo);
		}
		return limpo;
	}

	// CRC16-CCITT (False): polinomio 0x1021, inicial 0xFFFF, sem reflexao, sem XOR final.
	private static String crc16(String payload) {
		int polinomio = 0x1021;
		int crc = 0xFFFF;
		byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
		for (byte b : bytes) {
			crc ^= (b & 0xFF) << 8;
			for (int i = 0; i < 8; i++) {
				if ((crc & 0x8000) != 0) {
					crc = (crc << 1) ^ polinomio;
				} else {
					crc = crc << 1;
				}
				crc &= 0xFFFF;
			}
		}
		return String.format("%04X", crc);
	}

}
