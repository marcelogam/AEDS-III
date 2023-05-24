package Compactacao;
import java.io.RandomAccessFile;
import java.sql.Time;
import java.util.HashMap;

import Compactacao.Huffman;

public class IniciarCompac {
    public static HashMap<Character, Integer> makeFrequency(String arq){
            var frequency = new HashMap<Character, Integer>();
            try {
                RandomAccessFile raf = new RandomAccessFile(arq, "rw");
                while (raf.getFilePointer() < raf.length()) {
                    char c = (char) raf.readByte();
                    frequency.merge(c, 1, Integer::sum);
                }
                raf.seek(0);
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return frequency;
        }
        
    public static long main(String arq) {
        var frequency = makeFrequency(arq);
        var tree = new Huffman(frequency);
        tree.traverse(Huffman.root, "");
        long time0 = 0;
        try {
            RandomAccessFile source = new RandomAccessFile(arq, "rw");
            RandomAccessFile dest = new RandomAccessFile("BancoDeDados/h_compressed.bin", "rw");
            RandomAccessFile desc = new RandomAccessFile("BancoDeDados/h_descompressed.bin", "rw");
            long startTime0 = System.currentTimeMillis();
            Huffman.compress(source, dest);
            long endTime0 = System.currentTimeMillis();
            time0 = endTime0 - startTime0;
            Huffman.decompress(dest, desc);
 
            source.seek(0);
            dest.seek(0);
            source.close();
            dest.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time0;
    }
    }

