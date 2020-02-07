package test.buzanov.accountmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import test.buzanov.accountmanager.enumurated.Role;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "app_token")
@NoArgsConstructor
@AllArgsConstructor
public class Token extends AbstractEntity  {

    private String value;

//    @Nullable
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
}
