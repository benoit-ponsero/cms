package models.cms;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import play.db.jpa.Model;

/**
 * @author benoit
 */
@Entity
@Table(name="cms_domain")
public class Domain extends Model {
    
    @Column(nullable=false, length=255, unique=true)
    public String host;

    @Column(nullable=false, length=5)
    public String defaultLocale;

    @Column(nullable=false)
    public boolean doTracking = false;

    @ElementCollection
    public List<String> supportedLocales;
}
