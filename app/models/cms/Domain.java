package models.cms;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

/**
 * @author benoit
 */
public class Domain extends Model {
    
    @Column(nullable=false, length=255, unique=true)
    public String host;

    @Column(nullable=false, length=5)
    public String defaultLocale;

    @Column(nullable=false)
    public boolean doTracking = false;

    @OneToMany
    public List<String> supportedLocales;
}
