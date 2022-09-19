package com.sept.authmicroservice.payload;

import com.sept.authmicroservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDTO {
    private int userID;
    private List<Role> roles;
}
