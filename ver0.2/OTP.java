import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.Base64;

public class OTP {
    
    private static boolean authenticate() throws Exception {
        if(!fileCheck()) {
            setup();
            return true;
        } else {
            System.out.print("\nPlease enter your password: ");
            master_passwd = sc.next();


        }
    }

    private static void setup()
            throws Exception {
        Scanner sc = new Scanner(System.in);
        //create passwd_file path
        String local_auth_string = System.getProperty("user.dir");
        local_auth_string += "/passwd_file";

        //Used for master_passwd path
        String external_auth_string = System.getProperty("user.dir");
        external_auth_string += "/master_passwd";

        //initialize file paths
        Path local_auth_path = Paths.get(local_auth_string);
        Path external_auth_path = Paths.get(external_auth_string);

        //initialize files
        File local_auth_file = new File(external_auth_string);
        File external_auth_file = new File(local_auth_string);

        //delete old files if they exists
        Files.deleteIfExists(local_auth_path);
        Files.deleteIfExists(external_auth_path);

        //create files
        external_auth_file.createNewFile();
        local_auth_file.createNewFile();

        //get master password
        System.out.print("\nPlease provide a master password: ");
        String passwd = sc.next();
        byte[] password = passwd.getBytes();

        //get salts and combine with master password
        encSalt = SecurityFunction.randomNumberGenerator(256);
        macSalt = SecurityFunction.randomNumberGenerator(256);
        byte[] salted_password = Arrays.concatenate(encSalt, password);

        //setup local_auth file
        byte[] hash = SecurityFunction.hash(salted_password);
        byte[] salt_and_hash = Arrays.concatenate(encSalt, hash);

        //write data to local_auth file
        try (FileOutputStream output = new FileOutputStream(local_auth_string)) {
            output.write(salt_and_hash);
            output.close();
        }

        //get hash for passwd_file and append to file
        byte[] local_auth_data = Files.readAllBytes(local_auth_path);
        byte[] encrypted = SecurityFunction.encrypt(local_auth_data, encKey);
        byte[] hmac = SecurityFunction.hmac(encrypted, macKey);
        byte[] salt_hmac_and_encrypted = Arrays.concatenate(macSalt, hmac, encrypted);

        //generate hashes for external_auth_file
        byte[][] pStorage = new byte[3][];
        try {
            for (int i = 0; i < 3; i++) {
                pStorage[i] = SecurityFunction.hash(SecurityFunction.randomNumberGenerator(160));
                System.out.println("Hash " + i + ": " + Base64.getEncoder().encodeToString(pStorage[i]));
            }
        } catch (Exception e) {
            throw e;
        }

        //write data to passwd_file
        try (FileOutputStream output = new FileOutputStream(external_auth_string)) {
            output.write(salt_hmac_and_encrypted);
            output.close();
        }
    }
    public static void main(String[] args) throws Exception {

        
    }
}