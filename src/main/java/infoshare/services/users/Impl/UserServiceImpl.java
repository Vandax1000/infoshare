package infoshare.services.users.Impl;

import infoshare.domain.Role;
import infoshare.domain.User;
import infoshare.services.users.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hashcode on 2015/06/24.
 */
@Service
public class UserServiceImpl implements UserService{

    List<User> users = new ArrayList<>();
    public  void addValues(){
        Set<Role> roles = new HashSet<>();
        Role role = new Role.Builder("CareGiver")
                .description("care giver").id("1").build();
        Role role1 = new Role.Builder("Editor")
                .description("Content Editor").id("2").build();
        roles.add(role);
        roles.add(role1);
        User user = new User.Builder("Hadebe")
                .firstname("Thulebona")
                .othername("Emmanuel")
                .role(roles)
                .id("1")
                .username("thuleh")
                .build();
        users.add(user);
    }
    @Override
    public User find(String s) {
        addValues();
        User user = null;
        for(User user1 : users){
            if(user1.getId().equals(s))
                user= user1;
        }
        return user;
    }

    @Override
    public User save(User entity) {

        User user = new User.Builder(entity.getLastname())
                        .firstname(entity.getFirstname())
                        .othername(entity.getOthername())
                        .role(entity.getRole())
                        .username(entity.getUsername())
                        .enable(entity.isEnable())
                        .build();
        users.add(user);
        System.out.println(user.getLastname());
        return user;
    }

    @Override
    public User merge(User entity) {
        return null;
    }

    @Override
    public void remove(User entity) {

    }

    @Override
    public List<User> findAll() {
        addValues();
        return users;
    }
}
