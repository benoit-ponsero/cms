package models.cms;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import play.db.jpa.Model;


/**
 * @author benoit
 */
@Entity
@Table(name="cms_user")
public class User extends Model {

    @Column(nullable=false,length=64)
    public String firstName;

    @Column(nullable=false,length=64)
    public String lastName;

    @Column(length=128, nullable=false,unique=true)
    public String mail;

    @Column(length=16, nullable=false)
    public String password;

    @JoinTable(name="cms_user_role")
    @ManyToMany
    public List<Role> roles;
}
