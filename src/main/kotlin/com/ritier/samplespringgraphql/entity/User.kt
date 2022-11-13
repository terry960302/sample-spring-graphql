package com.ritier.samplespringgraphql.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") val id: Long = 0,

    @Column(name = "nickname") val nickname: String,

    @Column(name = "age") val age: Int,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_img_id")
    val profileImg: Image? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    val postings: List<Posting>? = null,

    @Transient
    var hasPosting: Boolean = false,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val credential: UserCredential? = null,
) {

    @PostLoad
    fun postload(){
        hasPosting = if (postings == null) {
            false
        } else {
            postings!!.isNotEmpty()
        }
    }
}