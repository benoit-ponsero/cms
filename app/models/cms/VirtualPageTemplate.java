package models.cms;

import javax.persistence.Entity;
import play.db.jpa.Model;

/**
 * @author benoit
 */
@Entity
public class VirtualPageTemplate extends Model{
    
    public String name;
    
    public String view;
}
