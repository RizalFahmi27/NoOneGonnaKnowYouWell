package id.developer.tanitionary;

/**
 * Created by Naufal on 8/28/2016.
 */
public class ObjectDomisili implements Comparable<ObjectDomisili> {

    private Integer id;
    private String name, prov;

    public ObjectDomisili(Integer id, String name, String prov){
        this.id = id;
        this.name = name;
        this.prov = prov;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProv() {
        return prov;
    }

    @Override
    public int compareTo(ObjectDomisili another) {
        return this.name.compareTo(another.getName());
    }
}
