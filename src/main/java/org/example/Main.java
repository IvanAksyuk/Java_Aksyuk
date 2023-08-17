
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


import java.io.*;
import java.util.*;


import static java.lang.System.out;



public class Main {
    public static void main(String[] args) {

        boolean sort_descending = Arrays.asList(args).contains("-d");
        boolean sort_ascending = Arrays.asList(args).contains("-a");
        String[] in_files;
        String out_file;
        if (!(sort_descending || sort_ascending)) {
            sort_ascending = true;
            try{
            out_file = args[1];
            in_files = Arrays.copyOfRange(args, 2, args.length);
            }
            catch (ArrayIndexOutOfBoundsException e){
                out.println("Output file is missing.");
                out.print("Output file \"out.txt\" is created." );
                out_file = "out.txt";
                in_files = new String[0];
            }

        } else {

            try{
                out_file = args[2];
                in_files = Arrays.copyOfRange(args, 3, args.length);
            }
            catch (ArrayIndexOutOfBoundsException e){
                out.println("Output file is missing.");
                out.print("Output file \"out.txt\" is created." );
                out_file = "out.txt";
                in_files = new String[0];
            }
        }


        boolean is_integer = Arrays.asList(args).contains("-i");

        //for (String arg : args) out.print(arg + " ");

        out.println();
        merge_sort(in_files, out_file, is_integer, sort_ascending);

    }


    static void merge_sort(String[] in_files, String out_file, boolean type_is_int, boolean sort_is_ascending)  {
        out.println("Output file: "+out_file);
        out.print("Input files: ");
        for (String inFile : in_files) {
            out.print(inFile + " ");

        }
        out.println();


        int[] sorted_int = new int[in_files.length];

        String[] sorted_str = new String[in_files.length];

        BufferedReader[] br = new BufferedReader[in_files.length];

        for (int i=0;i<in_files.length;i++) {

            try {
                br[i] = new BufferedReader(new FileReader(in_files[i]));
            } catch (FileNotFoundException e) {
                File f = new File( in_files[i]);

                try {
                    f.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    br[i] =  new BufferedReader(new FileReader(f));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                //throw new RuntimeException(e);
            }


        }

        boolean [] free_indexes = new boolean[in_files.length];
        Arrays.fill(free_indexes, true);
        int last_int = 0;
        String last_str = "";
        boolean k = true;

        FileWriter writer;
        try {
            writer = new FileWriter(out_file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while(true){

            //out.println(sorted);


            for (int i=0;i<in_files.length;i++) {
                if(free_indexes[i]) {
                    String s;
                    try {
                        while ((s = br[i].readLine()) != null) {

                            if (!s.isEmpty()) {


                                try {
                                    if(type_is_int){
                                        sorted_int[i] = Integer.parseInt(s);}
                                    else {
                                        sorted_str[i] = s;
                                    }

                                    free_indexes[i] = false;
                                    break;
                                } catch (NumberFormatException ignored) {

                                }

                            }

                        }
                    } catch (IOException ignored) {

                    }

                }
            }
            int min_ind;
            if(type_is_int) min_ind = index_of_min_or_max(sorted_int, free_indexes, sort_is_ascending);
            else min_ind = index_of_min_or_max(sorted_str, free_indexes, sort_is_ascending);
            if(min_ind ==-1){
                break;
            }
            if(type_is_int){
                if(((!sort_is_ascending && (!a_less_b(last_int, sorted_int[min_ind])||last_int==sorted_int[min_ind]) )||
                        (sort_is_ascending && a_less_b(last_int,sorted_int[min_ind]))) || k){
                    k = false;

                    String s_ = String.valueOf(sorted_int[min_ind]);
                    try {
                        writer.write(s_+"\n");
                    } catch (IOException ignored) {

                    }


                    //out.println(sorted_int[min_ind]);
                    last_int = sorted_int[min_ind];
                }} else{
                if(((!sort_is_ascending && (!a_less_b(last_str, sorted_str[min_ind])|| last_str.equals(sorted_str[min_ind])) )||
                        (sort_is_ascending && a_less_b(last_str,sorted_str[min_ind]))) || k){
                    k = false;

                    String s_ = String.valueOf(sorted_str[min_ind]);
                    try {
                        writer.write(s_+"\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    //out.println(sorted_str[min_ind]);
                    last_str = sorted_str[min_ind];
                }
            }





            free_indexes[min_ind]= true;


        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    out.println("Done!");
    }


    static int index_of_min_or_max(int[] arr, boolean[] free_indexes, boolean sort_is_asc){

        int m_index = -1;

        for(int i=0;i<free_indexes.length;i++){
            if(!free_indexes[i]) {
                m_index= i;
                break;}
        }
        if(m_index == -1){
            return -1;
        }
        for(int i=m_index+1;i<arr.length;i++){
            if(sort_is_asc){
                if( !free_indexes[i] && a_less_b(arr[i] ,arr[m_index])){
                    m_index = i;
                }} else {
                if(!free_indexes[i]  && !a_less_b(arr[i] ,arr[m_index])){
                    m_index = i;
                }
            }
        }
        return m_index;
    }
    static int index_of_min_or_max(String[] arr, boolean[] free_indexes, boolean sort_is_asc){

        int m_index = -1;

        for(int i=0;i<free_indexes.length;i++){
            if(!free_indexes[i]) {
                m_index= i;
                break;}
        }
        if(m_index == -1){
            return -1;
        }
        for(int i=m_index+1;i<arr.length;i++){
            if(sort_is_asc){
                if(!free_indexes[i] && a_less_b(arr[i] ,arr[m_index]) ){
                    m_index = i;
                }} else {
                if(!free_indexes[i] && !a_less_b(arr[i] ,arr[m_index])  ){
                    m_index = i;
                }
            }
        }
        return m_index;
    }
    static boolean a_less_b(int a, int b) {
        return a<=b;
    }
    static boolean a_less_b(String a, String b) {
        return a.compareTo(b)<=0;
    }
}
