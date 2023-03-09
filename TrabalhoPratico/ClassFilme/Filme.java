package ClassFilme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

public class Filme {
    private int id;
    private String titulo; // Flexivel
    private String type; // Fixa
    private Date data;
    private String[] diretor;

    // Methods constructions

    public Filme (byte [] ba) throws Exception {
        this.data = new Date();
        this.fromByteArray(ba);
    } // end constructor

    public Filme(int id, String titulo, String type, Date data, String[] diretor) {
        this.id = id;
        this.type = type;
        this.titulo = titulo;
        this.data = data;
        this.diretor = diretor;
    }

    public Filme() {
        this.id = 0;
        this.type = "";
        this.titulo = "";
        this.data = new Date();
        this.diretor = new String[0];
    }

    // End methods constructions

    // Methods gets and sets

    public int getId() {
        return this.id;
    }

    public String gettype() {
        return this.type;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Date getData() {
        return this.data;
    }

    public String[] getDiretor() {
        return this.diretor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void settype(String type) {
        this.type = type;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setDiretor(String[] diretor) {
        this.diretor = diretor;
    }

    // End methods gets and sets

    public static String arrayToString(String directors[]) {
        String s = "{ ";
        if (directors != null) {

            for (int i = 0; i < directors.length; i++) {
                s += directors[i];
                if (i != directors.length - 1)
                    s += ", ";
            } // end for

        } else {
            s += "vazio";
        }
        s += " }";
        return s;
    }

    public static void writeUTFarray(String[] sarr, DataOutputStream dos) throws IOException {
        if (sarr != null) {

            dos.writeInt(sarr.length);
            for (int i = 0; i < sarr.length; i++) {
                dos.writeUTF(sarr[i]);
            }
        }else{
            dos.writeInt(1);
            dos.writeUTF("Nao a diretores");
        }
    }

    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.titulo);
        dos.writeUTF(this.type);
        if(data != null){
            dos.writeLong(this.data.getTime());
        }else{
            dos.writeLong(0);
        }
        
        writeUTFarray(this.diretor, dos);

        byte[] barr = baos.toByteArray();
        baos.close();
        dos.close();

        return barr;
    }

    public static String[] readUTFarray(DataInputStream dis) throws IOException {
        
        int tam = dis.readInt();

        String[] sarr = new String[tam];

        for (int i = 0; i < tam; i++) {
            sarr[i] = dis.readUTF();
        }
        return sarr;
    }

    public void fromByteArray(byte ba[]) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais); 
        
        this.id = dis.readInt();
        this.titulo = dis.readUTF();
        this.type = dis.readUTF();
        this.data.setTime(dis.readLong());
        this.diretor = readUTFarray(dis);
    }

    public String toString() {
        String str = "id= " + this.id + '\n'
                + "title= " + this.titulo + '\n'
                + "type= " + this.type + '\n'
                + "Date= " + this.data + '\n'
                + "Directors= " + arrayToString(this.diretor) + '\n';
        return str;

    }

    public static void main(String[] args) {

    }
}