package com.example.algoworld.service

import com.example.algoworld.domain.StageStatus
import com.example.algoworld.domain.UserProgress
import com.example.algoworld.dto.ClearStageRequest
import com.example.algoworld.dto.StageWithProgressDto
import com.example.algoworld.repository.StageRepository
import com.example.algoworld.repository.UserProgressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StageProgressService(
    private val stageRepository: StageRepository,
    private val userProgressRepository: UserProgressRepository
) {

    @Transactional
    fun clearStage(request: ClearStageRequest): StageWithProgressDto {
        val stage = stageRepository.findById(request.stageId)
            .orElseThrow { IllegalArgumentException("Stage not found: ${'$'}{request.stageId}") }

        val progress = userProgressRepository.findByUserIdAndStage(request.userId, stage)
            ?: UserProgress(userId = request.userId, stage = stage, status = StageStatus.UNLOCKED)

        progress.markCleared(request.bestScore)
        userProgressRepository.save(progress)

        unlockFollowingStages(request.userId, stage.id)

        return stage.toDto(StageStatus.CLEARED)
    }

    private fun unlockFollowingStages(userId: String, clearedStageId: Long) {
        val dependents = stageRepository.findByUnlockConditionStageId(clearedStageId)
        val progresses = userProgressRepository.findByUserId(userId).associateBy { it.stage.id }.toMutableMap()

        dependents.forEach { nextStage ->
            val current = progresses[nextStage.id]
            if (current == null) {
                progresses[nextStage.id] = userProgressRepository.save(
                    UserProgress(userId = userId, stage = nextStage, status = StageStatus.UNLOCKED)
                )
            } else if (current.status == StageStatus.LOCKED) {
                current.unlockIfLocked()
                userProgressRepository.save(current)
            }
        }
    }
}
