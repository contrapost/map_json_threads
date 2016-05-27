package no.westerdals.shiale14.pikachucatcher.DB;

/**
 * Created by Alexander on 26.05.2016.
 */
public class Pikachu {

    private int id;
    private String _id, pikachuId, name, imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPikachuId() {
        return pikachuId;
    }

    public void setPikachuId(String pikachuId) {
        this.pikachuId = pikachuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Pikachu{" +
                "_id='" + _id + '\'' +
                ", pikachuId='" + pikachuId + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
