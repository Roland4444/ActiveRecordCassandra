package roland.activerecord.jpa;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import roland.activerecord.utils.Values;

import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import static roland.activerecord.utils.Values.OPERATION_PERFORM_ITEM.*;
import static roland.activerecord.utils.Values.isEmail;
import static roland.activerecord.utils.Values.isTelephoneNumber;

@Data
@Table("users")
public class User {
    @PrimaryKey
    private UUID uuid;
    private Set<String> phones;
    private Set<String> emails;
    private String account;
    private String name;
    private Date dateOfBirth;
    private String hashPassword;
    private BigDecimal currentBalance;
    private BigDecimal initialBalance;

    public static List<User> findAll(UserRepository userRepo) {
        return userRepo.findAll();
    }
    public static Slice<User> findAll(UserRepository userRepo, Pageable pageable) {
        return userRepo.findAll(pageable);
    }
    public static Values.OPERATION_PERFORM_ITEM changeItem(User user, UserRepository repo, String initialItem,
                                                           String changeTo){
        if (findByEmailOrPhone(repo, changeTo).isPresent())
            return Values.OPERATION_PERFORM_ITEM.ITEM_EXIST_IN_OTHER;
        if (isEmail(initialItem)){
            if (!isEmail(changeTo))
                return BAD_VALUE_TO_UPDATE;
            if (!user.getEmails().contains(initialItem))
                return MODIFYING_PARAM_NOT_FOUND;
            user.getEmails().remove(initialItem);
            user.getEmails().add(changeTo);
            repo.save(user);
            return SUCCESS;
        }
        if (isTelephoneNumber(initialItem)){
            if (!isTelephoneNumber(changeTo))
                return BAD_VALUE_TO_UPDATE;
            if (!user.getPhones().contains(initialItem))
                return MODIFYING_PARAM_NOT_FOUND;
            user.getPhones().remove(initialItem);
            user.getPhones().add(changeTo);
            repo.save(user);
            return SUCCESS;
        }
        return Values.OPERATION_PERFORM_ITEM.UKNOWN_INPUT_VALUES;
    };

    public static Values.OPERATION_PERFORM_ITEM addItem(User user, String item, UserRepository repo){
        if (findByEmailOrPhone(repo, item).isPresent())
            return Values.OPERATION_PERFORM_ITEM.ITEM_EXIST_IN_OTHER;
        if (isEmail(item)) {
            user.getEmails().add(item);
            repo.save(user);
            return Values.OPERATION_PERFORM_ITEM.SUCCESS;
        }
        if (isTelephoneNumber(item)) {
            user.getPhones().add(item);
            repo.save(user);
            return Values.OPERATION_PERFORM_ITEM.SUCCESS;
        }
        return Values.OPERATION_PERFORM_ITEM.UKNOWN_INPUT_VALUES;
    };

    public static Values.OPERATION_PERFORM_ITEM deleteItem(User user, String item, UserRepository repo){
        if (isEmail(item)) {
            if (user.getEmails().size()==1)
                return Values.OPERATION_PERFORM_ITEM.CANT_DELETE_LAST_ELEM;
            user.getEmails().remove(item);
            repo.save(user);
            return Values.OPERATION_PERFORM_ITEM.SUCCESS;
        }
        if (isTelephoneNumber(item)) {
            if (user.getPhones().size()==1)
                return Values.OPERATION_PERFORM_ITEM.CANT_DELETE_LAST_ELEM;
            user.getPhones().remove(item);
            repo.save(user);
            return Values.OPERATION_PERFORM_ITEM.SUCCESS;
        }
        return Values.OPERATION_PERFORM_ITEM.UKNOWN_INPUT_VALUES;
    };

    public static Optional<User> findByEmailOrPhone(UserRepository repo, String entry){
        return repo.findAll().stream().filter(a-> a.getEmails().contains(entry)
                || a.getPhones().contains(entry)).findFirst();
    };

    public static Optional<User> findByUUID(UserRepository repo, UUID uuid){
        return repo.findById(uuid);
    };

    public static List<User>filterByName(List<User> input, String name){
        return input.stream().filter(a -> a.getName().contains(name)).collect(Collectors.toList());
    };
    public static List<User>filterByEmail(List<User> input, String email){
        return input.stream().filter(a -> a.getEmails().contains(email)).collect(Collectors.toList());
    };
    public static List<User>filterByPhone(List<User> input, String phone){
        return input.stream().filter(a -> a.getPhones().contains(phone)).collect(Collectors.toList());
    };
    public static List<User>filterByDate(List<User> input, Date date){
        return input.stream().filter(a ->  a.getDateOfBirth().after(date)).collect(Collectors.toList());
    };




}

