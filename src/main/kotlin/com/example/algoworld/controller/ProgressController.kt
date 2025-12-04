package com.example.algoworld.controller

import com.example.algoworld.dto.ClearStageRequest
import com.example.algoworld.dto.StageWithProgressDto
import com.example.algoworld.service.StageProgressService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/progress")
class ProgressController(
    private val stageProgressService: StageProgressService
) {

    @PostMapping("/clear")
    fun clearStage(@RequestBody request: ClearStageRequest): StageWithProgressDto {
        return stageProgressService.clearStage(request)
    }
}
