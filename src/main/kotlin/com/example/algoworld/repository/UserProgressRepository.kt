package com.example.algoworld.repository

import com.example.algoworld.domain.Stage
import com.example.algoworld.domain.UserProgress
import org.springframework.data.jpa.repository.JpaRepository

interface UserProgressRepository : JpaRepository<UserProgress, Long> {
    fun findByUserId(userId: String): List<UserProgress>
    fun findByUserIdAndStage(userId: String, stage: Stage): UserProgress?
}
