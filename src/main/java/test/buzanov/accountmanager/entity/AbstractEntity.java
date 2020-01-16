package test.buzanov.accountmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Id
    @NotNull
    private String id = UUID.randomUUID().toString();

    @NotNull
    @Column(updatable = false)
    private Date creation = new Date(System.currentTimeMillis());

    private static final long serialVersionUID = 1L;
}
