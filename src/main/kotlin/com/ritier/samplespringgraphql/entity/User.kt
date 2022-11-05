package com.ritier.samplespringgraphql.entity

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") val id: Long = 0,

    @Column(name = "nickname") val nickname: String,

    @Column(name = "age") val age: Int,

    @OneToOne
    @JoinColumn(name = "profile_img_id")
    val profileImg: Image? = null,

    @OneToMany(mappedBy = "user")
    val postings: List<Posting>? = null
) {
//    override fun toString(): String {
//        return "User{id = $id, nickname = $nickname, age = $age, profile_img_id : $${profileImg.toString()}}"
//    }
}