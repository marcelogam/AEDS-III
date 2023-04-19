package Crud;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;

import Arvore.ArvoreBMais_String_Int;
import ClassFilme.Filme;
import HashDinamico.HashExtensivel;

public class Crud {
    private static RandomAccessFile arq = null;

    private static Filme criarFilme() throws Exception {
        // Cria um filme
        Filme filme = new Filme();

        // Pegar o id do filme baseado na quantidade de ids.
        arq.seek(0);
        int id = arq.readInt() + 1;

        filme.setId(id);

        // Pegar o titulo do filme
        filme.setTitulo(JOptionPane.showInputDialog("Digite o titulo do conteudo: "));

        // Pegar o type do filme
        String str = "";
        str = JOptionPane.showInputDialog("Digite o type do conteudo[M(Movie)/T(TV Show)]:");
        if (str.charAt(0) == 'M') {
            filme.settype("Movie");
        } else {
            filme.settype("TV Show");
        }

        // Pegar a data do filme
        Date data;
        str = JOptionPane.showInputDialog("Digite a data do " + (str.charAt(0) == 'M' ? "Movie" : "TV Show")
                + " [Exemplo: {Sep 1, 2021},{Jan 20, 1997} (Em ingles)]: ");
        SimpleDateFormat formato = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
        data = formato.parse(str);
        filme.setData(data);

        // Pegar os diretores
        str = JOptionPane.showInputDialog("Digite os diretores do " + (str.charAt(0) == 'M' ? "Movie" : "TV Show")
                + " [Exemplo: {fulano,ciclano} (entre virgulas)]");
        String[] diretores;
        if (str != null) {
            int j = 0;
            int i = 0;
            while (i < str.length()) {
                if (str.charAt(i) == ',') {
                    j++;
                }
                i++;
            }
            diretores = new String[j + 1];
            diretores = str.split(",");
        } else {
            diretores = null;
        }
        filme.setDiretor(diretores);
        JOptionPane.showMessageDialog(null, "Filme Criado:\n" + filme.toString(), "Filme criado",
                JOptionPane.INFORMATION_MESSAGE);
        return filme;
    }

    private static Filme atualizarFilme() throws Exception {
        // Cria um filme
        Filme filme = new Filme();

        filme.setId(Integer.parseInt(JOptionPane.showInputDialog("Digite o id do conteudo")));

        // Pegar o titulo do filme
        filme.setTitulo(JOptionPane.showInputDialog("Digite o titulo do conteudo: "));

        // Pegar o type do filme
        String str = "";
        str = JOptionPane.showInputDialog("Digite o type do conteudo[M(Movie)/T(TV Show)]:");
        if (str.charAt(0) == 'M') {
            filme.settype("Movie");
        } else {
            filme.settype("TV Show");
        }

        // Pegar a data do filme
        Date data;
        str = JOptionPane.showInputDialog("Digite a data do " + (str.charAt(0) == 'M' ? "Movie" : "TV Show")
                + " [Exemplo: {Sep 1, 2021},{Jan 20, 1997} (Em ingles)]: ");
        SimpleDateFormat formato = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
        data = formato.parse(str);
        filme.setData(data);

        // Pegar os diretores
        str = JOptionPane.showInputDialog("Digite os diretores do " + (str.charAt(0) == 'M' ? "Movie" : "TV Show")
                + " [Exemplo: {fulano,ciclano} (entre virgulas)]");
        String[] diretores;
        if (str != null) {
            int j = 0;
            int i = 0;
            while (i < str.length()) {
                if (str.charAt(i) == ',') {
                    j++;
                }
                i++;
            }
            diretores = new String[j + 1];
            diretores = str.split(",");
        } else {
            diretores = null;
        }
        filme.setDiretor(diretores);
        return filme;
    }

    private static void create(Filme filme, ArvoreBMais_String_Int indexArv, HashExtensivel hash) throws Exception {
        // Mover ponteiro para o inicio do arquivo
        arq.seek(0);
        // Ler o ultimo Id
        int quantIds = arq.readInt();
        // O objeto criado sempre tera o ultimo id mais 1
        filme.setId(quantIds + 1);
        // Mover o ponteiro para o incio do arquivo
        arq.seek(0);
        // Escrever o id do objeto
        arq.writeInt(filme.getId());
        // Criar registro
        byte[] barr = filme.toByteArray();
        // Mover ponteiro para o final do arquivo
        arq.seek(arq.length());
        // Escrever na arvoreB
        indexArv.create(Integer.toString(filme.getId()), Long.valueOf(arq.length()).intValue());
        // Escrever na Tabela hash
        hash.create(filme.getId(), arq.length());
        // Escrever registro
        arq.writeBoolean(false); // escrever lapide
        arq.writeInt(barr.length); // escrever tamanho
        arq.write(barr); // escrever registro
    }

    private static Filme read(int id) throws Exception {
        // Criar conjunto vazio
        Filme filme = null;
        // Mover o ponteiro para o primeiro registro
        arq.seek(4);
        // Enquanto nao atingir o fim do arquivo
        int tamReg = 0;
        for (long i = 4; i < arq.length(); i += tamReg + 5) {

            // extrair objeto do registro
            boolean lapide = arq.readBoolean();
            tamReg = arq.readInt();
            byte reg[] = new byte[tamReg];
            arq.read(reg);
            Filme temp = new Filme(reg);

            // Se a lapide for falsa
            if (!(lapide)) {
                // Se id do objeto extraido igual ao id passado como parametro
                if (temp.getId() == id) {
                    filme = temp;
                }
            }
        }
        return filme;
    }

    private static Filme readArvore(int id, ArvoreBMais_String_Int arvore) throws Exception {
        // Cria um filme vazio
        Filme filme = null;
        // Pega a posicao no arquivo do id passado como parametro
        long posFilme = arvore.read(String.valueOf(id));
        if (posFilme != -1) {

            // Posiciona o ponteiro na posicao do filme correspondente ao id
            arq.seek(posFilme);
            // Extrai objeto
            boolean lapide = arq.readBoolean();
            int tamReg;
            tamReg = arq.readInt();
            byte reg[] = new byte[tamReg];
            arq.read(reg);
            Filme temp = new Filme(reg);
            // Se a lapide for falsa
            if (!(lapide)) {
                if (temp.getId() == id) {
                    filme = temp;
                }

            }
        }
        return filme;
    }

    private static Filme readHash(int id, HashExtensivel hash) throws Exception {
        // Cria um filme vazio
        Filme filme = null;
        // Pega a posicao no arquivo do id passado como parametro
        long posFilme = hash.read(id);
        if (posFilme != -1) {

            // Posiciona o ponteiro na posicao do filme correspondente ao id
            arq.seek(posFilme);
            // Extrai objeto
            boolean lapide = arq.readBoolean();
            int tamReg;
            tamReg = arq.readInt();
            byte reg[] = new byte[tamReg];
            arq.read(reg);
            Filme temp = new Filme(reg);
            // Se a lapide for falsa
            if (!(lapide)) {
                if (temp.getId() == id) {
                    filme = temp;
                }

            }
        }
        return filme;
    }

    private static boolean update(Filme novo, ArvoreBMais_String_Int arvore, HashExtensivel hash) throws Exception {
        // Descobrir a posicao que o id do filme atualizado se encontra no arquivo
        long posFilme = arvore.read(String.valueOf(novo.getId()));

        // Posicionar ponteiro no endereco do filme
        arq.seek(posFilme);

        // leitura da lapide
        boolean lapide = arq.readBoolean();

        // Variavel para descobrir o tamanho do registro
        int tamRegistro;

        // leitura do tamanho
        tamRegistro = arq.readInt();

        // criacao do filme pelo byte array
        byte[] ba = new byte[tamRegistro];
        arq.read(ba);
        Filme filme = new Filme(ba);
        if (!lapide)

            // se o filme tiver o mesmo id do novo filme
            if (filme.getId() == novo.getId()) {

                // criacao de novo registro
                byte[] baNovo = novo.toByteArray();

                // se o tamanho do novo registro for igual ao antigo
                if (baNovo.length <= tamRegistro) {
                    // reescrever filme na posicao
                    arq.seek(posFilme + 5);
                    arq.write(baNovo);
                    return true;
                } else {
                    // atualizar lapide
                    arq.seek(posFilme);
                    arq.writeBoolean(true);

                    // create sem atualizar o id
                    // mover o ponteiro para o fim do arquivo
                    arq.seek(arq.length());

                    //Editar na arvore
                    arvore.update(Integer.toString(filme.getId()), Long.valueOf(arq.length()).intValue());
                    //Edita na tabela hash
                    hash.update(filme.getId(), arq.length());

                    // escrever lapide
                    arq.writeBoolean(false);
                    // escrever tamanho do byte
                    arq.writeInt(novo.toByteArray().length);
                    // escrever registro
                    arq.write(novo.toByteArray());
                    return true;
                } // end if
            } // end if

        return false;
    } // end update ()

    private static boolean delete(int id, ArvoreBMais_String_Int arvore, HashExtensivel hash) throws Exception {
        // Variavel para saber o tamanho do registro
        int tamReg;
        // Descobrir a posicao no arquivo do id passado como parametro
        long posFilme = arvore.read(String.valueOf(id));
        // Posicionar o ponteiro no endereco do filme
        arq.seek(posFilme);
        // Pegar a lapide do filme
        boolean lapide = arq.readBoolean();
        // Extrair filme
        tamReg = arq.readInt();
        byte reg[] = new byte[tamReg];
        arq.read(reg);
        Filme filme = new Filme(reg);
        // Verifica se lapide e falsa
        if (!(lapide)) {
            // Verifica se o filme extraido e igual ao id passado como parametro
            if (filme.getId() == id) {
                // Posiciona o ponteiro para a posica inicial do filme
                arq.seek(posFilme);
                // Escreve lapide como verdadeira
                arq.writeBoolean(true);
                // Deleta na arvore
                arvore.delete(String.valueOf(id));
                // Deleta na tabela hash
                hash.delete(id);
                return true;
            }
        }
        return false;
    }

    public static void iniciarCrud(ArvoreBMais_String_Int arvore, HashExtensivel indexHash) throws Exception {
        arq = new RandomAccessFile("BancoDeDados/Filmes.db", "rw");
        int opcao = 0;
        do {
            opcao = Integer.parseInt(JOptionPane.showInputDialog("           Menu Inicial \n" +
                    "Digite a opcao desejada:\n" +
                    "Opcao 0: Sair\n" +
                    "Opcao 1: Create\n" +
                    "Opcao 2: Read\n" +
                    "Opcao 3: Update\n" +
                    "Opcao 4: Delete"));
            switch (opcao) {
                case 0:
                    break;
                case 1:
                    create(criarFilme(), arvore, indexHash);
                    opcao = -1;
                    break;
                case 2:
                    int opcaoRead = Integer.parseInt(JOptionPane.showInputDialog("           Menu Inicial \n" +
                            "Digite a opcao desejada:\n" +
                            "Opcao 0: Read sequencialmente\n" +
                            "Opcao 1: Read na ArvoreB\n" +
                            "Opcao 2: Read na Tabela Hash\n"));
                    switch (opcaoRead) {
                        case 0:
                            long startTime0 = System.currentTimeMillis();
                            Filme filme0 = read(Integer.parseInt(JOptionPane.showInputDialog("Digite um id para a busca")));
                            long endTime0 = System.currentTimeMillis();
                            long time0 = endTime0 - startTime0;
                            if (filme0 != null) {
                                JOptionPane.showMessageDialog(null, "Filme econtrado:\n" + filme0.toString() + "Tempo para achar o filme= " + time0 + "ms" , "Sucesso",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Filme nao econtrado", "Atencao",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 1:
                            long startTime1 = System.currentTimeMillis();
                            Filme filme1 = readArvore(Integer.parseInt(JOptionPane.showInputDialog("Digite um id para a busca")), arvore);
                            long endTime1 = System.currentTimeMillis();
                            long time1 = endTime1 - startTime1;
                            if (filme1 != null) {
                                JOptionPane.showMessageDialog(null, "Filme econtrado:\n" + filme1.toString() + "Tempo para achar o filme= " + time1 + "ms", "Sucesso",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Filme nao econtrado", "Atencao",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case 2:
                            long startTime2 = System.currentTimeMillis();
                            Filme filme2 = readHash(Integer.parseInt(JOptionPane.showInputDialog("Digite um id para a busca")),indexHash);
                            long endTime2 = System.currentTimeMillis();
                            long time2 = endTime2 - startTime2;
                            if (filme2 != null) {
                                JOptionPane.showMessageDialog(null, "Filme econtrado:\n" + filme2.toString() + "Tempo para achar o filme= " + time2 + "ms", "Sucesso",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Filme nao econtrado", "Atencao",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        default:
                            break;
                    }
                    opcao = -1;
                    break;
                case 3:
                    if (update(atualizarFilme(), arvore, indexHash)) {
                        JOptionPane.showMessageDialog(null, "Filme atualizado", "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Filme nao atualizado", "Atencao",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    opcao = -1;
                    break;
                case 4:
                    if (delete(Integer.parseInt(JOptionPane.showInputDialog("Digite um id para deletar")), arvore,
                            indexHash)) {
                        JOptionPane.showMessageDialog(null, "Filme deletado", "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Filme nao deletado", "Atencao", JOptionPane.ERROR_MESSAGE);
                    }
                    opcao = -1;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Erro, Digite novamente! ", "ERRO",
                            JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (opcao < 0 || opcao > 4);
    }
}
