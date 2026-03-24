package com.mod.cx.post_sales_orchestrator.service;


import com.mod.cx.post_sales_orchestrator.dto.UpdateUserPassRequest;
import com.mod.cx.post_sales_orchestrator.dto.UserRequest;
import com.mod.cx.post_sales_orchestrator.jooq.tables.daos.UsersDao;
import com.mod.cx.post_sales_orchestrator.jooq.tables.pojos.Users;
import com.mod.cx.post_sales_orchestrator.jooq.tables.records.UsersRecord;
import com.mod.cx.post_sales_orchestrator.service.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DAO;
import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mod.cx.post_sales_orchestrator.jooq.Tables.USERS;

@Service
@RequiredArgsConstructor
public class UserService extends BaseServiceImpl<UsersRecord, Users, Long> {

    
    private final DSLContext dsl;
    private final PasswordEncoder passwordEncoder;
    private final UsersDao usersDao;
    
    @Override
    protected DAO<UsersRecord, Users, Long> getDao() {
        return usersDao;
    }
    
    public List<Users> getClientUsers(Long clientId, int page, int size) {
        int offset = page * size;

        return dsl.selectFrom(USERS)
                .where(USERS.CLIENT_ID.eq(clientId))
                .limit(size)
                .offset(offset)
                .fetchInto(Users.class);
    }

    @Override
    public void deleteById(Long id) {
        int deletedRows = dsl.deleteFrom(USERS)
                .where(USERS.ID.eq(id))
                .execute();
        if (deletedRows == 0) {
            throw new RuntimeException("User not found");
        }
    }

    public Users updateUser(Long id, UserRequest request) {
        UsersRecord user = dsl.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if(request.getLastName() != null) user.setLastName(request.getLastName());
        if(request.getEmail() != null) user.setEmail(request.getEmail());

        user.store();

        return user.into(Users.class);
    }
    
    public Users makeUserActive(Long id) {
        UsersRecord user = dsl.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive((byte) 1);
        user.store();

        return user.into(Users.class);
    }

    public Users makeUserInActive(Long id) {
        UsersRecord user = dsl.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive((byte) 0);
        user.store();

        return user.into(Users.class);
    }
    
    public void updatePassword(Long id, UpdateUserPassRequest request) {
        UsersRecord user = dsl.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional()
                .orElseThrow(()-> new RuntimeException("User not found"));
        
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }
        
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.store();
    }
    
}
