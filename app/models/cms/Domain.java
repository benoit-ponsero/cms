package models.cms;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import play.db.jpa.Model;

/**
 * @author benoit
 */
@Entity
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
