package com.relation;

public class RelationDto {

    protected long id;
    public enum RelationType {
        ASSOCIATION, AGGREGATION, COMPOSITION, INHERITANCE, USE;
    }
    private RelationType relationType;
    private Long incoming;
    private Long outgoing;

    public RelationDto() {}

    public RelationDto(RelationType relationType, Long incoming, Long outgoing) {
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
}