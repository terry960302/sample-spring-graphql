package com.ritier.samplespringgraphql.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="comments")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") val id: Long = 0,

    @ManyToOne(targetEntity = User::class)
    @JoinColumn(name="user_id")
    val user: User,

    @ManyToOne(targetEntity = Posting::class)
    @JoinColumn(name="posting_id") val posting: Posting,

    @Column(name="contents") val contents : String,

    @GeneratedValue
    @Column(name="created_at") val createdAt: LocalDateTime = LocalDateTime.now(),
) {
//    override fun toString(): String {
//        return "Comment { id : $id, userId : $userId, user : ${user.toString()} contents : $contents, postingId : ${postingId.toString()} }"
//    }
}
