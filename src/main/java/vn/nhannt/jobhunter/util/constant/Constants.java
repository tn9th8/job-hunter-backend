package vn.nhannt.jobhunter.util.constant;

public class Constants {

    // Security
    public static final String jwtKey = "${jobhunter.jwt.base64-secret}";
    public static final String jwtExpiration = "${jobhunter.jwt.token-validity-in-seconds}";
    // Entity
    public static final String Datetime = "yyyy-MM-dd HH-mm-ss a";
    public static final String GMT7 = "GMT+7";
}
