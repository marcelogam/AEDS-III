package Compactacao;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;

public class LZW {
    //Alterar o que é lido e o que é escrito
    

    // ---------------------------------------------------------------------------------------------- //

    public static void compress(String source, String destin) throws Exception {

        // --------------------------------------------------- //

        // 1. Convert original file to string
        String originString = "";

        try {

            RandomAccessFile raf = new RandomAccessFile(source, "rw");

            // read globalId
            originString += raf.readInt() + "~";
   
            while(raf.getFilePointer() < raf.length() - 1) {

                // read lapide
                originString += raf.readBoolean() + "~";
               
                // read size
                originString += raf.readInt() + "~";

                // read Id
                originString += raf.readInt() + "~";

                //read titulo
                originString += raf.readUTF() + "~";

                //read tipo
                originString += raf.readUTF() + "~";

                //read data
                originString += raf.readLong() + "~";

                //read diretores
                int tamDiretores = raf.readInt();
                originString += tamDiretores + "~";
                for(int i = 0; i < tamDiretores; i++){
                    originString += raf.readUTF() + "~";
                }
            }

            raf.close();
        }
        catch(Exception e) { e.printStackTrace(); }

        // --------------------------------------------------- //

        // 2. Create dictionary
        originString = originString.replaceAll(" ", "\\^");

        ArrayList<String> dictionary = new ArrayList<String>();

        for(int i = 0; i < originString.length(); i++) {

            String s = Character.toString(originString.charAt(i));
            
            if(!dictionary.contains(s)) dictionary.add(s);
        }

        // -------------------------------------------------------------- //

        // 3. Create dictionary output
        ArrayList<Integer> output = new ArrayList<Integer>();

        for(int i = 0; i < originString.length(); i++) {

            String s = Character.toString(originString.charAt(i));

            while(true) {

                if(i == originString.length() - 1) break;

                s += originString.charAt(i + 1);

                if(dictionary.contains(s)) {
                    
                    if(i == originString.length() - 2) {
                        
                        output.add(dictionary.indexOf(s));
                        break;
                    }
                    else i++;
                }
                else {

                    dictionary.add(s);
                    
                    if(i == originString.length() - 2) output.add(dictionary.indexOf(s));
                    else output.add(dictionary.indexOf(s.substring(0, s.length() - 1)));
                    break;
                }
            }

            // --------------- //

            if(i == originString.length() - 1) break;
        }

        // -------------------------------------------------------------- //

        // 4. Create compressed file
        
        RandomAccessFile raf = new RandomAccessFile(destin, "rw");

        raf.writeInt(dictionary.size());

        for(String str : dictionary) raf.writeUTF(str);

        raf.writeInt(output.size());

        if(dictionary.size() < 256) {

            for(int i : output) raf.writeByte(i);
        }
        else if(dictionary.size() < 65536) {

            for(int i : output) raf.writeShort(i);
        }
        else {

            for(int i : output) raf.writeInt(i);
        }

        raf.close();

        // -------------------------------------------------------------- //

        //new File(source).delete();
    }

    // ---------------------------------------------------------------------------------------------- //
    
    public static void decompress(String source, String destin) {

        ArrayList<String> dictionary = new ArrayList<String>();
        ArrayList<Integer> output = new ArrayList<Integer>();

        // -------------------------------------------------------------- //

        // 1. Read dictionary and output
        try {

            RandomAccessFile raf = new RandomAccessFile(source, "rw");

            int dictionarySize = raf.readInt();

            for(int i = 0; i < dictionarySize; i++) dictionary.add(raf.readUTF());

            int outputSize = raf.readInt();

            if(dictionarySize < 256) {

                for(int i = 0; i < outputSize; i++) output.add((int)raf.readByte());
            }
            else if(dictionarySize < 65536) {

                for(int i = 0; i < outputSize; i++) output.add((int)raf.readShort());
            }
            else {

                for(int i = 0; i < outputSize; i++) output.add(raf.readInt());
            }

            // -------------------------------------------------------------- //

            raf.close();
        }
        catch(Exception e) { e.printStackTrace(); }

        // -------------------------------------------------------------- //

        // 2. Create descompact string
        String file = "";

        for(int i : output) file += dictionary.get(i);

        file = file.replaceAll("\\^", " ");

        // -------------------------------------------------------------- //

        // 3. Create descompact file
        String args[] = file.split("~");

        try {
            
            RandomAccessFile raf = new RandomAccessFile(destin, "rw");

            raf.writeInt(Integer.parseInt(args[0]));

            for(int i = 1; i < args.length; i++) {
                //write cabecalho
                raf.writeBoolean(Boolean.parseBoolean(args[i]));
                raf.writeInt(Integer.parseInt(args[++i]));
                raf.writeInt(Integer.parseInt(args[++i]));
                //write titulo
                raf.writeUTF(args[++i]);
                //write tipo
                raf.writeUTF(args[++i]);
                //write data
                raf.writeLong(Long.parseLong(args[++i]));
                //write diretores
                int countDiretores = Integer.parseInt(args[++i]);
                raf.writeInt(countDiretores);
                for(int j = 0; j < countDiretores; j++){
                    raf.writeUTF(args[++i]);
                }
            }

            raf.close();
        }
        catch(Exception e) { e.printStackTrace(); }

        // -------------------------------------------------------------- //

        //new File(source).delete();
    }

    // ------------------------------------------------------------------------------------------------------------ //

}