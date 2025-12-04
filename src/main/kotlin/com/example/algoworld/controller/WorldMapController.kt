package com.example.algoworld.controller

import com.example.algoworld.dto.WorldMapResponse
import com.example.algoworld.service.WorldMapService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/worldmap")
class WorldMapController(
    private val worldMapService: WorldMapService
) {

    @GetMapping
    fun getWorldMap(@RequestParam userId: String): WorldMapResponse {
        return worldMapService.getWorldMap(userId)
    }
}
