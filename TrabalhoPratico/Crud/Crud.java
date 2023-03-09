package Crud;

import java.io.File;
import LeitorCsv.LeitorCSV;

public class Crud {


    public static void main(String[] args) throws Exception {
        File arq = new File("BancoDeDados/filmes.db");
        System.out.println("arquivo deletado? " + arq.delete());
        
        LeitorCSV.inciarBD();
    }

}
