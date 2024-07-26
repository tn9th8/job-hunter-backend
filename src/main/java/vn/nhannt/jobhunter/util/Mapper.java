package vn.nhannt.jobhunter.util;

public class Mapper {
    public static Long toLong(String sNum) {
        try {
            return Long.valueOf(sNum);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("ID phải là 1 số tự nhiên");
        }
    }
}
