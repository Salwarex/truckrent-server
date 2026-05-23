package utmn.truckrent.server.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenMaster {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final int byteLength;

    public TokenMaster(int byteLength) throws IllegalArgumentException {
        if(byteLength < 32) throw new IllegalArgumentException("Key minimal length - 32 bytes.");
        this.byteLength = byteLength;
    }

    public String generate(){
        byte[] randomBytes = new byte[byteLength];
        secureRandom.nextBytes(randomBytes);
        return "bm_" + encoder.encodeToString(randomBytes);
    }

    public static boolean isCorrectTokenFormat(String keyCandidate, int byteLength){
        byte[] bytesArr = keyCandidate.getBytes();
        int length = bytesArr.length;

        return length == "bm_".getBytes().length + byteLength && keyCandidate.startsWith("bm_");
    }
}

