import java.io.File;

import Crud.Crud;
import LeitorCsv.LeitorCSV;
import Arvore.ArvoreBMais_String_Int;
import HashDinamico.HashExtensivel;

import javax.swing.JOptionPane;



public class Main {
    public static void main(String[] args) throws Exception {
        //Criacao da arvore
        ArvoreBMais_String_Int arvore = new ArvoreBMais_String_Int(8, "BancoDeDados/indexArvore");
        
        //Criacao da Tabelahash
        HashExtensivel indexHash = new HashExtensivel(2000, "BancoDeDados/diretorio.db", "BancoDeDados/indexTabelaHash.db");

        // Sempre reiniciar arquivo
        File arq = new File("BancoDeDados/filmes.db");
        
        try {
            arq.delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        LeitorCSV.inciarBD(arvore,indexHash);

        int opcao =0;
        do {
            opcao = Integer.parseInt(JOptionPane.showInputDialog("           Menu Inicial \n" +
                    "Digite a opcao desejada:\n" +
                    "Opcao 0: Sair\n" + 
                    "Opcao 1: CRUD\n"));
            switch (opcao) {
                case 0:
                    JOptionPane.showMessageDialog(null,"Obrigado!","Saindo...",JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1:
                    Crud.iniciarCrud(arvore, indexHash);
                    opcao = -1;
                    break;
                default:
                    JOptionPane.showMessageDialog(null,"Erro, Digite novamente! ","ERRO",JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (opcao < 0 || opcao > 1);

        
    }
}
