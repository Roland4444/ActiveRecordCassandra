package roland.activerecord.JWT;

import lombok.Data;
@Data
public class JwtResponse {
    private String token;
    public JwtResponse(String token) {
        this.token = token;
    }
}
