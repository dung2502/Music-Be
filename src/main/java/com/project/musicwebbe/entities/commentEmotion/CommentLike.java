package com.project.musicwebbe.entities.commentEmotion;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
public class CommentLike extends CommentEmotion {

}
