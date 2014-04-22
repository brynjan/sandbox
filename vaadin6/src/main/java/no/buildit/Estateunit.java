package no.buildit;

/**
 * This class..
 *
 * @author bno
 */
public class Estateunit {
    private Integer id;
    private String name;

    public Estateunit(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
