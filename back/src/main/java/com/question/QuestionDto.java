package com.question;

import com.itinerary.block.BlockDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionDto {

    protected long id;
    protected String subtype;
    protected String questionText;
    protected int totalAnswers;
    protected int totalCorrectAnswers;
    protected int totalWrongAnswers;
    protected Set<BlockDto> blocks;

    public QuestionDto() {
        this.subtype = this.getClass().getSimpleName();
        this.totalAnswers = 0;
        this.totalCorrectAnswers = 0;
        this.totalWrongAnswers = 0;
        this.blocks = new HashSet<>();
    }

    public QuestionDto(String questionText) {
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

    public Set<BlockDto> getBlocks() {
        return blocks;
    }

    public void addBlock(BlockDto block){
        this.blocks.add(block);
    }

    public void deleteBlock(BlockDto block){
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

    public void setBlocks(Set<BlockDto> blocks) {
        this.blocks = blocks;
    }

    public void addBlocks(List<BlockDto> blocks) {
        this.blocks.addAll(blocks);
    }

    public void deleteBlocks(List<BlockDto> blocks) {
        this.blocks.removeAll(blocks);
    }
}