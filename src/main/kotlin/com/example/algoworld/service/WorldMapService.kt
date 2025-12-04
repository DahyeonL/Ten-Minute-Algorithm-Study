package com.example.algoworld.service

import com.example.algoworld.domain.Area
import com.example.algoworld.domain.Stage
import com.example.algoworld.domain.StageStatus
import com.example.algoworld.domain.UserProgress
import com.example.algoworld.dto.AreaDto
import com.example.algoworld.dto.StageWithProgressDto
import com.example.algoworld.dto.WorldMapResponse
import com.example.algoworld.repository.AreaRepository
import com.example.algoworld.repository.StageRepository
import com.example.algoworld.repository.UserProgressRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorldMapService(
    private val areaRepository: AreaRepository,
    private val stageRepository: StageRepository,
    private val userProgressRepository: UserProgressRepository
) {

    @Transactional
    fun getWorldMap(userId: String): WorldMapResponse {
        val areas = areaRepository.findAll().sortedBy { it.orderIndex }
        val stages = stageRepository.findAllByOrderById()
        val progresses = userProgressRepository.findByUserId(userId).associateBy { it.stage.id }

        val newProgress = mutableListOf<UserProgress>()

        val stageDtos = stages.map { stage ->
            val currentProgress = progresses[stage.id]
            val status = currentProgress?.status ?: run {
                val unlocked = stage.unlockConditionStageId == null ||
                    progresses[stage.unlockConditionStageId]?.status == StageStatus.CLEARED
                if (unlocked) StageStatus.UNLOCKED else StageStatus.LOCKED
            }

            if (currentProgress == null && status != StageStatus.LOCKED) {
                newProgress += UserProgress(userId = userId, stage = stage, status = status)
            }

            stage.toDto(status)
        }

        if (newProgress.isNotEmpty()) {
            userProgressRepository.saveAll(newProgress)
        }

        return WorldMapResponse(
            areas = areas.map { it.toDto() },
            stages = stageDtos
        )
    }
}

fun Area.toDto() = AreaDto(
    id = id,
    name = name,
    description = description,
    orderIndex = orderIndex,
    posX = posX,
    posY = posY
)

fun Stage.toDto(status: StageStatus) = StageWithProgressDto(
    id = id,
    areaId = area.id,
    name = name,
    description = description,
    difficulty = difficulty,
    status = status,
    problemType = problemType,
    posX = posX,
    posY = posY
)
