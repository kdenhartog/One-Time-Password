import java.util.*;
import javax.crypto.*;
import java.security.*;
import java.io.*;

/**
 *
 * @author Kyle Den Hartog
 * @version 1.0
 */
public class HexGenerator
{
    SecretKey blowfishKey;
    StringEncrypter blowfishEncrypter;

    /** 8 digit hex code
     *
     * Creates an 8 digit hex code
     * @return code
     */
    private String generator()
    {
        Random r = new Random();
        String code = null;
        for(int y = 0; y < 8; y++){
            if(y < 8){
                int rNum = r.nextInt(61);
                if(rNum <= 9){
                    if(code == null){
                        char temp = (char)(rNum+48);//converts random number to a random char
                        code = Character.toString(temp);//adds a random char between 0 and 9 to code
                    }else{
                        code = code + (char)(rNum+48);//adds a random char between 0 and 9 to code
                    }
                }else if(rNum <= 35 && rNum > 9){
                    if(code == null){
                        char temp = (char)(rNum+55);//converts random number to a random char
                        code = Character.toString(temp);//adds a random char between A and Z to code
                    }else{
                        code = code + (char)(rNum+55);//adds a random char between A and Z to code
                    }
                }else if(rNum <= 61 && rNum > 35){
                    if(code == null){
                        char temp = (char)(rNum+61);//converts random number to a random char
                        code = Character.toString(temp);//adds a random char between a and z to code
                    }else{
                        code = code + (char)(rNum+61);//adds a random char between a and z to code
                    }
                }
            }
        }
        return code;
    }

    /**
     * Method tester
     *
     * @param pStorage the 3 index array of the Phones storage data
     * @param tStorage the 3 index array of the NFC_tag storage data
     * @return if the data is the same return true, if not the same return false
     */
    private boolean tester(String[] pStore, String[] tStore){
        Random rg = new Random();
        int firstPick = rg.nextInt(3);
        int secondPick = rg.nextInt(3);
        while(firstPick == secondPick){
            secondPick = rg.nextInt(3);
            if(firstPick == 0 && secondPick == 1)
            {
                secondPick = rg.nextInt(3);
                firstPick = rg.nextInt(3);
            }
        }
        System.out.println("1st Pick:"+ (firstPick+1) + " 2nd Pick:" + (secondPick+1));
        String pCode = pStore[firstPick] +" "+ pStore[secondPick];
        String tCode = tStore[firstPick] +" "+ tStore[secondPick];
        System.out.println("Phone PassKey:" + pCode);
        System.out.println("Tag PassKey:" + tCode);

        if(pCode.equals(tCode)){
            System.out.println("Tester Results: True");
            return true;
        }else{
            System.out.println("Tester Results: False");
            return false;
        }
    }

    private String encrypt(String[] code){
      try{
          blowfishKey = KeyGenerator.getInstance("Blowfish").generateKey();
          blowfishEncrypter =new StringEncrypter(blowfishKey, blowfishKey.getAlgorithm());
      }catch (NoSuchAlgorithmException e) {
      }
        String temp = code[0] + " " + code[1] + " " + code[2];
        String tempEncrypted  = blowfishEncrypter.encrypt(temp);
        System.out.println("\nEncrypted text:" + tempEncrypted);
        return tempEncrypted;
    }

    private String[] decrypt(String cipherText){
        System.out.println("CipherText:" + cipherText);
        String cDecrypted  = blowfishEncrypter.decrypt(cipherText);
        String[] code = new String[3];
        code[0] = cDecrypted.substring(0,cDecrypted.indexOf(" "));
        code[1] = cDecrypted.substring(cDecrypted.indexOf(" ")+1,cDecrypted.lastIndexOf(" "));
        code[2] = cDecrypted.substring(cDecrypted.lastIndexOf(" ")+1);
        System.out.println("\ndecrypted tStorage:"+ code[0] + " " + code[1] + " " + code[2]);
        return code;
    }

    public static void main(String[] args){
        HexGenerator hg = new HexGenerator();
        Scanner sc = new Scanner(System.in);
        boolean c = true;
        String[] pStorage = new String[3];
        String[] tStorage = new String[3];

        //Generates the first 3 8char strings
        for(int i = 0; i < 3; i++){
          pStorage[i] = hg.generator();
        }
        System.out.println("pStorage:"+pStorage[0]+" "+pStorage[1]+" "+pStorage[2]);
        String cipherText = hg.encrypt(pStorage);
        tStorage = hg.decrypt(cipherText);

        //this is the test loop
        while(c == true){
            if(hg.tester(pStorage,tStorage) == true){
                System.out.println("Access Granted\n\n\n");
                pStorage[0] = pStorage[1];
                pStorage[1] = pStorage[2];
                pStorage[2]= hg.generator();
                System.out.println("new pStorage:"+pStorage[0]+" "+pStorage[1]+" "+pStorage[2]);
                cipherText = hg.encrypt(pStorage);
                tStorage = hg.decrypt(cipherText);
            }else{
                System.out.println("\nAccess Denied");
            }
            System.out.println("\n\ncontinue?");
            if(sc.next().equals("yes")){
            }else{
                c = false;
            }
        }
    }
}
