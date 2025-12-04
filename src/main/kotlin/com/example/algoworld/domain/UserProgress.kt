package com.example.algoworld.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "user_progress",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "stage_id"])]
)
data class UserProgress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage_id", nullable = false)
    val stage: Stage,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: StageStatus = StageStatus.LOCKED,

    var bestScore: Int? = null
) {
    fun markCleared(score: Int?) {
        status = StageStatus.CLEARED
        bestScore = score ?: bestScore
    }

    fun unlockIfLocked() {
        if (status == StageStatus.LOCKED) {
            status = StageStatus.UNLOCKED
        }
    }
}
