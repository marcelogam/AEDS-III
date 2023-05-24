package Compactacao;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman {
    class Node {
        public int frequency;
        public char data;
        public Node left;
        public Node right;
    }

    static class Pair<A, B> {
        public A first;
        public B second;
    }

    static Node root;
    static HashMap<Character, String> charMap = new HashMap<>();// hashmap de char pra String do byte O(1) 
    static HashMap<String, Character> codeMap = new HashMap<>();// hashmap de String do byte pra char O(1) 

    public Huffman(HashMap<Character, Integer> frequency) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>(frequency.size(), new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                if (o1.frequency > o2.frequency) {
                    return 1;
                } else if (o1.frequency < o2.frequency) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        for (char c : frequency.keySet()) {
            Node node = new Node();
            node.frequency = frequency.get(c);
            node.data = c;
            node.left = null;
            node.right = null;
            pq.add(node);
        }
        while (pq.size() != 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node();
            parent.frequency = left.frequency + right.frequency;
            parent.data = '\0';
            parent.left = left;
            parent.right = right;
            pq.add(parent);
        }
        root = pq.poll();
    }

    /*
     * percorre a arvore e adiciona o codigo de cada letra no map
     * 
     * @param node: no atual
     * 
     * @param s: codigo da letra
     */
    public void traverse(Node root, String s) {
        if (root.left == null && root.right == null) {// && Character.isLetter(root.data)){
            charMap.put(root.data, s);
            codeMap.put(s, root.data);
            return;
        }
        traverse(root.left, s + '0');
        traverse(root.right, s + '1');
    }

    public static void printCode(Node root, String s) {
        if (root.left == null && root.right == null && Character.isLetter(root.data)) {
            System.out.println(root.data + ":" + s);
            return;
        }
        printCode(root.left, s + "0");
        printCode(root.right, s + "1");
    }

    public void printCode() {
        printCode(root, "");
    }

    static int index = 0;

    public static char travFromBitset(Node root, String s, char ans) {
        if (root.left == null && root.right == null) {
            // ans = root.data;
            // quando chegar no valor nulo retorna
            return root.data;
        }
        if (s.charAt(index) == '1') {// bitSet.get(i)){
            // se o valor do bitset for 1, vai pra direita
            index++;
            ans = travFromBitset(root.right, s, ans);
        } else {
            // else vai pra esquerda
            index++;
            ans = travFromBitset(root.left, s, ans);
        }
        return ans;
    }

    /*
     * descomprime a string de bits
     * 
     * @param s: string a ser decodificada e descomprimida
     */
    public static void decompress(RandomAccessFile source, RandomAccessFile dest) throws IOException {
        source.seek(0);
        String s = new String();
        while (source.getFilePointer() < source.length()) {
            byte b = source.readByte();
            BitSet bitSet = BitSet.valueOf(new byte[] { b });
            for (int i = 7; i >= 0; --i) {
                if (bitSet.get(i)) {
                    s += '1';
                } else {
                    s += '0';
                }
            }
        }
        decompress(s, dest);

    }

    static int lengthDiff = 0;

    public static void decompress(String s, RandomAccessFile dest) {
        String ans = new String();
        while (index < s.length() - lengthDiff) {
            char k = ' ';
            k = travFromBitset(root, s, k);
            ans += k;
        }
        try {
            dest.writeBytes(ans);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * comprime o arquivo @source para o arquivo @dest
     */
    public static void compress(RandomAccessFile source, RandomAccessFile dest) throws IOException {
        // 2508
        String binary = new String();
        while (source.getFilePointer() < source.length()) {
            char c = (char) source.readByte();
            binary += charMap.get(c);
        }
        
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < binary.length(); ++i) {
            tmp.append(binary.charAt(i)); 
            if (tmp.length() == 8) {
                byte b = (byte) Integer.parseInt(tmp.toString(), 2);// passa 8 bits pra byte
                dest.write(b);// escreve o byte no arquivo comprimido
                tmp.setLength(0); // reseta o stringBuilder
            } else if (tmp.length() < 8 && i == binary.length() - 1 && tmp.length() != 0) {
                lengthDiff = 8 - tmp.length();
                while (tmp.length() < 8) {
                    tmp.append('0');
                }
                byte b = (byte) Integer.parseInt(tmp.toString(), 2);// passa 8 bits pra byte
                dest.write(b);// escreve o byte no arquivo comprimido
                tmp.setLength(0); // reseta o stringBuilder
            }
        }
    }
}