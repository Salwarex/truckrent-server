package utmn.truckrent.server.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class ServiceHash {

    private static final int[] salt_pattern = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

    private static String saltDisguise(String hash, String salt){

        if(hash == null || salt == null || hash.length() != 64){
            System.err.println("Ошибка обработки соли: Предоставлены неверные данные.");
            return null;
        }

        StringBuilder builder = new StringBuilder(hash);
        for (int i = 0; i < salt.length(); i++) {
            char c = salt.charAt(i);
            int pos = getPos(i);

            if (pos < 0 || pos > builder.length()) {
                throw new IllegalArgumentException("Ошибка обработки соли: Позиция " + pos + " вне диапазона для вставки символа '" + c + "'");
            }

            builder.insert(pos, c);
        }

        return builder.toString();
    }

    private static Map<String, String> splitHashSalt(String combined) {
        char[] chars = combined.toCharArray();
        StringBuilder originalBuilder = new StringBuilder(combined);
        StringBuilder insertedBuilder = new StringBuilder();

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < chars.length - 64; i++) {
            int pos = getPos(i);
            if (pos >= 0 && pos < chars.length) {
                positions.add(pos);
            }
        }

        Collections.sort(positions, Collections.reverseOrder());

        for (int pos : positions) {
            if (pos >= 0 && pos < originalBuilder.length()) {
                char c = originalBuilder.charAt(pos);
                insertedBuilder.insert(0, c);
                originalBuilder.deleteCharAt(pos);
            }
        }

        Map<String, String> result = new HashMap<>();
        result.put("hash", originalBuilder.toString());
        result.put("salt", insertedBuilder.toString());

        return result;
    }

    private static int getPos(int i){
        return salt_pattern[i] + i;
    }

    public static String createSalt(){
        int len = Math.min(salt_pattern.length, 64); //64 = sha256 hashcode lenght
        String alph = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < len; i++){
            int index = random.nextInt(alph.length());
            sb.append(alph.charAt(index));
        }
        return sb.toString();
    }

    private static ArrayList<String> getHashAndSalt(String combined){
        if(combined.length() == 64 + salt_pattern.length){
            Map<String, String> map = splitHashSalt(combined);
            ArrayList<String> result = new ArrayList<>();
            String hashcode = map.get("hash");
            String salt = map.get("salt");

            result.add(hashcode); result.add(salt);
            return result;
        }
        System.err.println("Ошибка обработки хеша: запись не является обработанным хеш-кодом");
        return null;
    }

    public static String sha256(String prov) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(prov.getBytes(StandardCharsets.UTF_8));
            StringBuilder str = new StringBuilder();
            for(byte b: hashBytes){
                String hex = Integer.toHexString(0xFF & b);
                if(hex.length() == 1) str.append('0');
                str.append(hex);
            }
            return str.toString();
        }
        catch (Exception e){
            System.err.println("Can't find SHA-256 method");
        }
        return null;
    }

    public static String hash(String string, String salt){
        if(salt == null) salt = createSalt();
        String hashcode = sha256(salt + string);
        return saltDisguise(hashcode, salt);
    }

    public static boolean isMatch(String provided, String combined){
        ArrayList<String> hs = getHashAndSalt(combined);
        assert hs != null;
        String salt = hs.get(1);

        String providedHash = hash(provided, salt);

        return combined.equalsIgnoreCase(providedHash);
    }
}
