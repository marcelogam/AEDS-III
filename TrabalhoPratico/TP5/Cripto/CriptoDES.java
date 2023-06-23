package Cripto;

import java.security.NoSuchAlgorithmException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class CriptoDES {

	public static void mostraAlgoritimo(String texto) throws IOException {
		// TODO Auto-generated method stub

		try {

			// Informa o tipo de criptografia DES
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");

			// Gera a chave da criptografia
			SecretKey secretKey = keyGenerator.generateKey();

			Cipher cifraDES;
			// Cria a cifra
			cifraDES = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Inicializa a cifra para o processo de criptografia
			// Cipher.ENCRYPT_MODE -> informa que sera feita uma criptografia
			cifraDES.init(Cipher.ENCRYPT_MODE, secretKey);

			/*
			 * Texto puro -> Boa noite Marcelo aprendendo criptografia
			 * Transforma o Texto puro em Bytes
			 */
			byte[] textoPuro = texto.getBytes();

			/*
			 * Texto criptografado -> ou seja criptografa a frase que esta na variavel
			 * textoPuro
			 */
			byte[] textoCriptografado = cifraDES.doFinal(textoPuro);
			RandomAccessFile arq1 = new RandomAccessFile("BancoDeDados/dadosCriptografadoDES.db", "rw");
			arq1.write(textoCriptografado);

			/*
			 * Inicializa a cifra para o processo de decriptografia
			 * Cipher.DECRYPT_MODE -> informa que sera feita uma decriptografia
			 */
			cifraDES.init(Cipher.DECRYPT_MODE, secretKey);

			// Decriptografa o texto
			byte[] textoDecriptografado = cifraDES.doFinal(textoCriptografado);
			RandomAccessFile arq2 = new RandomAccessFile("BancoDeDados/dadosDescriptografadoDES.db", "rw");
			arq2.write(textoDecriptografado);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}