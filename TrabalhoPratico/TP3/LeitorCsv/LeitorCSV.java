package LeitorCsv;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ClassFilme.Filme;
import Arvore.ArvoreB;
import HashDinamico.HashExtensivel;

public class LeitorCSV {
    /*
     * Este metodo tem a finalidade de pegar uma string que foi
     * do csv e tranformar em varios atributos.
     */
    private static Filme tratarLinha(String str) throws Exception {
        int i = 0;
        String atributos[] = new String[12];
        int j = 0;
        while (j < 12) {
            atributos[j] = "";
            if (i < str.length()) {
                if (str.charAt(i) == '"') {
                    ++i;
                    while (str.charAt(i) != '"') {
                        // tratar o caso de duas aspas
                        if (i + 2 < str.length()) {
                            if (str.charAt(i + 1) == '"' && str.charAt(i + 2) == '"') {
                                atributos[j] += str.charAt(i); // Vai pegar a letra que esta
                                i++;
                                atributos[j] += str.charAt(i);// Vai pegar a primeira aspas
                                i++;
                                atributos[j] += str.charAt(i);// Vai pegar a segunda aspas
                            }
                        }
                        atributos[j] += str.charAt(i);
                        i++;
                    }
                    j++;
                    i = i + 2;
                } else if (str.charAt(i) == ',') {
                    atributos[j] = null;
                    j++;
                    i++;
                } else if (str.charAt(i) != ',' && str.charAt(i) != '"') {
                    while (i < str.length() && str.charAt(i) != ',') {
                        atributos[j] += str.charAt(i);
                        i++;
                    }
                    j++;
                    i++;
                }
            } else {
                atributos[j] = null;
                j++;
            }
        }
        return criarFilme(atributos);
    }

    private static Filme criarFilme(String atributos[]) throws Exception {

        Filme filme = new Filme(converterStringParaInt(atributos[0]), // ID
                atributos[2], // title
                atributos[1], // type
                tranformarData(atributos[6]), // Data
                converterStringParaVetor(atributos[3])); // Director

        return filme;
    }

    private static String[] converterStringParaVetor(String linha) {
        String[] diretores;
        if (linha != null) {
            int j = 0;
            int i = 0;
            while (i < linha.length()) {
                if (linha.charAt(i) == ',') {
                    j++;
                }
                i++;
            }
            diretores = new String[j + 1];
            diretores = linha.split(",");
        } else {
            diretores = null;
        }
        return diretores;
    }

    private static Date tranformarData(String str) throws Exception {
        Date data;
        if (str != null) {
            String temp = "";
            int tamanho = str.length();
            int i = 0;
            if (str.charAt(i) == ' ') {
                ++i;
                for (; i < 4; i++) {
                    temp += str.charAt(i);
                }
            } else {
                for (; i < 3; i++) {
                    temp += str.charAt(i);
                }
            }

            while (i < tamanho) {
                if (str.charAt(i) == ' ') {
                    i++;
                    temp += ' ';
                    for (; i < tamanho; i++) {
                        temp += str.charAt(i);
                    }
                }
                i++;
            }
            SimpleDateFormat formato = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
            data = formato.parse(temp);
        } else {
            data = null;
        }
        return data;
    }

    private static int converterStringParaInt(String str) {
        int tamanho = str.length();
        String novo = "";
        for (int i = 1; i < tamanho; i++) {
            novo += str.charAt(i);
        }
        int valor = Integer.parseInt(novo);
        return valor;
    }

    private static void lerBancoDeDados(RandomAccessFile arq) throws Exception {
        arq.seek(0);
        int quantidade = arq.readInt();
        System.out.println(quantidade);
        Filme filme[] = new Filme[quantidade];
        for (int i = 0; i < quantidade; i++) {

            if (arq.readBoolean() == false) { // Verifica se o registro existe

                int tamanho = arq.readInt(); // Ler o tamanho do registro

                byte registro[] = new byte[tamanho]; // Cria um array de bytes para alocar o filme.
                arq.read(registro);
                filme[i] = new Filme(registro);
                System.out.println(filme[i].toString());
            }
        }

    }

    public static void inciarBD(ArvoreB arvore, HashExtensivel indexHash) throws Exception {
        // String para temporario para ler do arquivo CSV
        String linha = "";
        // Inteiro para controlar o array de filmes
        int i = 0;
        // Array de filmes 
        Filme filme[] = new Filme[8807];

        // Criacao do arquivo de banco de dados
        RandomAccessFile arq = new RandomAccessFile("./BancoDeDados/Filmes.db", "rw");

        try (BufferedReader br = new BufferedReader(new FileReader("netflix_titlesTeste.csv"))) {
            br.readLine();
            while ((linha = br.readLine()) != null) {
                filme[i] = tratarLinha(linha);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int j = 0;

        try {
            
            arq.writeInt(i); // Escreve a quantidade de filmes

            while (j < i) {

                // escrever registro no arquivo
                byte[] barr = filme[j].toByteArray();

                //Populando a arvore
                arvore.create(Integer.toString(filme[j].getId()), Long.valueOf(arq.length()).intValue());
                //Populano a tabela hash
                indexHash.create(filme[j].getId(), arq.length());

                arq.writeBoolean(false); // escrever lapide
                arq.writeInt(barr.length); // escrever tamanho
                arq.write(barr); // escrever registro
                j++;
            }

            //lerBancoDeDados(arq);

            arq.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
