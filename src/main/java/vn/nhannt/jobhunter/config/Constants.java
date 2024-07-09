package vn.nhannt.jobhunter.config;

public class Constants {

    // Security
    public static final String jwtKey = "${jobhunter.jwt.base64-secret}";
    public static final String jwtExpiration = "${jobhunter.jwt.token-validity-in-seconds}";
    //
}
