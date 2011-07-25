package models.cms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import play.db.jpa.Model;

/**
 * @author benoit
 */
@Entity
@Table(name="cms_virtual_page",uniqueConstraints=@UniqueConstraint(columnNames={"path"}))
public class VirtualPage extends Model {

    @Column(nullable=false,length=128)
    public String path;

    @Column(nullable=false,length=255)
    public String view;
}
