package roland.activerecord.utils;

import roland.activerecord.jpa.User;

import java.util.List;

public interface Callback {
    void actionSave(List<User>  users);
}
