package com.question;

import com.itinerary.block.Block;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@ApiModel(description = "Question model")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SerializedName("questionId")
    @ApiModelProperty(notes = "The question ID. It is unique",  required = true)
    protected long id;

    @ApiModelProperty(notes = "The question subtype", required = true)
    protected String subtype;

    @ApiModelProperty(notes = "The question itself", required = true)
    protected String questionText;

    @ApiModelProperty(notes = "Total answers to a question")
    protected int totalAnswers;
    @ApiModelProperty(notes = "Total correct answers to a question")
    protected int totalCorrectAnswers;
    @ApiModelProperty(notes = "Total wrong answers to a question")
    protected int totalWrongAnswers;

    @ApiModelProperty(notes = "List of blocks in which the question is found", required = true)
    @ManyToMany(fetch = FetchType.EAGER)
    protected Set<Block> blocks;

    public Question() {
        this.subtype = this.getClass().getSimpleName();
        this.totalAnswers = 0;
        this.totalCorrectAnswers = 0;
        this.totalWrongAnswers = 0;
        this.blocks = new HashSet<>();
    }

    public Question(String questionText) {
        this();
        this.questionText = questionText;
        this.blocks = new HashSet<>();
    }

    /**
     * Getters and Setters
     */

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getSubtype() {
        return subtype;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public int getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public int getTotalWrongAnswers() {
        return totalWrongAnswers;
    }

    public Set<Block> getBlocks() {
        return blocks;
    }

    public void addBlock(Block block){
        this.blocks.add(block);
    }

    public void deleteBlock(Block block){
        this.blocks.remove(block);
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public void setTotalCorrectAnswers(int totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public void setTotalWrongAnswers(int totalWrongAnswers) {
        this.totalWrongAnswers = totalWrongAnswers;
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    public void addBlocks(List<Block> blocks) {
        this.blocks.addAll(blocks);
    }

    public void deleteBlocks(List<Block> blocks) {
        this.blocks.removeAll(blocks);
    }

    public void increaseTotalCorrectAnswers() {
        this.totalCorrectAnswers += 1;
        this.totalAnswers += 1;
    }

    public void increaseTotalWrongAnswers() {
        this.totalWrongAnswers += 1;
        this.totalAnswers += 1;
    }
}