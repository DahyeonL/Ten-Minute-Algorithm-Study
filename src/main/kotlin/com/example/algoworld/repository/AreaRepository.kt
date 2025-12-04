package com.example.algoworld.repository

import com.example.algoworld.domain.Area
import org.springframework.data.jpa.repository.JpaRepository

interface AreaRepository : JpaRepository<Area, Long>
