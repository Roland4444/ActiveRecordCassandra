package roland.activerecord;


import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import roland.activerecord.JWT.JwtResponse;
import roland.activerecord.JWT.JwtUtil;
import roland.activerecord.balance.TransferService;
import roland.activerecord.jpa.User;
import roland.activerecord.jpa.UserRepository;

import static java.util.Objects.isNull;
import static roland.activerecord.utils.Constants.*;
import static roland.activerecord.JWT.JwtUtil.getUserUUID;
import static roland.activerecord.jpa.User.*;
import static roland.activerecord.jpa.User.filterByName;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    TransferService transferService;

    @GetMapping("/getallusers")
    public List<User> getAllusers(){
        return User.findAll(userRepo);
    };

    @Cacheable("login_cash")
    @GetMapping("/login")
    public ResponseEntity<String> loginPage() throws IOException {
        String strHtml = null;
        ClassPathResource classPathResource = new ClassPathResource("templates/login.html");
        try {
            byte[] binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            strHtml = new String(binaryData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(strHtml, HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam(name = "login") String login, @RequestParam String password) {
        Optional<User> userOpt;
        if (isNull(login))
            return ResponseEntity.badRequest().body(MUST_SPECIFIED_EMAIL_OR_PHONE);
        userOpt = User.findByEmailOrPhone(userRepo, login);
        if (userOpt.isEmpty())
            return ResponseEntity.status(401).body(USER_NOT_FOUND);
        var user = userOpt.get();
        if (!user.getHashPassword().equals(password))
            return ResponseEntity.status(401).body(WRONG_PASSWD);
        String token = JwtUtil.generateToken(user.getUuid());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmailOrPhone(@RequestHeader(name="Authorization") String token,
                                             @RequestParam(name = "param") String param) {
        if (isNull(token))
            return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        if (isNull(param))
            return new ResponseEntity<String>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        UUID uuid;
        try {
            uuid = getUserUUID(token);
        }
        catch (ExpiredJwtException x) {
            return new ResponseEntity<String>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
        }
        var userOpt = User.findByUUID(userRepo, uuid);
        if (userOpt.isEmpty())
            return new ResponseEntity<String>("USER NOT FOUND", HttpStatus.INTERNAL_SERVER_ERROR);
        var user = userOpt.get();
        return ResponseEntity.ok(addItem(user, param , userRepo));
    }

    @PutMapping("/change")
    public ResponseEntity<?> changeEmailOrPhone(@RequestHeader(name="Authorization") String token,
                                         @RequestParam(name = "paramToChange") String param1,
                                         @RequestParam(name = "targetValue") String param2) {
        if (isNull(token))
            return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        if (isNull(param1) || isNull(param2) )
            return new ResponseEntity<String>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        UUID uuid;
        try {
            uuid = getUserUUID(token);
        }
        catch (ExpiredJwtException x) {
            return new ResponseEntity<String>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
        }
        var userOpt = User.findByUUID(userRepo, uuid);
        if (userOpt.isEmpty())
            return new ResponseEntity<String>("USER NOT FOUND", HttpStatus.INTERNAL_SERVER_ERROR);
        var user = userOpt.get();        ;

        return ResponseEntity.ok(changeItem(user, userRepo, param1, param2));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEmailOrPhone(@RequestHeader(name="Authorization") String token,
                                         @RequestParam(name = "param") String param) {
        if (isNull(token))
            return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        if (isNull(param))
            return new ResponseEntity<String>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        UUID uuid;
        try {
            uuid = getUserUUID(token);
        }
        catch (ExpiredJwtException x) {
            return new ResponseEntity<String>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
        }
        var userOpt = User.findByUUID(userRepo, uuid);
        if (userOpt.isEmpty())
            return new ResponseEntity<String>("USER NOT FOUND", HttpStatus.INTERNAL_SERVER_ERROR);
        var user = userOpt.get();        ;

        return ResponseEntity.ok(deleteItem(user, param, userRepo));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> search(    @RequestParam(name = "phone", required=false) String phone,
                                             @RequestParam(name = "name", required=false) String name,
                                             @RequestParam(name = "email", required=false) String email,
                                             @RequestParam(name = "dateOfBirth", required=false) String dataString,
                                             @RequestParam(name = "page", required=false) Integer page,
                                             @RequestParam(name = "size", required=false) Integer size
    ) {
        var all = User.findAll(userRepo);
        if (!isNull(name))
            all = filterByName(all, name);
        if (!isNull(phone))
            all = filterByPhone(all, phone);
        if (!isNull(email))
            all = filterByEmail(all, email);
        try {
            if (!isNull(dataString) && (new SimpleDateFormat("dd.MM.yyyy").parse(dataString) != null))
                all = filterByName(all, name);
        } catch (ParseException e) {
            return new ResponseEntity<>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        }
        if (isNull(page) || isNull(size))
            return new ResponseEntity<>(all, HttpStatus.OK);
        Pageable pageRequest = PageRequest.of(page, size);

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), all.size());

        List<User> pageContent = all.subList(start, end);
        return ResponseEntity.ok(new PageImpl<>(pageContent, pageRequest, all.size()));
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer( @RequestHeader(name="Authorization") String token,
                                            @RequestParam(name = "sendToUuid") String uuidTarget,
                                            @RequestParam(name = "amount") BigDecimal amount
    ) {
        if (isNull(token))
            return new ResponseEntity<String>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
        if (isNull(uuidTarget))
            return new ResponseEntity<String>("BAD REQUEST", HttpStatus.BAD_REQUEST);
        UUID uuid;
        try {
            uuid = getUserUUID(token);
        }
        catch (ExpiredJwtException x) {
            return new ResponseEntity<String>("INVALID TOKEN", HttpStatus.UNAUTHORIZED);
        }
        var userOpt = User.findByUUID(userRepo, uuid);
        var userReceiverOpt = User.findByUUID(userRepo, UUID.fromString(uuidTarget));
        if (userOpt.isEmpty() || userReceiverOpt.isEmpty())
            return new ResponseEntity<String>("USER NOT FOUND", HttpStatus.INTERNAL_SERVER_ERROR);
        var sender = userOpt.get();
        var receiver = userReceiverOpt.get();                 ;
        return ResponseEntity.ok(
                transferService.transferMoney(sender, receiver, amount, users -> userRepo.saveAll(users)));
    }



}
