package com.example.algoworld.repository

import com.example.algoworld.domain.Stage
import org.springframework.data.jpa.repository.JpaRepository

interface StageRepository : JpaRepository<Stage, Long> {
    fun findAllByOrderById(): List<Stage>
    fun findByUnlockConditionStageId(stageId: Long): List<Stage>
}
