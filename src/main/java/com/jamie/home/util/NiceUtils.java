package com.jamie.home.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.auth.Data;
import com.jamie.home.api.model.auth.Key;
import com.jamie.home.api.model.common.SEARCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class NiceUtils {
    private static final String access_token = "f9e98aeb-d01d-4e01-abd2-4c759604c246";
    private static final String client_id = "b5e63960-129c-4ea1-a71d-8a310d42e408";
    private static final String client_secret = "8d828d3bb6483b90756d42edb41aa31171e0ca";
    private static final Logger logger = LoggerFactory.getLogger(NiceUtils.class);

    public static ResponseEntity<String> makeToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/x-www-form-urlencoded");
        long current_timestamp = new Date().getTime()/1000;
        String encodedString = client_id+":"+client_secret;
        String Authorization = "Basic " +  Base64.getUrlEncoder().encodeToString(encodedString.getBytes());
        headers.add("Authorization",Authorization);
        headers.add("grant_type","client_credentials");
        headers.add("scope","default");

        // 파라미터 생성
        String requestJson = "grant_type=client_credentials&scope=default";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        // 요청
        RestTemplate rt = new RestTemplate();
        String uri = "https://svc.niceapi.co.kr:22001/digital/niceid/oauth/oauth/token";
        ResponseEntity<String> response = rt.exchange(
                uri, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        System.out.println(response);

        return response;
    }

    public static Key requestEncryptionToken(SEARCH search) throws Exception {
        String req_dtim = new SimpleDateFormat("yyyyMMddhhmmss").format(new Timestamp(System.currentTimeMillis()));
        UUID uuid = UUID.randomUUID();
        String req_no = uuid.toString().substring(6);
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        long current_timestamp = new Date().getTime()/1000;
        String encodedString = access_token+":"+current_timestamp+":"+client_id;
        String Authorization = "bearer " +  Base64.getUrlEncoder().encodeToString(encodedString.getBytes());
        headers.add("Authorization",Authorization);
        headers.add("client_id",client_id);
        headers.add("ProductID","2101979031");

        // 파라미터 생성
        String requestJson = "{\n" +
                "      \"dataHeader\":{\"CNTY_CD\":\"ko\"},\n" +
                "      \"dataBody\":{\"req_dtim\": \""+req_dtim+"\",\n" +
                "                       \"req_no\":\""+req_no+"\",\n" +
                "                       \"enc_mode\":\"1\"}\n" +
                "}";
        //logger.info("Request JSON ="+requestJson); // Construct your JSON
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        // 요청
        RestTemplate rt = new RestTemplate();
        String uri = "https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token";
        ResponseEntity<String> response = rt.exchange(
                uri, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        // 대칭키 생성
        ObjectMapper mapper = new ObjectMapper();
        Data resultData = mapper.readValue(response.getBody(), Data.class);
        String value = req_dtim.trim()+req_no.trim()+resultData.getDataBody().getToken_val().trim();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(value.getBytes());
        byte[] arrHashValue = md.digest();
        String resultVal = new String(Base64.getEncoder().encode(arrHashValue));
        String key = resultVal.substring(0,16);
        String iv = resultVal.substring(resultVal.length()-16,resultVal.length());
        String hmac_key = resultVal.substring(0,32);

        // 요청데이터 암호화
        String reqData = "{\"requestno\":\""+req_no+"\""+
                ",\"returnurl\":\""+search.getReturnurl()+"\"" +
                ",\"methodtype\":\"get\"" +
                ",\"sitecode\":\""+resultData.getDataBody().getSite_code()+"\"}";

        //logger.info("reqData JSON ="+reqData); // Construct your JSON

        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted = c.doFinal(reqData.trim().getBytes());
        String enc_data = new String(Base64.getEncoder().encode(encrypted));

        byte[] hmacSha256 = hmac256(hmac_key.getBytes(), enc_data.getBytes());
        String integrity_value = Base64.getEncoder().encodeToString(hmacSha256);

        Key result = new Key(key, iv, hmac_key, req_dtim, req_no, enc_data, integrity_value, resultData.getDataBody());

        return result;
    }

    private static byte[] hmac256(byte[] secretKey,byte[] message){
        byte[] hmac256 = null;
        try{
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
            mac.init(sks);
            hmac256 = mac.doFinal(message);
            return hmac256;
        } catch(Exception e){
            throw new RuntimeException("Failed to generate HMACSHA256 encrypt");
        }
    }

    public static String decodeResult(SEARCH search) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        String encData = search.getEnc_data();
        String key = search.getAuth_key(); //요청 시 암호화한 key와 동일
        String iv = search.getAuth_iv(); //요청 시 암호화한 iv와 동일

        // 복호화
        SecretKey secureKey = new SecretKeySpec(key.trim().getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.trim().getBytes()));
        byte[] cipherEnc = Base64.getDecoder().decode(encData);
        String resData = new String(c.doFinal(cipherEnc), "euc-kr");

        return resData;
    }

    public static Data requestForeignerCheck(SEARCH search) throws Exception {
        String req_dtim = new SimpleDateFormat("yyyyMMddhhmmss").format(new Timestamp(System.currentTimeMillis()));
        UUID uuid = UUID.randomUUID();
        String req_no = uuid.toString().substring(6);
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        long current_timestamp = new Date().getTime()/1000;
        String encodedString = access_token+":"+current_timestamp+":"+client_id;
        String Authorization = "bearer " +  Base64.getUrlEncoder().encodeToString(encodedString.getBytes());
        headers.add("Authorization",Authorization);
        headers.add("client_id",client_id);
        headers.add("ProductID","2101960011");

        // 파라미터 생성
        String requestJson = "{\n" +
                "      \"dataHeader\":{\"CNTY_CD\":\"ko\"},\n" +
                "      \"dataBody\":{\"req_dtim\": \""+req_dtim+"\",\n" +
                "                       \"req_no\":\""+req_no+"\",\n" +
                "                       \"enc_mode\":\"1\"}\n" +
                "}";
        //logger.info("Request JSON ="+requestJson); // Construct your JSON
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        // 요청
        RestTemplate rt = new RestTemplate();
        String uri = "https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/common/crypto/token";
        ResponseEntity<String> response = rt.exchange(
                uri, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        // 대칭키 생성
        ObjectMapper mapper = new ObjectMapper();
        Data resultData = mapper.readValue(response.getBody(), Data.class);
        String value = req_dtim.trim()+req_no.trim()+resultData.getDataBody().getToken_val().trim();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(value.getBytes());
        byte[] arrHashValue = md.digest();
        String resultVal = new String(Base64.getEncoder().encode(arrHashValue));
        String key = resultVal.substring(0,16);
        String iv = resultVal.substring(resultVal.length()-16,resultVal.length());
        String hmac_key = resultVal.substring(resultVal.length()-32,resultVal.length());


        // 외국인등록번호, 이름 암호화
        String foreigner_id = search.getForeigner_number();
        String foreigner_name = search.getForeigner_name();

        // 등록번호
        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
        byte[] encrypted_id = c.doFinal(foreigner_id.trim().getBytes());
        String enc_foreigner_id = new String(Base64.getEncoder().encode(encrypted_id));
        byte[] encrypted_name = c.doFinal(foreigner_name.trim().getBytes());
        String enc_name = new String(Base64.getEncoder().encode(encrypted_name));


        String target_msg = resultData.getDataBody().getToken_version_id().trim() + enc_foreigner_id.trim() + enc_name.trim();
        byte[] hmacSha256 = hmac256(hmac_key.getBytes(), target_msg.getBytes());
        String integrity_value = Base64.getEncoder().encodeToString(hmacSha256);
        String token_version_id = resultData.getDataBody().getToken_version_id();


        // 헤더 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-Type","application/json");
        long current_timestamp2 = new Date().getTime()/1000;
        String encodedString2 = access_token+":"+current_timestamp2+":"+client_id;
        String Authorization2 = "bearer " +  Base64.getUrlEncoder().encodeToString(encodedString2.getBytes());
        headers2.add("Authorization",Authorization2);
        headers2.add("client_id",client_id);
        headers2.add("ProductID","2101960011");

        // 파라미터 생성
        String requestJson2 = "{\n" +
                "      \"dataHeader\":{\"CNTY_CD\":\"ko\"},\n" +
                "      \"dataBody\":{\"token_version_id\": \""+token_version_id+"\",\n" +
                "                       \"integrity_value\":\""+integrity_value+"\",\n" +
                "                       \"enc_foreigner_id\":\""+enc_foreigner_id+"\",\n" +
                "                       \"enc_name\":\""+enc_name+"\"}\n" +
                "}";
        HttpEntity<String> entity2 = new HttpEntity<String>(requestJson2, headers2);

        // 요청
        RestTemplate rt2 = new RestTemplate();
        String uri2 = "https://svc.niceapi.co.kr:22001/digital/niceid/api/v1.0/name/foreigner/check";
        ResponseEntity<String> response2 = rt2.exchange(
                uri2, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity2, // {요청할 때 보낼 데이터}
                String.class //{요청시 반환되는 데이터 타입}
        );

        // 결과값 확인
        ObjectMapper mapper2 = new ObjectMapper();
        Data resultData2 = mapper2.readValue(response2.getBody(), Data.class);
        return resultData2;
    }
}
