package pukhalsky;

import com.google.gson.stream.JsonReader;

import java.io.*;


public class Entire {
    private static String access = "Access allowed";

    private static void checkForAllowed(JsonReader reader, String[] IpGroups, int level) throws IOException {
        reader.beginObject();
        String name = "Access allowed";
        while(reader.hasNext())
        {
            name = reader.nextName();

            if(name.equals(IpGroups[level])){
                try{
                    reader.nextNull();
                    access = "Access disallowed";
                    return;
                }catch(IllegalStateException e){
                    checkForAllowed(reader, IpGroups, ++level);
                    break;
                }
            }else{
                reader.skipValue();
            }
        }

        return;
    }

    public static void main(String[] args) {
        String entireIp = null;

        //reading an entire ip from command line
        try {
            entireIp = args[0];
        }catch(IndexOutOfBoundsException e){
            System.out.println("Please input ip-address");
        }

        //generating array of groups
        String[] IpGroups = entireIp.split("[.]");

        //search for allowed
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(Entire.class.getResourceAsStream("/pukhalsky/blacklist.json"), "UTF-8"));
            checkForAllowed(reader, IpGroups, 0);
        }catch(FileNotFoundException f){
            f.printStackTrace();
        }catch(IOException i){
            i.printStackTrace();
        }

        System.out.println(access);
    }
}
