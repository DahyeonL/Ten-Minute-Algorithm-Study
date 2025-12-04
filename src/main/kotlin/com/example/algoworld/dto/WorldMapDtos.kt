package com.example.algoworld.dto

import com.example.algoworld.domain.StageStatus

data class WorldMapResponse(
    val areas: List<AreaDto>,
    val stages: List<StageWithProgressDto>
)

data class AreaDto(
    val id: Long,
    val name: String,
    val description: String?,
    val orderIndex: Int,
    val posX: Int,
    val posY: Int
)

data class StageWithProgressDto(
    val id: Long,
    val areaId: Long,
    val name: String,
    val description: String,
    val difficulty: Int,
    val status: StageStatus,
    val problemType: String,
    val posX: Int?,
    val posY: Int?
)

data class ClearStageRequest(
    val userId: String,
    val stageId: Long,
    val bestScore: Int?
)

fun StageWithProgressDto.withStatus(status: StageStatus) =
    copy(status = status)
