package vn.nhannt.jobhunter.util.constant;

public class Constants {

    // Security
    public static final String jwtKey = "${jobhunter.jwt.base64-secret}";
    public static final String accessTokenExpiration = "${jobhunter.jwt.access-token-validity-in-seconds}";
    public static final String refreshTokenExpiration = "${jobhunter.jwt.refresh-token-validity-in-seconds}";
    // Instant for Entity: don't user
    public static final String Datetime = "yyyy-MM-dd HH-mm-ss a";
    public static final String GMT7 = "GMT+7";
}
