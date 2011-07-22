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
@Table(name="nemo_navigation_mapped_item",uniqueConstraints=@UniqueConstraint(columnNames={"source", "language"}))
public class NavigationMappedItem extends Model {

    @Column(nullable=false,length=128)
    public String source;

    @Column(nullable=false,length=128)
    public String destination;

    @Column(nullable=true,length=2)
    public String language;

    @Column(nullable=false)
    public boolean redirect = false;
}
