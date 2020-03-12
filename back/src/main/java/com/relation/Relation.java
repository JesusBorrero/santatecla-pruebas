package com.relation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Relation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    public enum RelationType {
        ASSOCIATION, AGGREGATION, COMPOSITION, INHERITANCE, USE;
    };

    private RelationType relationType;

    private Long incoming;

    private Long outgoing;

    public Relation() {}

    public Relation(RelationType relationType, Long incoming, Long outgoing) {
        this();
        this.relationType = relationType;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType type) {
        this.relationType = type;
    }

    public Long getIncoming() { return incoming; }

    public void setIncoming(Long incoming) { this.incoming = incoming; }

    public Long getOutgoing() { return outgoing; }

    public void setOutgoing(Long outgoing) { this.outgoing = outgoing; }

    public void update(Relation relation) {
        this.relationType = relation.getRelationType();
        this.incoming = relation.getIncoming();
        this.outgoing = relation.getOutgoing();
    }

    public static int compareType(RelationType relationType1, RelationType relationType2) {
        int value = 0;
        if (relationType1.equals(relationType2)) {
            value = 0;
        } else if (relationType1.equals(RelationType.COMPOSITION)) {
            value = 1;
        } else if (relationType1.equals(RelationType.INHERITANCE)) {
            if (relationType2.equals(RelationType.COMPOSITION)) {
                value = -1;
            } else {
                value = 1;
            }
        } else if (relationType1.equals(RelationType.AGGREGATION)) {
            if (relationType2.equals(RelationType.COMPOSITION) || relationType2.equals(RelationType.INHERITANCE)) {
                value = -1;
            } else {
                value = 1;
            }
        } else if (relationType1.equals(RelationType.ASSOCIATION)) {
            if (relationType2.equals(RelationType.USE)) {
                value = 1;
            } else {
                value = -1;
            }
        } else if (relationType1.equals(RelationType.USE)) {
            value = -1;
        }
        return value;
    }

    @Override
    public boolean equals(Object object) {
        return (((Relation)object).getId() == this.id);
    }

}