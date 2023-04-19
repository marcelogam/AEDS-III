import java.io.File;

import Crud.Crud;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) throws Exception {
        // Sempre reiniciar arquivo
        File arq = new File("BancoDeDados/filmes.db");
        try {
            arq.delete();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // LeitorCSV.inciarBD();
        int opcao =0;
        do {
            opcao = Integer.parseInt(JOptionPane.showInputDialog("           Menu Inicial \n" +
                    "Digite a opcao desejada:\n" +
                    "Opcao 0: Sair\n" + 
                    "Opcao 1: CRUD\n" +
                    "Opcao 2: Intercalacoes"));
            switch (opcao) {
                case 0:
                    JOptionPane.showMessageDialog(null,"Obrigado!","Saindo...",JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1:
                    Crud.iniciarCrud();
                    opcao = -1;
                    break;
                case 2:
                    opcao = -1;
                    break;
                default:
                    JOptionPane.showMessageDialog(null,"Erro, Digite novamente! ","ERRO",JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (opcao < 0 || opcao > 2);

        
    }
}
