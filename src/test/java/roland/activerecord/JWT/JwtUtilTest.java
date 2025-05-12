package roland.activerecord.JWT;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static roland.activerecord.JWT.JwtUtil.*;

class JwtUtilTest {
    UUID def = UUID.fromString("88dc2e98-569e-43f4-b2c0-453ab3a11d87");

    @Test
    void generateTokenTest() {
        assertNotEquals(null, generateToken(def));
        System.out.println(generateToken(def));
    }

    @Test
    void decodeToken(){
        var token = "eyJhbGciOiJIUzI1NiJ9.eyJVU0VSX0lEIjoiODhkYzJlOTgtNTY5ZS00M2Y0LWIyYzAtNDUzYWIzYTExZDg3IiwiaWF0IjoxNzQ2OTk2MTMyLCJleHAiOjE3NDcwODI1MzJ9.uwJSVuTu1RlCCAnGhkhIX4-jgGi1TSPaL4guM4hwXxE";
        assertEquals(def, getUserUUID(token));
        System.out.println(getUserUUID(token));

    }
}