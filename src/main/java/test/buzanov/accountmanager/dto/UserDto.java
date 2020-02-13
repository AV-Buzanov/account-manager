package test.buzanov.accountmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.buzanov.accountmanager.enumurated.Role;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String name;
    private Date creation;
    private Set<Role> authorities;
}
