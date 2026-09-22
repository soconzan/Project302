package org.example.project302.deal.dto;

import lombok.Data;
import org.example.project302.user.entity.User;

@Data
public class GetSimpleRvwRes {
    private String grades;
    private Float score;
    private int[] simpleReviews;

    public GetSimpleRvwRes(User user, int[] simpleReviews) {
        this.grades = getGrades(user.getScore());
        this.score = user.getScore();
        this.simpleReviews = simpleReviews;
    }

    private String getGrades(Float score) {
        if (score == null)
            return "--";
        else if (score >= 90)
            return "A+";
        else if (score >= 80)
            return "A";
        else if (score >= 70)
            return "B+";
        else if (score >= 60)
            return "B";
        else if (score >= 50)
            return "C+";
        else if (score >= 40)
            return "C";
        else if (score >= 30)
            return "D+";
        else if (score >= 20)
            return "D";
        else
            return "F";
    }
}
