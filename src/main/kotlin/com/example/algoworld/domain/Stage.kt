package com.example.algoworld.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "stages")
data class Stage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    val area: Area,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false)
    val difficulty: Int,

    val unlockConditionStageId: Long? = null,

    @Column(nullable = false)
    val problemType: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val inputSpec: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val outputSpec: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val exampleInput: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val exampleOutput: String,

    val posX: Int? = null,

    val posY: Int? = null
)
