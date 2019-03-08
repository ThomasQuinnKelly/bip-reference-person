package gov.va.ocp.vetservices.claims.orm;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


@Entity
public class Claim
{
    @OneToOne
    @JoinColumn(name = "id")
    private Attributes attributes;

    @Id
    @GeneratedValue
    private long id;

    private String type;

    public Attributes getAttributes ()
    {
        return attributes;
    }

    public void setAttributes (Attributes attributes)
    {
        this.attributes = attributes;
    }

    public long getId ()
    {
        return id;
    }

    public void setId (long id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [attributes = "+attributes+", id = "+id+", type = "+type+"]";
    }
}
			